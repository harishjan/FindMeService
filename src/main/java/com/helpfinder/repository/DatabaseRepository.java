/*
 * BU Term project for cs622
 
  This interface defines the functionalities required for a database repository
 * @author  Harish Janardhanan * 
 * @since   21-jan-2022
 */
package com.helpfinder.repository;

import java.sql.*;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.transaction.NotSupportedException;

import org.springframework.stereotype.Component;

@Component
public interface DatabaseRepository {
    
    // executes select query and returns the results
    public <R> R executeSelectQuery(String query, Consumer<PreparedStatement> addParameters, Function<ResultSet, R> processResult)  throws SQLException;
    // executes an update query and returns the results
    public int executeUpdateQuery(String query, Connection connection, Consumer<PreparedStatement> addParameters)  throws SQLException;    
    // executes an insert query and returns the results
    public int executeInsertQuery(String query, Connection connection, Consumer<PreparedStatement> addParameters)  throws SQLException;
    // executes an delete query and returns the results
    public int executeDeleteQuery(String query, Connection connection, Consumer<PreparedStatement> addParameters)  throws SQLException;    
    // executes sproc
    public <R> R executeSproc(String sprocName, Function<ResultSet, R> processResult)  throws SQLException, NotSupportedException;
    public Connection getConnection() throws ClassNotFoundException, SQLException;
    
    

}
