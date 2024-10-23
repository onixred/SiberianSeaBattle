/*
 *  Copyright 2023 Contributors to the Sports-club.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package ru.cbr.siberian.sea.battle.model.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description: Нотификация получение информации поля (для наблюдений)
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 22.09.2024
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GridsResponseMessage extends  BaseResponseMessage {

    /**
     * Поле первого игрока
     */
    private int[][] playerOneGrids;
    /**
     * Логин первого игрока
     */
    private String playerOneName;
    /**
     * Идентификатор первого игрока
     */
    private String playerOneId;
    /**
     * Поле второго игрока
     */
    private int[][] playerTwoGrids;
    /**
     * Логин второго игрока
     */
    private String playerTwoName;
    /**
     * Идентификатор второго игрока
     */
    private String playerTwoId;
    /**
     * Статус игры
     */
    private MatchStatus matchStatus;

}
