package by.epam.bigdatalab.dao.impl;

import by.epam.bigdatalab.bean.Crime;
import by.epam.bigdatalab.bean.CrimeLocation;
import by.epam.bigdatalab.bean.CrimeLocationStreet;
import by.epam.bigdatalab.bean.CrimeOutcomeStatus;
import by.epam.bigdatalab.dao.DataBaseDAO;
import by.epam.bigdatalab.dao.connectionpool.factory.ConnectionPoolFactory;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Query;

import javax.sql.DataSource;
import java.util.*;

public class PostgreSQLDAOImpl implements DataBaseDAO {

    private static final String STREET_ID = "id";
    private static final String STREET_NAME = "name";

    private static final String LOCATION_ID = "id";
    private static final String LOCATION_LATITUDE = "latitude";
    private static final String LOCATION_LONGITUDE = "longitude";
    private static final String LOCATION_STREET_ID = "street-id";

    private static final String OUTCOME_STATUS_ID = "id";
    private static final String OUTCOME_STATUS_CATEGORY = "category";
    private static final String OUTCOME_STATUS_DATE = "date";

    private static final String CRIMES_ID = "id";
    private static final String CRIMES_CATEGORY = "category";
    private static final String CRIMES_CONTEXT = "context";
    private static final String CRIMES_LOCATION_SUBTYPE = "location-subtype";
    private static final String CRIMES_LOCATION_TYPE = "location-type";
    private static final String CRIMES_MONTH = "month";
    private static final String CRIMES_PERSISTENT_ID = "persistent-id";
    private static final String CRIMES_LOCATION_ID = "location-id";
    private static final String CRIMES_OUTCOME_STATUS_ID = "outcome-status-id";

    private static final String ADD_CRIME_WITH_OUTCOME = "INSERT INTO \"Crimes\"(category, context, id, \"location-subtype\", \"location-type\", month, \"persistent-id\", \"location-id\", \"outcome-status-id\") values(?,?,?,?,?,?,?,?,?)";
    private static final String ADD_CRIME_WITHOUT_OUTCOME = "INSERT INTO \"Crimes\"(category, context, id, \"location-subtype\", \"location-type\", month, \"persistent-id\", \"location-id\") values(?,?,?,?,?,?,?,?)";
    private static final String ADD_STREET = "INSERT INTO \"Street\"(id, name) values(?,?)";
    private static final String ADD_LOCATION = "INSERT INTO \"Location\"(latitude, longitude, \"street-id\") values(?,?,?)";
    private static final String ADD_OUTCOME_STATUS = "INSERT INTO \"Outcome-status\"(category, date) values(?,?)";
    private static final String GET_OUTCOME_STATUSES = "SELECT * FROM \"Outcome-status\"";
    private static final String GET_STREETS = "SELECT * FROM \"Street\"";
    private static final String GET_LOCATIONS = "SELECT * FROM \"Location\"";
    private static final String GET_CRIMES = "SELECT * FROM \"Crimes\"";

    private static final String GET_STREET_BY_ID = "Select * from \"Street\" where id = ?;";
    private static final String GET_LOCATION_BY_ID = "Select * from \"Location\" where id = ?;";
    private static final String GET_OUTCOME_STATUS_BY_ID = "Select * from \"Outcome-status\" where id = ?;";
    private static final String GET_LOCATION_BY_LAT_AND_LONG = "Select * from \"Location\" where latitude = ? and longitude = ?;";
    private static final String GET_OUTCOME_BY_CATEGORY_AND_DATE = "Select * from \"Outcome-status\" where category = ? and date = ?;";




    DataSource dataSource = ConnectionPoolFactory.getInstance().getConnectionPool().getSource();
    FluentJdbc fluentJdbc = new FluentJdbcBuilder()
            .connectionProvider(dataSource)
            .build();
    Query query = fluentJdbc.query();

