package by.tut.mdcatalog.data.resources.impl;

import by.tut.mdcatalog.data.resources.AuditItemRepository;
import static by.tut.mdcatalog.data.resources.constant.DataErrorMessages.NO_AUDIT_ITEM_ID;
import static by.tut.mdcatalog.data.resources.constant.DataErrorMessages.NO_ITEMS_AFFECTED;
import by.tut.mdcatalog.data.resources.exception.PreparedStatementException;
import by.tut.mdcatalog.data.resources.model.AuditItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class AuditItemRepositoryImpl implements AuditItemRepository {
    private static final Logger logger = LoggerFactory.getLogger(AuditItemRepositoryImpl.class);

    @Override
    public AuditItem add(Connection connection, AuditItem auditItem) throws PreparedStatementException {
        String insertTableSQL = "INSERT INTO audit_item (action,item_id,date) VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(
                insertTableSQL,
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, auditItem.getAction());
            statement.setLong(2, auditItem.getItemID());
            statement.setDate(3, auditItem.getDate());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException(String.format(NO_ITEMS_AFFECTED, auditItem));
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    auditItem.setId(generatedKeys.getLong(1));
                    return auditItem;
                } else {
                    throw new SQLException(String.format(NO_AUDIT_ITEM_ID, auditItem));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new PreparedStatementException(e.getMessage(), e);
        }
    }
}
