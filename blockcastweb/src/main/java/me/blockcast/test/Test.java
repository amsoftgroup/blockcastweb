package me.blockcast.test;

import java.util.ArrayList;

import me.blockcast.data.*;
import me.blockcast.common.Location;
import me.blockcast.common.Post;

public class Test {

	
	public static void main(String[] args){
		
		ArrayList<Post> posts = BlockcastManager.getPostWithinRadius(100, 72.1, 50);
		
		for (int i = 0; i < posts.size(); i ++){
			System.out.println("Content: " + posts.get(i).getContent());
			System.out.println("->Distance: " + posts.get(i).getDistance());
		}
		
		Location location = new Location();
		location.setLon(72);
		location.setLat(50);
			
		Post post = new Post();
		post.setContent("lorem ipsum 2");
		post.setLocation(location);
		post.setParentId(-1);
		
		//BlockcastManager.insertPost(post);
		
		System.exit(0);
	}
}
