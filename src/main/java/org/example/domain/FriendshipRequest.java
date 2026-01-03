package org.example.domain;

import org.example.utils.Status;

public class FriendshipRequest {
    private Long id; // Adăugăm câmpul ID pentru baza de date
    private User sender;
    private User recipient;
    private Status status;

    // Constructorul inițial (fără ID, folosit la crearea unei cereri noi)
    public FriendshipRequest(User sender, User recipient, Status status) {
        this.sender = sender;
        this.recipient = recipient;
        this.status = status;
    }

    // Constructor pentru încărcarea din baza de date (cu ID)
    public FriendshipRequest(Long id, User sender, User recipient, Status status) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "FriendshipRequest{" +
                "id=" + id +
                ", sender=" + (sender != null ? sender.getId() : "null") +
                ", recipient=" + (recipient != null ? recipient.getId() : "null") +
                ", status=" + status +
                '}';
    }
}