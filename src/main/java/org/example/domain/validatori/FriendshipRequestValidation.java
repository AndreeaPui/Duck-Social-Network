package org.example.domain.validatori;

import org.example.domain.FriendshipRequest;
import org.example.domain.User;
import org.example.utils.Status;

public class FriendshipRequestValidation implements Validation<FriendshipRequest> {

    @Override
    public void validate(FriendshipRequest request) throws ValidationException {
        if (request == null) {
            throw new ValidationException("FriendshipRequest cannot be null.");
        }

        User sender = request.getSender();
        User recipient = request.getRecipient();
        Status status = request.getStatus();

        if (sender == null) {
            throw new ValidationException("Sender cannot be null.");
        }

        if (recipient == null) {
            throw new ValidationException("Recipient cannot be null.");
        }

        if (sender.getId().equals(recipient.getId())) {
            throw new ValidationException("A user cannot send a friendship request to themselves.");
        }

        if (status == null) {
            throw new ValidationException("Status of friendship request must be set.");
        }

        if (status != Status.PENDING && status != Status.ACCEPTED && status != Status.REJECTED) {
            throw new ValidationException("Status must be one of: PENDING, ACCEPTED, REJECTED.");
        }
    }
}
