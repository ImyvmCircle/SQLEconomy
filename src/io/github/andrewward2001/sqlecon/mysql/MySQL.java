package io.github.andrewward2001.sqlecon.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.andrewward2001.sqlecon.Database;

/**
 * Connects to and uses a MySQL database
 * 
 * @author -_Husky_-
 * @author tips48
 */
public class MySQL extends Database {
    private final String user;
    private final String database;
    private final String password;
    private final String port;
    private final String hostname;
    private final boolean ssl;
    private final boolean trustSSL;

    /**
     * Creates a new MySQL instance
     *
     * @param hostname
     *            Name of the host
     * @param port
     *            Port number
     * @param username
     *            Username
     * @param password
     *            Password
     */
    public MySQL(String hostname, String port, String username,
            String password, boolean ssl, boolean trustSSL) {
        this(hostname, port, null, username, password, ssl, trustSSL);
    }

    /**
     * Creates a new MySQL instance for a specific database
     *
     * @param hostname
     *            Name of the host
     * @param port
     *            Port number
     * @param database
     *            Database name
     * @param username
     *            Username
     * @param password
     *            Password
     */
    public MySQL(String hostname, String port, String database,
            String username, String password, boolean ssl, boolean trustSSL) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.user = username;
        this.password = password;
        this.ssl = ssl;
        this.trustSSL = trustSSL;
    }

    @Override
    public Connection openConnection() throws SQLException,
            ClassNotFoundException {
        if (checkConnection()) {
            return connection;
        }

        String connectionURL = "jdbc:mysql://"
                + this.hostname + ":" + this.port;
        if (database != null) {
            connectionURL = connectionURL + "/" + this.database;
        }
        if (ssl == true) {
            connectionURL += "?";

            if(trustSSL == true)
                connectionURL += "verifyServerCertificate=false&";

            connectionURL += "useSSL=true";
        }

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(connectionURL,
                this.user, this.password);
        return connection;
    }
}
