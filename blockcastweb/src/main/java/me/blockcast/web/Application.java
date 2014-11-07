package me.blockcast.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
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
import me.bockcast.utils.Utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
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
import org.json.JSONArray;
import org.json.JSONObject;


//Sets the path to base URL + /restapi
@Path("/")
public class Application extends ResourceConfig  {

	private Logger logger = Logger.getLogger(Application.class.getName());  
	private SimpleDateFormat sdf = new SimpleDateFormat(Utils.timeformat);  

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
	@Path("/getPostsByDistance/{distance}/{lat}/{lon}")
	@Produces({"application/json", "text/xml"})
	public List<Post> getEntityByDistance(@PathParam("distance") int distance,
@PathParam("lat") double lat,
			@PathParam("lon") double lon) {
		// return eq.getEntityWithinRadius(entityTypeId, distance, lon, lat);

		return BlockcastManager.getPostWithinRadius(distance, lat, lon);
	}
	
	@GET
	@Path("/getPosts")
	@Produces({"application/json", "text/xml"})
	public List<Post> getEntitys() {
		// return eq.getEntityWithinRadius(entityTypeId, distance, lon, lat);

		return BlockcastManager.getPosts();
	}
	
	@GET
	@Path("/getPostsByDistanceAndDuration/{distance}/{lat}/{lon}")
	@Produces({"application/json"})
	public List<Post> getPostsByDistanceAndDuration(
			@PathParam("distance") int distance,
			@PathParam("lat") double lat,
			@PathParam("lon") double lon) {
		logger.info("Calling  BlockcastManager.getPostWithinRadiusAndDuration(" + distance + "," +  lat +","  + lon + ")");
		return BlockcastManager.getPostWithinRadiusAndDuration(distance, lat, lon);
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
	public boolean insertPost(FormDataMultiPart formDataMultiPart) {

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
						post.setPostTimestamp(sdf.parse(value));
					} catch (ParseException e) {
						logger.log(Level.SEVERE, e.toString());
					}
					//post.setPostTimestamp(Timestamp.valueOf(value));
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
		boolean ret = BlockcastManager.insertPost(post);

		return ret;
	}

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
	/*
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
	 */
	/*
	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String uploadFile(@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {


		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		if (isMultipart) {

			logger.log(Level.SEVERE, "isMultipart");	    
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			try {
				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (!item.isFormField()) {
						String fileName = item.getName();

						String root = System.getProperty("java.io.tmpdir");
						File path = new File(root + File.separator + "fileuploads");
						if (!path.exists()) {
							boolean status = path.mkdirs();
						}
						logger.log(Level.INFO, "path + File.separator + fileName= " + path + File.separator + fileName);	  
						File uploadedFile = new File(path + File.separator + fileName);
						item.write(uploadedFile);
					}
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, "err: "+e.toString());	    
			}
		}else{
			logger.log(Level.INFO, "is NOT Multipart");	  
		}
		//String retString = "{\"Name\":\"reedbn\",\"Age\":\"38\"}";

		String retString = "{" +
						   "\"files\":"+
						   "["+
						     "{" + 
			        	       "\"url\": \"url\","+
			                   "\"thumbnail_url\": \"thumbnail_url\","+
			                   "\"name\": \"name\","+
			                   "\"type\": \"type\","+
			                   "\"size\": \"46353\","+
			                   "\"delete_url\": \"delete_url\","+
			                   "\"delete_type\": \"delete_type\""+
			        	   	 "}"+
			        	   "]" +
			        	   "}";

		logger.log(Level.INFO, "retString" + retString);	  

		return retString;
	}
	 */

	
	/*
	 * 
	 * JsonObjectBuilder jsonbuilder = Json.createObjectBuilder();

if (!ServletFileUpload.isMultiPartContent(request){
    return jsonbuilder.add("error", "REQ is not multipart").build().toString();
}




ServletFileUpload uploadHandler = new ServletFileUpload(new
DiskFileItemFactory());


String portUrl = "";
if (request.getServerPort() != 80){
    portUrl = ":" + request.getServerPort();
}
String restUrl = request.getScheme() + "://" + request.getServerName()
+ portUrl + request.getContextPath();


File tempfile = new File(tempdir);

// if tempdir doesnt exist make it

JsonArrayBuilder jarraybuilder = Json.CreateArrayBuilder();

List<FileItem> items = uploadHandler.parseRequest(request);

For (FileItem item : items){

if (!item.isFormField()){




JsonObjectBuilder job = new...
String nameOnly =
item.getName().substring(item.getName().lastIndexOf("\\")+1,
item.getName().length());

String index = item.getName().lastIndexOf('.');

int length = item.getName().length();

String extension = item.getName().substring(index,length);

File f = new File(tempdir, "FILENAME");
item.write(f);

job.add("","")
//...
jarraybuilder.add(job.build());

}

ret jarraybuilder.build().toString();
*/
	
