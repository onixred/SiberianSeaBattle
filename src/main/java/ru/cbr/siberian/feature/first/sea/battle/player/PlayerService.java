package ru.cbr.siberian.feature.first.sea.battle.player;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.cbr.siberian.feature.first.sea.battle.notification.NotificationService;
import ru.cbr.siberian.feature.first.sea.battle.common.ValidatorService;
import ru.cbr.siberian.feature.first.sea.battle.common.Status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final ValidatorService validatorService;
    private final NotificationService notificationService;
    private final PlayerMapper mapper;


    /**
     * Создать игрока
     *
     * @param request запрос на создание игрока
     */
    public void createUser(CreateUserRequestMessage request) {
        log.info(String.format("Start createUser username %s", request.getUsername()));
        CreateUserResponseMessage response = new CreateUserResponseMessage();
        try {
            validatorService.validateRequest(request);

            if(!StringUtils.hasText(request.getUsername())) {
                throw new RuntimeException("Не указан логин пользователя");
            }
            if(!StringUtils.hasText(request.getPassword())) {
                throw new RuntimeException("Не указан пароль пользователя");
            }
            Optional<Player> user = getPlayerByName(request.getUsername());

            if(user.isEmpty()) {
                var player = createPlayer(request.getUsername(), request.getPassword(), UUID.fromString(request.getChanelId()));
                response.setUserId(player.getId().toString());
                response.setChanelId(player.getChanelId().toString());
                response.setStatus(Status.OK);
            } else {
                throw new RuntimeException(String.format("Ошибка игрок с логином %s уже существует", request.getUsername()));

            }
            notificationService.sendMessage(request.getChanelId(), "/see-battle/create-user/response", response);
        } catch (RuntimeException re) {
            response.setStatus(Status.ERROR);
            response.setErrorDescription(re.getMessage());
            notificationService.sendMessage(request.getChanelId(), "/see-battle/create-user/response", response);
        } finally {
            log.info(String.format("End createUser username %s", request.getUsername()));
        }
    }

    /**
     * Получить игрока
     *
     * @param request запрос на получение игрока
     */
    public void getUser(CreateUserRequestMessage request) {
        log.info(String.format("Start getUser username %s", request.getUsername()));
        GetUserResponseMessage response = new GetUserResponseMessage();
        try {
            validatorService.validateRequest(request);
            if(!StringUtils.hasText(request.getUsername())) {
                throw new RuntimeException("Не указан логин");
            }
            if(!StringUtils.hasText(request.getPassword())) {
                throw new RuntimeException("Не указан пароль пользователя");
            }
            Optional<Player> user = getPlayerByName(request.getUsername());

            if(user.isEmpty()) {
                throw new RuntimeException(String.format("Игрок с логином %s не найден", request.getUsername()));
            } else {
                var player =  user.get();
                if(!player.getPassword().equals(request.getPassword())) {
                    throw new RuntimeException(String.format("Игрок с логином %s указал не верный пароль", request.getUsername()));
                }
                player.setChanelId(UUID.fromString(request.getChanelId()));
                updatePlayer(player);
                response.setUserId(player.getId().toString());
                response.setChanelId(player.getChanelId().toString());
                response.setStatus(Status.OK);
            }
            notificationService.sendMessage(request.getChanelId(), "/see-battle/get-user/response", response);
        } catch (RuntimeException re) {
            response.setStatus(Status.ERROR);
            response.setErrorDescription(re.getMessage());
            notificationService.sendMessage(request.getChanelId(), "/see-battle/get-user/response", response);
        } finally {
            log.info(String.format("End getUser username %s", request.getUsername()));
        }
    }


    @Transactional
    public Player createPlayer(String name, String password, UUID chanelId) {
         var playerDao =  mapper.creteDao(name, password, chanelId);
        return mapper.map(playerRepository.save(playerDao));
    }

    @Transactional
    public Optional<Player> getPlayer(String id) {
        return getPlayer(UUID.fromString(id));
    }

    @Transactional
    public Optional<Player> getPlayerByName(String name) {
        return playerRepository.findByName(name)
                .map(mapper::map);
    }

    @Transactional
    public Optional<Player> getPlayer(UUID id) {
        return playerRepository.findById(id)
                .map(mapper::map);
    }

    @Transactional
    public List<Player> getAllPlayers() {
        return playerRepository.findAll()
                .stream()
                .map(mapper::map)
                .toList();
    }

    @Transactional
    public void updatePlayer(Player player) {
        var playerDao = mapper.mapDao(player);
        playerRepository.save(playerDao);
    }

}
