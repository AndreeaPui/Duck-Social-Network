package org.example.repository.database;

import lombok.RequiredArgsConstructor;
import org.example.domain.Card;
import org.example.domain.Event;
import org.example.domain.RaceEvent;
import org.example.domain.UserEvent;
import org.example.exceptii.NotInListException;
import org.example.repository.Repository;
import org.example.repository.dto.Page;
import org.example.repository.dto.Pageable;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RepoDBUserEvent {
    private final String url;
    private final String username;
    private final String password;

    public UserEvent findOne(Long ide, Long idu) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("SELECT * FROM UserEvent WHERE ide = ? AND idu = ?");
            statement.setLong(1, ide);
            statement.setLong(2, idu);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                throw new NotInListException("Relatie not found în tabela UserEvent");
            }

            var relatie = getRelatie(connection, resultSet);
            return relatie;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la căutarea relatiei: " + ide + idu, e);
        }
    }


    public List<UserEvent> findAll() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("SELECT * FROM UserEvent");
            ResultSet resultSet = statement.executeQuery();

            List<UserEvent> events = new ArrayList<>();
            while (resultSet.next()) {
                var rel = getRelatie(connection,resultSet);
                events.add(rel);
            }
            return events;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Integer save(UserEvent rel) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Inserare în tabela EVENTS
            PreparedStatement st = connection.prepareStatement(
                    "INSERT INTO UserEvent (ide, idu) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            st.setLong(1,rel.getIdEvent());
            st.setLong(2,rel.getIdUtilizator());
            st.executeUpdate();
            return 0;

        } catch (SQLException e) {
            return -1;
        }
    }

    public Integer delete(Long ide, Long idu) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("DELETE FROM UserEvent WHERE ide = ? AND idu = ?");

            statement.setLong(1, ide);
            statement.setLong(2, idu);

            int rez = statement.executeUpdate();
            System.out.println("Delete rel id=" + ide + idu + " -> " + rez + " rows");

            if(rez > 0)
                return 0;
            else
                return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static UserEvent getRelatie(Connection connection, ResultSet resultSet) throws SQLException {
        var ide = resultSet.getLong("ide");
        var idu = resultSet.getLong("idu");

        UserEvent rel = new UserEvent(idu,ide);

        rel.setIdEvent(ide);
        rel.setIdUtilizator(idu);// suprascrie orice id generat în constructor
        return rel;
    }

    public Page<UserEvent> findAllOnPage(Pageable pageable) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var totalNumberOfRel = count(connection);
            List<UserEvent> relOnPage = totalNumberOfRel > 0 ? findAllOnPage(connection, pageable) : List.of();
            return new Page<>(relOnPage, totalNumberOfRel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<UserEvent> findAllOnPage(Connection connection, Pageable pageable) {
        ResultSet resultSet;
        try (var statement = connection.prepareStatement("SELECT * FROM UserEvent limit ? offset ?")) {
            statement.setInt(1,pageable.getPageSize());
            statement.setInt(2,pageable.getPageSize()*pageable.getPageNUmber());
            resultSet = statement.executeQuery();
            List<UserEvent> rels = new LinkedList<>();
            while (resultSet.next()) {
                var rel = getRelatie(connection,resultSet);
                rels.add(rel);
            }
            return rels;
        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int count(Connection connection){
        try (var statement = connection.prepareStatement("SELECT COUNT(*) AS count FROM UserEvent")) {
            var result =  statement.executeQuery();
            return result.next() ? result.getInt("count") : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
