package by.epam.bigdatalab.dao.impl;

import by.epam.bigdatalab.bean.Crime;
import by.epam.bigdatalab.bean.CrimeLocation;
import by.epam.bigdatalab.bean.CrimeLocationStreet;
import by.epam.bigdatalab.bean.CrimeOutcomeStatus;
import by.epam.bigdatalab.dao.DataBaseDAO;
import by.epam.bigdatalab.dao.connectionpool.ConnectionPoolHolder;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Query;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class PostgreSQLDAOImpl implements DataBaseDAO {

    private static final String STREET_ID = "id";
    private static final String STREET_NAME = "name";
    private static final String LOCATION_ID = "id";
    private static final String OUTCOME_STATUS_ID = "id";
    private static final String CRIMES_ID = "id";

    private static final String ADD_CRIME_WITH_OUTCOME = "INSERT INTO \"Crimes\"(category, context, id, \"location-subtype\", \"location-type\", month, \"persistent-id\", \"location-id\", \"outcome-status-id\") values(?,?,?,?,?,?,?,?,?)";
    private static final String ADD_CRIME_WITHOUT_OUTCOME = "INSERT INTO \"Crimes\"(category, context, id, \"location-subtype\", \"location-type\", month, \"persistent-id\", \"location-id\") values(?,?,?,?,?,?,?,?)";
    private static final String ADD_STREET = "INSERT INTO \"Street\"(id, name) values(?,?)";
    private static final String ADD_LOCATION = "INSERT INTO \"Location\"(latitude, longitude, \"street-id\") values(?,?,?)";
    private static final String ADD_OUTCOME_STATUS = "INSERT INTO \"Outcome-status\"(category, date) values(?,?)";

    private static final String GET_STREET_BY_ID = "Select * from \"Street\" where id = ?;";
    private static final String GET_CRIME_BY_ID = "Select * from \"Crimes\" where id = ?;";
    private static final String GET_LOCATION_BY_LAT_AND_LONG = "Select * from \"Location\" where latitude = ? and longitude = ?;";
    private static final String GET_OUTCOME_BY_CATEGORY_AND_DATE = "Select * from \"Outcome-status\" where category = ? and date = ?;";


    DataSource dataSource = ConnectionPoolHolder.getInstance().getConnectionPool().getSource();
    FluentJdbc fluentJdbc = new FluentJdbcBuilder()
            .connectionProvider(dataSource)
            .build();
    Query query = fluentJdbc.query();

    @Override
    public synchronized void saveCrimesToDB(List<Crime> crimes) {

        saveCrimeLocationStreets(crimes);
        saveCrimeLocations(crimes);
        saveCrimeOutcomeStatuses(crimes);
        saveCrimes(crimes);

    }

    private void saveCrimeLocationStreets(List<Crime> crimes) {
        for (Crime crime : crimes) {
            CrimeLocationStreet street = crime.getLocation().getStreet();
            if (!crimeLocationStreetExists(street)) {
                addCrimeLocationStreet(street);
            }
        }
    }

    private void saveCrimeLocations(List<Crime> crimes) {
        for (Crime crime : crimes) {
            CrimeLocation crimeLocation = crime.getLocation();
            if (!crimeLocationExists(crimeLocation)) {
                addCrimeLocation(crimeLocation);
            }
        }
    }

    private void saveCrimeOutcomeStatuses(List<Crime> crimes) {
        for (Crime crime : crimes) {
            CrimeOutcomeStatus crimeOutcomeStatus = crime.getOutcomeStatus();
            if (crimeOutcomeStatus == null) {
                continue;
            }
            if (!crimeOutcomeStatusExists(crimeOutcomeStatus)) {
                addCrimeOutcomeStatus(crimeOutcomeStatus);
            }
        }
    }

    private void saveCrimes(List<Crime> crimes) {
        for (Crime crime : crimes) {
            if (!crimeExists(crime)) {
                addCrime(crime);
            }
        }
    }

    private boolean crimeLocationStreetExists(CrimeLocationStreet crimeLocationStreet) {
        return getCrimeLocationStreetById(crimeLocationStreet.getId()) != null;
    }

    private boolean crimeLocationExists(CrimeLocation crimeLocation) {
        return getCrimeLocationId(crimeLocation) != -1;
    }

    private boolean crimeOutcomeStatusExists(CrimeOutcomeStatus crimeOutcomeStatus) {
        return getCrimeOutcomeStatusId(crimeOutcomeStatus) != -1;
    }

    private boolean crimeExists(Crime crime) {
        return getCrimeId(crime) != -1;
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
        Optional<CrimeLocationStreet> street = query
                .select(GET_STREET_BY_ID)
                .params(streetId).firstResult((resultSet ->
                        new CrimeLocationStreet(resultSet.getLong(STREET_ID),
                                resultSet.getString(STREET_NAME)
                        )));
        return street.orElse(null);
    }

    private long getCrimeLocationId(CrimeLocation crimeLocation) {
        Optional<Long> id = query.select(GET_LOCATION_BY_LAT_AND_LONG)
                .params(crimeLocation.getLatitude(),
                        crimeLocation.getLongitude()).firstResult((resultSet ->
                        (resultSet.getLong(LOCATION_ID))));
        return id.orElse(-1L);
    }


    private long getCrimeOutcomeStatusId(CrimeOutcomeStatus crimeOutcomeStatus) {
        Optional<Long> id = query.select(GET_OUTCOME_BY_CATEGORY_AND_DATE)
                .params(crimeOutcomeStatus.getCategory(),
                        crimeOutcomeStatus.getDate()
                ).firstResult((resultSet ->
                        (resultSet.getLong(OUTCOME_STATUS_ID))));
        return id.orElse(-1L);
    }

    private long getCrimeId(Crime crime) {

        Optional<Long> id = query.select(GET_CRIME_BY_ID)
                .params(crime.getId())
                .firstResult((resultSet ->
                        (resultSet.getLong(CRIMES_ID))));
        return id.orElse(-1L);
    }


}
