package by.epam.bigdatalab.dao;

import by.epam.bigdatalab.bean.Crime;

import java.util.List;


public interface DataBaseDAO {

    void saveCrimesToDB(List<Crime> crimes);
}
