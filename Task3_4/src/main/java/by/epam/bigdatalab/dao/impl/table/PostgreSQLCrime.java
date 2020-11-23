package by.epam.bigdatalab.dao.impl.table;

import by.epam.bigdatalab.bean.Crime;
import by.epam.bigdatalab.dao.connectionpool.ConnectionPoolHolder;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Query;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class PostgreSQLCrime {

    private static final String ID_FIELD = "id";

    private static final String ADD_CRIME_WITH_OUTCOME = "INSERT INTO \"Crimes\"(category, context, id, \"location-subtype\", \"location-type\", month, \"persistent-id\", \"location-id\", \"outcome-status-id\") VALUES(?,?,?,?,?,?,?,?,?)";

    private static final String GET_CRIME_BY_ID = "SELECT id FROM \"Crimes\" WHERE id = ?;";

    DataSource dataSource = ConnectionPoolHolder.getInstance().getConnectionPool().getSource();
    FluentJdbc fluentJdbc = new FluentJdbcBuilder()
            .connectionProvider(dataSource)
            .build();
    Query query = fluentJdbc.query();

    public void saveCrimes(List<Crime> crimes) {
        for (Crime crime : crimes) {
            if (crime != null && !crimeExists(crime)) {
                addCrime(crime);
            }
        }
    }

    private boolean crimeExists(Crime crime) {
        return getCrimeId(crime) != -1;
    }

    private void addCrime(Crime crime) {
        Long outcomeStatusId = null;
        long id = TablesHolder.getInstance().getPostgreSQLOutcomeStatus().getOutcomeStatusId(crime.getOutcomeStatus());
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
                        TablesHolder.getInstance().getPostgreSQLLocation().getLocationId(crime.getLocation()),
                        outcomeStatusId)
                .run();
    }

    private long getCrimeId(Crime crime) {
        Optional<Long> id = query.select(GET_CRIME_BY_ID)
                .params(crime.getId())
                .firstResult((resultSet ->
                        (resultSet.getLong(ID_FIELD))));
        return id.orElse(-1L);
    }
}