	@GET
	@Path("upload")
	@Produces(MediaType.APPLICATION_JSON)
	public String uploadget(@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {
		
		return "(\"success\": \"true\")";
	}
	
	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String uploadFile(@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {
		
		JsonObjectBuilder arraybuilder = Json.createObjectBuilder();
		//String tmpdir = System.getProperty("java.io.tmpdir");
		String tmpdir = "/opt/tomcat/webapps/ROOT/images/";
		
		logger.info("in uploadFile: tmpdir=" + tmpdir);
		File tmpdirfile = new File(tmpdir);

		boolean mkfolder = false;
		if(!tmpdirfile.exists()) {
			mkfolder = tmpdirfile.mkdir();
		}
		

		// Commons file upload classes are specifically instantiated
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		logger.info("setSizeThreshold=" + factory.getSizeThreshold());
		factory.setSizeThreshold(100000000);
		logger.info("NEW setSizeThreshold=" + factory.getSizeThreshold());
		
		ServletFileUpload upload = new ServletFileUpload(factory);

		String portUrl = "";
		if (request.getServerPort() != 80){
		    portUrl = ":" + request.getServerPort();
		}
		String restUrl = request.getScheme() + "://" + request.getServerName()
		+ portUrl + request.getContextPath();
		
		
		// Parse the request
		List<FileItem> items = upload.parseRequest(request);
		boolean ismultipart = ServletFileUpload.isMultipartContent(request);
		String content_len = ServletFileUpload.CONTENT_LENGTH;
		logger.info("ismultipart=" + ismultipart);
		logger.info("content_len=" + content_len);
		JsonArrayBuilder jarraybuilder = Json.createArrayBuilder();

		
		logger.info("items: " + items.size()); 
		
		Iterator<FileItem> iter = items.iterator();

		//ServletOutputStream out = null;

		String nameOnly = null;
		String extension = null;
		int index = -1;
		int length = -1;
		
		while (iter.hasNext()) {
			// Get the current item in the iteration
			FileItem item = (FileItem) iter.next();
			logger.info("item: " + item.getFieldName());
			// If the current item is an HTML form field
			if (item.isFormField()) {
				// Return an XML node with the field name and value
				logger.info("this is a form data " + item.getFieldName() + "<br>");

				// If the current item is file data
			} else {
				logger.info("this is not  form data " + item.getFieldName() + "<br>");
				// Specify where on disk to write the file
				// Using a servlet init param to specify location on disk
				// Write the file data to disk
				// TODO: Place restrictions on upload data



				//File disk = new File(tmpdir +item.getName());
				nameOnly =
						item.getName().substring(item.getName().lastIndexOf("\\")+1,
								item.getName().length());

				index = item.getName().lastIndexOf('.');

				length = item.getName().length();

				extension = item.getName().substring(index,length);

				File uploadedFile = new File(tmpdir + File.separator + "TEST.txt");
				item.write(uploadedFile);
				
				JsonObjectBuilder builder = Json.createObjectBuilder();
                builder.add("name", item.getName());
                builder.add("type", item.getContentType());
                builder.add("size", item.getSize());
                builder.add("url", tmpdir + "/" + item.getName());
                builder.add("thumbnail_url", extension);
                builder.add("delete_url", tmpdir + "/" + item.getName());
                builder.add("delete_type", "DELETE");
                    
                arraybuilder.add("\"files\"", builder);

			}
		}

		/*try{


			// Close off the response XML data and stream
			//out.write(arraybuilder.build().toString().getBytes());
			//out.close();
			// Rudimentary handling of any exceptions
			// TODO: Something useful if an error occurs
		} catch (FileUploadException fue) {
			logger.info("FileUploadException: " + fue.toString());  
		} catch (IOException ioe) {
			logger.info("IOException: " + ioe.toString());  
		} catch (Exception e) {
			logger.info("Exception: " + e.toString());  
		}
		 */
		
		String ret = "{\"files\":[{" + 
	        "\"url\":\"http://url.to/file/or/page\"," + 
	        "\"thumbnail_url\":\"http://url.to/thumnail.jpg\"," + 
	        "\"name\":\"thumb2.jpg\"," + 
	        "\"type\":\"image/jpeg\"," + 
	        "\"size\":\"46353\"," + 
	        "\"delete_url\":\"http://url.to/delete/file/\"," + 
	        "\"delete_type\":\"DELETE\"" + 
	      "}]}";
		

		//System.out.println(arraybuilder.build().toString());
		return arraybuilder.build().toString();
		//return ret;

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