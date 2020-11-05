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
import java.util.ArrayList;
import java.util.List;

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


    DataSource dataSource = ConnectionPoolFactory.getInstance().getConnectionPool().getSource();
    FluentJdbc fluentJdbc = new FluentJdbcBuilder()
            .connectionProvider(dataSource)
            .build();
    Query query = fluentJdbc.query();

    @Override
    public void saveCrimesToDB(List<Crime> crimes) {

        saveCrimeLocationStreets(crimes);
        saveCrimeLocations(crimes);
        saveCrimeOutcomeStatuses(crimes);
        saveCrimes(crimes);

    }

    private void saveCrimeLocationStreets(List<Crime> crimes) {
        List<CrimeLocationStreet> existingCrimeLocationStreets = new ArrayList<>(getAllCrimeLocationStreets());

        for (Crime crime : crimes) {
            CrimeLocationStreet street = crime.getLocation().getStreet();
            if (!existingCrimeLocationStreets.contains(street)) {
                addCrimeLocationStreet(street);
                existingCrimeLocationStreets.add(street);
            }
        }
    }

    private void saveCrimeLocations(List<Crime> crimes) {
        List<CrimeLocation> existingCrimeLocations = new ArrayList<>(getAllCrimeLocations());

        for (Crime crime : crimes) {
            CrimeLocation crimeLocation = crime.getLocation();
            if (!existingCrimeLocations.contains(crimeLocation)) {
                addCrimeLocation(crimeLocation);
                existingCrimeLocations.add(crimeLocation);
            }
        }
    }

    private void saveCrimeOutcomeStatuses(List<Crime> crimes) {
        List<CrimeOutcomeStatus> existingCrimeOutcomeStatuses = new ArrayList<>(getAllCrimeOutcomeStatuses());

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

    private void saveCrimes(List<Crime> crimes) {
        List<Crime> existingCrimes = new ArrayList<>(getAllCrimes());

        for (Crime crime : crimes) {
            if (!existingCrimes.contains(crime)) {
                addCrime(crime);
                existingCrimes.add(crime);
            }
        }
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
                .listResult(resultSet -> new CrimeOutcomeStatus(resultSet.getInt(OUTCOME_STATUS_ID),
                        resultSet.getString(OUTCOME_STATUS_CATEGORY),
                        resultSet.getDate(OUTCOME_STATUS_DATE)));
    }

    private List<CrimeLocationStreet> getAllCrimeLocationStreets() {

        return query.select(GET_STREETS)
                .listResult(resultSet -> new CrimeLocationStreet(resultSet.getInt(STREET_ID),
                        resultSet.getString(STREET_NAME)));
    }

    private List<CrimeLocation> getAllCrimeLocations() {
        List<CrimeLocationStreet> crimeLocationStreets = new ArrayList<>(getAllCrimeLocationStreets());

        return query.select(GET_LOCATIONS)
                .listResult(resultSet ->
                        new CrimeLocation(resultSet.getInt(LOCATION_ID),
                                resultSet.getDouble(LOCATION_LATITUDE),
                                resultSet.getDouble(LOCATION_LONGITUDE),
                                getCrimeLocationStreet(crimeLocationStreets, resultSet.getInt(LOCATION_STREET_ID))));
    }


    private List<Crime> getAllCrimes() {
        List<CrimeLocation> crimeLocations = new ArrayList<>(getAllCrimeLocations());
        List<CrimeOutcomeStatus> crimeOutcomeStatuses = new ArrayList<>(getAllCrimeOutcomeStatuses());

        return query.select(GET_CRIMES)
                .listResult(resultSet ->
                        new Crime(resultSet.getInt(CRIMES_ID),
                                resultSet.getString(CRIMES_CATEGORY),
                                resultSet.getString(CRIMES_PERSISTENT_ID),
                                resultSet.getDate(CRIMES_MONTH),
                                resultSet.getString(CRIMES_CONTEXT),
                                resultSet.getString(CRIMES_LOCATION_TYPE),
                                resultSet.getString(CRIMES_LOCATION_SUBTYPE),
                                getCrimeLocation(crimeLocations, resultSet.getInt(CRIMES_LOCATION_ID)),
                                getCrimeOutcomeStatus(crimeOutcomeStatuses, resultSet.getInt(CRIMES_OUTCOME_STATUS_ID)
                                )));

    }

    private CrimeLocationStreet getCrimeLocationStreet(List<CrimeLocationStreet> crimeLocationStreets, int streetId) {
        for (CrimeLocationStreet street : crimeLocationStreets) {
            if (street.getId() == streetId) {
                return street;
            }
        }
        return null;
    }

    private CrimeLocation getCrimeLocation(List<CrimeLocation> crimeLocations, int crimeLocationId) {

        for (CrimeLocation crimeLocation : crimeLocations) {
            if (crimeLocation.getId() == crimeLocationId) {
                return crimeLocation;
            }
        }
        return null;
    }

    private CrimeOutcomeStatus getCrimeOutcomeStatus(List<CrimeOutcomeStatus> crimeOutcomeStatuses, int crimeOutcomeId) {

        for (CrimeOutcomeStatus crimeOutcomeStatus : crimeOutcomeStatuses) {
            if (crimeOutcomeStatus.getId() == crimeOutcomeId) {
                return crimeOutcomeStatus;
            }
        }
        return null;
    }

    private int getCrimeLocationId(CrimeLocation crimeLocation) {
        List<CrimeLocation> crimeLocations = new ArrayList<>(getAllCrimeLocations());
        for (CrimeLocation crimeLoc : crimeLocations) {
            if (crimeLoc.equals(crimeLocation)) {
                return crimeLoc.getId();
            }
        }
        return -1;
    }

    private int getCrimeOutcomeStatusId(CrimeOutcomeStatus crimeOutcomeStatus) {

        List<CrimeOutcomeStatus> crimeOut = new ArrayList<>(getAllCrimeOutcomeStatuses());
        for (CrimeOutcomeStatus status : crimeOut) {
            if (status.equals(crimeOutcomeStatus)) {
                return status.getId();
            }
        }
        return -1;
    }


}
