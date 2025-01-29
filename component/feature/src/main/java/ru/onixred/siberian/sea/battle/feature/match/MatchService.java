package ru.onixred.siberian.sea.battle.feature.match;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.onixred.siberian.sea.battle.feature.match.action_history.ActionHistoryService;
import ru.onixred.siberian.sea.battle.feature.common.*;
import ru.onixred.siberian.sea.battle.feature.game.*;
import ru.onixred.siberian.sea.battle.feature.notification.NotificationResponseMessage;
import ru.onixred.siberian.sea.battle.feature.notification.NotificationService;
import ru.onixred.siberian.sea.battle.feature.notification.TypeNotification;
import ru.onixred.siberian.sea.battle.feature.player.Player;
import ru.onixred.siberian.sea.battle.feature.player.PlayerService;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {
    private final Map<UUID, MatchFleet> matchIdToMatchFleet = new ConcurrentHashMap<>();
    private final MatchRepository matchRepository;

    private final MatchMapper matchMapper;
    private final ValidatorService validatorService;
    private final PlayerService playerService;
    private final NotificationService notificationService;
    private final GameService gameService;
    private final ActionHistoryService actionHistoryService;

    /**
     * Создать матч игры
     *
     * @param request запрос на создание игры
     */
    public void createGame(CreateGameRequestMessage request) {
        log.info(String.format("Start createGame userId %s", request.getUserId()));
        CreateGameResponseMessage response = new CreateGameResponseMessage();
        Player owner = null;
        try {
            validatorService.validateRequest(request);
            int sizeGrid = Optional.ofNullable(request.getSizeGrid()).orElse(5);

            Optional<Player> user = playerService.getPlayer(request.getUserId());

            if(user.isEmpty()) {
                throw new RuntimeException(String.format("Игрок с id %s не найден", request.getUserId()));
            } else {
                owner =  user.get();
            }

            findAndStopOldMatch(owner.getId());
            Match match = createMatch(owner, sizeGrid);


            //TODO возможно нужно закинуть в бд но уччитывая маппинги это накладно
            matchIdToMatchFleet.put(match.getId(), new MatchFleet(new ConcurrentHashMap<>()));

            response.setMatchId(match.getId().toString());
            response.setStatus(Status.OK);

            notificationService.sendMessage(owner.getChanelId().toString(), "/see-battle/create-game/response", response);
            allNotification(TypeNotification.MATCH_WAIT);
        } catch (RuntimeException re) {
            response.setStatus(Status.ERROR);
            response.setErrorDescription(re.getMessage());
            if(owner != null) {
                notificationService.sendMessage(owner.getChanelId().toString(), "/see-battle/create-game/response", response);
            }

        } finally {
            log.info(String.format("End createGame userId %s", request.getUserId()));
        }
    }

    /**
     * Создаем флот
     *
     * @param request запрос на создание флота
     */
    @Transactional
    public void createFleet(CreateFleetRequestMessage request) {
        CreateFleetResponseMessage response = new CreateFleetResponseMessage();
        log.info(String.format("Start createFleet userId %s", request.getUserId()));
        Player player = null;
        try {
            validatorService.validateRequest(request);
            Match match = checkMatch(request.getMatchId());
            checkUser(request.getUserId(), match);
            UUID userId = UUID.fromString(request.getUserId());
            player = getPlayer(userId);
            MatchFleet matchFleet = getMatchFleet(match.getId(), player.getName());
            checkInitFleetByUser(matchFleet, userId, player.getName(), match.getId());


            CustomFleet customFleet = gameService.checkCustomFleet(request.getGrids());
            if (customFleet.isStatus()) {
                matchFleet.userIdToFleet().put(userId, customFleet.getFleet());
                response.setStatus(Status.OK);
                updateMatchStatus(matchFleet, match, userId);
            } else {
                response.setStatus(Status.ERROR);
                response.setErrorDescription("Ошибка в расстановке флота");
                response.setErrorGrids(customFleet.getErrorGrids());
            }
            Optional<UUID> opponentUserId = matchFleet.findOpponentUserId(userId);
            response.setStartGame(match.getStatus().equals(MatchStatus.START_GAME));

            notificationService.sendMessage(player.getChanelId().toString(), "/see-battle/create-fleet/response", response);

            //Оповещение второго игрока что соперник готов\игра начинается
            if(opponentUserId.isPresent()) {
                Player opponentUser = getPlayer(opponentUserId.get());
                FleetOpponentResponseMessage opponentResponse = new FleetOpponentResponseMessage();
                opponentResponse.setStatus(Status.OK);
                opponentResponse.setStartGame(response.isStartGame());
                notificationService.sendMessage(opponentUser.getChanelId().toString(), "/see-battle/fleet-opponent/response", opponentResponse);
            }

        } catch (RuntimeException re) {
            response.setStatus(Status.ERROR);
            response.setErrorDescription(re.getMessage());
            if(player != null) {
                notificationService.sendMessage(player.getChanelId().toString(), "/see-battle/create-fleet/response", response);
            }
        } finally {
            log.info(String.format("End createFleet userId %s", request.getUserId()));
        }
    }

    /**
     * Генерируем флот (автоматическая расстановка флота)
     *
     * @param request запрос
     */
    public void generateFleet(GenerateFleetRequestMessage request) {
        log.info(String.format("Start generateFleet userId %s", request.getUserId()));
        GenerateFleetResponseMessage response = new GenerateFleetResponseMessage();
        Player player = null;
        try {
            validatorService.validateRequest(request);
            Match match = checkMatch(request.getMatchId());
            checkUser(request.getUserId(), match);
            UUID userId = UUID.fromString(request.getUserId());
            player = getPlayer(userId);
            MatchFleet matchFleet = getMatchFleet(match.getId(), player.getName());
            checkInitFleetByUser(matchFleet, userId, player.getName(), match.getId());

            Fleet fleet = gameService.getFleet(match.getSizeGrid(), match.getSizeGrid());
            matchFleet.userIdToFleet().put(userId, fleet);
            updateMatchStatus(matchFleet, match, userId);
            response.setStatus(Status.OK);
            int[][] grids = gameService.toGridsForOwner(fleet.getGrids());
            response.setGrids(grids);
            Optional<UUID> opponentUserId = matchFleet.findOpponentUserId(userId);
            response.setStartGame(opponentUserId.isPresent());
            notificationService.sendMessage(player.getChanelId().toString(), "/see-battle/generate-fleet/response", response);

            //Оповещение второго игрока что соперник готов\игра начилась
            if(opponentUserId.isPresent()) {
                Player opponentUser = getPlayer(opponentUserId.get());
                FleetOpponentResponseMessage opponentResponse = new FleetOpponentResponseMessage();
                opponentResponse.setStatus(Status.OK);
                opponentResponse.setStartGame(true);
                notificationService.sendMessage(opponentUser.getChanelId().toString(), "/see-battle/fleet-opponent/response", opponentResponse);
            }
        } catch (RuntimeException re) {
            log.error(String.format("Error generateFleet userId %s message %s", request.getUserId(), re.getMessage()), re);
            response.setStatus(Status.ERROR);
            response.setErrorDescription(re.getMessage());
            if(player != null) {
                notificationService.sendMessage(player.getChanelId().toString(), "/see-battle/generate-fleet/response", response);
            }
        } finally {
            log.info(String.format("End generateFleet userId %s", request.getUserId()));
        }
    }

    /**
     * Запрос списка игр в указанном статусе
     * @param request запрос
     */
    public void getMatches(MatchesRequestMessage request) {
        log.info(String.format("Start getMatches chanelId %s", request.getChanelId()));
        MatchesResponseMessage response = new MatchesResponseMessage();
        try {
            validatorService.validateRequest(request);
            List<Match> matches = getAllMatchesByStatus(request.getMatchStatus());
            response.setMatches(matches
                    .stream()
                    .map(matchDao -> new MatchUI(
                            matchDao.getId(),
                            matchDao.getSizeGrid(),
                            getName(matchDao.getOwner()),
                            getName(matchDao.getOpponent()),
                            getName(matchDao.getWinner()),
                            matchDao.getStatus()))
                    .toList());


            response.setStatus(Status.OK);
            notificationService.sendMessage(request.getChanelId(), "see-battle/matches/response", response);
        } catch (RuntimeException re) {
            response.setStatus(Status.ERROR);
            response.setErrorDescription(re.getMessage());
            notificationService.sendMessage(request.getChanelId(), "see-battle/matches/response", response);
        } finally {
            log.info(String.format("End getMatches chanelId %s", request.getChanelId()));
        }
    }

    /**
     * Запрос игра для Игрока
     * @param request запрос
     */
    public void getMatch(MatchRequestMessage request) {
        log.info(String.format("Start getMatch userId %s", request.getUserId()));
        MatchResponseMessage response = new MatchResponseMessage();
        Player player = null;
        try {
            validatorService.validateRequest(request);
            UUID userId = UUID.fromString(request.getUserId());
            player = getPlayer(userId);
            Optional<Match> matchOpt = getWaitMatchByPlayerId(userId);
            if (matchOpt.isEmpty()) {
                throw new RuntimeException("Игр в статусе ожидания нет");
            }
            Match match = matchOpt.get();
            MatchFleet matchFleet = getMatchFleet(match.getId(), player.getName());
            Fleet fleet = matchFleet.userIdToFleet().get(userId);
            int[][] grids = gameService.getGrids(fleet, match.getSizeGrid(), true);
            Fleet opponentFleet = matchFleet.findOpponentFleet(userId).orElse(null);
            int[][] opponentGrids = gameService.getGrids(opponentFleet, match.getSizeGrid(), false);
            response.setGrids(grids);
            response.setOpponentGrids(opponentGrids);
            response.setMatchStatus(match.getStatus());
            response.setStatus(Status.OK);
            notificationService.sendMessage(player.getChanelId().toString(), "see-battle/match/response", response);
        } catch (RuntimeException re) {
            response.setStatus(Status.ERROR);
            response.setErrorDescription(re.getMessage());
            if (player != null) {
                notificationService.sendMessage(player.getChanelId().toString(), "/see-battle/match/response", response);
            }
        } finally {
            log.info(String.format("End getMatch userId %s", request.getUserId()));
        }
    }

    /**
     * Выстрел в игре по полю
     * @param request запрос
     */
    public void shotGame(ShotGameRequestMessage request) {
        log.info(String.format("Start shotGame userId %s", request.getUserId()));
        ShotGameResponseMessage response = new ShotGameResponseMessage();
        Player player = null;
        try {
            validatorService.validateRequest(request);
            Match match = checkMatch(request.getMatchId());
            checkUser(request.getUserId(), match);
            UUID userId = UUID.fromString(request.getUserId());
            player = getPlayer(userId);
            MatchFleet matchFleet = getMatchFleet(match.getId(), player.getName());
            UUID opponentUserId =  matchFleet.getOpponentUserId(userId);
            Fleet opponentFleet = matchFleet.userIdToFleet().get(opponentUserId);
            boolean isHit = gameService.checkShot(opponentFleet, request.getX(), request.getY());

            response.setHit(isHit);
            int[][] opponentGrids  = gameService.toGridsForOpponent(opponentFleet.getGrids());
            response.setOpponentGrids(opponentGrids);
            response.setStatus(Status.OK);
            actionHistoryService.createActionHistory(match, player, request.getX(), request.getY());
            boolean isWin = false;
            if(isHit){
                isWin = true;
                for(Warship warship: opponentFleet.getWarships()) {
                    if(!warship.isKill()) {
                        //Увы но все корабли флота ушли под воду
                        isWin = false;
                        break;
                    }
                }
                if(isWin) {
                    allNotification(TypeNotification.MATCH_COMPLETED);
                    match.setWinner(player);
                    match.setStatus(MatchStatus.COMPLETED);
                    updateMatch(match);
                }
            }
            response.setWinn(isWin);

            notificationService.sendMessage(player.getChanelId().toString(), "/see-battle/shot-game/response", response);

            //нотификация сопернику
            var opponent = getPlayer(opponentUserId);
            int[][] ownerGrids  = gameService.toGridsForOwner(opponentFleet.getGrids());
            ShotGameOwnerResponseMessage opponentResponse = new ShotGameOwnerResponseMessage();
            opponentResponse.setStatus(Status.OK);
            opponentResponse.setHit(isHit);
            opponentResponse.setOpponentWin(isWin);
            opponentResponse.setGrids(ownerGrids);

            notificationService.sendMessage(opponent.getChanelId().toString(), "/see-battle/shot-game-owner/response", opponentResponse);
            allNotification(TypeNotification.MATCH_HISTORY);
            allNotification(TypeNotification.GRIDS_UPDATE, match.getId().toString());
        } catch (RuntimeException re) {
            response.setStatus(Status.ERROR);
            response.setErrorDescription(re.getMessage());
            if(player != null) {
                notificationService.sendMessage(player.getChanelId().toString(), "/see-battle/shot-game/response", response);
            }
        } finally {
            log.info(String.format("End shotGame userId %s", request.getUserId()));
        }
    }

    /**
     * Запрос игры для наблюдателя
     * @param request запрос
     */
    public void getGrids(GridsRequestMessage request) {
        log.info(String.format("Start getGrids matchId %s", request.getMatchId()));
        GridsResponseMessage response = new GridsResponseMessage();
        try {
            validatorService.validateRequest(request);
            Match match = getMatchById(request.getMatchId())
                    .orElseThrow(()->new RuntimeException(String.format("Игра %s не найдена", request.getMatchId())));
            response.setMatchStatus(match.getStatus());
            MatchFleet matchFleet = getMatchFleet(UUID.fromString(request.getMatchId()), "Нет данных");
            List<Map.Entry<UUID, Fleet>> entries = matchFleet.userIdToFleet().entrySet().stream().sorted(Map.Entry.comparingByKey()).toList();
            for (int number = 0; number < entries.size(); number++) {
                var entry = entries.get(number);
                int[][] grids = gameService.toGridsForOpponent(entry.getValue().getGrids());
                Optional<Player> player = playerService.getPlayer(entry.getKey());
                if (number == 0) {
                    response.setPlayerOneGrids(grids);
                    player.ifPresent(p -> response.setPlayerOneName(p.getName()));
                    response.setPlayerOneId(entry.getKey().toString());
                } else {
                    response.setPlayerTwoGrids(grids);
                    player.ifPresent(p -> response.setPlayerTwoName(p.getName()));
                    response.setPlayerTwoId(entry.getKey().toString());
                }
            }
        } catch (RuntimeException re) {
            response.setStatus(Status.ERROR);
            response.setErrorDescription(re.getMessage());
            notificationService.sendMessage(request.getChanelId(), "/see-battle/grids/response", response);
        } finally {
            log.info(String.format("End getGrids matchId %s", request.getMatchId()));
        }
    }

    private String getName(Player player) {
        if(player == null) {
            return "Нет данных";
        }
        return player.getName();
    }

    private void checkUser(String userId, Match match) {
        if (notEqualsUser(userId, match.getOwner()) && notEqualsUser(userId, match.getOpponent())) {
            throw new RuntimeException(String.format("Игрок с %s не участвует в игре %s ", userId, match.getId()));

        }
    }

    private boolean notEqualsUser(String userId, Player player) {
        if (player == null) {
            return true;
        }
        return !userId.equals(player.getId().toString());
    }

    /**
     * Запрос на подключение к игре
     * @param request запрос
     */
    public void joinGame(JoinGameRequestMessage request) {
        log.info(String.format("Start joinGame userId %s", request.getUserId()));
        JoinGameResponseMessage response = new JoinGameResponseMessage();
        Player opponent = null;
        try {
            validatorService.validateRequest(request);
            opponent = getPlayer(UUID.fromString(request.getUserId()));
            findAndStopOldMatch(opponent.getId());


            Match match = checkMatch(request.getMatchId());
            if (match.getOwner() != null && match.getOwner().getId().equals(opponent.getId())) {
                log.warn(String.format("Попытка подключится к игре %s автором игры логин %s", match.getId(), opponent.getName()));
                return;
            }
            if(match.getOpponent() != null) {
                throw new RuntimeException(
                        String.format("В игре %s уже есть соперник %s", match.getId(), match.getOpponent().getName()));
            }


            match.setOpponent(opponent);
            MatchFleet matchFleet = getMatchFleet(match.getId(), opponent.getName());
            updateMatchStatus(matchFleet, match, opponent.getId());

            response.setStatus(Status.OK);

            notificationService.sendMessage(opponent.getChanelId().toString(), "/see-battle/join-game/response", response);
            //Оповещение владельца игры
            Player owner = getPlayer(match.getOwner().getId());
            JoinGameOwnerResponseMessage ownerResponse = new JoinGameOwnerResponseMessage();
            ownerResponse.setStatus(Status.OK);
            notificationService.sendMessage(owner.getChanelId().toString(), "/see-battle/join-game-owner/response", ownerResponse);
            allNotification(TypeNotification.MATCH_WAIT);
        } catch (RuntimeException re) {
            response.setStatus(Status.ERROR);
            response.setErrorDescription(re.getMessage());
            if(opponent != null)  {
                notificationService.sendMessage(opponent.getChanelId().toString(), "/see-battle/join-game/response", response);
            }

        } finally {
            log.info(String.format("End joinGame userId %s", request.getUserId()));
        }
    }

    private void updateMatchStatus(MatchFleet matchFleet, Match match, UUID userId) {
        if(matchFleet.checkWaitAllFleet()) {
            match.setStatus(MatchStatus.IN_PROGRESS);
        } else if(matchFleet.checkOpponentDone()) {
            match.setStatus(MatchStatus.START_GAME);
        } else {
            MatchStatus status = match.getOwner().getId().equals(userId)?MatchStatus.IN_PROGRESS_WAIT_FLEET_OPPONENT:MatchStatus.IN_PROGRESS_WAIT_FLEET_OWNER;
            match.setStatus(status);
        }
        updateMatch(match);
    }

    private MatchFleet getMatchFleet(UUID matchId, String userName) {
        MatchFleet matchFleet = matchIdToMatchFleet.get(matchId);
        if (matchFleet == null) {
            throw new RuntimeException(String.format("Игра %s c пользователем %s не найдена, произошла ошибка", matchId, userName));
        }



        return matchFleet;
    }

    private Match checkMatch(String matchId) {
        Optional<Match> matchOpt = getMatchById(matchId);
        if (matchOpt.isEmpty()) {
            throw new RuntimeException(String.format("Игра %s не найдена", matchId));
        }
        Match match = matchOpt.get();
        if (match.getWinner() != null) {
            throw new RuntimeException(String.format("Игра %s закончилась, победил %s ", matchId, match.getWinner().getName()));
        }
        return match;
    }

    private Player getPlayer(UUID userId) {
        Optional<Player> player = playerService.getPlayer(userId);
        if(player.isEmpty()) {
            throw new RuntimeException(String.format("Игрок %s не найден", userId));
        }
        return player.get();
    }

    private static void checkInitFleetByUser(MatchFleet matchFleet, UUID userId, String userName, UUID matchId) {
        if (matchFleet.userIdToFleet().get(userId) != null) {
            throw new RuntimeException(
                    String.format("В игре %s игрок %s, уже расставил флот", matchId, userName));
        }
    }


    @Transactional
    public Match createMatch(Player owner, int sizeGrid) {

        var match = matchRepository.save(matchMapper.createMapper(owner, sizeGrid));

        return matchMapper.matchMapper(match);
    }

    @Transactional
    public Optional<Match> getMatchById(String id) {
        return getMatchById(UUID.fromString(id));
    }

    @Transactional
    public Optional<Match> getMatchById(UUID id) {
        return matchRepository.findById(id)
                .map(matchMapper::matchMapper);
    }

    @Transactional
    public List<Match> getAllMatches() {
        return matchRepository.findAll()
                .stream()
                .map(matchMapper::matchMapper)
                .toList();
    }

    @Transactional
    public void updateMatch(Match match) {
        var matchDao =  matchMapper.matchMapperDao(match);
        matchRepository.save(matchDao);
    }

    @Transactional
    public List<Match> getAllMatchesByStatus(MatchStatus matchStatus) {
        var  matches = switch (matchStatus) {
            case ALL -> matchRepository.findAll();
            case IN_PROGRESS ->  matchRepository.findAllByStatusIn(List.of(MatchStatus.IN_PROGRESS,
                    MatchStatus.IN_PROGRESS_WAIT_FLEET_OWNER, MatchStatus.IN_PROGRESS_WAIT_FLEET_OPPONENT,
                    MatchStatus.START_GAME));
            default -> matchRepository.findAllByStatus(matchStatus);
        };

        return matches.stream()
                .map(matchMapper::matchMapper)
                .toList();
     }

    public Optional<Match> getWaitMatchByPlayerId(UUID playerId) {
        var match = matchRepository.findByOwner_idAndStatusNot(playerId, MatchStatus.COMPLETED);
        if (match.isEmpty()) {
            match = matchRepository.findByOpponent_idAndStatusNot(playerId, MatchStatus.COMPLETED);
        }

        return match.map(matchMapper::matchMapper);
    }

    private void findAndStopOldMatch(UUID playerId) {
        Optional<Match> currentMatchOpt = getWaitMatchByPlayerId(playerId);
        if(currentMatchOpt.isPresent()) {
            Match currentMatch = currentMatchOpt.get();
            currentMatch.setStatus(MatchStatus.COMPLETED);
            updateMatch(currentMatch);
        }
    }

    private void allNotification(TypeNotification type) {
        allNotification(type, null);
    }

    private void allNotification(TypeNotification type, String matchId) {
        NotificationResponseMessage response = new NotificationResponseMessage();
        response.setType(type);
        response.setMatchId(matchId);
        response.setStatus(Status.OK);
        notificationService.sendNotificationAll( "/see-battle/notification-all/response", response);

    }
}
