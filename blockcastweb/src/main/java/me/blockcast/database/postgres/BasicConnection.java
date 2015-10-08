package me.blockcast.database.postgres;


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class BasicConnection {

	//private static String host = "192.168.211.1";
	private static String host = "xxxxxxxxxx";
	private static int port = 5432;
	private static String database = "xxxxxxxxxx";
	private static String user = "xxxxxxxxxx";
	private static String password = "xxxxxxxxxx";

	private static String TAG = "Database";

	//Static class instantiation
	public Connection getConnection() {
		Connection conn = null;
		
		try{
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://" + host + "/blockcast";
			Properties props = new Properties();
			props.setProperty("user",user);
			props.setProperty("password",password);
			//props.setProperty("ssl","true");
			conn = DriverManager.getConnection(url, props);
			System.out.println("Connection Succeeded: ");

		}catch(Exception e){
			System.out.println("Connection Failed: " + e.toString());
		}
		return conn;
	}
}




