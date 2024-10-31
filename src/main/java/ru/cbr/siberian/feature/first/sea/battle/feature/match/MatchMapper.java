package ru.cbr.siberian.feature.first.sea.battle.feature.match;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.cbr.siberian.sea.battle.dao.MatchDao;
import ru.cbr.siberian.sea.battle.dao.PlayerDao;
import ru.cbr.siberian.sea.battle.model.Match;
import ru.cbr.siberian.sea.battle.model.Player;
import ru.cbr.siberian.sea.battle.model.enumeration.MatchStatus;

@Component
@RequiredArgsConstructor
public class MatchMapper {
    private final ModelMapper modelMapper;
    public Match matchMapper(MatchDao dao) {
      return  modelMapper.map(dao, Match.class);
    }

    public MatchDao createMapper(Player owner, int sizeGrid) {
        PlayerDao ownerDao = modelMapper.map(owner, PlayerDao.class);
        MatchDao matchDao = new MatchDao();
        matchDao.setOwner(ownerDao);
        matchDao.setSizeGrid(sizeGrid);
        matchDao.setStatus(MatchStatus.WAIT);
        return matchDao;
    }

    public MatchDao matchMapperDao(Match match) {
        return modelMapper.map(match, MatchDao.class);
    }
}
