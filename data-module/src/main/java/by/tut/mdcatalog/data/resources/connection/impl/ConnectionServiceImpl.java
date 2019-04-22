package by.tut.mdcatalog.data.resources.connection.impl;

import static by.tut.mdcatalog.data.resources.constant.DataErrorMessages.CONNECTION_ERROR;
import static by.tut.mdcatalog.data.resources.constant.DataErrorMessages.DRIVER_ERROR;
import by.tut.mdcatalog.data.resources.connection.ConnectionService;
import by.tut.mdcatalog.data.resources.exception.DBConnectionException;
import by.tut.mdcatalog.data.resources.exception.JDBCException;
import by.tut.mdcatalog.data.resources.properties.DatabaseProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


@Component
public class ConnectionServiceImpl implements ConnectionService {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionServiceImpl.class);
    private final DatabaseProperties properties;

    @Autowired
    public ConnectionServiceImpl(DatabaseProperties properties) {
        try {
            Class.forName(properties.getDriver());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new JDBCException(DRIVER_ERROR, e);
        }
        this.properties = properties;
    }

    public Connection getConnection() {
        try {
            Properties properties = new Properties();
            properties.setProperty("user", this.properties.getUsername());
            properties.setProperty("password", this.properties.getPassword());
            return DriverManager.getConnection(this.properties.getUrl(), properties);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DBConnectionException(CONNECTION_ERROR, e);
        }
    }
}
