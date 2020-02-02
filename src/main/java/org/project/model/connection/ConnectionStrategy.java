package org.project.model.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionStrategy {
    Connection getConnection()throws SQLException;
}
