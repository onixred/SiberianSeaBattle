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

    private UUID id;

    private Integer sizeGrid;

    private Player owner;


    private Player opponent;

    private Player winner;

    private MatchStatus status;

}
