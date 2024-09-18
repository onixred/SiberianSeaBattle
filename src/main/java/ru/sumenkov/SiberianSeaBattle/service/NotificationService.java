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
package ru.sumenkov.SiberianSeaBattle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Description:
 *
 * @author <a href="mailto:onixbed@gmail.com">amaksimov</a>
 * crested on 18.09.2024
 */
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;


    public void sendMessage(UUID chanelId, String destination, Object object) {
        messagingTemplate.convertAndSendToUser(
                chanelId.toString(), destination,
                object);
    }
}
