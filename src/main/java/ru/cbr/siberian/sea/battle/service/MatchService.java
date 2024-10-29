package ru.cbr.siberian.sea.battle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cbr.siberian.sea.battle.acl.MatchMapper;
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

    private final MatchMapper matchMapper;

    @Transactional
    public Match createMatch(Player owner, int sizeGrid) {

        var match = matchRepository.save(matchMapper.createMapper(owner, sizeGrid));

        return matchMapper.matchMapper(match);
    }

    @Transactional
    public Optional<Match> getMatchById(String id) {
        return getMatchById(UUID.fromString(id));
    }

    @Transactional
    public Optional<Match> getMatchById(UUID id) {
        return matchRepository.findById(id)
                .map(matchMapper::matchMapper);
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
        var matchDao =  matchMapper.matchMapperDao(match);
        matchRepository.save(matchDao);
    }

    @Transactional
    public List<Match> getAllMatchesByStatus(MatchStatus matchStatus) {
        var  matches = switch (matchStatus) {
            case ALL -> matchRepository.findAll();
            case IN_PROGRESS ->  matchRepository.findAllByStatusIn(List.of(MatchStatus.IN_PROGRESS,
                    MatchStatus.IN_PROGRESS_WAIT_FLEET_OWNER, MatchStatus.IN_PROGRESS_WAIT_FLEET_OPPONENT,
                    MatchStatus.START_GAME));
            default -> matchRepository.findAllByStatus(matchStatus);
        };

        return matches.stream()
                .map(matchMapper::matchMapper)
                .toList();
     }

    public Optional<Match> getWaitMatchByPlayerId(UUID playerId) {
        var match = matchRepository.findByOwner_idAndStatusNot(playerId, MatchStatus.COMPLETED);
        if (match.isEmpty()) {
            match = matchRepository.findByOpponent_idAndStatusNot(playerId, MatchStatus.COMPLETED);
        }

        return match.map(matchMapper::matchMapper);
    }
}
