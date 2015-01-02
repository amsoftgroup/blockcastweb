package me.blockcast.web;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.imageio.ImageIO;
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
import me.blockcast.common.Location;
import me.blockcast.common.Post;
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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import static org.imgscalr.Scalr.*;

import org.imgscalr.Scalr;
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
	@Path("/getPostsByDistanceAndDuration/{distance}/{lat}/{lon}")
	@Produces({"application/json"})
	public List<Post> getPostsByDistanceAndDuration(
			@PathParam("distance") int distance,
			@PathParam("lat") double lat,
			@PathParam("lon") double lon,
			@Context HttpServletRequest req) {
	    String remoteHost = req.getRemoteHost();
	    String remoteAddr = req.getRemoteAddr();
	    int remotePort = req.getRemotePort();
	    String msg = remoteHost + " (" + remoteAddr + ":" + remotePort + ")";
		logger.info("Calling  BlockcastManager.getPostWithinRadiusAndDuration(" + distance + "," +  lat +","  + lon + ")");
		logger.info("msg:" + msg);
	    return BlockcastManager.getPostWithinRadiusAndDuration(distance, lat, lon);
		
	}

	@GET
	@Path("/getPostsByDistanceAndDurationWithGuid/{distance}/{lat}/{lon}/{guid}")
	@Produces({"application/json"})
	public List<Post> getPostsByDistanceAndDurationWithGuid(
			@PathParam("distance") int distance,
			@PathParam("lat") double lat,
			@PathParam("lon") double lon,
			@PathParam("guid") String guid,
			@Context HttpServletRequest req) {
	    String remoteHost = req.getRemoteHost();
	    String remoteAddr = req.getRemoteAddr();
	    int remotePort = req.getRemotePort();
	    String msg = remoteHost + " (" + remoteAddr + ":" + remotePort + ")";
		logger.info("Calling  BlockcastManager.getPostsByDistanceAndDurationWithGuid(" + distance + "," +  lat +","  + lon + "," + guid + ")");
		logger.info("msg:" + msg);
	    return BlockcastManager.getPostsByDistanceAndDurationWithGuid(distance, lat, lon, guid);
		
	}
	
	@GET
	@Path("/insertPost")
	@Produces(MediaType.TEXT_HTML)
	public String insertPost() {
		return "returned from /insertPost: " + new Date().toGMTString();
	}

	@POST
	@Path("/deletePost")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_HTML)
	public int deletePost(FormDataMultiPart formDataMultiPart, @Context HttpServletRequest req) {
		
	    String remoteHost = req.getRemoteHost();
	    String remoteAddr = req.getRemoteAddr();
	    int remotePort = req.getRemotePort();
	    String commentid = null;
	    String guid = null;
	    
		Map<String, List<FormDataBodyPart>> fields = formDataMultiPart.getFields();

		for (Entry<String, List<FormDataBodyPart>> entry : fields.entrySet()){
			List<FormDataBodyPart> formdatabodyparts = entry.getValue();

			for (int i = 0; i < formdatabodyparts.size(); i++){
				String name = formdatabodyparts.get(i).getName();

				if ("commentid".equalsIgnoreCase(name)){
					commentid = formdatabodyparts.get(i).getValue();
				}else if ("guid".equalsIgnoreCase(name)){
					guid = formdatabodyparts.get(i).getValue();
				}
			}
		}
		int retval = BlockcastManager.delete(commentid, guid);
		return retval;
	}
	
	@POST
	@Path("/insertPost")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_HTML)
	public String insertPost(FormDataMultiPart formDataMultiPart, @Context HttpServletRequest req) {

	    String remoteHost = req.getRemoteHost();
	    String remoteAddr = req.getRemoteAddr();
	    int remotePort = req.getRemotePort();

		Map<String, List<FormDataBodyPart>> fields = formDataMultiPart.getFields();

		Post post = new Post();
		double lon = Double.MAX_VALUE;
		double lat = Double.MAX_VALUE;
		String guid = null;
		
		post.setIp(remoteHost);
		
		// TODO: no need to loop, we're only interested in element 0 (for now?)
		for (Entry<String, List<FormDataBodyPart>> entry : fields.entrySet()){
			List<FormDataBodyPart> formdatabodyparts = entry.getValue();

			
			for (int i = 0; i < formdatabodyparts.size(); i++){
				String name = formdatabodyparts.get(i).getName();

				if ("content".equalsIgnoreCase(name)){
					post.setContent(formdatabodyparts.get(i).getValue());
				}else if ("distance".equalsIgnoreCase(name)){
					post.setDistance(Long.valueOf(formdatabodyparts.get(i).getValue()));
				}else if ("parentId".equalsIgnoreCase(name)){
					post.setParentId(Long.valueOf(formdatabodyparts.get(i).getValue()));
				}else if ("duration".equalsIgnoreCase(name)){
					post.setDuration(Long.valueOf(formdatabodyparts.get(i).getValue()));
				}else if ("time".equalsIgnoreCase(name)){
					long epoch  = Long.parseLong(formdatabodyparts.get(i).getValue()) / 1000l;				
					post.setEpoch(epoch);
				}else if ("lon".equalsIgnoreCase(name)){
					lon = (Double.valueOf(formdatabodyparts.get(i).getValue()));
				}else if ("lat".equalsIgnoreCase(name)){
					lat = (Double.valueOf(formdatabodyparts.get(i).getValue()));
				}else if ("guid".equalsIgnoreCase(name)){
					post.setGuid(formdatabodyparts.get(i).getValue());
				}else if ("file".equalsIgnoreCase(name)){	
					
					UUID uuid = UUID.randomUUID();

					String filename = formdatabodyparts.get(i).getContentDisposition().getFileName();
					File f = formdatabodyparts.get(i).getValueAs(File.class);
					
					logger.info("File f.getName(): " + f.getName());		
					logger.info("filename: " + filename);
					
					String ext = FilenameUtils.getExtension(filename);
					
					logger.info("file ext: " + ext);
					String newfile = Utils.uploadfolder + uuid.toString() + "." + ext;
					logger.info("newfile: " + newfile);
					
					File smallfile = new File( Utils.uploadfolder + uuid.toString() + "_small." + ext);
					File bigfile = new File( Utils.uploadfolder + uuid.toString() + "." + ext);
					
					try {
						FileUtils.copyFile(f, bigfile);
					} catch (IOException e2) {
						logger.severe(e2.toString());
					}
					
					BufferedImage image = null;
					try {
						image = ImageIO.read(f);
					} catch (IOException e1) {
						logger.severe(e1.toString());
					}
					
					BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics2D g = newImage.createGraphics();
					g.drawImage(image, 0, 0, null);
					g.dispose();

					BufferedImage thumbnail = Scalr.resize(image, 150);
					
					try {
					    // retrieve image
					    BufferedImage bi = thumbnail;					    
					    ImageIO.write(bi, ext, smallfile);
					} catch (IOException e) {
						logger.severe(e.toString());
					}
					
					post.setMedia_name(bigfile.getName());
					post.setMedia_file(smallfile);
					
				}		
			}    
		}
		Location location = new Location();
		location.setLat(lat);
		location.setLon(lon);
		post.setLocation(location);	
		
		boolean ret = BlockcastManager.insertPost(post);

		return "" + ret;
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

		//System.out.println(arraybuilder.build().toString());
		return arraybuilder.build().toString();

	}


	@GET
	@Path("/getPosts")
	@Produces({"application/json", "text/xml"})
	public List<Post> getEntitys() {
		// return eq.getEntityWithinRadius(entityTypeId, distance, lon, lat);

		return BlockcastManager.getPosts();
	}

}