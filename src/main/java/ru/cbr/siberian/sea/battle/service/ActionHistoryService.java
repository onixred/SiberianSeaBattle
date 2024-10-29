package ru.cbr.siberian.sea.battle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cbr.siberian.sea.battle.acl.ActionHistoryMapper;
import ru.cbr.siberian.sea.battle.model.ActionHistory;
import ru.cbr.siberian.sea.battle.model.Match;
import ru.cbr.siberian.sea.battle.model.Player;
import ru.cbr.siberian.sea.battle.repository.ActionHistoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActionHistoryService {

    private final ActionHistoryRepository actionHistoryRepository;

    private final ActionHistoryMapper mapper;


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
