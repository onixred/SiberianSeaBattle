package ru.onixred.siberian.sea.battle.layer.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.onixred.siberian.sea.battle.layer.model.Match;
import ru.onixred.siberian.sea.battle.layer.model.Player;
import ru.onixred.siberian.sea.battle.layer.service.MatchService;
import ru.onixred.siberian.sea.battle.layer.service.PlayerService;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
@Disabled
@SpringBootTest
class MatchServiceTest {

    @Autowired
    private MatchService matchService;

    @Autowired
    private PlayerService playerService;

    @Test
    void createMatchTest() {
        Player player = playerService.createPlayer("p1" + UUID.randomUUID(), "p1" + UUID.randomUUID(), UUID.randomUUID());
        Match match = matchService.createMatch(player, 3);
        assertNotNull(match);
        assertNotNull(match.getId());
    }
}