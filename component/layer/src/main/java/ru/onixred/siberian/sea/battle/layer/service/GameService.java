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
package ru.onixred.siberian.sea.battle.layer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.onixred.siberian.sea.battle.layer.model.game.*;

import java.util.*;

/**
 * Description: Сервис по работе с логикой игры
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 09.09.2024
 */
@Slf4j
@Service
public class GameService {

    public static final String ERROR_GENERATE_FLEET = "Алгоритм не смог расставить флот на поле";
    public static final int MINI_SIZE = 5;
    private final Random rand = new Random();
    List<WarshipDescription> warshipDescriptionsForRegular = List.of(
            new WarshipDescription(1, 4),
            new WarshipDescription(2, 3),
            new WarshipDescription(3, 2),
            new WarshipDescription(4, 1)
    );
    List<WarshipDescription> warshipDescriptionsForMini = List.of(
            new WarshipDescription(1, 3),
            new WarshipDescription(1, 2),
            new WarshipDescription(2, 1)
    );

    /**
     * Метод генерации флота и карты
     *
     * @param xSize размкер карты по OX
     * @param ySize размкер карты по OY
     * @return флот
     */
    public Fleet getFleet(int xSize, int ySize) {
        final List<WarshipDescription> warshipDescriptions = getWarshipDescriptions(xSize, ySize);
        RuntimeException exception = new RuntimeException(ERROR_GENERATE_FLEET);
        for (int count = 0; count < 10; count++) {
            try {
                return getFleet(xSize, ySize, warshipDescriptions);
            } catch (RuntimeException e) {
                exception = e;
            }
        }
        throw exception;

    }

    private List<WarshipDescription> getWarshipDescriptions(int xSize, int ySize) {
        final List<WarshipDescription> warshipDescriptions;
        if (xSize <= MINI_SIZE | ySize <= MINI_SIZE) {
            warshipDescriptions = warshipDescriptionsForMini;
        } else {
            warshipDescriptions = warshipDescriptionsForRegular;
        }
        return warshipDescriptions;
    }

    private Fleet getFleet(int xSize, int ySize, List<WarshipDescription> warshipDescriptions) {
        Fleet fleet = new Fleet();
        GridPoint[][] grids = new GridPoint[xSize][ySize];
        initEmptyGrids(grids);
        for (WarshipDescription warshipDescription : warshipDescriptions) {
            for (int warshipCount = 0; warshipCount < warshipDescription.count(); warshipCount++) {
                Warship warship = getWarship(grids, warshipDescription.size());
                fleet.addWarship(warship);
            }
        }
        fleet.setGrids(grids);
        return fleet;
    }

