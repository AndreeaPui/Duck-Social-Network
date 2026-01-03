package org.example.repository.database;

import lombok.RequiredArgsConstructor;
import org.example.domain.Card;
import org.example.domain.Friendship;
import org.example.domain.Persoana;
import org.example.domain.Rata;
import org.example.domain.cardFamily.CardInotatoare;
import org.example.domain.cardFamily.CardZburatoare;
import org.example.exceptii.ArgumentException;
import org.example.repository.PagedRepository;
import org.example.repository.Repository;
import org.example.repository.dto.Page;
import org.example.repository.dto.Pageable;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RepoDBCard<T extends Rata> implements PagedRepository<Card<T>> {

        private final String url;
        private final String username;
        private final String password;

        @Override
        public Optional<Card<T>> findOne(Long id) {
            if(id == null)
                throw new ArgumentException("Id-ul nu poate sa fie null!");

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement st = connection.prepareStatement(
                         "SELECT idc, nume, tip FROM Carduri WHERE idc = ?")) {

                st.setLong(1, id);
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(getCard(rs));
                    }
                }
                return Optional.empty();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public List<Card<T>> findAll() {
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                var statement = connection.prepareStatement("SELECT * FROM Carduri ORDER BY idc");
                ResultSet resultSet = statement.executeQuery();

                List<Card<T>> carduri = new ArrayList<>();
                while (resultSet.next()) {
                    var card = getCard(resultSet);
                    carduri.add(card);
                }
                return carduri;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Optional<Card<T>> save(Card<T> card) {
            if(card == null)
                throw new ArgumentException("Card-ul dat nu poate sa fie null!");
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                PreparedStatement st = connection.prepareStatement(
                        "INSERT INTO Carduri (nume, tip) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );

                st.setString(1,card.getNumeCard());

                if (card instanceof CardZburatoare cardz)
                    st.setString(2, "FLYING");
                else if (card instanceof CardInotatoare cardin)
                    st.setString(2, "SWIMMING");

                st.executeUpdate();

                try (ResultSet keys = st.getGeneratedKeys()) {
                    if (keys.next()) {
                        long generatedId = keys.getLong(1); // prima coloană, fără nume!
                        card.setId(generatedId);
                        System.out.println("INSERT OK, id generat = " + generatedId);
                    } else {
                        System.out.println("Nu s-a generat niciun ID!");
                    }
                }

            } catch (SQLException e) {
                return Optional.of(card);
            }
            return Optional.empty();
        }

        @Override
        public Optional<Card<T>> update(Card<T> card) {
            if(card == null)
                throw new ArgumentException("Card-ul dat nu poate sa fie null!");

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                var statement = connection.prepareStatement("UPDATE Carduri SET nume = ?, tip = ? WHERE idc = ?");
                statement.setString(1, card.getNumeCard());

                if (card instanceof CardZburatoare cardz)
                    statement.setString(2, "FLYING");
                else if (card instanceof CardInotatoare cardin)
                    statement.setString(2, "SWIMMING");

                statement.setLong(3, card.getId());

                var rez = statement.executeUpdate();
                if( statement.executeUpdate() > 0 )
                    return Optional.empty();
                return Optional.of(card);
                //return rez > 0 ? Optional.empty() : Optional.of(friendship);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Optional<Card<T>> delete(Long id) {
            if(id == null)
                throw new ArgumentException("Id-ul nu poate sa fie null!");

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                var statement = connection.prepareStatement("DELETE FROM Carduri WHERE idc = ?");

                Optional<Card<T>> card = findOne(id);

                if(card.isPresent()) {
                    statement.setLong(1, id);
                    int rez = statement.executeUpdate();
                    System.out.println("Delete carduri id=" + id + " -> " + rez + " rows");
                }

                return card;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

//        private static Friendship getFriendship(ResultSet resultSet) throws SQLException {
//            var id = resultSet.getLong("idF");
//            var user1 = resultSet.getLong("user1");
//            var user2 = resultSet.getLong("user2");
//
//            var friendship = new Friendship(user1, user2);
//            friendship.setId(id);
//            return friendship;
//        }

        private Card<T> getCard(ResultSet rs) throws SQLException {
            long id = rs.getLong("idc");
            var nume = rs.getString("nume");
            var tip = rs.getString("tip");

            Card<? extends Rata> card = null;
            if(tip.equalsIgnoreCase("SWIMMING")){
                card = new CardInotatoare(nume);
            }
            else if (tip.equalsIgnoreCase("FLYING"))
                card = new CardZburatoare(nume);

            Card<T> cardN = (Card<T>) card;

            cardN.setId(id); // suprascrie orice id generat în constructor
            return cardN;
        }

    @Override
    public Page<Card<T>> findAllOnPage(Pageable pageable) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var totalNumberOfCarduri = count(connection);
            List<Card<T>> carduriOnPage = totalNumberOfCarduri > 0 ? findAllOnPage(connection, pageable) : List.of();
            return new Page<>(carduriOnPage, totalNumberOfCarduri);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Card<T>> findAllOnPage(Connection connection, Pageable pageable) {
        ResultSet resultSet;
        try (var statement = connection.prepareStatement("SELECT * FROM Carduri limit ? offset ?")) {
            statement.setInt(1,pageable.getPageSize());
            statement.setInt(2,pageable.getPageSize()*pageable.getPageNUmber());
            resultSet = statement.executeQuery();
            List<Card<T>> carduri = new LinkedList<>();
            while (resultSet.next()) {
                var card = getCard(resultSet);
                carduri.add(card);
            }
            return carduri;
        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int count(Connection connection){
        try (var statement = connection.prepareStatement("SELECT COUNT(*) AS count FROM Carduri")) {
            var result =  statement.executeQuery();
            return result.next() ? result.getInt("count") : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

