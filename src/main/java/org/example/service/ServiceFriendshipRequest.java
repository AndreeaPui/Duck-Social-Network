package org.example.service;

import org.example.domain.Friendship;
import org.example.domain.FriendshipRequest;
import org.example.domain.User;
import org.example.exceptii.ArgumentException;
import org.example.observers.Observable;
import org.example.observers.Observer;
import org.example.repository.database.RepoDBFriendshipRequest;
import org.example.repository.database.RepoDBFriendships; // Folosim repo-ul corect
import org.example.repository.database.RepoDBUtilizatori; // Folosim repo-ul corect
import org.example.utils.Status;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceFriendshipRequest implements Observable {

    private final RepoDBFriendshipRequest requestRepository;
    private final RepoDBUtilizatori userRepository;
    private final RepoDBFriendships friendshipRepository;
    private final List<Observer> observers = new ArrayList<>();

    public ServiceFriendshipRequest(RepoDBFriendshipRequest requestRepository, RepoDBUtilizatori userRepository, RepoDBFriendships friendshipRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public Optional<FriendshipRequest> delete(Long senderId, Long recipientId) {
        Optional<FriendshipRequest> request = requestRepository.findRequestBySenderAndRecipient(senderId, recipientId);
        if (request.isPresent()) {
            Optional<FriendshipRequest> result = requestRepository.delete(request.get().getId());
            notifyObserver();
            return result;
        }
        return Optional.empty();
    }

    public Iterable<FriendshipRequest> findAll() {
        throw new UnsupportedOperationException("findAll not implemented in RepoDBFriendshipRequest yet.");
    }


    /**
     * Trimiterea unei cereri de prietenie.
     * Construiește obiectul FriendshipRequest și îl salvează.
     */
    public void sendFriendRequest(Long idSender, Long idRecipient) {
        if (idSender == null || idRecipient == null)
            throw new ArgumentException("ID-urile nu pot fi null!");

        User sender = userRepository.findOne(idSender).orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findOne(idRecipient).orElseThrow(() -> new RuntimeException("Recipient not found"));

        // Verificăm dacă cererea există deja (PENDING, ACCEPTED sau REJECTED)
        Optional<FriendshipRequest> existingRequest = requestRepository.findRequestBySenderAndRecipient(idSender, idRecipient);
        if (existingRequest.isPresent()) {
            throw new RuntimeException("Friendship request already exists/processed.");
        }

        FriendshipRequest newRequest = new FriendshipRequest(sender, recipient, Status.PENDING);
        requestRepository.save(newRequest);
        notifyObserver();
    }

    /**
     * Obține lista de utilizatori care au trimis cereri PENDING către userId.
     */
    public List<User> getFriendRequestsUser(Long userId) {
        // Această metodă ar trebui să fie funcțională dacă ați implementat-o în RepoDBFriendshipRequest
        return requestRepository.getFriendRequestsUser(userId);
    }

    /**
     * Acceptă o cerere de prietenie: actualizează statusul și creează o nouă prietenie.
     */
    public void acceptFriendRequest(Long recipientId, Long senderId) {
        Optional<FriendshipRequest> requestOptional = requestRepository.findRequestBySenderAndRecipient(senderId, recipientId);

        if (requestOptional.isPresent()) {
            FriendshipRequest request = requestOptional.get();

            // 1. Actualizare Status (DB)
            request.setStatus(Status.ACCEPTED);
            requestRepository.updateFR(request); // Actualizează statusul în DB

            // 2. Creare Prietenie (DB)
            // Folosim utilizatorii deja încărcați din cerere
            User user1 = request.getSender();
            User user2 = request.getRecipient();
            LocalDateTime date = LocalDateTime.now();

            friendshipRepository.save(new Friendship(user1.getId(), user2.getId()));

            notifyObserver();
        } else {
            throw new RuntimeException("Friendship request not found.");
        }
    }

    /**
     * Refuză o cerere de prietenie: actualizează statusul în REJECTED.
     */
    public void denyFriendRequest(Long recipientId, Long senderId) {
        Optional<FriendshipRequest> requestOptional = requestRepository.findRequestBySenderAndRecipient(senderId, recipientId);
        if (requestOptional.isPresent()) {
            FriendshipRequest request = requestOptional.get();
            request.setStatus(Status.REJECTED);
            requestRepository.updateFR(request);
            notifyObserver();
        } else {
            throw new RuntimeException("Friendship request not found.");
        }
    }

    // ... (Metodele Observer și hasPendingRequests rămân neschimbate) ...
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public boolean hasPendingRequests(Long userId) {
        if (userId == null) {
            throw new ArgumentException("ID-ul nu poate fi null.");
        }

        // Metoda getFriendRequestsUser(userId) utilizează RepoDBFriendshipRequest
        // pentru a interoga baza de date: WHERE idrecipient = ? AND status = 'PENDING'.
        // Dacă lista returnată de useri (expeditori) nu este goală, înseamnă că există cereri.
        List<User> pendingSenders = getFriendRequestsUser(userId);

        return !pendingSenders.isEmpty();
    }
}