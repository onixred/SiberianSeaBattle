package ru.cbr.siberian.sea.battle.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.cbr.siberian.sea.battle.model.ActionHistory;
import ru.cbr.siberian.sea.battle.model.Match;
import ru.cbr.siberian.sea.battle.model.Player;

import java.util.UUID;

@SpringBootTest
class ActionHistoryServiceTest {

    @Autowired
    private ActionHistoryService actionHistoryService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private PlayerService playerService;

    @Test
    void createActionHistoryTest() {
        Player player = playerService.createPlayer("un1" + UUID.randomUUID(), "p1" + UUID.randomUUID(), UUID.randomUUID());
        Match match = matchService.createMatch(player, 5);
        ActionHistory actionHistory = actionHistoryService.createActionHistory(match, player, 1, 1);
        Assertions.assertNotNull(actionHistory);
    }

}