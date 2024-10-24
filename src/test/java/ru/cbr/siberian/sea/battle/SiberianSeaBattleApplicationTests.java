package ru.cbr.siberian.sea.battle;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.cbr.siberian.sea.battle.model.Match;
import ru.cbr.siberian.sea.battle.model.enumeration.MatchStatus;
import ru.cbr.siberian.sea.battle.model.game.CustomFleet;
import ru.cbr.siberian.sea.battle.model.game.Fleet;
import ru.cbr.siberian.sea.battle.model.game.GridPoint;
import ru.cbr.siberian.sea.battle.model.game.Warship;
import ru.cbr.siberian.sea.battle.model.message.*;
import ru.cbr.siberian.sea.battle.service.*;

import java.util.UUID;

@Slf4j
@SpringBootTest
class SiberianSeaBattleApplicationTests {

    @Autowired
    private GameService gameService;
    @Autowired
    private SeaBattleService seaBattleService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private MatchService matchService;
    @Autowired
    private ActionHistoryService actionHistoryService;

    @Test
    void gameServiceTest() {
        Fleet fleet = gameService.getFleet(10, 10);
        for (int countGame = 0; countGame < 10; countGame++) {
            log.info("fleat game " + countGame);
            for (Warship warship : fleet.getWarships()) {
                log.info(String.format("start (x=%s, y=%s) end (x=%s, y=%s) size %s live %s",
                        warship.getStart().x() + 1,
                        warship.getStart().y() + 1,
                        warship.getEnd().x() + 1,
                        warship.getEnd().y() + 1,
                        warship.getSize(),
                        warship.getLives()));
            }
            log.info("grids");
            GridPoint[][] grids = fleet.getGrids();
            GameService.print(grids);
            int x = Math.toIntExact(Math.round(Math.random() * 9));
            int y = Math.toIntExact(Math.round(Math.random() * 9));
            boolean isHit = gameService.checkShot(fleet,
                    x,
                    y);
            log.info(String.format("is hit %s x %s y %s",
                    isHit,
                    x + 1,
                    y + 1));

        }
        log.info("-------------------");
        log.info("start checkCustomFleet (проверка расстановки флота от пользователя)");
        int[][] customGrids = new int[10][10];
        customGrids[3][4] = 1;
        customGrids[5][5] = 2;
        customGrids[5][6] = 2;

        customGrids[5][7] = 3;

        CustomFleet customFleet = gameService.checkCustomFleet(customGrids);


        for (Warship warship : customFleet.getFleet().getWarships()) {
            log.info(String.format("start (x=%s, y=%s) end (x=%s, y=%s) size %s live %s",
                    warship.getStart().x() + 1,
                    warship.getStart().y() + 1,
                    warship.getEnd().x() + 1,
                    warship.getEnd().y() + 1,
                    warship.getSize(),
                    warship.getLives()));
        }
        log.info("grids");
        GridPoint[][] grids = customFleet.getFleet().getGrids();
        GameService.print(grids);
        int x = Math.toIntExact(Math.round(Math.random() * 9));
        int y = Math.toIntExact(Math.round(Math.random() * 9));
        boolean isHit = gameService.checkShot(fleet,
                x,
                y);
        log.info(String.format("is hit %s x %s y %s",
                isHit,
                x + 1,
                y + 1));
    }

    @Test
    void seaBattleServiceTest() {
        MatchesRequestMessage matchesRequestMessage = new MatchesRequestMessage();
        matchesRequestMessage.setMatchStatus(MatchStatus.IN_PROGRESS);
        matchesRequestMessage.setChanelId(UUID.randomUUID().toString());
        seaBattleService.getMatches(matchesRequestMessage);
        CreateUserRequestMessage createUserRequestMessage = new CreateUserRequestMessage();
        createUserRequestMessage.setUsername("userName");
        createUserRequestMessage.setPassword("123");
        createUserRequestMessage.setChanelId(UUID.randomUUID().toString());
        seaBattleService.createUser(createUserRequestMessage);
        var player = playerService.getPlayerByName("userName").orElse(null);
        CreateGameRequestMessage createGameRequestMessage = new CreateGameRequestMessage();
        createGameRequestMessage.setSizeGrid(5);
        createGameRequestMessage.setUserId(player.getId().toString());
        MatchRequestMessage message = new MatchRequestMessage();
        message.setUserId(player.getId().toString());
        seaBattleService.getMatch(message);
        seaBattleService.createGame(createGameRequestMessage);
        seaBattleService.getMatch(message);

        Match match = matchService.getWaitMatchByPlayerId(player.getId()).orElse(null);

        CreateFleetRequestMessage createFleetRequestMessage = new CreateFleetRequestMessage();

        createFleetRequestMessage.setMatchId(match.getId().toString());
        createFleetRequestMessage.setUserId(player.getId().toString());
        int[][] grids = {{3,0,0,0,0},{3,0,2,0,0},{3,0,2,0,1},{0,0,0,0,0},{0,0,0,0,1}};
        createFleetRequestMessage.setGrids(grids);
        seaBattleService.createFleet(createFleetRequestMessage);

        seaBattleService.getMatch(message);
        seaBattleService.getMatches(matchesRequestMessage);

        CreateUserRequestMessage createUserRequestMessage2 = new CreateUserRequestMessage();
        createUserRequestMessage2.setUsername("userName2");
        createUserRequestMessage2.setPassword("123");
        createUserRequestMessage2.setChanelId(UUID.randomUUID().toString());
        seaBattleService.createUser(createUserRequestMessage2);
        var player2 = playerService.getPlayerByName("userName2").orElse(null);
        JoinGameRequestMessage joinGameRequestMessage = new JoinGameRequestMessage();
        joinGameRequestMessage.setMatchId(match.getId().toString());
        joinGameRequestMessage.setUserId(player2.getId().toString());
        seaBattleService.joinGame(joinGameRequestMessage);
        seaBattleService.getMatch(message);
        GenerateFleetRequestMessage generateFleetRequestMessage2 = new GenerateFleetRequestMessage();
        generateFleetRequestMessage2.setMatchId(match.getId().toString());
        generateFleetRequestMessage2.setUserId(player2.getId().toString());
        seaBattleService.generateFleet(generateFleetRequestMessage2);
        seaBattleService.getMatch(message);
        seaBattleService.getMatches(matchesRequestMessage);
    }
}
