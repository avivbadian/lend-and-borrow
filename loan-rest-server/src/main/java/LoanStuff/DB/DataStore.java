package LoanStuff.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import LoanStuff.Config.DbConfig;

public class DataStore {
    private Connection conn;


    // TODO: Create connection pool instead of ctor connect.
    public DataStore() throws SQLException, ClassNotFoundException {
        connect();
    }

    // Connects to the specified database
    public boolean connect() throws SQLException, ClassNotFoundException {
        if (DbConfig.Instance.HOST.isEmpty() || DbConfig.Instance.DB_NAME.isEmpty() || DbConfig.Instance.USER.isEmpty() || DbConfig.Instance.PASS.isEmpty()) {
            throw new SQLException("Database credentials missing");
        }

        // Loading the driver
        // Class.forName("org.postgresql.Driver");

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

    public int insert(String table, Map<String, Object> values) throws SQLException {

        StringBuilder columns = new StringBuilder();
        StringBuilder vals = new StringBuilder();

        for (String col : values.keySet()) {
            columns.append(col).append(",");

            if (values.get(col) instanceof String) {
                vals.append("'").append(values.get(col)).append("',");
            }
            else vals.append(values.get(col)).append(",");
        }

        columns.setLength(columns.length()-1);
        vals.setLength(vals.length()-1);
        String query = String.format("INSERT INTO %s (%s) VALUES (%s)", table,
                columns.toString(), vals.toString());
        return conn.createStatement().executeUpdate(query);
    }


    public int update(String table, String cond, Map<String, Object> updateValues) throws SQLException{
        StringBuilder sb = new StringBuilder();
        // Building update query
        for (String col : updateValues.keySet()) {
            sb.append(col).append("=");

            if (updateValues.get(col) instanceof String) {
                sb.append("'").append(updateValues.get(col)).append("',");
            } else {
                sb.append(updateValues.get(col)).append(",");
            }
        }
        // Removing last ','
        sb.setLength(sb.length() - 1);
        String query = String.format("UPDATE %s SET %s WHERE %s;", table,
                sb.toString(), cond);
        return conn.createStatement().executeUpdate(query);
    }

    public int delete(String table, String cond) throws SQLException {
        String query = String.format("DELETE FROM %s WHERE %s;", table, cond);
        return conn.createStatement().executeUpdate(query);
    }
}
