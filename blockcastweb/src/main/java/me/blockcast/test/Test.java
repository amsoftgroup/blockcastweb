package me.blockcast.test;

import java.util.ArrayList;

import me.blockcast.data.*;
import me.blockcast.web.pojo.Location;
import me.blockcast.web.pojo.Post;
public class Test {

	
	public static void main(String[] args){
		
		ArrayList<Post> posts = BlockcastManager.getPostWithinRadius(100, -71.060316, 48.432044);
		
		for (int i = 0; i < posts.size(); i ++){
			System.out.println("Content: " + posts.get(i).getContent());
		}
		
		Location location = new Location();
		location.setLon(-71.060316);
		location.setLat(48.432044);
			
		Post post = new Post();
		post.setContent("lorem ipsum 2");
		post.setLocation(location);
		post.setParentId(-1);
		
		BlockcastManager.insertPost(post);
		
		System.exit(0);
	}
}
