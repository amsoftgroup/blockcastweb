<%@ page import="me.blockcast.data.BlockcastManager" %>
<%@ page import="me.blockcast.web.pojo.Post" %>
<%@ page import="me.blockcast.web.pojo.Location" %>
<%@ page import="java.util.Random" %>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">


	<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7/leaflet.css" />
	<!-- <link rel="stylesheet" href="./js/leaflet-0.7.3/leaflet.css" />-->
	<link rel="stylesheet" href="./css/MarkerCluster.css" />
	<link rel="stylesheet" href="./css/MarkerCluster.Default.css" />
	<link rel="stylesheet" href="./css/application.css" />
	<style>
		.mycluster {
			width: 40px;
			height: 40px;
			background-color: black;
			color: white;
			text-align: center;
			font-size: 24px;
		}
	</style>
</head>
<body>
  <div id="header">
	<div id="title"><img src='./images/blockcast_mod3.png'></div> 
	<div id='actions'><a href='#'>Find me!</a></div>
  </div>
    <div id="map" style="width: 100%; height: 80%; background: white"></div>
    
	<script src="./js/leaflet-0.7.3/leaflet.js"></script>
	<script src="./js/leaflet-0.7.3/leaflet.markercluster-src.js"></script>
	<script src="./js/jquery-2.1.1/jquery-2.1.1.js"></script>
	<script src="./js/application.js"></script>
<%!
private String makeSentence(){

	String article[]={"the","a","one","some"};
	String noun[]={"boy","girl","dog","town","car"};
	String verb[]={"drove","jumped","ran","walked","skipped"};
	String preposition[]={"to","from","over","under","on"};
	StringBuffer sb = new StringBuffer();
	
	for (int i = 0; i < 20; i++)
	{

		sb.append(article[randInt(0,3)]);
		sb.append(" ");
		sb.append(noun[randInt(0,4)]);
		sb.append(" ");
		sb.append(verb[randInt(0,4)]);
		sb.append(" ");
		sb.append(preposition[randInt(0,4)]);
		sb.append(" ");
		sb.append(article[randInt(0,3)]);
		sb.append(" ");
		sb.append(noun[randInt(0,4)]);
		sb.append(" ");
	}
	
	return sb.toString();
}

public static int randInt(int min, int max) {

    // NOTE: Usually this should be a field rather than a method
    // variable so that it is not re-seeded every call.
    Random rand = new Random();

    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    int randomNum = rand.nextInt((max - min) + 1) + min;

    return randomNum;
}%>
<%
for (int i = 0; i<randInt(5,10); i++) {
	Post post = new Post();
	Location location = new Location();
	// washington dc
	double lat = Math.random() + 38.3951;
	double lon = Math.random() - 77.5367;
	location.setLat(lat);
	location.setLon(lon);
	post.setLocation(location);
	post.setContent(makeSentence());
	post.setDuration(randInt(3600, 86400));
	post.setDistance((long)(100 * Math.random()));
	BlockcastManager.insertPost(post);
	
/*
	//Paris
	lat = Math.random() + 48.34935;
	lon = Math.random() + 1.74558;
	location.setLat(lat);
	location.setLon(lon);
	post.setLocation(location);
	BlockcastManager.insertPost(post);
	
	//Mexico City
	lat = Math.random() + 18.4333;
	lon = Math.random() - 98.6333;
	location.setLat(lat);
	location.setLon(lon);
	post.setLocation(location);
	BlockcastManager.insertPost(post);
*/
	
}
%>
</body>
</html>
    

</body>
</html>
