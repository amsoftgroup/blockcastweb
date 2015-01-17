package me.blockcast.sandbox.spring;


import me.blockcast.data.BlockcastManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class BlockcastTextWebSocketHandler extends TextWebSocketHandler{

	private static Log log = LogFactory.getLog(BlockcastTextWebSocketHandler.class);
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		log.info("websocket message handled: " + message.getPayload());
	}


}
