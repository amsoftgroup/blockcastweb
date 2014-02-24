<html>


<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/sample.css"/>

<body>
<h1>blockcast->me</h1>
<img src='images/blockcast.gif'>
<input id="fileupload" type="file" name="files[]" data-url="restapi/upload/" multiple>
</br>
</br>
System.getProperty("java.io.tmpdir")=<%=System.getProperty("java.io.tmpdir") %>


<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-2.1.0/jquery-2.1.0.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-ui-1.10.4/jquery-ui-1.10.4.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jQuery-File-Upload-7.2.1/jquery.fileupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jQuery-File-Upload-7.2.1/jquery.fileupload-fp.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jQuery-File-Upload-7.2.1/jquery.fileupload-jui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jQuery-File-Upload-7.2.1/jquery.fileupload-ui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jQuery-File-Upload-7.2.1/jquery.iframe-transport.js"></script>

<!-- 
<script src="js/vendor/jquery.ui.widget.js"></script>
<script src="js/jquery.iframe-transport.js"></script>
<script src="js/jquery.fileupload.js"></script>
 -->
<!-- 
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/blockcast.js"></script>
 -->
 
 
<script>

var blockcast_url = 'http://localhost:8088/blockcastweb/';

var tgt = blockcast_url + 'restapi/upload/';

$(function () {

	alert(tgt);
	
	$('#fileupload').fileupload({
	    dataType: 'json',
	    url: tgt,
	    done: function (e, data) {
	    	alert('fileupload.done');
	       // $.each(data.result.files, function (index, file) {
	       //     $('<p/>').text(file.name).appendTo(document.body);
	       // });
	    }
	});
	
	$("h1").click(function(){
		$(this).addClass("red");
	});

});
</script>
</body>
</html>
