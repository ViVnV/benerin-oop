package com.benerin.interfaces;

import java.sql.SQLException;

public interface CRUDOperations<T> {
    void add(T t) throws SQLException;
    void update(T t) throws SQLException;
    void delete(int id) throws SQLException;
}
