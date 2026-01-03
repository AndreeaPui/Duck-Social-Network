package org.example.domain.validatori;

import org.example.domain.Friendship;
import org.example.domain.User;


public class FriendshipValidation  implements Validation<Friendship> {

    @Override
    public void validate(Friendship entity) throws ValidationException {
        if (entity == null) {
            throw new ValidationException("Friendship cannot be null.");
        }

        Long firstFriend = entity.getUser1();
        Long secondFriend = entity.getUser2();

        if (firstFriend == null || secondFriend == null) {
            throw new ValidationException("The users involved in the friendship cannot be null.");
        }

        if (firstFriend.equals(secondFriend)) {
            throw new ValidationException("A user cannot be friends with themselves.");
        }
    }
}