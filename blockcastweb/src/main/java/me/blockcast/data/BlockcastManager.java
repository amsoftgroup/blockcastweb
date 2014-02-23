package me.blockcast.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.postgresql.geometric.PGpoint;

import me.blockcast.database.postgres.Database;
import me.blockcast.web.pojo.Post;

public class BlockcastManager {

	public static ArrayList<Post> getPostWithinRadius(int distance, double lon, double lat){

		//String sql = "SELECT ST_Distance(location, GeomFromText('POINT(" + lon + " " + lat + ")',4326)  ) as dist_meters,  " +
		String sql = "SELECT ST_Distance_Sphere(location , GeomFromText(ST_MakePoint(?, ?),4326)  ) as dist_meters,  " +
				"id, content, parent_id, post_timestamp from op ";
		//sql += " WHERE st_dwithin(location, GeomFromText('POINT(" + lon + " " + lat +")',4326)," + distance + ")" +
		sql += " WHERE st_dwithin(location, GeomFromText(ST_MakePoint(?, ?),4326), ? )" +
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

		String sql = " INSERT INTO op(location, content, parent_id, post_timestamp) " + 
				//"VALUES(ST_GeomFromText('POINT(" + lon + " " +  lat + ")', 4326), ? , -1, CURRENT_TIMESTAMP);";
				"VALUES(ST_GeomFromText(ST_MakePoint(?, ?), 4326), ? , -1, CURRENT_TIMESTAMP);";
		PreparedStatement ps =  null;
		Connection c = null;

		Database d = new Database();

		try {

			c = d.getConnection();
			ps = c.prepareStatement(sql);
		
			ps.setDouble(1, post.getLocation().getLon());
			ps.setDouble(2, post.getLocation().getLat());
			ps.setString(3, post.getContent());
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
}
