package org.example.repository.database;

import org.example.domain.FriendshipRequest;
import org.example.domain.User;
import org.example.exceptii.ArgumentException;
import org.example.utils.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepoDBFriendshipRequest {

    private final String url;
    private final String username;
    private final String password;
    private final RepoDBUtilizatori userRepo; // Dependență pentru a încărca obiecte User concrete

    public RepoDBFriendshipRequest(String url, String username, String password, RepoDBUtilizatori userRepo) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userRepo = userRepo;
    }

    protected String getTableName() {
        return "friendship_request";
    }

    protected FriendshipRequest mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Long idRequest = resultSet.getLong("id");
        Long idSender = resultSet.getLong("idsender");
        Long idRecipient = resultSet.getLong("idrecipient");
        String statusStr = resultSet.getString("status");

        User sender = userRepo.findOne(idSender).orElseThrow(() -> new SQLException("Sender with ID " + idSender + " not found."));
        User recipient = userRepo.findOne(idRecipient).orElseThrow(() -> new SQLException("Recipient with ID " + idRecipient + " not found."));

        Status status = Status.valueOf(statusStr.toUpperCase());

        FriendshipRequest request = new FriendshipRequest(idRequest, sender, recipient, status);
        return request;
    }


    public Optional<FriendshipRequest> save(FriendshipRequest request) {
        if (request == null)
            throw new ArgumentException("Cererea de prietenie nu poate fi null!");

        String sql = "INSERT INTO friendship_request (idsender, idrecipient, status) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, request.getSender().getId());
            stmt.setLong(2, request.getRecipient().getId());
            stmt.setString(3, request.getStatus().name());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    request.setId(generatedKeys.getLong(1));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving friendship request to DB: " + e.getMessage(), e);
        }
        return Optional.of(request);
    }

    public List<User> getFriendRequestsUser(Long recipientId) {
        if (recipientId == null)
            throw new ArgumentException("ID-ul destinatarului nu poate fi null");

        String sql = "SELECT idsender FROM friendship_request fr WHERE fr.idrecipient = ? AND fr.status = 'PENDING'";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, recipientId);
            ResultSet rs = stmt.executeQuery();

            List<User> friendRequests = new ArrayList<>();
            while (rs.next()) {
                Long senderId = rs.getLong("idsender");
                userRepo.findOne(senderId).ifPresent(friendRequests::add);
            }
            return friendRequests;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding friend requests for user with ID " + recipientId + ": " + e.getMessage(), e);
        }
    }


    public Optional<FriendshipRequest> findRequestBySenderAndRecipient(Long senderId, Long recipientId) {
        if (senderId == null || recipientId == null)
            throw new ArgumentException("ID-urile nu pot fi null");

        String sql = "SELECT * FROM friendship_request WHERE idsender = ? AND idrecipient = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, senderId);
            stmt.setLong(2, recipientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding friendship request: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public void updateFR(FriendshipRequest entity) {
        if (entity == null || entity.getId() == null)
            throw new ArgumentException("Entitatea sau ID-ul nu poate fi null pentru actualizare.");

        String sql = "UPDATE friendship_request SET idsender = ?, idrecipient = ?, status = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, entity.getSender().getId());
            stmt.setLong(2, entity.getRecipient().getId());
            stmt.setString(3, entity.getStatus().name());
            stmt.setLong(4, entity.getId()); // ID-ul cererii pentru clauza WHERE

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating friendship request: " + e.getMessage(), e);
        }
    }

    public Optional<FriendshipRequest> delete(Long id) {
        if (id == null)
            throw new ArgumentException("ID-ul nu poate fi null pentru ștergere.");

        Optional<FriendshipRequest> requestToDelete = findOne(id); // Metoda findOne ar trebui implementată în acest repo

        String sql = "DELETE FROM friendship_request WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                return requestToDelete;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting friendship request with ID " + id + ": " + e.getMessage(), e);
        }
        return Optional.empty();
    }


    public Optional<FriendshipRequest> findOne(Long id) {
        if (id == null)
            throw new ArgumentException("ID-ul nu poate fi null.");

        String sql = "SELECT * FROM friendship_request WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding friendship request by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

}