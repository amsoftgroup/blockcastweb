package me.blockcast.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.swing.text.html.parser.Entity;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import me.blockcast.data.BlockcastManager;
import me.blockcast.web.pojo.Location;
import me.blockcast.web.pojo.Post;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;


//Sets the path to base URL + /restapi
@Path("/")
public class Application extends ResourceConfig  {

	private Logger logger = Logger.getLogger("MyLog");  
	private String timeformat = "yyyy/MM/dd HH:mm:ss";
	private SimpleDateFormat df = new SimpleDateFormat(timeformat);  
	
	public Application(){
    	register(MultiPartFeature.class);
    	registerInstances(new LoggingFilter(Logger.getLogger(Application.class.getName()), true));
	}

	@GET
	@Path("/testdb")
	@Produces({"application/json", "text/xml"})
	public String getTestDB() {
		// return eq.getEntityWithinRadius(entityTypeId, distance, lon, lat);

		return BlockcastManager.testDB();
	}
	
	@GET
	@Path("/getPostsByDistance/{distance}/{lon}/{lat}")
	@Produces({"application/json", "text/xml"})
	public List<Post> getEntityByDistance(@PathParam("id") int entityTypeId,
			@PathParam("distance") int distance,
			@PathParam("lon") double lon,
			@PathParam("lat") double lat) {
		// return eq.getEntityWithinRadius(entityTypeId, distance, lon, lat);

		return BlockcastManager.getPostWithinRadius(distance, lon, lat);
	}
	
	@GET
	@Path("/insertPost")
	@Produces(MediaType.TEXT_HTML)
	public String insertPost() {

	    return "returned from /insertPost: " + new Date().toGMTString();
	}
	
	
	@POST
	@Path("/insertPost")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_HTML)
	//public String insertPost(@FormDataParam("schema") final String schema) {
	public String insertPost(FormDataMultiPart formDataMultiPart) {
	
		Map<String, List<FormDataBodyPart>> fields = formDataMultiPart.getFields();
		
		Post post = new Post();
		double lon = Double.MAX_VALUE;
		double lat = Double.MAX_VALUE;
		
		// TODO: no need to loop, we're only interested in element 0 (for now?)
		for (Entry<String, List<FormDataBodyPart>> entry : fields.entrySet()){
			List<FormDataBodyPart> formdatabodyparts = entry.getValue();
			

			for (int i = 0; i < formdatabodyparts.size(); i++){
				String name = formdatabodyparts.get(i).getName();
				String value = formdatabodyparts.get(i).getValue();
				System.out.println(entry.getKey() + " ( " + i + " )/ " + "N" + name + " V"+value);
				if ("content".equalsIgnoreCase(name)){
					post.setContent(value);
				}else if ("distance".equalsIgnoreCase(name)){
					post.setDistance(Long.valueOf(value));
				}else if ("parentId".equalsIgnoreCase(name)){
					post.setParentId(Long.valueOf(value));
				}else if ("duration".equalsIgnoreCase(name)){
					post.setDuration(Long.valueOf(value));
				}else if ("time".equalsIgnoreCase(name)){
					try {
						post.setPostTimestamp(df.parse(value));
					} catch (ParseException e) {
						System.out.println("Application ParseException: " + e.toString());
					}
				}else if ("lon".equalsIgnoreCase(name)){
					lon = (Double.valueOf(value));
				}else if ("lat".equalsIgnoreCase(name)){
					lat = (Double.valueOf(value));
				}	
			}    
		}
		Location location = new Location();
		location.setLat(lat);
		location.setLon(lon);
		post.setLocation(location);	
		BlockcastManager.insertPost(post);

	    return "returned " + formDataMultiPart.getFields().size();
	}
	
	@POST
	@Path("/insertPost/{lon}/{lat}/{parent_id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void insertPost(@PathParam("parent_id") int parent_id,
			@PathParam("lon") double lon,
			@PathParam("lat") double lat
			) {

		//FormDataMultiPart contentbp = data;
		//String content = (String)contentbp.getEntity();
		System.out.println("VALUE=<null>" );
	        /*
		for (int i = 0; i < data.getBodyParts().size(); i++){
			BodyPart bodypart = data.getBodyParts().get(i);
			bodypart.getEntity();
			 HttpHeaders httpHeaders = bodypart.getHttpHeaders();
		      UriInfo uriInfo = bodypart.getUriInfo();
		      String entitySetName = bodypart.getEntitySetName();
		      String entityId = bodypart.getEntityKey();
		      String entityString = bodypart.getEntity();

		}
		*/
			
		/*
		double dlon = Double.parseDouble(lon);
		double dlat = Double.parseDouble(lat);
		long lparentId = Long.parseLong(parentId);
		long ldistance = Long.parseLong(distance);
		Date datetime = null;
		try {
			datetime = new SimpleDateFormat(timeformat).parse(time);
		} catch (ParseException e2) {
			Log.e(TAG, e2.toString());
		}
		long lduration = Long.parseLong(duration);
		*/
		Post post = new Post();
		BlockcastManager.insertPost(post);

	}
	/*
	@POST
	@Path("/insertPost")
	@Consumes("multipart/mixed")
	public void insertPost(
			FormDataMultiPart data
			) {

			Post post = data.getBodyParts().get(0).getEntityAs(Post.class);
			BlockcastManager.insertPost(post);
		
	}
	*/
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
				+ "<body><h1>" + "Current time: " + new Date().toString() + "</body></h1><br><br>" + 
				"temp directory: " + System.getProperty("java.io.tmpdir")+ "</html> ";
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

		logger.info("in uploadFile");

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
					logger.info("this is a form data " + item.getFieldName() + "<br>");

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
					logger.info("this is a file with name: " + item.getName());
				}
			}

			// Close off the response XML data and stream

			out.close();
			// Rudimentary handling of any exceptions
			// TODO: Something useful if an error occurs
		} catch (FileUploadException fue) {
			logger.info("FileUploadException: " + fue.toString());  
		} catch (IOException ioe) {
			logger.info("IOException: " + ioe.toString());  
		} catch (Exception e) {
			logger.info("Exception: " + e.toString());  
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