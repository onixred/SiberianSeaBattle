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

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Description: Военный корабль
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 09.09.2024
 */
@Data
@AllArgsConstructor
public final class Warship {
    /**
     * Начальная точка
     */
    private final Point start;
    /**
     * Конечная точка
     */
    private Point end;
    /**
     * Размер корабля
     */
    private int size;
    /**
     * Количество живых точек
     */
    private int lives;

    /**
     * Нанести удар
     * @return true - если убил, false - если живой
     */
    public boolean hit() {
        lives--;
        return isKill();
    }
    public boolean isKill() {
        return lives == 0;
    }
}