    @Override
    public void saveCrimesToDB(Set<Crime> crimes) {

        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        saveCrimeLocationStreets(crimes);
        saveCrimeLocations(crimes);
        saveCrimeOutcomeStatuses(crimes);
        saveCrimes(crimes);

    }

    private void saveCrimeLocationStreets(Set<Crime> crimes) {
        Set<CrimeLocationStreet> existingCrimeLocationStreets = new HashSet<>(getAllCrimeLocationStreets());

        for (Crime crime : crimes) {
            CrimeLocationStreet street = crime.getLocation().getStreet();
            if (!existingCrimeLocationStreets.contains(street)) {
                addCrimeLocationStreet(street);
                existingCrimeLocationStreets.add(street);
            }
        }
    }

    private void saveCrimeLocations(Set<Crime> crimes) {
        Set<CrimeLocation> existingCrimeLocations = new HashSet<>(getAllCrimeLocations());
        for (Crime crime : crimes) {
            CrimeLocation crimeLocation = crime.getLocation();
            if (!existingCrimeLocations.contains(crimeLocation)) {
                addCrimeLocation(crimeLocation);
                existingCrimeLocations.add(crimeLocation);
            }
        }
    }

    private void saveCrimeOutcomeStatuses(Set<Crime> crimes) {
        Set<CrimeOutcomeStatus> existingCrimeOutcomeStatuses = new HashSet<>(getAllCrimeOutcomeStatuses());
        for (Crime crime : crimes) {
            CrimeOutcomeStatus crimeOutcomeStatus = crime.getOutcomeStatus();
            if (crimeOutcomeStatus == null) {
                continue;
            }
            if (!existingCrimeOutcomeStatuses.contains(crimeOutcomeStatus)) {
                addCrimeOutcomeStatus(crimeOutcomeStatus);
                existingCrimeOutcomeStatuses.add(crimeOutcomeStatus);
            }
        }
    }

    private void saveCrimes(Set<Crime> crimes) {
        Set<Crime> existingCrimes = new HashSet<>(getAllCrimes());

        for (Crime crime : crimes) {
            if (!existingCrimes.contains(crime)) {
                addCrime(crime);
                existingCrimes.add(crime);
            }
        }
    }

    private void addCrimeLocationStreet(CrimeLocationStreet crimeLocationStreet) {
        query.update(ADD_STREET)
                .params(crimeLocationStreet.getId(),
                        crimeLocationStreet.getName())
                .run();
    }

    private void addCrimeOutcomeStatus(CrimeOutcomeStatus crimeOutcomeStatus) {
        query.update(ADD_OUTCOME_STATUS)
                .params(crimeOutcomeStatus.getCategory(),
                        crimeOutcomeStatus.getDate())
                .run();
    }

    private void addCrimeLocation(CrimeLocation crimeLocation) {
        query.update(ADD_LOCATION)
                .params(crimeLocation.getLatitude(),
                        crimeLocation.getLongitude(),
                        crimeLocation.getStreet().getId())
                .run();
    }

    private List<CrimeOutcomeStatus> getAllCrimeOutcomeStatuses() {

        return query.select(GET_OUTCOME_STATUSES)
                .listResult(resultSet -> new CrimeOutcomeStatus(resultSet.getLong(OUTCOME_STATUS_ID),
                        resultSet.getString(OUTCOME_STATUS_CATEGORY),
                        resultSet.getDate(OUTCOME_STATUS_DATE)));
    }

    private List<CrimeLocationStreet> getAllCrimeLocationStreets() {

        return query.select(GET_STREETS)
                .listResult(resultSet -> new CrimeLocationStreet(resultSet.getLong(STREET_ID),
                        resultSet.getString(STREET_NAME)));
    }

    private List<CrimeLocation> getAllCrimeLocations() {
        return query.select(GET_LOCATIONS)
                .listResult(resultSet ->
                        new CrimeLocation(resultSet.getLong(LOCATION_ID),
                                resultSet.getDouble(LOCATION_LATITUDE),
                                resultSet.getDouble(LOCATION_LONGITUDE),
                                getCrimeLocationStreetById(resultSet.getLong(LOCATION_STREET_ID))));
    }


