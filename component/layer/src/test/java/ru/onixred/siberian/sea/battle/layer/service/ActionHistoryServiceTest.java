package ru.onixred.siberian.sea.battle.layer.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.onixred.siberian.sea.battle.layer.model.ActionHistory;
import ru.onixred.siberian.sea.battle.layer.model.Match;
import ru.onixred.siberian.sea.battle.layer.model.Player;

import java.util.UUID;

@Disabled
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