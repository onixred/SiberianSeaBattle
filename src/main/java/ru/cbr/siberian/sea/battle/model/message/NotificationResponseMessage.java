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
package ru.cbr.siberian.sea.battle.model.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.cbr.siberian.sea.battle.model.enumeration.TypeNotification;

/**
 * Description: Нотификация об обновлениях
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 19.09.2024
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NotificationResponseMessage extends BaseResponseMessage {
    /**
     *  Тип нотификации
     */
    private TypeNotification type;
    /**
     *  Идентификатор игры, только для типа  @see TypeNotification#GRIDS_UPDATE
     */
    private String matchId;
}