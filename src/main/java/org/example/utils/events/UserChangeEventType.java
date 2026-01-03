package org.example.utils.events;

import org.example.domain.User;

public class UserChangeEventType implements Event{

    private ChangeEventType type;
    private User data, oldData;

    public void UtilizatorEntityChangeEvent(ChangeEventType type, User data) {
        this.type = type;
        this.data = data;
    }
    public void UtilizatorEntityChangeEvent(ChangeEventType type, User data, User oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public User getData() {
        return data;
    }

    public User getOldData() {
        return oldData;
    }
}
