package me.blockcast.sandbox.spring;

import java.util.logging.Logger;

import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DeviceDetectionController {

	private Logger logger = Logger.getLogger(DeviceDetectionController.class.getName()); 
	
    @RequestMapping("/detect-device")
    public @ResponseBody String detectDevice(Device device) {
    	
    	logger.info("SPRING entered DeviceDetectionController");
    	
        String deviceType = "unknown";
        if (device.isNormal()) {
            deviceType = "normal";
        } else if (device.isMobile()) {
            deviceType = "mobile";
        } else if (device.isTablet()) {
            deviceType = "tablet";
        }
        return "Hello " + deviceType + " browser!";
    }

}