package ru.cbr.siberian.sea.battle.acl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.cbr.siberian.sea.battle.dao.MatchDao;
import ru.cbr.siberian.sea.battle.model.Match;

@Component
@RequiredArgsConstructor
public class MatchMapper {
    private final ModelMapper modelMapper;
    public Match matchMapper(MatchDao dao) {
      return  modelMapper.map(dao, Match.class);
    }

}
