package ru.onixred.siberian.sea.battle.feature.match;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

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
