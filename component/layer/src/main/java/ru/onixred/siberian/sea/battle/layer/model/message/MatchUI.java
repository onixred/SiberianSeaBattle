package ru.onixred.siberian.sea.battle.layer.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.onixred.siberian.sea.battle.layer.model.enumeration.MatchStatus;

import java.util.UUID;

/**
 * Description: Структура игры
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 25.09.2024
 */
@Data
@AllArgsConstructor
public class MatchUI {

    /**
     *  Идентификатор игры
     */
    private UUID id;

    /**
     *  Размер поля
     */
    private Integer sizeGrid;

    /**
     *  Логин владельца игры
     */
    private String ownerName;

    /**
     *  Логин соперника игры
     */
    private String opponentName;

    /**
     *  Логин победителя
     */
    private String winnerName;

    /**
     * Статус игры
     */
    private MatchStatus matchStatus;
}
