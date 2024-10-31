package ru.cbr.siberian.feature.first.sea.battle.feature.actio_history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionHistory {

    /**
     *  Идентификатор истории игры
     */
    private UUID id;

    /**
     *  Идентификатор игрока
     */
    private UUID playerId;

    /**
     *  Идентификатор игры
     */
    private UUID matchId;

    /**
     *  Точка удара по OX
     */
    private Integer x;
    /**
     *  Точка удара по OY
     */
    private Integer y;

}
