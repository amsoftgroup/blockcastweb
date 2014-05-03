package me.blockcast.database.postgres;


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class BasicConnection {

	//private static String host = "192.168.211.1";
	private static String host = "ec2-107-20-45-104.compute-1.amazonaws.com";
	private static int port = 5432;
	private static String database = "blockcast";
	private static String user = "blockcast";
	private static String password = "Cym4ge!!Cym4ge!!";

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




