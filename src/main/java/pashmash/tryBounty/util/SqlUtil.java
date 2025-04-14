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

    public void update(String query) {
        if (isConnected()) {
            try {
                connection.createStatement().executeUpdate(query);
            } catch (SQLException ignored) { }
        }
    }
    public void execute(String query, Object... params) {
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

    public ResultSet getResult(String query) {
        if (isConnected()) {
            try {
                return connection.createStatement().executeQuery(query);
            } catch (SQLException ignored) { }
        }

        return null;
    }

    public String get(String database, String value) {
        return get(database, value, "");
    }

    public String get(String database, String value, String where) {
        if (isConnected()) {
            ResultSet resultSet = getResult("SELECT " + value + " FROM " + database + (where == null || where.equalsIgnoreCase("") ? "" : " WHERE " + where));

            if (resultSet != null) {
                try {
                    if (resultSet.next()) {
                        return resultSet.getString(value);
                    }
                } catch (SQLException ignored) { }
            }
        }

        return "";
    }

}
