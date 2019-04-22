package by.tut.mdcatalog.data.resources;

import by.tut.mdcatalog.data.resources.exception.PreparedStatementException;
import by.tut.mdcatalog.data.resources.model.Item;
import java.sql.Connection;
import java.util.List;

public interface ItemRepository {
        Item add(Connection connection, Item item) throws PreparedStatementException;

        Item read(Connection connection, Long id) throws PreparedStatementException;

        List<Item> readAll(Connection connection) throws PreparedStatementException;

        int update(Connection connection, Long id, String newStatus) throws PreparedStatementException;
    }