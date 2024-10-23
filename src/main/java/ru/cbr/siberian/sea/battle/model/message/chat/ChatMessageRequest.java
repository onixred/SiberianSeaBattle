package ru.cbr.siberian.sea.battle.model.message.chat;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatMessageRequest {

    /**
     * Текст в чате
     */
    @NotBlank
    private String name;
    /**
     * Идентификатор для подписки (канала)
     */
    @NotBlank
    private String id;

}
