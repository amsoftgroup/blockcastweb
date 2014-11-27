package me.bockcast.utils;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.commons.io.FilenameUtils;

public class Utils {

	public static String timeformat = "yyyy/MM/dd HH:mm:ss";

	public static String uploadfolder = File.separator + "tmp" + File.separator;
	public static String downloadfolder = File.separator + "static" + File.separator;
	
	public static BufferedImage scale(BufferedImage source,double ratio) {
		int w = (int) (source.getWidth() * ratio);
		int h = (int) (source.getHeight() * ratio);
		BufferedImage bi = Utils.getCompatibleImage(w, h);
		Graphics2D g2d = bi.createGraphics();
		double xScale = (double) w / source.getWidth();
		double yScale = (double) h / source.getHeight();
		AffineTransform at = AffineTransform.getScaleInstance(xScale,yScale);
		g2d.drawRenderedImage(source, at);
		g2d.dispose();
		return bi;
	}

	private static BufferedImage getCompatibleImage(int w, int h) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		BufferedImage image = gc.createCompatibleImage(w, h);
		return image;
	}
	
	public static String getPreview(String medianame){
		String preview = null;
		String ext = null;
		if ((medianame != null ) && (medianame.length() > 0)){
			ext = FilenameUtils.getExtension(medianame);
		}else{
			return null;
		}
		 
		
		if ((ext.equalsIgnoreCase("jpg")
				|| (ext.equalsIgnoreCase("png")))){
			preview = FilenameUtils.getBaseName(medianame);
			preview = preview + "_small." + ext;
		}else{
			preview = medianame;
		}
		
		
		return preview;		
	}
}
