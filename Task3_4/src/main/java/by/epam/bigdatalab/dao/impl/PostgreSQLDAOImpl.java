package by.epam.bigdatalab.dao.impl;

import by.epam.bigdatalab.bean.*;
import by.epam.bigdatalab.dao.DataBaseDAO;
import by.epam.bigdatalab.dao.impl.table.TablesHolder;

import java.util.LinkedList;
import java.util.List;

public class PostgreSQLDAOImpl implements DataBaseDAO {
    private  static  final TablesHolder tablesHolder = TablesHolder.getInstance();

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

        tablesHolder.getPostgreSQLStreet().saveStreets(streets);
        tablesHolder.getPostgreSQLLocation().saveLocations(locations);
        tablesHolder.getPostgreSQLStopAndSearch().saveStopAndSearches(stopAndSearches);

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

        tablesHolder.getPostgreSQLStreet().saveStreets(streets);
        tablesHolder.getPostgreSQLLocation().saveLocations(locations);
        tablesHolder.getPostgreSQLOutcomeStatus().saveOutcomeStatuses(outcomeStatuses);
        tablesHolder.getPostgreSQLCrime().saveCrimes(crimes);

    }

}
