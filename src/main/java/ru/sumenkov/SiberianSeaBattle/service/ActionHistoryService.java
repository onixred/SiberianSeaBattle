package ru.sumenkov.SiberianSeaBattle.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sumenkov.SiberianSeaBattle.dao.ActionHistoryDao;
import ru.sumenkov.SiberianSeaBattle.dao.MatchDao;
import ru.sumenkov.SiberianSeaBattle.dao.PlayerDao;
import ru.sumenkov.SiberianSeaBattle.model.ActionHistory;
import ru.sumenkov.SiberianSeaBattle.model.Match;
import ru.sumenkov.SiberianSeaBattle.model.Player;
import ru.sumenkov.SiberianSeaBattle.repository.ActionHistoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActionHistoryService {

    private final ActionHistoryRepository actionHistoryRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public ActionHistory createActionHistory(Match match, Player player, int x, int y) {
        MatchDao matchDao = modelMapper.map(match, MatchDao.class);
        PlayerDao playerDao = modelMapper.map(player, PlayerDao.class);
        ActionHistoryDao actionHistoryDao = new ActionHistoryDao();
        actionHistoryDao.setMatch(matchDao);
        actionHistoryDao.setPlayer(playerDao);
        actionHistoryDao.setX(x);
        actionHistoryDao.setY(y);

        actionHistoryRepository.save(actionHistoryDao);

        return modelMapper.map(actionHistoryDao, ActionHistory.class);
    }

    @Transactional
    public Optional<ActionHistory> findActionHistoryById(String id) {
        return findActionHistoryById(UUID.fromString(id));
    }

    @Transactional
    public Optional<ActionHistory> findActionHistoryById(UUID id) {
        return actionHistoryRepository.findById(id)
                .map(actionHistoryDao -> modelMapper.map(actionHistoryDao, ActionHistory.class));
    }

    @Transactional
    public List<ActionHistory> findAllByPlayerId(UUID playerId) {
        return actionHistoryRepository.findAllByPlayerId(playerId)
                .stream()
                .map(actionHistoryDao -> modelMapper.map(actionHistoryDao, ActionHistory.class))
                .toList();
    }

    @Transactional
    public List<ActionHistory> findAllByMatchId(UUID matchId) {
        return actionHistoryRepository.findAllByMatchId(matchId)
                .stream()
                .map(actionHistoryDao -> modelMapper.map(actionHistoryDao, ActionHistory.class))
                .toList();
    }

    @Transactional
    public void updateActionHistory(ActionHistory actionHistory) {
        ActionHistoryDao actionHistoryDao = modelMapper.map(actionHistory, ActionHistoryDao.class);
        actionHistoryRepository.save(actionHistoryDao);
    }

}
