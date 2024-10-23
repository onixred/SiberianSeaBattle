package ru.cbr.siberian.sea.battle.model.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description: Нотификация о расстановки кораблей для соперника
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 18.09.2024
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateUserResponseMessage extends  BaseResponseMessage {
    /**
     * Идентификатор игрока
     */
    private String userId;
    /**
     * Идентификатор канала
     */
    private String chanelId;
}
