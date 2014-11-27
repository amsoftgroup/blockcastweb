
var map = L.map('map').setView([38.8951, -77.0367], 13);
var servername = "http://www.blockcast.me";
var static_path = "/static/";
var api = "/restapiv1.0/";

$(function () {

	locateUser();
	
	var date = new Date();

	L.tileLayer('http://api.tiles.mapbox.com/v4/sombrerosoft.jigalil3/{z}/{x}/{y}.png?access_token=pk.eyJ1Ijoic29tYnJlcm9zb2Z0IiwiYSI6Ik9NZDl3QlUifQ.OhFgRaj5AbP5BQvHJIk3Wg', {
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
	
	var refreshId = setInterval(markPosts(), 5000);
	
});



$('#actions').find('a').on('click', function() {
  locateUser();
});


