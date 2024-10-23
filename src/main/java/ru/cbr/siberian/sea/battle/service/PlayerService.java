package ru.cbr.siberian.sea.battle.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cbr.siberian.sea.battle.dao.PlayerDao;
import ru.cbr.siberian.sea.battle.model.Player;
import ru.cbr.siberian.sea.battle.repository.PlayerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final ModelMapper modelMapper;



    @Transactional
    public Player createPlayer(String name, String password, UUID chanelId) {
        PlayerDao playerDao =  new PlayerDao();
        playerDao.setName(name);
        playerDao.setPassword(password);
        playerDao.setChanelId(chanelId);
        playerDao = playerRepository.save(playerDao);

        return modelMapper.map(playerDao, Player.class);
    }

    @Transactional
    public Optional<Player> getPlayer(String id) {
        return getPlayer(UUID.fromString(id));
    }

    @Transactional
    public Optional<Player> getPlayerByName(String name) {
        return playerRepository.findByName(name)
                .map(playerDao -> modelMapper.map(playerDao, Player.class));
    }

    @Transactional
    public Optional<Player> getPlayer(UUID id) {
        return playerRepository.findById(id)
                .map(playerDao -> modelMapper.map(playerDao, Player.class));
    }

    @Transactional
    public List<Player> getAllPlayers() {
        return playerRepository.findAll()
                .stream()
                .map(playerDao -> modelMapper.map(playerDao, Player.class))
                .toList();
    }

    @Transactional
    public void updatePlayer(Player player) {
        PlayerDao playerDao = modelMapper.map(player, PlayerDao.class);
        playerRepository.save(playerDao);
    }

}
