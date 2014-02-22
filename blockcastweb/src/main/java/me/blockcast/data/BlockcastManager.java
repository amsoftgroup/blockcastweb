package me.blockcast.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.blockcast.database.postgres.Database;

public class BlockcastManager {

	
	public static String getTopLevelPosts(int teamid){
		
		String result = null;
		PreparedStatement ps =  null;
		Connection c = null;
		Database d = new Database();

		try {
			
			c = d.getConnection();
			String sql = "select name from teams where id = ?;";

			ps = c.prepareStatement(sql);
			ps.setInt(1, teamid);
			ResultSet rs = ps.executeQuery();

			if( rs.next() ){		
				result = rs.getString("name");
				
			}

			rs.close();
			ps.close();
			c.close();
		}
		catch( SQLException se )
		{
			System.out.println( "SQL Exception:" ) ;

			while( se != null )
			{
				System.out.println( "State  : " + se.getSQLState()  ) ;
				System.out.println( "Message: " + se.getMessage()   ) ;
				System.out.println( "Error  : " + se.getErrorCode() ) ;

				se = se.getNextException() ;
			}
		}
		catch( Exception e )
		{
			System.out.println( e ) ;
		}
		
		return result;
	}
	
	
	
}
