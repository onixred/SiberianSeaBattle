package ru.onixred.siberian.sea.battle.feature.match.action_history;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class ActionHistoryController {

    private final ActionHistoryService actionHistoryService;

    @MessageMapping("/see-battle/match-history/request")
    public void getMatchHistory(MatchHistoryRequestMessage request) {
        actionHistoryService.getMatchHistory(request);
    }
}
