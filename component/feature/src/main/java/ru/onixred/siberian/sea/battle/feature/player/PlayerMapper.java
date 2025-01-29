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
package ru.onixred.siberian.sea.battle.feature.player;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


import java.util.UUID;

/**
 * Description:
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 29.10.2024
 */
@Component
@RequiredArgsConstructor
public class PlayerMapper {
    private final ModelMapper modelMapper;

    public PlayerDao creteDao(String name, String password, UUID chanelId) {
        PlayerDao playerDao =  new PlayerDao();
        playerDao.setName(name);
        playerDao.setPassword(password);
        playerDao.setChanelId(chanelId);
      return playerDao;
    }

    public Player map(PlayerDao playerDao) {
        return modelMapper.map(playerDao, Player.class);
    }

    public PlayerDao mapDao(Player player) {
        return modelMapper.map(player, PlayerDao.class);
    }
}
