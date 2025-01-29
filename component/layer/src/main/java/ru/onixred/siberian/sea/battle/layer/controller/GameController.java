package ru.onixred.siberian.sea.battle.layer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.onixred.siberian.sea.battle.layer.model.message.*;
import ru.onixred.siberian.sea.battle.layer.model.message.chat.ChatMessageRequest;
import ru.onixred.siberian.sea.battle.layer.service.NotificationService;
import ru.onixred.siberian.sea.battle.layer.service.SeaBattleService;

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



    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @MessageMapping("/see-battle/chat/request")
    public void greeting(ChatMessageRequest message) {
        notificationService.sendMessage(message.getId(), "/see-battle/chat/response", message);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @MessageMapping("/see-battle/create-user/request")
    public void createUser(CreateUserRequestMessage request) {
        seaBattleService.createUser(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @MessageMapping("/see-battle/get-user/request")
    public void getUser(CreateUserRequestMessage request) {
        seaBattleService.getUser(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @MessageMapping("/see-battle/create-game/request")
    public void createGame(CreateGameRequestMessage request) {
        seaBattleService.createGame(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @MessageMapping("/see-battle/create-fleet/request")
      public void createFleet(CreateFleetRequestMessage request) {
        seaBattleService.createFleet(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @MessageMapping("/see-battle/generate-fleet/request")
    public void generateFleet(GenerateFleetRequestMessage request) {
        seaBattleService.generateFleet(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @MessageMapping("/see-battle/join-game/request")
    public void joinGame(JoinGameRequestMessage request) {
        seaBattleService.joinGame(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @MessageMapping("/see-battle/shot-game/request")
    public void shotGame(ShotGameRequestMessage request) {
        seaBattleService.shotGame(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @MessageMapping("/see-battle/matches/request")
    public void getMatches(MatchesRequestMessage request) {
        seaBattleService.getMatches(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @MessageMapping("/see-battle/match/request")
    public void getMatch(MatchRequestMessage request) {
        seaBattleService.getMatch(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @MessageMapping("/see-battle/match-history/request")
    public void getMatchHistory(MatchHistoryRequestMessage request) {
        seaBattleService.getMatchHistory(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @MessageMapping("/see-battle/grids/request")
    public void getGrids(GridsRequestMessage request) {
        seaBattleService.getGrids(request);
    }

}