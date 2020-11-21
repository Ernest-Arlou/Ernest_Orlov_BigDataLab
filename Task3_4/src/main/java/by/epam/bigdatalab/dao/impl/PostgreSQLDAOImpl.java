package by.epam.bigdatalab.dao.impl;

import by.epam.bigdatalab.bean.*;
import by.epam.bigdatalab.dao.DataBaseDAO;
import by.epam.bigdatalab.dao.connectionpool.ConnectionPoolHolder;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Query;

import javax.sql.DataSource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PostgreSQLDAOImpl implements DataBaseDAO {

    private static final String ID_FIELD = "id";
    private static final String STREET_NAME = "name";

    private static final String ADD_CRIME_WITH_OUTCOME = "INSERT INTO \"Crimes\"(category, context, id, \"location-subtype\", \"location-type\", month, \"persistent-id\", \"location-id\", \"outcome-status-id\") VALUES(?,?,?,?,?,?,?,?,?)";
    private static final String ADD_CRIME_WITHOUT_OUTCOME = "INSERT INTO \"Crimes\"(category, context, id, \"location-subtype\", \"location-type\", month, \"persistent-id\", \"location-id\") VALUES(?,?,?,?,?,?,?,?)";
    private static final String ADD_STREET = "INSERT INTO \"Street\"(id, name) VALUES(?,?)";
    private static final String ADD_LOCATION = "INSERT INTO \"Location\"(latitude, longitude, \"street-id\") VALUES(?,?,?)";
    private static final String ADD_OUTCOME_STATUS = "INSERT INTO \"Outcome-status\"(category, date) VALUES(?,?)";
    private static final String ADD_STOP_AND_SEARCH = "INSERT INTO \"Stop-and-search\"(\n" +
            "type, \n" +
            "\"involved-person\", \n" +
            "\"date-time\",\n" +
            "\"operation\", \n" +
            "\"operation-name\", \n" +
            "\"location-id\",\n" +
            "\"gender\", \n" +
            "\"age-range\", \n" +
            "\"self-defined-ethnicity\", \n" +
            "\"officer-defined-ethnicity\",\n" +
            "\"legislation\", \n" +
            "\"object-of-search\", \n" +
            "\"outcome\",\n" +
            "\"outcome-linked-to-object-of-search\",\n" +
            "\"removal-of-more-than-outer-clothing\") \n" +
            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);\n";

    private static final String GET_STREET_BY_ID = "SELECT * FROM \"Street\" WHERE id = ?;";
    private static final String GET_CRIME_BY_ID = "SELECT * FROM \"Crimes\" WHERE id = ?;";
    private static final String GET_LOCATION_BY_LAT_AND_LONG = "SELECT * FROM \"Location\" WHERE latitude = ? AND longitude = ?;";
    private static final String GET_OUTCOME_BY_CATEGORY_AND_DATE = "SELECT * FROM \"Outcome-status\" WHERE category = ? AND date = ?;";

    private static final String GET_STOP_AND_SEARCH_BY_FIELDS = "SELECT\n" +
            "* \n" +
            "FROM \"Stop-and-search\" \n" +
            "WHERE \n" +
            "   \"type\" = ?\n" +
            "   AND \"involved-person\" = ?\n" +
            "   AND \"date-time\" = ?\n" +
            "   AND \"operation\" = ?\n" +
            "   AND \"operation-name\" = ?\n" +
            "   AND \"location-id\" = ?\n" +
            "   AND \"gender\" = ?\n" +
            "   AND \"age-range\" = ?\n" +
            "   AND \"self-defined-ethnicity\" = ?\n" +
            "   AND \"officer-defined-ethnicity\" = ?\n" +
            "   AND \"legislation\" = ?\n" +
            "   AND \"object-of-search\" = ?\n" +
            "   AND \"outcome\" = ?\n" +
            "   AND \"outcome-linked-to-object-of-search\" = ?\n" +
            "   AND \"removal-of-more-than-outer-clothing\" = ?";


    DataSource dataSource = ConnectionPoolHolder.getInstance().getConnectionPool().getSource();
    FluentJdbc fluentJdbc = new FluentJdbcBuilder()
            .connectionProvider(dataSource)
            .build();
    Query query = fluentJdbc.query();


    @Override
    public void saveStopAndSearches(List<StopAndSearch> stopAndSearches) {
        List<Street> streets = new LinkedList<>();
        List<Location> locations = new LinkedList<>();

        for (StopAndSearch stopAndSearch : stopAndSearches) {
            if (stopAndSearch.getLocation() == null) {
                continue;
            }
            locations.add(stopAndSearch.getLocation());
            streets.add(stopAndSearch.getLocation().getStreet());

        }
        saveStreets(streets);
        saveLocations(locations);
        saveStopAndSearchesToDB(stopAndSearches);

    }

    private void saveStopAndSearchesToDB(List<StopAndSearch> stopAndSearches) {
        for (StopAndSearch stopAndSearch : stopAndSearches) {
            if (stopAndSearch != null && !stopAndSearchExists(stopAndSearch)) {
                addStopAndSearch(stopAndSearch);
            }
        }
    }

    @Override
    public void saveCrimes(List<Crime> crimes) {
        List<Street> streets = new LinkedList<>();
        List<Location> locations = new LinkedList<>();
        List<OutcomeStatus> outcomeStatuses = new LinkedList<>();

        for (Crime crime : crimes) {
            if (crime.getOutcomeStatus() != null) {
                outcomeStatuses.add(crime.getOutcomeStatus());
            }
            if (crime.getLocation() == null) {
                continue;
            }
            locations.add(crime.getLocation());
            streets.add(crime.getLocation().getStreet());
        }


        saveStreets(streets);
        saveLocations(locations);

        saveOutcomeStatuses(outcomeStatuses);
        saveCrimesToDB(crimes);

    }

    private void saveStreets(List<Street> streets) {

        for (Street street : streets) {
            if (street != null && !streetExists(street)) {
                addCrimeLocationStreet(street);
            }
        }
    }

    private void saveLocations(List<Location> locations) {
        for (Location location : locations) {
            if (location != null && !locationExists(location)) {
                addCrimeLocation(location);
            }
        }
    }

    private void saveOutcomeStatuses(List<OutcomeStatus> outcomeStatuses) {
        for (OutcomeStatus outcomeStatus : outcomeStatuses) {
            if (outcomeStatus != null && !outcomeStatusExists(outcomeStatus)) {
                addCrimeOutcomeStatus(outcomeStatus);
            }
        }
    }

    private void saveCrimesToDB(List<Crime> crimes) {
        for (Crime crime : crimes) {
            if (crime != null && !crimeExists(crime)) {
                addCrime(crime);
            }
        }
    }

    private boolean streetExists(Street street) {
        return getStreetById(street.getId()) != -1;
    }

    private boolean locationExists(Location location) {
        return getLocationId(location) != -1;
    }

    private boolean outcomeStatusExists(OutcomeStatus outcomeStatus) {
        return getOutcomeStatusId(outcomeStatus) != -1;
    }

    private boolean crimeExists(Crime crime) {
        return getCrimeId(crime) != -1;
    }

    private boolean stopAndSearchExists(StopAndSearch stopAndSearch) {

        return getStopAndSearchId(stopAndSearch) != -1;
    }


    private void addCrimeLocationStreet(Street street) {
        query.update(ADD_STREET)
                .params(street.getId(),
                        street.getName())
                .run();
    }

    private void addCrimeOutcomeStatus(OutcomeStatus outcomeStatus) {
        query.update(ADD_OUTCOME_STATUS)
                .params(outcomeStatus.getCategory(),
                        outcomeStatus.getDate())
                .run();
    }

    private void addCrimeLocation(Location location) {
        query.update(ADD_LOCATION)
                .params(location.getLatitude(),
                        location.getLongitude(),
                        location.getStreet().getId())
                .run();
    }


    private void addCrime(Crime crime) {
        Long outcomeStatusId = null;
        long id = getOutcomeStatusId(crime.getOutcomeStatus());
        if (id != -1) {
            outcomeStatusId = id;
        }

        query.update(ADD_CRIME_WITH_OUTCOME)
                .params(crime.getCategory(),
                        crime.getContext(),
                        crime.getId(),
                        crime.getLocationSubtype(),
                        crime.getLocationType(),
                        crime.getMonth(),
                        crime.getPersistentId(),
                        getLocationId(crime.getLocation()),
                        outcomeStatusId)
                .run();
    }

    private void addStopAndSearch(StopAndSearch stopAndSearch) {
        Long locationId = null;
        long id = getLocationId(stopAndSearch.getLocation());
        if (id != -1) {
            locationId = id;
        }
        query.update(ADD_STOP_AND_SEARCH)
                .params(stopAndSearch.getType(),
                        stopAndSearch.isInvolvedPerson(),
                        stopAndSearch.getDatetime(),
                        stopAndSearch.isOperation(),
                        stopAndSearch.getOperationName(),
                        locationId,
                        stopAndSearch.getGender(),
                        stopAndSearch.getAgeRange(),
                        stopAndSearch.getSelfDefinedEthnicity(),
                        stopAndSearch.getOfficerDefinedEthnicity(),
                        stopAndSearch.getLegislation(),
                        stopAndSearch.getObjectOfSearch(),
                        stopAndSearch.isOutcome(),
                        stopAndSearch.getOutcomeLinkedToObjectOfSearch(),
                        stopAndSearch.isRemovalOfMoreThanOuterClothing())
                .run();
    }

    private long getStreetById(long streetId) {
        Optional<Long> id = query
                .select(GET_STREET_BY_ID)
                .params(streetId).firstResult((resultSet ->
                       (resultSet.getLong(ID_FIELD))));
        return id.orElse(-1L);
    }

    private long getLocationId(Location location) {
        if (location == null) {
            return -1L;
        }
        Optional<Long> id = query.select(GET_LOCATION_BY_LAT_AND_LONG)
                .params(location.getLatitude(),
                        location.getLongitude()).firstResult((resultSet ->
                        (resultSet.getLong(ID_FIELD))));
        return id.orElse(-1L);
    }


    private long getOutcomeStatusId(OutcomeStatus outcomeStatus) {
        if (outcomeStatus == null) {
            return -1L;
        }
        Optional<Long> id = query.select(GET_OUTCOME_BY_CATEGORY_AND_DATE)
                .params(outcomeStatus.getCategory(),
                        outcomeStatus.getDate()
                ).firstResult((resultSet ->
                        (resultSet.getLong(ID_FIELD))));
        return id.orElse(-1L);
    }

    private long getCrimeId(Crime crime) {
        Optional<Long> id = query.select(GET_CRIME_BY_ID)
                .params(crime.getId())
                .firstResult((resultSet ->
                        (resultSet.getLong(ID_FIELD))));
        return id.orElse(-1L);
    }

    private long getStopAndSearchId(StopAndSearch stopAndSearch) {

        Long locationId = null;
        long testId = getLocationId(stopAndSearch.getLocation());
        if (testId != -1) {
            locationId = testId;
        }
        Optional<Long> id = query.select(GET_STOP_AND_SEARCH_BY_FIELDS)
                .params(stopAndSearch.getType(),
                        stopAndSearch.isInvolvedPerson(),
                        stopAndSearch.getDatetime(),
                        stopAndSearch.isOperation(),
                        stopAndSearch.getOperationName(),
                        locationId,
                        stopAndSearch.getGender(),
                        stopAndSearch.getAgeRange(),
                        stopAndSearch.getSelfDefinedEthnicity(),
                        stopAndSearch.getOfficerDefinedEthnicity(),
                        stopAndSearch.getLegislation(),
                        stopAndSearch.getObjectOfSearch(),
                        stopAndSearch.isOutcome(),
                        stopAndSearch.getOutcomeLinkedToObjectOfSearch(),
                        stopAndSearch.isRemovalOfMoreThanOuterClothing())
                .firstResult((resultSet ->
                        (resultSet.getLong(ID_FIELD))));
        return id.orElse(-1L);
    }

}
