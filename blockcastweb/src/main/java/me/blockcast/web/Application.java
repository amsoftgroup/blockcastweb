package me.blockcast.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

//Sets the path to base URL + /restapi
@Path("/")
public class Application {

	  // This method is called if TEXT_PLAIN is request
	  @GET
	  @Produces(MediaType.TEXT_PLAIN)
	  public String sayPlainTextHello() {
	    return "Current time: " + new Date().toString();
	  }
	
	  // This method is called if XML is request
	  @GET
	  @Produces(MediaType.TEXT_XML)
	  public String sayXMLHello() {
	    return "<?xml version=\"1.0\"?>" + "<hello> Hello World" + "</hello>";
	  }
	
	  
	  @GET
	  @Path("count")
	  @Produces(MediaType.TEXT_PLAIN)
	  public String getCount() {
	    int count = 42;
	    return String.valueOf(count);
	  }
	  
	  
	  // This method is called if HTML is request
	  @GET
	  @Produces(MediaType.TEXT_HTML)
	  public String sayHtmlHello() {
	    return "<html> " + "<title>" + "Current time" + "</title>"
	        + "<body><h1>" + "Current time: " + new Date().toString() + "</body></h1>" + "</html> ";
	  }
	  
	  @GET
	  @Path("upload")
	  @Produces(MediaType.TEXT_HTML)
	  public String uploadGet() {
	    return "<html> " + "<title>" + "upload get" + "</title>"
	        + "<body><h1>" + "upload get:" + new Date().toString() + "</body></h1>" + "</html> ";
	  }
	  
	  @POST
	  @Path("upload")
	  @Consumes(MediaType.MULTIPART_FORM_DATA)
	  @Produces(MediaType.TEXT_PLAIN)
	  public Response uploadFile(@Context HttpServletRequest request,
	    @Context HttpServletResponse response) throws Exception {
		// Commons file upload classes are specifically instantiated
			FileItemFactory factory = new DiskFileItemFactory();
		 
			ServletFileUpload upload = new ServletFileUpload(factory);
			ServletOutputStream out = null;
		 
			try {
				// Parse the incoming HTTP request
				// Commons takes over incoming request at this point
				// Get an iterator for all the data that was sent
				List items = upload.parseRequest(request);
				Iterator iter = items.iterator();
		 
				// Set a response content type
				response.setContentType("text/html");
		 
				// Setup the output stream for the return XML data
				out = response.getOutputStream();
		 
				// Iterate through the incoming request data
				while (iter.hasNext()) {
					// Get the current item in the iteration
					FileItem item = (FileItem) iter.next();
		 
					// If the current item is an HTML form field
					if (item.isFormField()) {
						// Return an XML node with the field name and value
						out.println("this is a form data " + item.getFieldName() + "<br>");
		 
						// If the current item is file data
					} else {
						// Specify where on disk to write the file
						// Using a servlet init param to specify location on disk
						// Write the file data to disk
						// TODO: Place restrictions on upload data
						String tmpdir = System.getProperty("java.io.tmpdir");
						File disk = new File(tmpdir +item.getName());
						item.write(disk);
		 
						// Return an XML node with the file name and size (in bytes)
						//out.println(getServletContext().getRealPath("/WEB_INF"));
						out.println("this is a file with name: " + item.getName());
					}
				}
		 
				// Close off the response XML data and stream
		 
				out.close();
				// Rudimentary handling of any exceptions
				// TODO: Something useful if an error occurs
			} catch (FileUploadException fue) {
				fue.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
	  }
	  
	  /*
	  @POST
	  @Consumes(MediaType.MULTIPART_FORM_DATA)
	  @Produces(MediaType.APPLICATION_JSON)
	  @Path("/uploadfile")
	  public JSONObject uploadProductImage(BufferedInMultiPart bimp) throws IOException, JSONException {


	      OutputStream out = null ;
	      Random rand = new Random();
	      List parts = bimp.getParts();
	      Debug.logInfo("parts size : " + parts.size(), module);
	      Iterator it = parts.iterator();
	      byte[] bytes = null;
	      while(it.hasNext()){
	          InPart name = (InPart) it.next();
	          try{
	              InputStream inputStream= name.getInputStream();
	              out = new FileOutputStream("/home/aspire17/Pictures/Product.png");
	              int read=0;
	              bytes = new byte[1024];
	              while((read = inputStream.read(bytes))!= -1){
	                  out.write(bytes, 0, read);
	              }
	              inputStream.close();
	              out.flush();
	              out.close();
	          }
	          catch (IOException e){
	          }

	      }
	  }
	  */

}