    /**
     * Метод проверки расстановки флота от Пользователя
     *
     * @param grids поле с флотом
     *              Условные обозначения число размер корабля (палубы)
     *              0 - пустая клетка
     * @return Возвращает структуру с информацией, где кривые данные
     */
    public CustomFleet checkCustomFleet(int[][] grids) {
        CustomFleet customFleet = new CustomFleet();
        customFleet.setStatus(true);
        customFleet.setErrorGrids(new int[grids.length][grids[0].length]);
        initEmptyGrids(customFleet.getErrorGrids());
        customFleet.setFleet(new Fleet());
        GridPoint[][] fleetGrids = new GridPoint[grids.length][grids[0].length];
        customFleet.getFleet().setGrids(fleetGrids);
        initEmptyGrids(fleetGrids);
        for (int oy = 0; oy < grids.length; oy++) {
            for (int ox = 0; ox < grids[0].length; ox++) {
                int startSize = grids[oy][ox];
                if (startSize != 0) {
                    //Создаем палубу с первой точкой
                    Warship warship = new Warship(new Point(ox, oy), new Point(-1, -1), 0, 0);
                    int checkX = ox + 1;
                    final boolean directionOX = checkX < grids[0].length && grids[oy][checkX] != 0;
                    //выкусываем найденный кораблик
                    int endX = ox;
                    int endY = oy;
                    int checkEndX = ox;
                    int checkEndY = oy;
                    int size = 0;
                    do {
                        int checkSize = grids[checkEndY][checkEndX];
                        if (checkSize != 0) {
                            size++;
                            fleetGrids[checkEndY][checkEndX].setWarship(Optional.of(warship));
                            //проверка точки
                            boolean isFirst = ox == checkEndX && oy == checkEndY;
                            boolean isLast = size == grids[checkEndY][checkEndX];
                            boolean isInvalidPosition = isInvalidPositionAround(isFirst, isLast, directionOX, checkEndY,
                                    checkEndX, fleetGrids, 1);

                            boolean isDiffSize = startSize != checkSize;
                            if (isDiffSize) {
                                size--;
                            }
                            if (isInvalidPosition || isDiffSize) {
                                customFleet.setStatus(false);
                                customFleet.setErrorMessage("Ошибка в расстановке кораблей");
                                customFleet.getErrorGrids()[checkEndY][checkEndX] = Point.NUMBER_TO_ERROR;
                                break;
                            } else {
                                customFleet.getErrorGrids()[checkEndY][checkEndX] = grids[checkEndY][checkEndX];
                            }
                            endX = checkEndX;
                            endY = checkEndY;
                            grids[checkEndY][checkEndX] = 0;
                        } else {
                            break;
                        }
                        if (directionOX) {
                            checkEndX = checkEndX + 1;
                        } else {
                            checkEndY = checkEndY + 1;
                        }
                    } while (checkEndX < grids[0].length && checkEndY < grids.length);
                    warship.setEnd(new Point(endX, endY));
                    warship.setSize(size);
                    warship.setLives(size);
                    customFleet.getFleet().addWarship(warship);
                }
            }
        }
        if (customFleet.getFleet().getWarships() == null) {
            customFleet.getFleet().setWarships(new ArrayList<>());
            customFleet.setStatus(false);
            customFleet.setErrorMessage("Ошибка в расстановке кораблей");
        }
        final List<WarshipDescription> warshipDescriptions = getWarshipDescriptions(grids[0].length, grids.length);

        Map<Integer, Integer> sizeToCount = new HashMap<>();
        for(Warship warship: customFleet.getFleet().getWarships()) {
            Integer count = sizeToCount.computeIfAbsent(warship.getSize(), (s)->0);
            sizeToCount.put(warship.getSize(), 1 + count);

        }
        for(WarshipDescription description: warshipDescriptions) {
           Integer count = sizeToCount.get(description.size());
           if(count == null || !count.equals(description.count())) {
               customFleet.setStatus(false);
               customFleet.setErrorMessage(String.format("Ошибка в количестве кораблей, их должно быть %s размером %s", description.count(), description.size()));
               return customFleet;
           }

        }
        int sum = warshipDescriptions.stream().mapToInt(WarshipDescription::count).sum();
        //Проверка, что кораблей нужное количество
        if(customFleet.getFleet().getWarships().size() < sum) {
            customFleet.setStatus(false);
            customFleet.setErrorMessage("Ошибка в количестве кораблей, их должно быть " + sum);
        }

        return customFleet;
    }

    /**
     * Проверить выстрел
     * Внимание структура флота будет включить выстрел (произойдет изменение во флоте)
     *
     * @return true если выстрел попал, false - промах
     */
    public boolean checkShot(Fleet fleet, int x, int y) {
        GridPoint gridPoint = fleet.getGrids()[y][x];
        Optional<Warship> warshipOpt = gridPoint.getWarship();
        gridPoint.setExplored(true);

        if (warshipOpt.isEmpty()) {
            return false;
        }

        Warship warship = warshipOpt.get();
        if (warship.hit()) {
            kill(warship, fleet.getGrids());
        }

        return true;
    }

