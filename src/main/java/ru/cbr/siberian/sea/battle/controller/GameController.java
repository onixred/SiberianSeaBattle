package ru.cbr.siberian.sea.battle.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.cbr.siberian.sea.battle.model.message.*;
import ru.cbr.siberian.sea.battle.model.message.chat.ChatMessageRequest;
import ru.cbr.siberian.sea.battle.service.NotificationService;
import ru.cbr.siberian.sea.battle.service.SeaBattleService;

/**
 * Description: Основная точка входа API
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 09.09.2024
 */
@Controller
@RequiredArgsConstructor
public class GameController {
    private final SeaBattleService seaBattleService;
    private final NotificationService notificationService;



    @MessageMapping("/see-battle/chat/request")
    public void greeting(ChatMessageRequest message) {
        notificationService.sendMessage(message.getId(), "/see-battle/chat/response", message);
    }

    @MessageMapping("/see-battle/create-user/request")
    public void createUser(CreateUserRequestMessage request) {
        seaBattleService.createUser(request);
    }

    @MessageMapping("/see-battle/get-user/request")
    public void getUser(CreateUserRequestMessage request) {
        seaBattleService.getUser(request);
    }

    @MessageMapping("/see-battle/create-game/request")
    public void createGame(CreateGameRequestMessage request) {
        seaBattleService.createGame(request);
    }

    @MessageMapping("/see-battle/create-fleet/request")
      public void createFleet(CreateFleetRequestMessage request) {
        seaBattleService.createFleet(request);
    }

    @MessageMapping("/see-battle/generate-fleet/request")
    public void generateFleet(GenerateFleetRequestMessage request) {
        seaBattleService.generateFleet(request);
    }

    @MessageMapping("/see-battle/join-game/request")
    public void joinGame(JoinGameRequestMessage request) {
        seaBattleService.joinGame(request);
    }

    @MessageMapping("/see-battle/shot-game/request")
    public void shotGame(ShotGameRequestMessage request) {
        seaBattleService.shotGame(request);
    }

    @MessageMapping("/see-battle/matches/request")
    public void getMatches(MatchesRequestMessage request) {
        seaBattleService.getMatches(request);
    }

    @MessageMapping("/see-battle/match/request")
    public void getMatch(MatchRequestMessage request) {
        seaBattleService.getMatch(request);
    }

    @MessageMapping("/see-battle/match-history/request")
    public void getMatchHistory(MatchHistoryRequestMessage request) {
        seaBattleService.getMatchHistory(request);
    }

    @MessageMapping("/see-battle/grids/request")
    public void getGrids(GridsRequestMessage request) {
        seaBattleService.getGrids(request);
    }

}