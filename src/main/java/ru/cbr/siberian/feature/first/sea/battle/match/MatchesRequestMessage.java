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
package ru.cbr.siberian.feature.first.sea.battle.match;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.cbr.siberian.feature.first.sea.battle.common.BaseRequestMessage;


/**
 * Description: Запрос на получение списка игр в указанном статусе
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 19.09.2024
 */
@Data
public class MatchesRequestMessage implements BaseRequestMessage {

    /**
     * Идентификатор канала
     */
    @NotBlank
    private String chanelId;
    /**
     * Статус игр
     */
    @NotNull
    private MatchStatus matchStatus;
}
