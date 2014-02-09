<html>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/application.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jQuery-File-Upload-7.2.1/jquery.fileupload.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/sample.css"/>

<body>
<h1>Hello World!</h1>

<input id="file_upload" type="file" name="files[]" data-url="restapi/upload" multiple>
</body>
</br>
</br>
System.getProperty("java.io.tmpdir")=<%=System.getProperty("java.io.tmpdir") %>

</html>
