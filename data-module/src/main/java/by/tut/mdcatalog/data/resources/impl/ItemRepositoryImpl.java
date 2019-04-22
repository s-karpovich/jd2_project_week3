package by.tut.mdcatalog.data.resources.impl;

import static by.tut.mdcatalog.data.resources.constant.DataErrorMessages.NO_ITEM_ID;
import static by.tut.mdcatalog.data.resources.constant.DataErrorMessages.NO_AUDIT_ITEMS;
import by.tut.mdcatalog.data.resources.ItemRepository;
import by.tut.mdcatalog.data.resources.exception.PreparedStatementException;
import by.tut.mdcatalog.data.resources.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private static final Logger logger = LoggerFactory.getLogger(ItemRepositoryImpl.class);

    @Override
    public Item add(Connection connection, Item item) throws PreparedStatementException {
        String insertTableSQL = "INSERT INTO item (name,status) VALUES (?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                insertTableSQL,
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setString(1, item.getName());
            preparedStatement.setString(2, item.getStatus());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException(String.format(NO_AUDIT_ITEMS, item));
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getLong(1));
                    return item;
                } else {
                    throw new SQLException(String.format(NO_ITEM_ID, item));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new PreparedStatementException(e.getMessage(), e);
        }
    }

    @Override
    public Item read(Connection connection, Long id) throws PreparedStatementException {
        String selectTableSQL = "SELECT * FROM item WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectTableSQL)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return getItem(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new PreparedStatementException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Item> readAll(Connection connection) throws PreparedStatementException {
        String selectTableSQL = "SELECT * FROM item";
        List<Item> itemList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectTableSQL)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    itemList.add(getItem(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new PreparedStatementException(e.getMessage(), e);
        }
        return itemList;
    }

    @Override
    public int update(Connection connection, Long id, String newStatus) throws PreparedStatementException {
        String updateTableSQL = "UPDATE item SET status=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateTableSQL)) {
            preparedStatement.setString(1, newStatus);
            preparedStatement.setLong(2, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new PreparedStatementException(e.getMessage(), e);
        }
    }

    private Item getItem(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        item.setId(resultSet.getLong("id"));
        item.setName(resultSet.getString("name"));
        item.setStatus(resultSet.getString("status"));
        return item;
    }
}