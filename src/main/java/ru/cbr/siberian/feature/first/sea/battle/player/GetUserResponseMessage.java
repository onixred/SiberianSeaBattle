package ru.cbr.siberian.feature.first.sea.battle.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.cbr.siberian.feature.first.sea.battle.common.BaseResponseMessage;


/**
 * Description: Ответ информация пользователя
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 17.09.2024
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetUserResponseMessage extends BaseResponseMessage {
    /**
     * Идентификатор игрока
     */
    private String userId;
    /**
     * Идентификатор канала
     */
    private String chanelId;
}
