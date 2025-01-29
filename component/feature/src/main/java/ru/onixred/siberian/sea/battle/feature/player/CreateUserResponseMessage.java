package ru.onixred.siberian.sea.battle.feature.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.onixred.siberian.sea.battle.feature.common.BaseResponseMessage;


/**
 * Description: Нотификация о расстановки кораблей для соперника
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 18.09.2024
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateUserResponseMessage extends BaseResponseMessage {
    /**
     * Идентификатор игрока
     */
    private String userId;
    /**
     * Идентификатор канала
     */
    private String chanelId;
}
