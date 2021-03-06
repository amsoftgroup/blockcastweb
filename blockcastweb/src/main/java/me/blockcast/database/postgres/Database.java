package me.blockcast.database.postgres;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class Database {

	private static String host = "localhost";
	private static int port = 5432;
	private static String database = "xxxxxxx";
	private static String user = "xxxxxxx";
	private static String password = "xxxxxxx";
	private static String TAG = "Database";
	
	//Static class instantiation
	static {

		try{
			new JDCConnectionDriver(
					"org.postgresql.Driver", 
					"jdbc:postgresql://" + host + ":" + port + "/" + database,
					user, 
					password);

			System.out.println("Connection Succeeded: ");
	
		}catch(Exception e){
			System.out.println("Connection Failed: " + e.toString());
		}
	}


	public Connection getConnection() 
	throws SQLException{
		return DriverManager.getConnection(
		"jdbc:jdc:jdcpool");
	}

}


