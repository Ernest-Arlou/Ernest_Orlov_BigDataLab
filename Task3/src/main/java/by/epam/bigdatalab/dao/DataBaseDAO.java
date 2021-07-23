package by.epam.bigdatalab.dao;

import by.epam.bigdatalab.bean.Crime;
import by.epam.bigdatalab.bean.StopAndSearch;

import java.util.List;


public interface DataBaseDAO {

    void saveStopAndSearches(List<StopAndSearch> stopAndSearches);

    void saveCrimes(List<Crime> crimes);
}
