package me.blockcast.sandbox.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorld {

	@RequestMapping("/welcome")
	public ModelAndView helloWorld() {

		String message = "<br><div align='center'>"
				+ "<h3>********** Hello World, Spring MVC Tutorial</h3>This message is comming from CrunchifyHelloWorld.java **********<br><br>";
		return new ModelAndView("welcome", "message", message);
	}
	
	@RequestMapping("/sockets")
	public ModelAndView helloSockets() {

		String message = "<br><div align='center'>"
				+ "<h3>********** Sockets Go Here";
		return new ModelAndView("sockets", "message", message);
	}
}
