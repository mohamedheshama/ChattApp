package org.project.model.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnection implements ConnectionStrategy{

   static MysqlConnection connectionFactory;
    private MysqlConnection() {
    }
    //private String driverClassName = "oracle.jdbc.driver.OracleDriver";
    private String connectionUrl = "jdbc:mysql://localhost:3306/mydb";
    private String dbUser = "root";
    private String dbPwd = "password";
    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(connectionUrl, dbUser, dbPwd);
        return conn;
    }

    public static MysqlConnection getInstance() {
        if (connectionFactory == null) {
            connectionFactory = new MysqlConnection();
        }
        return connectionFactory;
    }


}
