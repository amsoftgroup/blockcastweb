/**
 * 
 */
var blockcast_url = 'http://localhost:8088/blockcastweb/';

alert(blockcast_url);

//$("h1").click(function(){
//	$(this).addClass("blue");
//});

$(function () {
	$('#file_upload').fileupload({
		dataType: 'json',
	    url: blockcast_url + 'restapi/upload',
	    done: function (e, data) {
	    	alert('fileupload.done');
	        // $.each(data.result.files, function (index, file) {
	        //     $('<p/>').text(file.name).appendTo(document.body);
	        // });
	    }
	});
});


alert('hi again');



