//package org.example.repository.database;
//
//import lombok.RequiredArgsConstructor;
//import org.example.domain.*;
//import org.example.domain.cardFamily.CardInotatoare;
//import org.example.domain.cardFamily.CardZburatoare;
//import org.example.domain.rataFamily.FlyingDuck;
//import org.example.domain.rataFamily.SwimmingDuck;
//import org.example.exceptii.ArgumentException;
//import org.example.exceptii.NotInListException;
//import org.example.repository.PagedRepository;
//import org.example.repository.Repository;
//import org.example.repository.dto.Page;
//import org.example.repository.dto.Pageable;
//import org.example.utils.TipRata;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Optional;
//
//@RequiredArgsConstructor
//public class RepoDBEvent implements PagedRepository<Event> {
//        private final String url;
//        private final String username;
//        private final String password;
//
//        @Override
//        public Optional<Event> findOne(Long id) {
//            if( id == null )
//                throw new ArgumentException("Id-ul nu poate sa fie null");
//
//            try (Connection connection = DriverManager.getConnection(url, username, password)) {
//                var statement = connection.prepareStatement("SELECT * FROM Events WHERE ide = ?");
//                statement.setLong(1, id);
//                ResultSet resultSet = statement.executeQuery();
//
//                if (!resultSet.next()) {
//                    return Optional.empty();
//                }
//
//                var user = getEvent(resultSet);
//                return Optional.of(user);
//            } catch (SQLException e) {
//                throw new RuntimeException("Eroare la căutarea eventului: " + id, e);
//            }
//        }
//
//        @Override
//        public List<Event> findAll() {
//            try (Connection connection = DriverManager.getConnection(url, username, password)) {
//                var statement = connection.prepareStatement("SELECT * FROM Events");
//                ResultSet resultSet = statement.executeQuery();
//
//                List<Event> events = new ArrayList<>();
//                while (resultSet.next()) {
//                    var event = getEvent(resultSet);
//                    events.add(event);
//                }
//                return events;
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        @Override
//        public Optional<Event> save(Event event) {
//            if(event == null)
//                throw new ArgumentException("Event-ul nu poate sa fie null");
//
//            try (Connection connection = DriverManager.getConnection(url, username, password)) {
//                // Inserare în tabela EVENTS
//                PreparedStatement st = connection.prepareStatement(
//                        "INSERT INTO Events (tip, nr_culoare) VALUES (?, ?)",
//                        Statement.RETURN_GENERATED_KEYS
//                );
//
//                if(event instanceof RaceEvent)
//                    st.setString(1,"RACE_EVENT");
//
//                RaceEvent raceEvent = (RaceEvent)event;
//
//                st.setInt(2,raceEvent.getNrBalize());
//                st.executeUpdate();
//
//                try (ResultSet keys = st.getGeneratedKeys()) {
//                    if (keys.next()) {
//                        long generatedId = keys.getLong(1); // prima coloană, fără nume!
//                        event.setId(generatedId);
//                        System.out.println("INSERT OK, id generat = " + generatedId);
//                    } else {
//                        System.out.println("Nu s-a generat niciun ID!");
//                    }
//                }
//            } catch (SQLException e) {
//                return Optional.of(event);
//            }
//            return Optional.empty();
//        }
//
//
//        @Override
//        public Optional<Event> update(Event event) {
//            if(event == null)
//                throw new ArgumentException("Event-ul nu poate sa fie null");
//
//            try (Connection connection = DriverManager.getConnection(url, username, password)) {
//                var statement = connection.prepareStatement("UPDATE Events SET tip = ?, nr_culoare = ? WHERE ide = ?");
//                statement.setString(1, "RACE_EVENT");
//
//                RaceEvent raceEvent = (RaceEvent)event;
//
//                statement.setInt(2,raceEvent.getNrBalize());
//                statement.setLong(3,raceEvent.getId());
//
//                var rez = statement.executeUpdate();
//                //return rez > 0 ? Optional.empty() : Optional.of(friendship);
//
//                if( statement.executeUpdate() > 0 )
//                    return Optional.empty();
//                return Optional.of(event);
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//
//        @Override
//        public Optional<Event> delete(Long id) {
//            if( id == null )
//                throw new ArgumentException("Id-ul nu poate sa fie null");
//
//            try (Connection connection = DriverManager.getConnection(url, username, password)) {
//                var statement = connection.prepareStatement("DELETE FROM Events WHERE ide = ?");
//
//                Optional<Event> event = findOne(id);
//
//                if(event.isPresent()) {
//                    statement.setLong(1, id);
//                    int rez = statement.executeUpdate();
//                    System.out.println("Delete events id=" + id + " -> " + rez + " rows");
//                }
//
//                return event;
//
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        private static Event getEvent(ResultSet resultSet) throws SQLException {
//            var id = resultSet.getLong("ide");
//            var tip = resultSet.getString("tip");
//            var nrCuloare = resultSet.getInt("nr_culoare");
//
//            Event event = null;
//            if(tip.equalsIgnoreCase("RACE_EVENT")){
//                event = new RaceEvent(nrCuloare);
//            }
//
//            Event eventN = (Event) event;
//
//            eventN.setId(id); // suprascrie orice id generat în constructor
//            return eventN;
//        }
//
//    @Override
//    public Page<Event> findAllOnPage(Pageable pageable) {
//        try (Connection connection = DriverManager.getConnection(url, username, password)) {
//            var totalNumberOfEvents = count(connection);
//            List<Event> eventsOnPage = totalNumberOfEvents > 0 ? findAllOnPage(connection, pageable) : List.of();
//            return new Page<>(eventsOnPage, totalNumberOfEvents);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private List<Event> findAllOnPage(Connection connection, Pageable pageable) {
//        ResultSet resultSet;
//        try (var statement = connection.prepareStatement("SELECT * FROM Events limit ? offset ?")) {
//            statement.setInt(1,pageable.getPageSize());
//            statement.setInt(2,pageable.getPageSize()*pageable.getPageNUmber());
//            resultSet = statement.executeQuery();
//            List<Event> events = new LinkedList<>();
//            while (resultSet.next()) {
//                var event = getEvent(resultSet);
//                events.add(event);
//            }
//            return events;
//        }  catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private int count(Connection connection){
//        try (var statement = connection.prepareStatement("SELECT COUNT(*) AS count FROM Events")) {
//            var result =  statement.executeQuery();
//            return result.next() ? result.getInt("count") : 0;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
