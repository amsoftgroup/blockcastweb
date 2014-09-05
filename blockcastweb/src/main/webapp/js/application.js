
var map = L.map('map').setView([38.8951, -77.0367], 13);
var servername = "http://www.blockcast.me";
var api = "/restapiv1.0/";

function getPosts(){
	var result="";
	$.ajax({ 
        type: "GET",
        dataType: "json",
        url: servername + api + "getPosts",
        async: false,
        success: function(data){        
        	result = data;
        }
    });
	return result;
}

$(function () {

	var date = new Date();
	//var time = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
    
	L.tileLayer('https://{s}.tiles.mapbox.com/v3/{id}/{z}/{x}/{y}.png', {
		noWrap: false,
		maxZoom: 18,
		minZoom: 3,
		attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
			'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
			'Imagery © <a href="http://mapbox.com">Mapbox</a>',
		id: 'examples.map-i86knfo3'
	}).addTo(map);
	
	L.control.scale().addTo(map);
	
	var popup = L.popup();

	function onMapClick(e) {
		popup
			.setLatLng(e.latlng)
			.setContent("There are no posts visible near " + e.latlng.toString() + " @ " + date )
			.openOn(map);
	}


	map.on('click', onMapClick);
/*
	L.tileLayer.wms("http://www.blockcast.me/geoserver/wms", {
		layers: 'blockcast:op',
		format: 'image/png',
		version: '1.1.0',
		transparent: false,
		attribution: "",
		tiled:true
		}).addTo(map);
	
*/

	
	/*
	var i = 0;
	var posts = getPosts();
	
	//alert('posts:' + posts);

	for (i = 0; i < posts.length; i++ )
	{
		//alert([posts[i].lat + ' ' +  posts[i].lon]);
		L.marker( [posts[i].lat, posts[i].lon] )
	      .bindPopup( '<a href="' + posts[i].url + '" target="_blank">' + posts[i].content + '</a><br>' + 
	    		  		'('+ posts[i].lat +',' + posts[i].lon +')<br>' +
	    		  		posts[i].postTimeString + '<br>' +
	    		  		posts[i].duration)
	      .addTo( map );
	}
*/
	
	var refreshId = setInterval(markPosts(), 5000);
/*	
	var markers = L.markerClusterGroup();

	var posts = getPosts();
	
	for (var i = 0; i < posts.length; i++) {
		var a = posts[i];
		var title = posts[i].content;
		var seconds = posts[i].sec_remaining;
		var postcolor = '#ff7800';
		if (seconds < 60){
			postcolor = '#ff0000';
		}else if (seconds < 3600){
			postcolor = '#ffcc00';
		}

		//
		var latlng = L.latLng(posts[i].lat, posts[i].lon);
		var circleMarker = new L.CircleMarker(latlng, posts[i].distance, {
			color: postcolor,
	        weight: 1,
	        opacity: 1,
	        fillOpacity: 0.6,
	    });
		circleMarker.bindPopup('test data <br>' + 
		  		'('+ posts[i].lat +',' + posts[i].lon +')<br>' +
		  		posts[i].postTimeString + '<br>' +
		  		posts[i].duration + ' second post<br>' +
		  		posts[i].sec_remaining + ' seconds remaining<br>' +
		  		posts[i].distance + ' meters<br>');
		markers.addLayer(circleMarker);
		//
	}

	map.addLayer(markers);
*/
	
	
});

function markPosts(){
	
	var markers = L.markerClusterGroup();

	var posts = getPosts();
	
	for (var i = 0; i < posts.length; i++) {

		var title = posts[i].content;
		var seconds = posts[i].sec_remaining;
		var postcolor = '#ff7800';
		if (seconds < 60){
			postcolor = '#ff0000';
		}else if (seconds < 3600){
			postcolor = '#ffcc00';
		}
		
		/*
		var marker = L.marker(new L.LatLng(posts[i].lat, posts[i].lon), { title: title });
		marker.bindPopup('test data <br>' + 
		  		'('+ posts[i].lat +',' + posts[i].lon +')<br>' +
		  		posts[i].postTimeString + '<br>' +
		  		posts[i].duration + ' second post<br>' +
		  		posts[i].sec_elapsed + ' seconds elapsed<br>' +
		  		posts[i].sec_remaining + ' seconds remaining<br>' +
		  		posts[i].distance + ' meters<br>');
		markers.addLayer(marker);
		*/
		
		//
	
		var latlng = L.latLng(posts[i].lat, posts[i].lon);
		var circleMarker = new L.Circle(latlng, posts[i].distance, {
			color: postcolor,
	        weight: 1,
	        opacity: 1,
	        fillOpacity: 0.6,
	    });
		circleMarker.bindPopup(title + '<br>' + 
		  		'('+ posts[i].lat +',' + posts[i].lon +')<br>' +
		  		posts[i].postTimeString + '<br>' +
		  		posts[i].duration + ' second post<br>' +
		  		posts[i].sec_elapsed + ' seconds elapsed<br>' +
		  		posts[i].sec_remaining + ' seconds remaining<br>' +
		  		posts[i].distance + ' meter radius<br>');
		
		markers.addLayer(circleMarker);
	
		//
	}

	map.addLayer(markers);
}

$('#actions').find('a').on('click', function() {
  locateUser();
});

function onLocationFound(e) {
    var radius = e.accuracy / 2;

    L.marker(e.latlng).addTo(map)
        .bindPopup("You are within " + radius + " meters from this point").openPopup();

    L.circle(e.latlng, radius).addTo(map);
}

function locateUser() {
	  this.map.locate({setView : true});
	  map.on('locationfound', onLocationFound);
}



