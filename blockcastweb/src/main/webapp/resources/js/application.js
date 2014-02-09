/**
 * 
 */
alert('hi');






$(function(){
	
	$("h1").click(function(){
		$(this).addClass("blue");
	});
	
	upload();
	
});

alert('hi again');

function upload(){
	$('#file_upload').fileupload({
	    url: 'restapi/upload',
	    done: function (e, data) {
	        alert('fileupload.done');
	    }
	});
}
