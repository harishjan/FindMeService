
/*
 * BU Term project for cs622
 
  This class implements the functionality required to create a sqllite repo
 * @author  Harish Janardhanan * 
 * @since   21-jan-2022
 */
package com.helpfinder.repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.sql.DataSource;
import javax.transaction.NotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// this is the implementation for database repository for sqlite
@Component
public class SqlliteRepository implements DatabaseRepository {
     @Autowired
    private DataSource dataSource;

    /**
     * constructor
     * 
     * @param dataSource the data source used
     */
    public SqlliteRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /***
     * get the sqllite connection
     * 
     * @return Connection connection to sql lite
     * @throws ClassNotFoundException exception if the sql lite driver is not found
     * @throws SQLException           if connection cannot be established
     */
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        System.out.println("Opened database successfully");
        return connection;

    }

    
    /***
     * gets the results of update statement like, update, insert and delete queries
     * @param query the sql query
     * @param addParameters Consumer<PreparedStatement> the lambda function called to add the parameter in PreparedStatment
     * @return return the number of rows impacted by the query
     * @throws SQLException
     */
    private int executeUpdateStatement(String query, Consumer<PreparedStatement> addParameters) throws SQLException{
        Connection connection = null;
        PreparedStatement statement = null;
        // -1 if the execution is not successful
        int result = -1;
        try {
            // get connection and create statement to execute
            connection = getConnection();
            statement = connection.prepareStatement(query);
            addParameters.accept(statement);
            result = statement.executeUpdate();
            connection.commit();
            
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error connecting to db " + ex.getMessage());
        } finally {
            // close the connection
            if (statement != null)                
                statement.close();                
            if (connection != null) 
                connection.close();
                
        }
        // return the result
        return result;
    }

    /***
     * gets result for a select statement
     * 
     * @param query the select query
     * @param addParameters Consumer<PreparedStatement> the lambda function called to add the parameter in PreparedStatment
     * @param processResult Function<ResultSet, R> the lambda function which is called to process the result and return the type of the final object
     * @return R the return type is the type returned by processResult lambda function     * 
     * @throws SQLException 
     */
    @Override
    public <R> R executeSelectQuery(String query, Consumer<PreparedStatement> addParameters, Function<ResultSet, R> processResult) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        R output = null;
        try {
            // get connection and create statement to execute            
            connection = getConnection();
            statement = connection.prepareStatement(query);
            addParameters.accept(statement);
            // execute the query
            result = statement.executeQuery();
            output = processResult.apply(result);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error connecting to db " + ex.getMessage());
        } finally {
            if(result != null)
            	result.close();
            if (statement != null) 
                    statement.close();
            if (connection != null) 
                    connection.close();
        }
		
        return output;
    }

    /***
     * executes the update statement
     * 
     * @param query update query
     * @param addParameters Consumer<PreparedStatement> the lambda function called to add the parameter in PreparedStatment
     * @return int the number of rows effected
     */
    @Override
    public int executeUpdateQuery(String query, Consumer<PreparedStatement> addParameters) throws SQLException{
        return executeUpdateStatement(query, addParameters);
    }

    /***
     * executes the insert statement
     * @param query update query
     * @param addParameters Consumer<PreparedStatement> the lambda function called to add the parameter in PreparedStatment
     * @return int the number of rows effected
     */
    @Override
    public int executeInsertQuery(String query, Consumer<PreparedStatement> addParameters) throws SQLException{
        return executeUpdateStatement(query, addParameters);
    }

    /***
     * executes the delete statement
     * @param query update query
     * @param addParameters Consumer<PreparedStatement> the lambda function called to add the parameter in PreparedStatment
     * @return int the number of rows effected
     */

    @Override
    public int executeDeleteQuery(String query, Consumer<PreparedStatement> addParameters) throws SQLException{
        return executeUpdateStatement(query, addParameters);
    }

    /***
     * not supported by sqllite
     * 
     * @throws NotSupportedException
     */
    public <R> R executeSproc(String sprocName, Function<ResultSet, R> processResult)
            throws SQLException, NotSupportedException {
        throw new NotSupportedException("Sprocs are not supported by sqllite");
    }
}
