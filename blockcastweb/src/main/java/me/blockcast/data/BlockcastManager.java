package me.blockcast.data;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import me.blockcast.database.postgres.Database;
import me.blockcast.common.Post;
import me.blockcast.utils.Utils;

public class BlockcastManager {
	
	private static Log log = LogFactory.getLog(BlockcastManager.class);
	private static String timeformat = "yyyy/MM/dd HH:mm:ss";
	private static SimpleDateFormat sdf = new SimpleDateFormat(Utils.timeformat); 
	
	public static ArrayList<Post> getPostWithinRadius(int distance, double lat, double lon){

		
		String sql = "SELECT ST_Distance_Sphere(location , ST_MakePoint(?,?)  ) as dist_meters,  post_duration, " +
				"id, content, parent_id, post_timestamp, ST_X(location::geometry) as lon, ST_Y(location::geometry) as lat from op ";
		sql += " WHERE ST_Distance_Sphere(location, ST_MakePoint(?,?) ) < ?" +
				" order by dist_meters asc";

		ArrayList<Post> ets = new ArrayList<Post>();

		PreparedStatement ps =  null;
		Connection c = null;

		Database d = new Database();

		try {

			c = d.getConnection();

			ps = c.prepareStatement(sql);
			
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
				java.util.Date date = rs.getTimestamp("post_timestamp");				
				op.setEpoch(date.getTime()/1000l);
				op.setDistance(rs.getLong("dist_meters"));
				op.setDuration(rs.getLong("post_duration"));
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

	public static ArrayList<Post> getPostWithinRadiusAndDuration(int distance, double lat, double lon){

		//String sql = "SELECT post_radius_meters as dist_meters, post_duration, round((SELECT (EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP))) - (SELECT EXTRACT(EPOCH FROM post_timestamp )))) as sec_elapsed, " +  
		//Use line below for actual distance to points instead of radius post
		
		/* ST_MakePoint: x is longitude and y is latitude */
		String sql = "SELECT ST_Distance_Sphere(location , ST_MakePoint(?,?)  ) as dist_meters,  post_duration, media_file, " + 
		"round((SELECT (EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP))) - (SELECT EXTRACT(EPOCH FROM post_timestamp )))) as sec_elapsed, " + 
		"id, content, parent_id, post_timestamp, ST_X(location::geometry) as lon, ST_Y(location::geometry) as lat, guid " + 
		"from op " + 
		"GROUP BY id " +  
		"HAVING round((SELECT (EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP))) - (SELECT EXTRACT(EPOCH FROM post_timestamp )))) < post_duration  " + 
		"and (ST_Distance_Sphere(location , ST_MakePoint(?,?)  ) < ?) " + 
		"order by dist_meters asc";
		
		log.info("**********SQL= " + sql + "lat=" + lat + "lon=" + lon + "distance=" + distance);

		ArrayList<Post> ets = new ArrayList<Post>();

		PreparedStatement ps =  null;
		Connection c = null;

		Database d = new Database();

		try {

			c = d.getConnection();

			ps = c.prepareStatement(sql);
			
			ps.setDouble(1, lon);
			ps.setDouble(2, lat);
			ps.setDouble(3, lon);
			ps.setDouble(4, lat);
			ps.setInt(5, distance);
			
			ResultSet rs = ps.executeQuery();
			log.info("**********rs= " + rs.getFetchSize());
			while( rs.next() ){		
				Post op = new Post();
				op.setContent(rs.getString("content"));
				op.setId(rs.getInt("id"));
				op.setParentId(rs.getInt("parent_id"));
				java.util.Date date = rs.getTimestamp("post_timestamp");
				op.setEpoch(date.getTime()/1000l);
				op.setDistance(rs.getLong("dist_meters"));
				op.setDuration(rs.getLong("post_duration"));
				op.setSec_elapsed(rs.getInt("sec_elapsed"));				
				String media_file = rs.getString("media_file");
				if (media_file!=null){
					log.info("media_file=" + media_file);
					op.setMedia_name(media_file);
					String media_name = Utils.getPreview(media_file);
					op.setMedia_preview(media_name);
				}else{
					log.info("media_file IS NULL");
				}
				ets.add(op);
			}

			rs.close();
			ps.close();
			c.close();
		}
		catch( SQLException se )
		{
			log.info("**********EXCEPTION= " + se.toString());
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
			log.info("**********EXCEPTION= " + e.toString());
			System.out.println( e ) ;
		}

		ps = null;
		c = null;
		d = null;
		
		return ets;
	}
	
	public static ArrayList<Post> getPostsByDistanceAndDurationWithGuid(int distance, double lat, double lon, String guid){
		
		String sql = "SELECT ST_Distance_Sphere(location , ST_MakePoint(?,?)  ) as dist_meters,  post_duration, media_file, " + 
		"round((SELECT (EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP))) - (SELECT EXTRACT(EPOCH FROM post_timestamp )))) as sec_elapsed, " + 
		"id, content, parent_id, post_timestamp, ST_X(location::geometry) as lon, ST_Y(location::geometry) as lat, guid " + 
		"from op " + 
		"GROUP BY id " +  
		"HAVING round((SELECT (EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP))) - (SELECT EXTRACT(EPOCH FROM post_timestamp )))) < post_duration  " + 
		"and (ST_Distance_Sphere(location , ST_MakePoint(?,?)  ) < ?) " + 
		"order by dist_meters asc";
		
		log.info("**********SQL= " + sql + "lat=" + lat + "lon=" + lon + "distance=" + distance);

		ArrayList<Post> ets = new ArrayList<Post>();

		PreparedStatement ps =  null;
		Connection c = null;

		Database d = new Database();

		try {

			c = d.getConnection();
			ps = c.prepareStatement(sql);
			
			ps.setDouble(1, lon);
			ps.setDouble(2, lat);
			ps.setDouble(3, lon);
			ps.setDouble(4, lat);
			ps.setInt(5, distance);
			
			ResultSet rs = ps.executeQuery();
			log.info("**********rs= " + rs.getFetchSize());
			while( rs.next() ){		
				Post op = new Post();
				op.setContent(rs.getString("content"));
				op.setId(rs.getInt("id"));
				op.setParentId(rs.getInt("parent_id"));
				java.util.Date date = rs.getTimestamp("post_timestamp");
				op.setEpoch(date.getTime()/1000l);
				op.setDistance(rs.getLong("dist_meters"));
				op.setDuration(rs.getLong("post_duration"));
				op.setSec_elapsed(rs.getInt("sec_elapsed"));				
				String media_file = rs.getString("media_file");
				if (media_file!=null){
					log.info("media_file=" + media_file);
					op.setMedia_name(media_file);
					String media_name = Utils.getPreview(media_file);
					op.setMedia_preview(media_name);
				}else{
					log.info("media_file IS NULL");
				}
				
				String ismine = rs.getString("guid");
				
				if ((ismine != null) && (ismine.length() > 0) && (!ismine.equals("")) && (ismine.equals(guid))){
					op.setMine(1);
				}else{
					op.setMine(0);
				}
				
				ets.add(op);
			}

			rs.close();
			ps.close();
			c.close();
		}
		catch( SQLException se )
		{
			log.info("**********EXCEPTION= " + se.toString());
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
			log.info("**********EXCEPTION= " + e.toString());
			System.out.println( e ) ;
		}

		ps = null;
		c = null;
		d = null;
		
		return ets;
	}
	
	public static ArrayList<Post> getPosts(){
		
		//Use line below for actual distance to points instead of radius post
		//String sql = "SELECT ST_Distance_Sphere(location , ST_MakePoint(?,?)  ) as dist_meters,  post_duration,   round((SELECT (EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP))) - (SELECT EXTRACT(EPOCH FROM post_timestamp )))) as sec_elapsed, " +
		String sql = "SELECT post_radius_meters as dist_meters, post_duration, round((SELECT (EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP))) - (SELECT EXTRACT(EPOCH FROM post_timestamp )))) as sec_elapsed, media_file, " +  
		" id, content, parent_id, post_timestamp, ST_X(location::geometry) as lon, ST_Y(location::geometry) as lat , " +
		" round( (SELECT EXTRACT(EPOCH FROM post_timestamp )) + post_duration - (SELECT (EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP))))) as sec_remaining" + 
		" from op " + 
		" GROUP BY id " + 
		" HAVING round((SELECT (EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP))) - (SELECT EXTRACT(EPOCH FROM post_timestamp )))) < post_duration " + 
		" order by dist_meters asc " +
		// throttle response; this is a debug method
		" limit 100 ";
		
		ArrayList<Post> ets = new ArrayList<Post>();

		PreparedStatement ps =  null;
		Connection c = null;

		Database d = new Database();

		try {

			c = d.getConnection();

			ps = c.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			while( rs.next() ){		
				Post op = new Post();
				op.setContent(rs.getString("content"));
				op.setId(rs.getInt("id"));
				op.setParentId(rs.getInt("parent_id"));
				java.util.Date date = rs.getTimestamp("post_timestamp");
				op.setEpoch(date.getTime()/1000l);
				op.setDistance(rs.getLong("dist_meters"));
				op.setDuration(rs.getLong("post_duration"));
				op.setLat(rs.getDouble("lat"));
				op.setLon(rs.getDouble("lon"));
				op.setSec_remaining(rs.getInt("sec_remaining"));
				op.setSec_elapsed(rs.getInt("sec_elapsed"));
				String media_file = rs.getString("media_file");
				if (media_file!=null){
					//log.info("media_file=" + media_file);
					op.setMedia_name(media_file);
					String media_name = Utils.getPreview(media_file);
					op.setMedia_preview(media_name);
				}else{
					log.info("media_file IS NULL");
				}
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

	public static boolean insertPost(Post post){

		boolean success = false;
		
		System.out.println( "post.getContent()  : " + post.getContent()) ;
		System.out.println( "post.getDistance()  : " + post.getDistance()) ;
		System.out.println( "post.getDuration()  : " + post.getDuration()) ;
		System.out.println( "post.getGuid()  : " + post.getGuid()) ;
		String sql = " INSERT INTO op(location, content, parent_id, post_timestamp, post_duration, post_radius_meters, media_file, ip, guid) " + 
				"VALUES(ST_SetSRID(ST_MakePoint(?, ?), 4326), ? , -1, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?);";
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
			ps.setString(6, post.getMedia_name());
			ps.setString(7, post.getIp());
			ps.setString(8, post.getGuid());
			ps.execute();
			success = true;
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
		return success;

	}
	
	public static int delete(String commentid, String userid){
		
		int i = Integer.parseInt(commentid);
		
		String sql = " DELETE FROM OP WHERE id = ? AND guid = ?";
		PreparedStatement ps =  null;
		Connection c = null;
		int rows = 0;
		Database d = new Database();

		try {

			c = d.getConnection();
			ps = c.prepareStatement(sql);

			ps.setInt(1, i);
			ps.setString(2, userid);
			rows = ps.executeUpdate();
			
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
		
		return rows;
		
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
