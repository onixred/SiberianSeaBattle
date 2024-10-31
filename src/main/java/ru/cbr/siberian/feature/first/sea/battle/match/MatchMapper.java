package ru.cbr.siberian.feature.first.sea.battle.match;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.cbr.siberian.feature.first.sea.battle.player.Player;
import ru.cbr.siberian.feature.first.sea.battle.player.PlayerMapper;


@Component
@RequiredArgsConstructor
public class MatchMapper {
    private final ModelMapper modelMapper;
    private final PlayerMapper playerMapper;
    public Match matchMapper(MatchDao dao) {
      return  modelMapper.map(dao, Match.class);
    }

    public MatchDao createMapper(Player owner, int sizeGrid) {
        var ownerDao = playerMapper.mapDao(owner);
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
