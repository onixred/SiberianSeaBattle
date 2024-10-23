package ru.cbr.siberian.sea.battle.model.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description: Запрос на создание игрока
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 17.09.2024
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateUserRequestMessage extends BaseRequestMessage {
    /**
     * Логин игрока
     */
    @NotBlank
    private String username;
    /**
     * Пароль игрока
     */
    @NotBlank
    private String password;

    /**
     * Идентификатор канала
     */
    @NotBlank
    private String chanelId;
}
