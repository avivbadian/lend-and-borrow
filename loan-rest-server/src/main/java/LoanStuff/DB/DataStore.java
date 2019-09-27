package LoanStuff.DB;

import LoanStuff.Config.DbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataStore {
    private Connection conn;

    public DataStore() throws SQLException {
        connect();
    }

    // Connects to the specified database
    public boolean connect() throws SQLException {
        if (DbConfig.Instance.HOST.isEmpty() || DbConfig.Instance.DB_NAME.isEmpty() || DbConfig.Instance.USER.isEmpty() || DbConfig.Instance.PASS.isEmpty()) {
            throw new SQLException("Database credentials missing");
        }
        
        conn = DriverManager.getConnection(
                DbConfig.Instance.HOST + DbConfig.Instance.DB_NAME,
                DbConfig.Instance.USER, DbConfig.Instance.PASS);
        return true;
    }

    public ResultSet execQuery(String query) throws SQLException {
        return this.conn.createStatement().executeQuery(query);
    }

    public int execUpdate(String query) throws SQLException {
        return this.conn.createStatement().executeUpdate(query);
    }
}
