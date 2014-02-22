<html>


<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/sample.css"/>

<body>
<h1>Hello World!</h1>

<input id="file_upload" type="file" name="files[]" data-url="restapi/upload" multiple>
</br>
</br>
System.getProperty("java.io.tmpdir")=<%=System.getProperty("java.io.tmpdir") %>

</body>

<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jQuery-File-Upload-7.2.1/jquery.fileupload.js"></script>
<!-- 
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/blockcast.js"></script>
 -->
<script>
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
</script>

</html>
