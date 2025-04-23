package pashmash.tryBounty.util;

import lombok.Getter;

import java.sql.*;
import java.util.Properties;

@Getter
public class SqlUtil {

    private Connection connection;

    public SqlUtil(String host, String port, String database, String username, String password) {
        if (!(isConnected())) {
            try {
                String login = "jdbc:mysql://" + host + ":" + port + "/" + database;
                Properties connectionProperties = new Properties();

                connectionProperties.put("user", username);
                connectionProperties.put("password", password);
                connectionProperties.put("autoReconnect", "true");

                connection = DriverManager.getConnection(login, connectionProperties);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void update(String query, Object... params) {
        if (!isConnected()) return;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResult(String query, Object... params) {
        if (!isConnected()) return null;

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String get(String database, String value, String where, Object... params) {
        if (!isConnected()) return "";

        String query = "SELECT " + value + " FROM " + database + (where == null || where.isEmpty() ? "" : " WHERE " + where);
        try (ResultSet resultSet = getResult(query, params)) {
            if (resultSet != null && resultSet.next()) {
                return resultSet.getString(value);
            }
        } catch (SQLException ignored) { }
        return "";
    }
}