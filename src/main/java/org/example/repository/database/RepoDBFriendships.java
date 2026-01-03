package org.example.repository.database;

import org.example.domain.Friendship;
import org.example.domain.User; // Asigurați-vă că User este importat!
import org.example.exceptii.ArgumentException;
import org.example.repository.PagedRepository;
import org.example.repository.dto.Page;
import org.example.repository.dto.Pageable;

import java.util.*;
import java.sql.*;

// NU MAI FOLOSIM Lombok @RequiredArgsConstructor - Adăugăm constructorul manual
// class RepoDBFriendships implements PagedRepository<Friendship> (Numele complet al clasei)

public class RepoDBFriendships implements PagedRepository<Friendship> {

    private final String url;
    private final String username;
    private final String password;

    // NOU: Dependența de RepoDBUtilizatori
    private final RepoDBUtilizatori userRepo;

    // Constructor manual (înlocuiește @RequiredArgsConstructor pentru a include userRepo)
    public RepoDBFriendships(String url, String username, String password, RepoDBUtilizatori userRepo) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userRepo = userRepo; // Setarea noii dependențe
    }

    // ... (Implementările metodelor existente: findOne, findAll, save, update, delete, getFriendship, findAllOnPage, count) ...

    @Override
    public Optional<Friendship> findOne(Long id) {
        if (id == null)
            throw new ArgumentException("Id-ul nu poate sa fie null");

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement st = connection.prepareStatement(
                     "SELECT idf, user1, user2 FROM friendships WHERE idf = ?")) {

            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(getFriendship(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Friendship> findAll() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("SELECT * FROM friendships ORDER BY idf");
            ResultSet resultSet = statement.executeQuery();

            List<Friendship> friendships = new ArrayList<>();
            while (resultSet.next()) {
                friendships.add(getFriendship(resultSet));
            }
            return friendships;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> save(Friendship friendship) {
        if (friendship == null)
            throw new ArgumentException("Prietenia nu poate sa fie null");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            long a = Math.min(friendship.getUser1(), friendship.getUser2());
            long b = Math.max(friendship.getUser1(), friendship.getUser2());

            PreparedStatement st = connection.prepareStatement(
                    "INSERT INTO friendships (user1, user2) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            st.setLong(1, a);
            st.setLong(2, b);

            st.executeUpdate();

            try (ResultSet keys = st.getGeneratedKeys()) {
                if (keys.next()) {
                    long generatedId = keys.getLong(1);
                    friendship.setId(generatedId);
                    System.out.println("INSERT OK, id generat = " + generatedId);
                } else {
                    System.out.println("Nu s-a generat niciun ID!");
                }
            }

        } catch (SQLException e) {
            return Optional.of(friendship);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> update(Friendship friendship) {
        if (friendship == null)
            throw new ArgumentException("Prietenia nu poate sa fie null");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement(
                    "UPDATE friendships SET user1 = ?, user2 = ? WHERE idf = ?"
            );
            statement.setLong(1, friendship.getUser1());
            statement.setLong(2, friendship.getUser2());
            statement.setLong(3, friendship.getId());

            int rez = statement.executeUpdate();
            return rez > 0 ? Optional.empty() : Optional.of(friendship);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> delete(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("DELETE FROM friendships WHERE idf = ?");
            Optional<Friendship> friendship = findOne(id);

            if (friendship.isPresent()) {
                statement.setLong(1, id);
                int rez = statement.executeUpdate();
                System.out.println("Delete Friendship id=" + id + " -> " + rez + " rows");
            }
            return friendship;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Friendship getFriendship(ResultSet rs) throws SQLException {
        long id = rs.getLong("idf");
        long u1 = rs.getLong("user1");
        long u2 = rs.getLong("user2");

        Friendship f = new Friendship(u1, u2);
        f.setId(id);
        return f;
    }

    @Override
    public Page<Friendship> findAllOnPage(Pageable pageable) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var totalNumberOfFriendships = count(connection);
            List<Friendship> friendshipsOnPage = totalNumberOfFriendships > 0 ? findAllOnPage(connection, pageable) : List.of();
            return new Page<>(friendshipsOnPage, totalNumberOfFriendships);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Friendship> findAllOnPage(Connection connection, Pageable pageable) {
        ResultSet resultSet;
        try (var statement = connection.prepareStatement("SELECT * FROM friendships LIMIT ? OFFSET ?")) {
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, pageable.getPageSize() * pageable.getPageNUmber());
            resultSet = statement.executeQuery();

            List<Friendship> friendships = new LinkedList<>();
            while (resultSet.next()) {
                friendships.add(getFriendship(resultSet));
            }
            return friendships;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int count(Connection connection) {
        try (var statement = connection.prepareStatement("SELECT COUNT(*) AS count FROM friendships")) {
            var result = statement.executeQuery();
            return result.next() ? result.getInt("count") : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ----------------------------------------------------------------------
    // METODE NOI PENTRU GĂSIREA PRIETENILOR (DELEGATE)
    // ----------------------------------------------------------------------

    /**
     * Găsește toți prietenii unui utilizator dat, cu suport pentru paginare.
     * @param userId ID-ul utilizatorului
     * @param page Numărul paginii (indexat de la 1)
     * @param pageSize Numărul de elemente pe pagină
     * @return Listă de obiecte User (care sunt, de fapt, Rata sau Persoana)
     */
    public List<User> findFriends(Long userId, int page, int pageSize) {
        if (userId == null)
            throw new ArgumentException("Id-ul nu poate sa fie null");

        // Interogarea selectează doar ID-urile utilizatorilor (idu)
        String sql = "SELECT u.idu " +
                "FROM users u " +
                "JOIN friendships f ON (u.idu = f.user1 OR u.idu = f.user2) " +
                "WHERE (f.user1 = ? OR f.user2 = ?) AND u.idu != ? " +
                "ORDER BY u.idu " +
                "LIMIT ? OFFSET ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, userId);
            stmt.setLong(3, userId);
            stmt.setInt(4, pageSize);
            // Paginare indexată de la 1
            stmt.setInt(5, (page - 1) * pageSize);

            ResultSet rs = stmt.executeQuery();

            List<User> friends = new ArrayList<>();
            while (rs.next()) {
                Long friendId = rs.getLong("idu");

                // Utilizăm userRepo.findOne pentru a încărca obiectul concret
                userRepo.findOne(friendId).ifPresent(friends::add);
            }
            return friends;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la căutarea prietenilor pentru userul cu ID " + userId + ": " + e.getMessage(), e);
        }
    }

    /**
     * Numără numărul total de prieteni ai unui utilizator.
     */
    public int countFriends(Long userId) {
        if (userId == null)
            throw new ArgumentException("Id-ul nu poate sa fie null");

        String sql = "SELECT COUNT(u.idu) AS count " +
                "FROM users u " +
                "JOIN friendships f ON (u.idu = f.user1 OR u.idu = f.user2) " +
                "WHERE (f.user1 = ? OR f.user2 = ?) AND u.idu != ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, userId);
            stmt.setLong(3, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la numărarea prietenilor pentru userul cu ID " + userId + ": " + e.getMessage(), e);
        }
        return 0;
    }
}