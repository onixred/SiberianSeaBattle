package ru.cbr.siberian.sea.battle.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.cbr.siberian.sea.battle.model.Player;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Disabled
class PlayerServiceTest {

    @Autowired
    private PlayerService playerService;

    @Test
    void createPlayerTest() {
        Player player = playerService.createPlayer("p1" + UUID.randomUUID(), "p1" + UUID.randomUUID(), UUID.randomUUID());
        assertNotNull(player);
        assertNotNull(player.getId());
    }

    @Test
    void updatePlayerTest() {
        Player oldPlayer = playerService.createPlayer("p1" + UUID.randomUUID(), "p1" + UUID.randomUUID(), UUID.randomUUID());

    }

}