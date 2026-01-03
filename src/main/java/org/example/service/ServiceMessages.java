package org.example.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.domain.Message;
import org.example.repository.database.RepoDBMessages;

import java.util.List;

public class ServiceMessages {
    private final RepoDBMessages messageRepository;
    private final ObservableList<Message> messages;

    public ServiceMessages(RepoDBMessages messageRepository) {
        this.messageRepository = messageRepository;
        this.messages = FXCollections.observableArrayList();
    }

    public void sendMessage(Message message) {
        messageRepository.save(message);
        messages.add(message);
    }

    public ObservableList<Message> getMessagesBetweenUsers(Long userId1, Long userId2) {
        List<Message> messageList = messageRepository.getMessagesBetweenUsers(userId1, userId2);
        messages.setAll(messageList);
        return messages;
    }
}