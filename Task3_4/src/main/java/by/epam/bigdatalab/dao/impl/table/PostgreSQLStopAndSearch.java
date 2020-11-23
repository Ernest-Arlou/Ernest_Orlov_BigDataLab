package by.epam.bigdatalab.dao.impl.table;

import by.epam.bigdatalab.bean.StopAndSearch;
import by.epam.bigdatalab.dao.connectionpool.ConnectionPoolHolder;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Query;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class PostgreSQLStopAndSearch {

    private static final String ID_FIELD = "id";

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

    private static final String GET_STOP_AND_SEARCH_BY_FIELDS = "SELECT\n" +
            "id \n" +
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


    public void saveStopAndSearches(List<StopAndSearch> stopAndSearches) {
        for (StopAndSearch stopAndSearch : stopAndSearches) {
            if (stopAndSearch != null && !stopAndSearchExists(stopAndSearch)) {
                addStopAndSearch(stopAndSearch);
            }
        }
    }

    private boolean stopAndSearchExists(StopAndSearch stopAndSearch) {

        return getStopAndSearchId(stopAndSearch) != -1;
    }

    private void addStopAndSearch(StopAndSearch stopAndSearch) {
        Long locationId = null;
        long id = TablesHolder.getInstance().getPostgreSQLLocation().getLocationId(stopAndSearch.getLocation());
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

    private long getStopAndSearchId(StopAndSearch stopAndSearch) {

        Long locationId = null;
        long testId = TablesHolder.getInstance().getPostgreSQLLocation().getLocationId(stopAndSearch.getLocation());
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
