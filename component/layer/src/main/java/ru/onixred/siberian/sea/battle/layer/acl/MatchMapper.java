package ru.onixred.siberian.sea.battle.layer.acl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.onixred.siberian.sea.battle.layer.dao.MatchDao;
import ru.onixred.siberian.sea.battle.layer.dao.PlayerDao;
import ru.onixred.siberian.sea.battle.layer.model.Match;
import ru.onixred.siberian.sea.battle.layer.model.Player;
import ru.onixred.siberian.sea.battle.layer.model.enumeration.MatchStatus;

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
