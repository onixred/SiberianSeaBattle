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
package ru.onixred.siberian.sea.battle.layer.model.game;

/**
 * Description: Точка
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 09.09.2024
 */
public record Point(int x, int y) {

    /**
     *  Код пустой клетки
     */
    public final static int NUMBER_TO_VOID = 0;
    /**
     *  Код промаха
     */
    public final static int NUMBER_TO_MISS = -100;
    /**
     * Код убийства (возможно нужно писать количество палуб со знаком минус)
     */
    public final static int NUMBER_TO_KILL = -200;
    /**
     * Код попадания, но чтобы не раскрывать размер говорим этот код
     */
    public final static int NUMBER_TO_HIT = -300;
    /**
     * Код тумана войны для соперника, чтобы не раскрывать карту
     */
    public final static int NUMBER_TO_FOG_WON  = -400;
    /**
     * Код прострела корабля когда его убили\потопили
     */
    public final static int NUMBER_TO_SHOOTING_AROUND = -500;
    /**
     * Код ошибки, например в эту точку нельзя поставить корабль или повторно попасть
     */
    public final static int NUMBER_TO_ERROR = -777;

}
