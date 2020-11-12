package by.epam.bigdatalab.dao;

import by.epam.bigdatalab.bean.Crime;

import java.util.Set;

public interface DataBaseDAO {

    void saveCrimesToDB(Set<Crime> crimes);
}
