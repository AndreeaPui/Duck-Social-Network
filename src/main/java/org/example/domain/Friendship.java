package org.example.domain;

import lombok.Setter;
import org.example.utils.FriendshipIdGenerator;

public class Friendship {
    private Long id;
    @Setter
    private Long user1;
    @Setter
    private Long user2;

    public Friendship(Long user1, Long user2) {
        this.id = FriendshipIdGenerator.nextId();
        this.user1 = user1;
        this.user2 = user2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser1() {
        return user1;
    }

    public Long getUser2() {
        return user2;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", user1=" + user1 +
                ", user2=" + user2 +
                '}';
    }
}
