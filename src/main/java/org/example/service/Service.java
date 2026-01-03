package org.example.service;

public class Service {
    private ServiceUtilizatori serviceUsers;
    private ServiceFriendship serviceFriendship;
    private ServiceCard serviceCard;
    //private ServiceEvenimente serviceEvenimente;

    public Service(ServiceUtilizatori serviceUsers, ServiceFriendship serviceFriendship, ServiceCard serviceCard) {
        this.serviceUsers = serviceUsers;
        this.serviceFriendship = serviceFriendship;
        this.serviceCard = serviceCard;
        //this.serviceEvenimente = serviceEvenimente;

        //serviceFriendship.rebuildFriendLists();
    }

    public ServiceUtilizatori getServiceUsers() {
        return serviceUsers;
    }

    public ServiceFriendship getServiceFriendship() {
        return serviceFriendship;
    }

    public ServiceCard getServiceCard() {
        return serviceCard;
    }

   // public ServiceEvenimente getServiceEvenimente() {
    // return serviceEvenimente;
    //}
}
