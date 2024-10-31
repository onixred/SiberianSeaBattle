package ru.cbr.siberian.feature.first.sea.battle.feature.match;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.cbr.siberian.sea.battle.model.message.*;

@Controller
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @MessageMapping("/see-battle/create-game/request")
    public void createGame(CreateGameRequestMessage request) {
        matchService.createGame(request);
    }

    @MessageMapping("/see-battle/join-game/request")
    public void joinGame(JoinGameRequestMessage request) {
        matchService.joinGame(request);
    }

    @MessageMapping("/see-battle/create-fleet/request")
    public void createFleet(CreateFleetRequestMessage request) {
        matchService.createFleet(request);
    }

    @MessageMapping("/see-battle/generate-fleet/request")
    public void generateFleet(GenerateFleetRequestMessage request) {
        matchService.generateFleet(request);
    }

    @MessageMapping("/see-battle/matches/request")
    public void getMatches(MatchesRequestMessage request) {
        matchService.getMatches(request);
    }

    @MessageMapping("/see-battle/match/request")
    public void getMatch(MatchRequestMessage request) {
        matchService.getMatch(request);
    }

    @MessageMapping("/see-battle/shot-game/request")
    public void shotGame(ShotGameRequestMessage request) {
        matchService.shotGame(request);
    }

    @MessageMapping("/see-battle/grids/request")
    public void getGrids(GridsRequestMessage request) {
        matchService.getGrids(request);
    }

}
