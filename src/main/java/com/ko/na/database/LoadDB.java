package com.ko.na.database;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

public class LoadDB extends SqlTable {
	public LoadDB() throws Exception {
		super();
	} // end constructor
	
	public boolean execute(String sql) throws Exception{
		boolean result = false;
		try {
			result = statement.execute(sql);
		} catch (MySQLSyntaxErrorException ex1){
			System.err.println(ex1.getLocalizedMessage());
		} // end try/catch 
		return result;
	} // end execute() method
	
	public static void main(String[] args) throws Exception{
		String[] sql = {
				       "DROP FUNCTION IF EXISTS CDate; "
				     + "DELIMITER $$ "
                     + "CREATE FUNCTION CDate(dateStr CHAR(12)) "
                     + "     RETURNS DATE DETERMINISTIC "
                     + "BEGIN "
                     + "     DECLARE result date; "
                     + "     IF(LENGTH(dateStr) = 4) then "
                     + "            set result = STR_TO_DATE(CONCAT('1/1/', dateStr), '%m/%d/%Y'); " 
                     + "	 else "
                     + "            set result = STR_TO_DATE(dateStr, '%m/%d/%Y'); "
                     + "	 END IF; "
                     + "	return (result); "
                     + "END$$ "
                     + "DELIMITER ;",
                     
                       "DROP FUNCTION IF EXISTS hello_world; \n"
        			 + "DELIMITER $$ \n"
                     + "CREATE FUNCTION hello_world() \n"
                     + "  RETURNS TEXT \n"
                     + "  LANGUAGE SQL \n"
                     + "BEGIN \n"
                     + "  RETURN 'Hello World'; \n"
                     + "END; \n"
                     + "$$ \n"
                     + "DELIMITER ;"
                     };
		
		LoadDB  t = new LoadDB();
		t.execute(sql[1]);
	} // end main() method

} // end LoadDB class
