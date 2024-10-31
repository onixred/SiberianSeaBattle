package ru.cbr.siberian.feature.first.sea.battle.feature.player;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.cbr.siberian.sea.battle.model.message.CreateUserRequestMessage;

@Controller
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @MessageMapping("/see-battle/create-user/request")
    public void createUser(CreateUserRequestMessage request) {
        playerService.createUser(request);
    }

    @MessageMapping("/see-battle/get-user/request")
    public void getUser(CreateUserRequestMessage request) {
        playerService.getUser(request);
    }
}
