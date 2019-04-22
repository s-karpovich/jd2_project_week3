package by.tut.mdcatalog.data.resources;

import by.tut.mdcatalog.data.resources.exception.PreparedStatementException;
import by.tut.mdcatalog.data.resources.model.AuditItem;
import java.sql.Connection;

public interface AuditItemRepository {
    AuditItem add(Connection connection, AuditItem auditItem) throws PreparedStatementException;
}