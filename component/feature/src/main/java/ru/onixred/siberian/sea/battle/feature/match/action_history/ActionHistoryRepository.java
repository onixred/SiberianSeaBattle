package ru.onixred.siberian.sea.battle.feature.match.action_history;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;

@Repository
public interface ActionHistoryRepository extends CrudRepository<ActionHistoryDao, UUID> {

    @NonNull
    List<ActionHistoryDao> findAll();

    List<ActionHistoryDao> findAllByMatchId(UUID id);

    List<ActionHistoryDao> findAllByPlayerId(UUID id);

}
