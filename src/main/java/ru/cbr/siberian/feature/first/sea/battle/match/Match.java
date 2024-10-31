package ru.cbr.siberian.feature.first.sea.battle.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.cbr.siberian.feature.first.sea.battle.player.Player;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Match {

    /**
     *  Идентификатор игры
     */
    private UUID id;

    /**
     *  Размер поля
     */
    private Integer sizeGrid;

    /**
     *  Владелец игры
     */
    private Player owner;

    /**
     *  Соперник в игре
     */
    private Player opponent;

    /**
     *  Победитель
     */
    private Player winner;

    /**
     *  Статус игры
     */
    private MatchStatus status;

}
