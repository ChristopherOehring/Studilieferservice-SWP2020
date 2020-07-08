package com.studilieferservice.chatmanager.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage createChatMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    public ChatMessage getChatMessage(Long id) {
        return chatMessageRepository.findById(id).orElseThrow();
    }

    public void deleteChatMessage(ChatMessage chatMessage) {
        chatMessageRepository.delete(chatMessage);
    }
}
