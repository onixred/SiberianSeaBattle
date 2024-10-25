package ru.cbr.siberian.sea.battle.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cbr.siberian.sea.battle.acl.MatchMapper;
import ru.cbr.siberian.sea.battle.dao.MatchDao;
import ru.cbr.siberian.sea.battle.dao.PlayerDao;
import ru.cbr.siberian.sea.battle.model.Match;
import ru.cbr.siberian.sea.battle.model.Player;
import ru.cbr.siberian.sea.battle.model.enumeration.MatchStatus;
import ru.cbr.siberian.sea.battle.repository.MatchRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final ModelMapper modelMapper;
    private final MatchMapper matchMapper;

    @Transactional
    public Match createMatch(Player owner, int sizeGrid) {
        PlayerDao ownerDao = modelMapper.map(owner, PlayerDao.class);
        MatchDao matchDao = new MatchDao();
        matchDao.setOwner(ownerDao);
        matchDao.setSizeGrid(sizeGrid);
        matchDao.setStatus(MatchStatus.WAIT);

        matchRepository.save(matchDao);

        return modelMapper.map(matchDao, Match.class);
    }

    @Transactional
    public Optional<Match> getMatchById(String id) {
        return getMatchById(UUID.fromString(id));
    }

    @Transactional
    public Optional<Match> getMatchById(UUID id) {
        return matchRepository.findById(id)
                .map(matchDao -> modelMapper.map(matchDao, Match.class));
    }

    @Transactional
    public List<Match> getAllMatches() {
        return matchRepository.findAll()
                .stream()
                .map(matchMapper::matchMapper)
                .toList();
    }

    @Transactional
    public void updateMatch(Match match) {
        MatchDao matchDao = modelMapper.map(match, MatchDao.class);
        matchRepository.save(matchDao);
    }

    @Transactional
    public List<Match> getAllMatchesByStatus(MatchStatus matchStatus) {
        List<MatchDao> matches = switch (matchStatus) {
            case ALL -> matchRepository.findAll();
            case IN_PROGRESS ->  matchRepository.findAllByStatusIn(List.of(MatchStatus.IN_PROGRESS,
                    MatchStatus.IN_PROGRESS_WAIT_FLEET_OWNER, MatchStatus.IN_PROGRESS_WAIT_FLEET_OPPONENT,
                    MatchStatus.START_GAME));
            default -> matchRepository.findAllByStatus(matchStatus);
        };

        return matches.stream()
                .map(matchDao -> modelMapper.map(matchDao, Match.class))
                .toList();
     }

    public Optional<Match> getWaitMatchByPlayerId(UUID playerId) {
        var match = matchRepository.findByOwner_idAndStatusNot(playerId, MatchStatus.COMPLETED);
        if (match.isEmpty()) {
            match = matchRepository.findByOpponent_idAndStatusNot(playerId, MatchStatus.COMPLETED);
        }

        return match.map(m -> modelMapper.map(m, Match.class));
    }
}
