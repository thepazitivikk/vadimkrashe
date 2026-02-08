package org.thepazitivik.krasheegorvadim.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseProvider {

    void connect() throws SQLException;

    void disconnect();

    Connection getConnection() throws SQLException;

    boolean isConnected();
}

