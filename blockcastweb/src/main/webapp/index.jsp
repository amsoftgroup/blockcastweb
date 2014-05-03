<html>


<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/sample.css"/>

<body>

<img src='../../images/blockcast.gif'>
<input id="file_upload" type="file" name="files[]" data-url="restapi/upload/" multiple>
</br>
</br>

<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-2.1.0/jquery-2.1.0.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-ui-1.10.4/jquery-ui-1.10.4.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jQuery-File-Upload-7.2.1/jquery.fileupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jQuery-File-Upload-7.2.1/jquery.fileupload-fp.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jQuery-File-Upload-7.2.1/jquery.fileupload-jui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jQuery-File-Upload-7.2.1/jquery.fileupload-ui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jQuery-File-Upload-7.2.1/jquery.iframe-transport.js"></script>



<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/blockcast.js"></script>

<script>
 buildupload();
</script>

</body>
</html>
