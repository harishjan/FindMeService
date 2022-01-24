
/*
 * BU Term project for cs622
 
 	This class tells Hibernate how SQLite handles @Id columns, 
 * @author  Harish Janardhanan * 
 * @since   20-jan-2022
 */
package com.helpfinder.repository;

import org.hibernate.MappingException;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;
import org.springframework.stereotype.Component;
@Component
public class SQLiteIdentityColumnSupport extends IdentityColumnSupportImpl {

	/***
	 * gets the value indicating if identity column is supported or not
	 */
    @Override
    public boolean supportsIdentityColumns() {
        return true;
    }

    /***
     * returns the select statement in sqlite to select identity column
     */
    @Override
    public String getIdentitySelectString(String table, String column, int type) 
      throws MappingException {
        return "select last_insert_rowid()";
    }
    /***
     * gets the type used for identity column
     */

    @Override
    public String getIdentityColumnString(int type) throws MappingException {
        return "integer";
    }
}