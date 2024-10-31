package ru.cbr.siberian.feature.first.sea.battle.feature.actio_history;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.cbr.siberian.sea.battle.model.message.MatchHistoryRequestMessage;

@Controller
@RequiredArgsConstructor
public class ActionHistoryController {

    private final ActionHistoryService actionHistoryService;

    @MessageMapping("/see-battle/match-history/request")
    public void getMatchHistory(MatchHistoryRequestMessage request) {
        actionHistoryService.getMatchHistory(request);
    }
}
