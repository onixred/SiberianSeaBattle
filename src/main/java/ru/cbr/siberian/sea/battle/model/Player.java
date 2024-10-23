package ru.cbr.siberian.sea.battle.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    /**
     *  Идентификатор игрока
     */
    private UUID id;
    /**
     *  Идентификатор канала
     */
    private UUID chanelId;
    /**
     *  Логин игрока
     */
    private String name;
    /**
     *  Пароль игрока
     */
    private String password;

}
