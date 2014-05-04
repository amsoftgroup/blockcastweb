package me.blockcast.data;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.postgresql.geometric.PGpoint;

import me.blockcast.database.postgres.Database;
import me.blockcast.web.pojo.Post;

public class BlockcastManager {

	public static ArrayList<Post> getPostWithinRadius(int distance, double lon, double lat){

		String sql = "SELECT ST_Distance_Sphere(location , ST_MakePoint(?,?)  ) as dist_meters,  " +
				"id, content, parent_id, post_timestamp from op ";
		sql += " WHERE st_dwithin(location, ST_MakePoint(?,?), ? )" +
				" order by dist_meters asc";

		ArrayList<Post> ets = new ArrayList<Post>();

		PreparedStatement ps =  null;
		Connection c = null;

		Database d = new Database();

		try {

			c = d.getConnection();

			ps = c.prepareStatement(sql);
			System.out.println("SQL: " + sql);
			ps.setDouble(1, lon);
			ps.setDouble(2, lat);
			ps.setDouble(3, lon);
			ps.setDouble(4, lat);	
			ps.setInt(5, distance);

			ResultSet rs = ps.executeQuery();

			while( rs.next() ){		
				Post op = new Post();
				op.setContent(rs.getString("content"));
				op.setId(rs.getInt("id"));
				op.setParentId(rs.getInt("parent_id"));
				op.setPostTimestamp(rs.getDate("post_timestamp"));
				op.setDistance(rs.getLong("dist_meters"));
				//System.out.println( "dist_meters:" + op.getDistance()) ;
				ets.add(op);
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

		ps = null;
		c = null;
		d = null;
		
		return ets;
	}


	public static void insertPost(Post post){

		System.out.println( "post.getContent()  : " + post.getContent()) ;
		System.out.println( "post.getDistance()  : " + post.getDistance()) ;
		System.out.println( "post.getDuration()  : " + post.getDuration()) ;
		String sql = " INSERT INTO op(location, content, parent_id, post_timestamp, post_duration, post_radius_meters) " + 
				//"VALUES(ST_GeomFromText('POINT(" + lon + " " +  lat + ")', 4326), ? , -1, CURRENT_TIMESTAMP);";
				//"VALUES(ST_GeomFromText(ST_MakePoint(?, ?), 4326), ? , -1, CURRENT_TIMESTAMP, ?, ?);";
				"VALUES(ST_MakePoint(?, ?), ? , -1, CURRENT_TIMESTAMP, ?, ?);";
		PreparedStatement ps =  null;
		Connection c = null;

		Database d = new Database();

		try {

			c = d.getConnection();
			ps = c.prepareStatement(sql);
		
			ps.setDouble(1, post.getLocation().getLon());
			ps.setDouble(2, post.getLocation().getLat());
			ps.setString(3, post.getContent());
			ps.setLong(4, post.getDuration());
			ps.setLong(5, post.getDistance());
			ps.execute();
			
			ps.close();
			c.close();
		
			
		}catch( SQLException se ){

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
		
		ps = null;
		c = null;
		d = null;

	}
	
	public static String testDB(){


		String sql = " Select version()";
		PreparedStatement ps =  null;
		Connection c = null;
		String version = null;
		
		Database d = new Database();

		try {

			c = d.getConnection();
			ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while( rs.next() ){		
				
				version = rs.getString("version");

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

		ps = null;
		c = null;
		d = null;
		
		return version;
	}

}
