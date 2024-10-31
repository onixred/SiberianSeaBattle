package ru.cbr.siberian.feature.first.sea.battle.match.action_history;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cbr.siberian.feature.first.sea.battle.notification.NotificationService;
import ru.cbr.siberian.feature.first.sea.battle.common.Status;
import ru.cbr.siberian.feature.first.sea.battle.common.ValidatorService;
import ru.cbr.siberian.feature.first.sea.battle.match.Match;
import ru.cbr.siberian.feature.first.sea.battle.player.Player;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActionHistoryService {

    private final ActionHistoryRepository actionHistoryRepository;
    private final ValidatorService validatorService;
    private final NotificationService notificationService;
    private final ActionHistoryMapper mapper;


    /**
     * Запрос истории игры
     * @param request запрос
     */
    public void getMatchHistory(MatchHistoryRequestMessage request) {
        log.info(String.format("Start getMatchHistory matchId %s", request.getMatchId()));
        MatchHistoryResponseMessage response = new MatchHistoryResponseMessage();
        try {
            validatorService.validateRequest(request);
            List<ActionHistory> actionHistories = findAllByMatchId(UUID.fromString(request.getMatchId()));
            response.setActionHistories(actionHistories);
            response.setStatus(Status.OK);
            notificationService.sendMessage(request.getChanelId(), "/see-battle/match-history/response", response);
        } catch (RuntimeException re) {
            response.setStatus(Status.ERROR);
            response.setErrorDescription(re.getMessage());
            notificationService.sendMessage(request.getChanelId(), "/see-battle/match-history/response", response);
        } finally {
            log.info(String.format("End getMatchHistory matchId %s", request.getMatchId()));
        }
    }


    @Transactional
    public ActionHistory createActionHistory(Match match, Player player, int x, int y) {
        var dao = mapper.creteDao(match, player, x, y);
        actionHistoryRepository.save(dao);

        return mapper.map(dao);
    }

    @Transactional
    public Optional<ActionHistory> findActionHistoryById(String id) {
        return findActionHistoryById(UUID.fromString(id));
    }

    @Transactional
    public Optional<ActionHistory> findActionHistoryById(UUID id) {
        return actionHistoryRepository.findById(id)
                .map(mapper::map);
    }

    @Transactional
    public List<ActionHistory> findAllByPlayerId(UUID playerId) {
        return actionHistoryRepository.findAllByPlayerId(playerId)
                .stream()
                .map(mapper::map)
                .toList();
    }

    @Transactional
    public List<ActionHistory> findAllByMatchId(UUID matchId) {
        return actionHistoryRepository.findAllByMatchId(matchId)
                .stream()
                .map(mapper::map)
                .toList();
    }

    @Transactional
    public void updateActionHistory(ActionHistory actionHistory) {
        var actionHistoryDao = mapper.mapDao(actionHistory);
        actionHistoryRepository.save(actionHistoryDao);
    }

}
