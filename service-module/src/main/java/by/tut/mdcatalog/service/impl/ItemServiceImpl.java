package by.tut.mdcatalog.service.impl;

import static by.tut.mdcatalog.service.constant.ServiceErrors.NO_CONNECTION;
import static by.tut.mdcatalog.service.constant.ServiceErrors.QUERY_FAILED;
import by.tut.mdcatalog.data.resources.ItemRepository;
import by.tut.mdcatalog.data.resources.connection.ConnectionService;
import by.tut.mdcatalog.data.resources.exception.PreparedStatementException;
import by.tut.mdcatalog.data.resources.model.Item;
import by.tut.mdcatalog.service.ItemService;
import by.tut.mdcatalog.service.converter.ItemConverter;
import by.tut.mdcatalog.service.exception.ConnectionException;
import by.tut.mdcatalog.service.exception.RequestException;
import by.tut.mdcatalog.service.model.ItemDTO;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ItemServiceImpl implements ItemService {
    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
    private final ConnectionService connectionService;
    private final ItemConverter itemConverter;
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ConnectionService connectionService, ItemConverter itemConverter, ItemRepository itemRepository) {
        this.connectionService = connectionService;
        this.itemConverter = itemConverter;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemDTO add(ItemDTO itemDTO) {
        Item item = itemConverter.fromDTO(itemDTO);
        try (Connection connection = connectionService.getConnection()) {
            try {
                connection.setAutoCommit(false);
                item = itemRepository.add(connection, item);
                connection.commit();
                return itemConverter.toDTO(item);
            } catch (PreparedStatementException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new RequestException(QUERY_FAILED, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionException(NO_CONNECTION, e);
        }
    }


    @Override
    public List<ItemDTO> getItems() {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        try (Connection connection = connectionService.getConnection()) {
            try {
                connection.setAutoCommit(false);
                List<Item> itemList = itemRepository.readAll(connection);
                for (Item item : itemList) {
                    itemDTOList.add(itemConverter.toDTO(item));
                }
                connection.commit();
                return itemDTOList;
            } catch (PreparedStatementException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new RequestException(QUERY_FAILED, e);
            }
        } catch (
                SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionException(NO_CONNECTION, e);
        }
    }

    @Override
    public int update(Long id, String newStatus) {
        try (Connection connection = connectionService.getConnection()) {
            try {
                connection.setAutoCommit(false);
                Item item = itemRepository.read(connection, id);
                if (item != null && item.getStatus().equals(newStatus)) {
                    return -1;
                }
                int affectedRows = itemRepository.update(connection, id, newStatus);
                connection.commit();
                return affectedRows;
            } catch (PreparedStatementException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new RequestException(QUERY_FAILED, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionException(NO_CONNECTION, e);
        }
    }
}
