package by.epam.bigdatalab.dao.impl.table;

import by.epam.bigdatalab.bean.Location;
import by.epam.bigdatalab.dao.connectionpool.ConnectionPoolHolder;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Query;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class PostgreSQLLocation {
    private static final String ID_FIELD = "id";
    private static final String ADD_LOCATION = "INSERT INTO \"Location\"(latitude, longitude, \"street-id\") VALUES(?,?,?)";
    private static final String GET_LOCATION_BY_LAT_AND_LONG = "SELECT id FROM \"Location\" WHERE latitude = ? AND longitude = ?;";

    DataSource dataSource = ConnectionPoolHolder.getInstance().getConnectionPool().getSource();
    FluentJdbc fluentJdbc = new FluentJdbcBuilder()
            .connectionProvider(dataSource)
            .build();
    Query query = fluentJdbc.query();


    public void saveLocations(List<Location> locations) {
        for (Location location : locations) {
            if (location != null && !locationExists(location)) {
                addLocation(location);
            }
        }
    }

    private boolean locationExists(Location location) {
        return getLocationId(location) != -1;
    }

    private void addLocation(Location location) {
        query.update(ADD_LOCATION)
                .params(location.getLatitude(),
                        location.getLongitude(),
                        location.getStreet().getId())
                .run();
    }

    public long getLocationId(Location location) {
        if (location == null) {
            return -1L;
        }
        Optional<Long> id = query.select(GET_LOCATION_BY_LAT_AND_LONG)
                .params(location.getLatitude(),
                        location.getLongitude()).firstResult((resultSet ->
                        (resultSet.getLong(ID_FIELD))));
        return id.orElse(-1L);
    }
}
