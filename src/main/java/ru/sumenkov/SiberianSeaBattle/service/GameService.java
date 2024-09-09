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
package ru.sumenkov.SiberianSeaBattle.service;

import org.springframework.stereotype.Service;
import ru.sumenkov.SiberianSeaBattle.model.Fleet;
import ru.sumenkov.SiberianSeaBattle.model.Point;
import ru.sumenkov.SiberianSeaBattle.model.Warship;
import ru.sumenkov.SiberianSeaBattle.model.WarshipDescription;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Description: Сервис по работе с логикой игры
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 09.09.2024
 */
@Service
public class GameService {

    private Random rand = new Random();
    List<WarshipDescription> warshipDescriptions = List.of(
            new WarshipDescription(1,
                                   4),
            new WarshipDescription(2,
                                   3),
            new WarshipDescription(3,
                                   2),
            new WarshipDescription(4,
                                   1)
                                                          );


    public Fleet getFleet(int xSize, int ySize) {
        Fleet fleet = new Fleet();
        Integer[][] grids = new Integer[xSize][ySize];
        for (WarshipDescription warshipDescription : warshipDescriptions) {
            for (int warshipCount = 0; warshipCount < warshipDescription.count(); warshipCount++) {
                Warship warship = getWarship(grids,
                                             warshipDescription.size());
                fleet.addWarship(warship);
            }
        }
        return fleet;
    }

    private Warship getWarship(Integer[][] grids, int size) {
        //TODO Нужно добавить разные алгоритмы растанновки
        int minX = 0;
        int minY = 0;
        Point start;
        Optional<Point> end;
        boolean directionOX;
        do {
            directionOX = rand.nextBoolean();
            int maxX = grids[0].length - (directionOX ? size : 1);
            int maxY = grids.length - (directionOX ? 1 : size);
            int x = getRandom(maxX,
                              minX);
            int y = getRandom(maxY,
                              minY);
            end = getPosition(x,
                              y,
                              directionOX,
                              grids,
                              size);
            start = new Point(x,
                              y);
        } while (end.isEmpty());

        return new Warship(start, end.get());
    }

    private Optional<Point> getPosition(int x, int y, boolean directionOX, Integer[][] grids, int size) {
        Optional<Point> end =  getPosition(x, y, directionOX, grids, size, 1);
        if(end.isPresent()) {
            return end;
        }
        return getPosition(x, y, directionOX, grids, size, -1);

    }
    private Optional<Point> getPosition(int x, int y, boolean directionOX, Integer[][] grids, int size,
                                        int direction) {
        int maxX = x + (size * direction);
        if (directionOX && maxX > grids[0].length || maxX < -1) {
            return Optional.empty();
        }
        int maxY = y + (size * direction);
        if (!directionOX && maxY > grids.length || maxY < -1) {
            return Optional.empty();
        }
        //TODO добавить проверку ВОКРУГ  лодки чтобы было ПУСТО!!
        for (int index = 0; index < size; index++) {
            if (directionOX) {
                int checkX = x + (index * direction);
                if (grids[y][checkX] != null) {
                    return Optional.empty();
                }
            } else {
                int checkY = y + (index * direction);
                if (grids[checkY][x] != null) {
                    return Optional.empty();
                }
            }
        }

        //После провкерки нужно пометить поле
        for (int index = 0; index < size; index++) {
            if (directionOX) {
                int checkX = x + (index * direction);
                grids[y][checkX] = size;
            } else {
                int checkY = y + (index * direction);
                grids[checkY][x] = size;
            }
        }

        if(directionOX)  {
            return Optional.of(new Point(x + ((size-1) * direction), y));
        } else {
            return Optional.of(new Point(x, y + ((size-1) * direction)));
        }
    }



    private int getRandom(int max, int min) {
        return rand.nextInt(max - min + 1) + min;
    }


}
