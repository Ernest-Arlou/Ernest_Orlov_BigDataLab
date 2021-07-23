package by.epam.bigdatalab.dao.impl.table;

import by.epam.bigdatalab.bean.OutcomeStatus;
import by.epam.bigdatalab.dao.connectionpool.ConnectionPoolHolder;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Query;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class PostgreSQLOutcomeStatus {
    private static final String ID_FIELD = "id";

    private static final String ADD_OUTCOME_STATUS = "INSERT INTO \"Outcome-status\"(category, date) VALUES(?,?)";
    private static final String GET_OUTCOME_BY_CATEGORY_AND_DATE = "SELECT id FROM \"Outcome-status\" WHERE category = ? AND date = ?;";

    DataSource dataSource = ConnectionPoolHolder.getInstance().getConnectionPool().getSource();
    FluentJdbc fluentJdbc = new FluentJdbcBuilder()
            .connectionProvider(dataSource)
            .build();
    Query query = fluentJdbc.query();


    public void saveOutcomeStatuses(List<OutcomeStatus> outcomeStatuses) {
        for (OutcomeStatus outcomeStatus : outcomeStatuses) {
            if (outcomeStatus != null && !outcomeStatusExists(outcomeStatus)) {
                addOutcomeStatus(outcomeStatus);
            }
        }

    }

    private boolean outcomeStatusExists(OutcomeStatus outcomeStatus) {
        return getOutcomeStatusId(outcomeStatus) != -1;
    }

    private void addOutcomeStatus(OutcomeStatus outcomeStatus) {
        query.update(ADD_OUTCOME_STATUS)
                .params(outcomeStatus.getCategory(),
                        outcomeStatus.getDate())
                .run();
    }

    public long getOutcomeStatusId(OutcomeStatus outcomeStatus) {
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

}
