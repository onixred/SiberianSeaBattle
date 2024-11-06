/*
 *  Copyright 2023 Contributors to the Sports-club.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package ru.cbr.siberian.feature.first.sea.battle.match.action_history;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.cbr.siberian.feature.first.sea.battle.match.Match;
import ru.cbr.siberian.feature.first.sea.battle.match.MatchMapper;
import ru.cbr.siberian.feature.first.sea.battle.player.Player;
import ru.cbr.siberian.feature.first.sea.battle.player.PlayerMapper;


/**
 * Description:
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 29.10.2024
 */
@Component
@RequiredArgsConstructor
public class ActionHistoryMapper {

    private final ModelMapper modelMapper;
    private final MatchMapper matchMapper;
    private final PlayerMapper playerMapper;

    public ActionHistoryDao creteDao(Match match, Player player, int x, int y) {
        var matchDao = matchMapper.matchMapperDao(match);
        var playerDao = playerMapper.mapDao(player);
        ActionHistoryDao actionHistoryDao = new ActionHistoryDao();
        actionHistoryDao.setMatch(matchDao);
        actionHistoryDao.setPlayer(playerDao);
        actionHistoryDao.setX(x);
        actionHistoryDao.setY(y);
        return actionHistoryDao;
    }

    public ActionHistory map(ActionHistoryDao dao) {
        return modelMapper.map(dao, ActionHistory.class);
    }

    public ActionHistoryDao mapDao(ActionHistory actionHistory) {
        return  modelMapper.map(actionHistory, ActionHistoryDao.class);
    }
}