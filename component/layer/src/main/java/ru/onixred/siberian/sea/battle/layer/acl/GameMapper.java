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
package ru.onixred.siberian.sea.battle.layer.acl;

import org.springframework.stereotype.Component;
import ru.onixred.siberian.sea.battle.layer.model.game.GridPoint;
import ru.onixred.siberian.sea.battle.layer.model.game.Point;
import ru.onixred.siberian.sea.battle.layer.model.game.Warship;

import java.util.Optional;

/**
 * Description: Маппер для работы с логикой игры
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 15.09.2024
 */
@Component
public class GameMapper {

    /**
     * Возвращает карту для владелца поля
     *
     * @param grids карта
     * @return карта боя
     */
    public int[][] toGridsForOwner(GridPoint[][] grids) {
        return toGrids(grids, true);
    }

    /**
     * Возвращает карту для соперника или наблюдателя (чужую карту с туманом войны)
     *
     * @param grids карта
     * @return карта боя
     */
    public int[][] toGridsForOpponent(GridPoint[][] grids) {
        return toGrids(grids, false);
    }

    private static int[][] toGrids(GridPoint[][] grids, boolean isOwner) {

        int[][] result = new int[grids.length][grids[0].length];

        for (int oy = 0; oy < grids.length; oy++) {
            for (int ox = 0; ox < grids[0].length; ox++) {
                result[oy][ox] = isOwner ? getPoint(grids[oy][ox].getWarship())
                        : grids[oy][ox].isExplored() ? getPoint(grids[oy][ox].getWarship()) : Point.NUMBER_TO_FOG_WON;
            }
        }

        return result;
    }

    private static int getPoint(Optional<Warship> warship) {
        return warship.map(Warship::getSize).orElse(Point.NUMBER_TO_VOID);
    }
}
