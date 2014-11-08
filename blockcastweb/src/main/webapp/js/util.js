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

	}

	map.addLayer(markers);
}

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

