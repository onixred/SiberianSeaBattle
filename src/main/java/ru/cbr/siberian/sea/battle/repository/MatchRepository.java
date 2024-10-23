package ru.cbr.siberian.sea.battle.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.cbr.siberian.sea.battle.model.message.MatchStatus;
import ru.cbr.siberian.sea.battle.dao.MatchDao;

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
