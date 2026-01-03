package org.example.domain;

public class UserEvent {
    private Long idUtilizator;
    private Long idEvent;

    public UserEvent(Long idUtilizator, Long idEvent) {
        this.idUtilizator = idUtilizator;
        this.idEvent = idEvent;
    }

    public Long getIdUtilizator() {
        return idUtilizator;
    }

    public void setIdUtilizator(Long idUtilizator) {
        this.idUtilizator = idUtilizator;
    }

    public Long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Long idEvent) {
        this.idEvent = idEvent;
    }
}
