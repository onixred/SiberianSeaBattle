package ru.sumenkov.SiberianSeaBattle.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sumenkov.SiberianSeaBattle.dao.ActionHistoryDao;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActionHistoryRepository extends CrudRepository<ActionHistoryDao, UUID> {

    List<ActionHistoryDao> findAll();

    List<ActionHistoryDao> findAllByMatchId(UUID id);

    List<ActionHistoryDao> findAllByPlayerId(UUID id);

}