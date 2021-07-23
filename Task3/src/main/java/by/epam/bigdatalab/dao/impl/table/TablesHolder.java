package by.epam.bigdatalab.dao.impl.table;

public final class TablesHolder {

    private static final PostgreSQLStreet postgreSQLStreet = new PostgreSQLStreet();
    private static final PostgreSQLLocation postgreSQLLocation = new PostgreSQLLocation();
    private static final PostgreSQLOutcomeStatus postgreSQLOutcomeStatus = new PostgreSQLOutcomeStatus();
    private static final PostgreSQLCrime postgreSQLCrime = new PostgreSQLCrime();
    private static final PostgreSQLStopAndSearch postgreSQLStopAndSearch = new PostgreSQLStopAndSearch();

    private TablesHolder() {
    }

    public static TablesHolder getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public PostgreSQLStreet getPostgreSQLStreet() {
        return postgreSQLStreet;
    }

    public PostgreSQLLocation getPostgreSQLLocation() {
        return postgreSQLLocation;
    }

    public PostgreSQLOutcomeStatus getPostgreSQLOutcomeStatus() {
        return postgreSQLOutcomeStatus;
    }

    public PostgreSQLCrime getPostgreSQLCrime() {
        return postgreSQLCrime;
    }

    public PostgreSQLStopAndSearch getPostgreSQLStopAndSearch() {
        return postgreSQLStopAndSearch;
    }


    public static class SingletonHolder {
        public static TablesHolder HOLDER_INSTANCE = new TablesHolder();
    }

}
