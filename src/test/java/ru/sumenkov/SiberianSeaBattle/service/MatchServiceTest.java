package ru.sumenkov.SiberianSeaBattle.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.sumenkov.SiberianSeaBattle.model.Match;
import ru.sumenkov.SiberianSeaBattle.model.Player;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MatchServiceTest {

    @Autowired
    private MatchService matchService;

    @Autowired
    private PlayerService playerService;

    @Test
    void createMatchTest() {
        Player player = playerService.createPlayer();
        Match match = matchService.createMatch(player);
        assertNotNull(match);
        assertNotNull(match.getId());
    }
}