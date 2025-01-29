package ru.onixred.siberian.sea.battle.layer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.onixred.siberian.sea.battle.layer.model.enumeration.MatchStatus;
import ru.onixred.siberian.sea.battle.layer.dao.MatchDao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchRepository extends CrudRepository<MatchDao, UUID> {

    @NonNull
    List<MatchDao> findAll();

    List<MatchDao> findAllByStatus(MatchStatus matchStatus);

    Optional<MatchDao> findByOwner_idAndStatusNot(UUID playerId, MatchStatus matchStatus);

    Optional<MatchDao> findByOpponent_idAndStatusNot(UUID playerId, MatchStatus matchStatus);

    List<MatchDao> findAllByStatusIn(List<MatchStatus> matchStatuses);
}
