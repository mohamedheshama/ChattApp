package org.project.model.dao;

import org.project.model.connection.ConnectionStrategy;
import org.project.model.connection.MysqlConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersTest {
    Connection connection = null;
    PreparedStatement ptmt = null;
    ResultSet resultSet = null;
    ConnectionStrategy connectionStrategy;

    public UsersTest(ConnectionStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
    }

    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = connectionStrategy.getConnection();
        return conn;
    }

    public void findAll() {
        try {
            String queryString = "SELECT NAME FROM MYDB.USERS";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            resultSet = ptmt.executeQuery();
            while (resultSet.next()) {
                System.out.println("USER_NAME " + resultSet.getString("NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public void delete(int rollNo) {

        try {
            String queryString = "DELETE FROM `MYDB`.`USERS` WHERE (`USERid` = ?)";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setInt(1, rollNo);
            ptmt.executeUpdate();
            System.out.println("Data deleted Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    public static void main(String[] args) {
        ConnectionStrategy connectionStrategy = new MysqlConnection();
        UsersTest usersTest = new UsersTest(connectionStrategy);
        usersTest.findAll();
        usersTest.delete(2);
        usersTest.findAll();
    }
}


