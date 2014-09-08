
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
		  		toDisplayTime(posts[i].duration) + ' post<br>' +
		  		toDisplayTime(posts[i].sec_elapsed) + ' elapsed<br>' +
		  		toDisplayTime(posts[i].sec_remaining) + ' remaining<br>' +
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

function toDisplayTime(seconds){
	// get total seconds between the times
	var delta = seconds;

	// calculate (and subtract) whole days
	var days = Math.floor(delta / 86400);
	delta -= days * 86400;

	if (days < 10){
		days = '0' + days;
	}
	
	// calculate (and subtract) whole hours
	var hours = Math.floor(delta / 3600) % 24;
	delta -= hours * 3600;

	if (hours < 10){
		hours = '0' + hours;
	}
	
	// calculate (and subtract) whole minutes
	var minutes = Math.floor(delta / 60) % 60;
	delta -= minutes * 60;

	if (minutes < 10){
		minutes = '0' + minutes;
	}
	
	
	// what's left is seconds
	var seconds = delta % 60;  // in theory the modulus is not required
	
	if (seconds < 10){
		seconds = '0' + seconds;
	}
	
	return days + 'd:' + hours + 'h:' + minutes + 'm:' + seconds + 's';
	
}
