package ru.onixred.siberian.sea.battle.layer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
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
