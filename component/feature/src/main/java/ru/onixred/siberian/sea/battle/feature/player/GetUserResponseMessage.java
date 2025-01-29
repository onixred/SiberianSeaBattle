package ru.onixred.siberian.sea.battle.feature.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.onixred.siberian.sea.battle.feature.common.BaseResponseMessage;


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
