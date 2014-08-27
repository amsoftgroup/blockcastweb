
var map = L.map('map').setView([38.8951, -77.0367], 13);
var servername = "http://www.blockcast.me";
var api = "/restapiv1.0/";
//var posts = -1;
/*
$.getJSON(servername + api + "getPosts", function(resultList){
    $.each(resultList, function(key, val) {
         alert("JSON Data: key" + key + " value: " + value);
    });
}); 
*/

/*
function testAjax() {
	return $.ajax({
	      url: servername + api + "getPosts"
	});
}
posts.success(function (data) {
	alert(data);
});
*/
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
			.setContent("You clicked the map at " + e.latlng.toString())
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
	

	var markers = L.markerClusterGroup();
	/*
	var markers = L.markerClusterGroup({
		maxClusterRadius: 120,
		iconCreateFunction: function (cluster) {
			var markers = cluster.getAllChildMarkers();
			var n = 0;
			for (var i = 0; i < markers.length; i++) {
				n += markers[i].number;
			}
			return L.divIcon({ html: n, className: 'mycluster', iconSize: L.point(40, 40) });
		},
		//Disable all of the defaults:
		//spiderfyOnMaxZoom: false, showCoverageOnHover: false, zoomToBoundsOnClick: false
	});
	*/
	var posts = getPosts();
	
	for (var i = 0; i < posts.length; i++) {
		var a = posts[i];
		var title = posts[i].content;
		var marker = L.marker(new L.LatLng(posts[i].lat, posts[i].lon), { title: title });
		marker.bindPopup( '<a href="' + posts[i].url + '" target="_blank">' + posts[i].content + '</a><br>' + 
		  		'('+ posts[i].lat +',' + posts[i].lon +')<br>' +
		  		posts[i].postTimeString + '<br>' +
		  		posts[i].duration);
		markers.addLayer(marker);
	}

	map.addLayer(markers);
	
	
});

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