    /**
     * Убить корабль (вокруг корабля пометить все клетки)
     *
     * @param warship корабль
     * @param grids   поле
     */
    private void kill(Warship warship, GridPoint[][] grids) {
        final int x1;
        final int x2;
        final int y1;
        final int y2;
        //Алгоритм вычисляет x1 y1 самую левую и верхнюю точку (минимальные)
        final boolean directionOX;
        if (warship.getStart().x() == warship.getEnd().x()) {
            directionOX = true;
            if (warship.getStart().y() < warship.getEnd().y()) {
                x1 = warship.getStart().x();
                y1 = warship.getStart().y();

                x2 = warship.getEnd().x();
                y2 = warship.getEnd().y();
            } else {
                x1 = warship.getEnd().x();
                y1 = warship.getEnd().y();

                x2 = warship.getStart().x();
                y2 = warship.getStart().y();
            }

        } else {
            directionOX = false;
            if (warship.getStart().x() < warship.getEnd().x()) {
                x1 = warship.getStart().x();
                y1 = warship.getStart().y();

                x2 = warship.getEnd().x();
                y2 = warship.getEnd().y();
            } else {
                x1 = warship.getEnd().x();
                y1 = warship.getEnd().y();

                x2 = warship.getStart().x();
                y2 = warship.getStart().y();
            }
        }
        for (int oy = y1; oy <= y2; oy++) {
            for (int ox = x1; ox <= x2; ox++) {
                // первая клетка
                if (ox == x1 && oy == y1) {
                    if (directionOX) {
                        int checkY = oy - 1;
                        int checkX = ox - 1;
                        if (checkX >= 0 && checkY >= 0) {
                            grids[checkY][checkX].setExplored(true);
                        }
                        checkY = oy + 1;
                        if (checkX >= 0 && checkY < grids.length) {
                            grids[checkY][checkX].setExplored(true);
                        }
                        if (checkX >= 0) {
                            grids[oy][checkX].setExplored(true);
                        }
                    } else {
                        int checkX = ox - 1;
                        int checkY = oy - 1;
                        if (checkY >= 0 && checkX >= 0) {
                            grids[checkY][checkX].setExplored(true);
                        }
                        checkX = ox + 1;
                        if (checkY >= 0 && checkX < grids[0].length) {
                            grids[checkY][checkX].setExplored(true);
                        }
                        if (checkY >= 0) {
                            grids[checkY][ox].setExplored(true);
                        }
                    }
                }
                if (directionOX) {
                    //клетку ниже
                    int checkY = oy + 1;
                    if (checkY < grids.length) {
                        grids[checkY][ox].setExplored(true);
                    }
                    //клетку выше
                    checkY = oy - 1;
                    if (checkY >= 0) {
                        grids[checkY][ox].setExplored(true);
                    }
                } else {
                    //клетку правее
                    int checkX = ox + 1;
                    if (checkX < grids[0].length) {
                        grids[oy][checkX].setExplored(true);
                    }
                    //клетку левее
                    checkX = ox - 1;
                    if (checkX >= 0) {
                        grids[oy][checkX].setExplored(true);
                    }
                }
                //последняя клетка
                if (ox == x2 && oy == y2) {
                    if (directionOX) {
                        int checkY = oy - 1;
                        int checkX = ox + 1;
                        if (checkX < grids[0].length && checkY >= 0) {
                            grids[checkY][checkX].setExplored(true);
                        }
                        checkY = oy + 1;
                        if (checkX < grids[0].length && checkY < grids.length) {
                            grids[checkY][checkX].setExplored(true);
                        }
                        if (checkX < grids[0].length) {
                            grids[oy][checkX].setExplored(true);
                        }
                    } else {
                        int checkX = ox - 1;
                        int checkY = oy + 1;
                        if (checkY < grids.length && checkX >= 0) {
                            grids[checkY][checkX].setExplored(true);
                        }
                        checkX = ox + 1;
                        if (checkY < grids.length && checkX < grids[0].length) {
                            grids[checkY][checkX].setExplored(true);
                        }
                        if (checkY < grids.length) {
                            grids[checkY][ox].setExplored(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Метод заполнения поля пустотой
     *
     * @param grids поле боя
     */
    private static void initEmptyGrids(GridPoint[][] grids) {
        //Заполняем поля пустыми клетками
        for (GridPoint[] points : grids) {
            for (int ox = 0; ox < points.length; ox++) {
                points[ox] = new GridPoint(Optional.empty(), false);
            }
        }
    }

    /**
     * Метод заполнения поля пустотой
     *
     * @param grids поле боя
     */
    public static void initEmptyGrids(int[][] grids) {
        //Заполняем поля пустыми клетками
        for (int[] points : grids) {
            Arrays.fill(points, 0);
        }
    }

    private Warship getWarship(GridPoint[][] grids, int size) {
        //TODO Нужно добавить разные алгоритмы расстановки
        int minX = 0;
        int minY = 0;
        Optional<Warship> warship;
        boolean directionOX;
        int count  = 0;
        do {
            if(count > 1_000_000) {
                print(grids);
                throw new RuntimeException(ERROR_GENERATE_FLEET);
            }
            directionOX = rand.nextBoolean();
            int maxX = grids[0].length - (directionOX ? size : 1);
            int maxY = grids.length - (directionOX ? 1 : size);
            int x = getRandom(maxX, minX);
            int y = getRandom(maxY, minY);
            warship = getWarship(x, y, directionOX, grids, size);
            count++;
        } while (warship.isEmpty());

        return warship.get();
    }

    private Optional<Warship> getWarship(int x, int y, boolean directionOX, GridPoint[][] grids, int size) {
        Optional<Warship> warship = getWarship(x, y, directionOX, grids, size, 1);
        if (warship.isPresent()) {
            return warship;
        }

        return getWarship(x, y, directionOX, grids, size, -1);
    }

    private Optional<Warship> getWarship(int x, int y, boolean directionOX, GridPoint[][] grids, int size, int direction) {
        int maxX = x + (size * direction);
        if (directionOX && maxX > grids[0].length || maxX < -1) {
            return Optional.empty();
        }
        int maxY = y + (size * direction);
        if (!directionOX && maxY > grids.length || maxY < -1) {
            return Optional.empty();
        }

        for (int index = 0; index < size; index++) {
            if (directionOX) {
                int checkX = x + (index * direction);
                if (grids[y][checkX].getWarship().isPresent()) {
                    return Optional.empty();
                }
                if (isInvalidPositionAround(index == 0, index == size - 1, directionOX, y, checkX, grids, direction)) {
                    return Optional.empty();
                }
            } else {
                int checkY = y + (index * direction);
                if (grids[checkY][x].getWarship().isPresent()) {
                    return Optional.empty();
                }
                if (isInvalidPositionAround(index == 0, index == size - 1, directionOX, checkY, x, grids, direction)) {
                    return Optional.empty();
                }
            }
        }
        Point end;
        if (directionOX) {
            end = new Point(x + ((size - 1) * direction), y);
        } else {
            end = new Point(x, y + ((size - 1) * direction));
        }

        Warship warship = new Warship(new Point(x, y), end, size, size);
        //После проверки нужно пометить поле
        for (int index = 0; index < size; index++) {
            if (directionOX) {
                int checkX = x + (index * direction);
                grids[y][checkX].setWarship(Optional.of(warship));
            } else {
                int checkY = y + (index * direction);
                grids[checkY][x].setWarship(Optional.of(warship));
            }
        }

        return Optional.of(warship);
    }

    private boolean isInvalidPositionAround(boolean isFirst, boolean isLast, boolean directionOX, int y, int x,
                                            GridPoint[][] grids, int direction) {
        if (direction < 0) {
            //Если направление обратное, то поменять местами признаки начала и конца
            boolean tmp = isFirst;
            isFirst = isLast;
            isLast = tmp;
        }
        if (directionOX) {
            //проверяем клетку ниже
            int checkY = y + 1;
            if (checkY < grids.length && grids[checkY][x].getWarship().isPresent()) {
                return true;
            }
            //проверяем клетку выше
            checkY = y - 1;
            if (checkY >= 0 && grids[checkY][x].getWarship().isPresent()) {
                return true;
            }
            //если это первая клетка проверяем левее 3 клетки
            if (isFirst) {
                checkY = y - 1;
                int checkX = x - 1;
                if (checkX >= 0 && checkY >= 0 && grids[checkY][checkX].getWarship().isPresent()) {
                    return true;
                }
                checkY = y + 1;
                if (checkX >= 0 && checkY < grids.length && grids[checkY][checkX].getWarship().isPresent()) {
                    return true;
                }
                if (checkX >= 0 && grids[y][checkX].getWarship().isPresent()) {
                    return true;
                }
            }
            //если это последняя клетка проверяем правые 3 клетки
            if (isLast) {
                checkY = y - 1;
                int checkX = x + 1;
                if (checkX < grids[0].length && checkY >= 0 && grids[checkY][checkX].getWarship().isPresent()) {
                    return true;
                }
                checkY = y + 1;
                if (checkX < grids[0].length && checkY < grids.length && grids[checkY][checkX].getWarship().isPresent()) {
                    return true;
                }

                return checkX < grids[0].length && grids[y][checkX].getWarship().isPresent();
            }

        } else {
            //проверяем клетку правее
            int checkX = x + 1;
            if (checkX < grids[0].length && grids[y][checkX].getWarship().isPresent()) {
                return true;
            }
            //проверяем клетку левее
            checkX = x - 1;
            if (checkX >= 0 && grids[y][checkX].getWarship().isPresent()) {
                return true;
            }
            //если это первая клетка проверяем выше 3 клетки
            if (isFirst) {
                checkX = x - 1;
                int checkY = y - 1;
                if (checkY >= 0 && checkX >= 0 && grids[checkY][checkX].getWarship().isPresent()) {
                    return true;
                }
                checkX = x + 1;
                if (checkY >= 0 && checkX < grids[0].length && grids[checkY][checkX].getWarship().isPresent()) {
                    return true;
                }
                if (checkY >= 0 && grids[checkY][x].getWarship().isPresent()) {
                    return true;
                }
            }
            //если это последняя клетка проверяем ниже 3 клетки
            if (isLast) {
                checkX = x - 1;
                int checkY = y + 1;
                if (checkY < grids.length && checkX >= 0 && grids[checkY][checkX].getWarship().isPresent()) {
                    return true;
                }
                checkX = x + 1;
                if (checkY < grids.length && checkX < grids[0].length && grids[checkY][checkX].getWarship().isPresent()) {
                    return true;
                }

                return checkY < grids.length && grids[checkY][x].getWarship().isPresent();
            }
        }

        return false;
    }

    private int getRandom(int max, int min) {
        return rand.nextInt(max - min + 1) + min;
    }

    public static void print(GridPoint[][] grids) {
        for (int oy = 0; oy < grids.length; oy++) {
            if (oy == 0) {
                log.debug("    1,2,3,4,5,6,7,8,9,10");
                log.debug("   ---------------------");
            }
            String line = getOxLine(grids,
                    oy);
            log.debug(line);
        }
    }

    private static String getOxLine(GridPoint[][] grids, int oy) {
        StringBuilder stRor = new StringBuilder();
        GridPoint[] pointsOx = grids[oy];
        for (int ox = 0; ox < pointsOx.length; ox++) {
            if (ox == 0) {
                if (oy < 9) {
                    stRor.append("0");
                }
                stRor.append((oy + 1));
                stRor.append(" |");
            }
            GridPoint point = pointsOx[ox];
            Optional<Warship> warship = point.getWarship();
            String pointX;
            pointX = warship.map(value -> value.getLives() == 0 ? "x" : "" + value.getSize())
                    .orElseGet(() -> point.isExplored() ? "x" : "*");
            stRor.append(pointX);
            stRor.append(" ");
        }
        return stRor.toString();
    }
}
