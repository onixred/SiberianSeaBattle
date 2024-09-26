package ru.sumenkov.SiberianSeaBattle.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sumenkov.SiberianSeaBattle.model.message.MatchStatus;

import java.util.UUID;

@Data
@Builder
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
