package ru.cbr.siberian.sea.battle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cbr.siberian.sea.battle.acl.PlayerMapper;
import ru.cbr.siberian.sea.battle.model.Player;
import ru.cbr.siberian.sea.battle.repository.PlayerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper mapper;



    @Transactional
    public Player createPlayer(String name, String password, UUID chanelId) {
         var playerDao =  mapper.creteDao(name, password, chanelId);
        return mapper.map(playerRepository.save(playerDao));
    }

    @Transactional
    public Optional<Player> getPlayer(String id) {
        return getPlayer(UUID.fromString(id));
    }

    @Transactional
    public Optional<Player> getPlayerByName(String name) {
        return playerRepository.findByName(name)
                .map(mapper::map);
    }

    @Transactional
    public Optional<Player> getPlayer(UUID id) {
        return playerRepository.findById(id)
                .map(mapper::map);
    }

    @Transactional
    public List<Player> getAllPlayers() {
        return playerRepository.findAll()
                .stream()
                .map(mapper::map)
                .toList();
    }

    @Transactional
    public void updatePlayer(Player player) {
        var playerDao = mapper.mapDao(player);
        playerRepository.save(playerDao);
    }

}