    private List<Crime> getAllCrimes() {
        return query.select(GET_CRIMES)
                .listResult(resultSet ->
                        new Crime(resultSet.getLong(CRIMES_ID),
                                resultSet.getString(CRIMES_CATEGORY),
                                resultSet.getString(CRIMES_PERSISTENT_ID),
                                resultSet.getDate(CRIMES_MONTH),
                                resultSet.getString(CRIMES_CONTEXT),
                                resultSet.getString(CRIMES_LOCATION_TYPE),
                                resultSet.getString(CRIMES_LOCATION_SUBTYPE),
                                getCrimeLocationById(resultSet.getLong(CRIMES_LOCATION_ID)),
                                getCrimeOutcomeStatusById(resultSet.getLong(CRIMES_OUTCOME_STATUS_ID)
                                )));

    }

    private void addCrime(Crime crime) {
        if (crime.getOutcomeStatus() == null) {
            query.update(ADD_CRIME_WITHOUT_OUTCOME)
                    .params(crime.getCategory(),
                            crime.getContext(),
                            crime.getId(),
                            crime.getLocationSubtype(),
                            crime.getLocationType(),
                            crime.getMonth(),
                            crime.getPersistentId(),
                            getCrimeLocationId(crime.getLocation()))

                    .run();
        } else {
            query.update(ADD_CRIME_WITH_OUTCOME)
                    .params(crime.getCategory(),
                            crime.getContext(),
                            crime.getId(),
                            crime.getLocationSubtype(),
                            crime.getLocationType(),
                            crime.getMonth(),
                            crime.getPersistentId(),
                            getCrimeLocationId(crime.getLocation()),
                            getCrimeOutcomeStatusId(crime.getOutcomeStatus()))
                    .run();
        }
    }

    private CrimeLocationStreet getCrimeLocationStreetById(long streetId) {
        return query.select(GET_STREET_BY_ID)
                .params(streetId).singleResult((resultSet ->
                        new CrimeLocationStreet(resultSet.getLong(STREET_ID),
                                resultSet.getString(STREET_NAME)
                                )));
    }

    private CrimeLocation getCrimeLocationById(long crimeLocationId) {
        return query.select(GET_LOCATION_BY_ID)
                .params(crimeLocationId).singleResult((resultSet ->
                        new CrimeLocation(crimeLocationId,
                                resultSet.getDouble(LOCATION_LATITUDE),
                                resultSet.getDouble(LOCATION_LONGITUDE),
                                getCrimeLocationStreetById(resultSet.getLong(LOCATION_STREET_ID)))));
    }


    private CrimeOutcomeStatus getCrimeOutcomeStatusById(long crimeOutcomeId) {
        if (crimeOutcomeId == 0) {
            return null;
        }
        return query.select(GET_OUTCOME_STATUS_BY_ID)
                .params(crimeOutcomeId).singleResult(
                        (resultSet ->
                        new CrimeOutcomeStatus(crimeOutcomeId,
                                resultSet.getString(OUTCOME_STATUS_CATEGORY),
                                resultSet.getDate(OUTCOME_STATUS_DATE))));
    }


    private long getCrimeLocationId(CrimeLocation crimeLocation) {
        return query.select(GET_LOCATION_BY_LAT_AND_LONG)
                .params(crimeLocation.getLatitude(),
                        crimeLocation.getLongitude()).singleResult((resultSet ->
                       (resultSet.getLong(LOCATION_ID))));

    }


    private long getCrimeOutcomeStatusId(CrimeOutcomeStatus crimeOutcomeStatus) {
        return query.select(GET_OUTCOME_BY_CATEGORY_AND_DATE)
                .params(crimeOutcomeStatus.getCategory(),
                        crimeOutcomeStatus.getDate()
                ).singleResult((resultSet ->
                        (resultSet.getLong(OUTCOME_STATUS_ID))));

    }



}
