package by.epam.bigdatalab.dao.impl.table;

import by.epam.bigdatalab.bean.Street;
import by.epam.bigdatalab.dao.connectionpool.ConnectionPoolHolder;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Query;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class PostgreSQLStreet {
    private static final String ID_FIELD = "id";

    private static final String ADD_STREET = "INSERT INTO \"Street\"(id, name) VALUES(?,?)";

    private static final String GET_STREET_BY_ID = "SELECT id FROM \"Street\" WHERE id = ?;";

    DataSource dataSource = ConnectionPoolHolder.getInstance().getConnectionPool().getSource();
    FluentJdbc fluentJdbc = new FluentJdbcBuilder()
            .connectionProvider(dataSource)
            .build();
    Query query = fluentJdbc.query();


    public void saveStreets(List<Street> streets) {
        for (Street street : streets) {
            if (street != null && !streetExists(street)) {
                addStreet(street);
            }
        }
    }

    private boolean streetExists(Street street) {
        return getStreetById(street.getId()) != -1;
    }

    private void addStreet(Street street) {
        query.update(ADD_STREET)
                .params(street.getId(),
                        street.getName())
                .run();
    }

    private long getStreetById(long streetId) {
        Optional<Long> id = query
                .select(GET_STREET_BY_ID)
                .params(streetId).firstResult((resultSet ->
                        (resultSet.getLong(ID_FIELD))));
        return id.orElse(-1L);
    }
}
