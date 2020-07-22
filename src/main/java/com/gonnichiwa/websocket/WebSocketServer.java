package com.gonnichiwa.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import lombok.extern.slf4j.Slf4j;

@Component
@ServerEndpoint("/websocket")
@Slf4j
public class WebSocketServer {
	// 웹소켓 연결시 호출 이벤트
	@OnOpen
	public void handleOpen(){
		log.debug("client is now connected...");
	}

	// 연결된 소켓 클라로부터 메세지 오면 호출됨.
	@OnMessage
	public String handleMessage(String message){
		log.debug("receive from client: " + message);
		String echoMessage = "echo: " + message;
		log.debug("send to client: " + echoMessage);
		return echoMessage;
	}

	// 연결된 클라 소켓 닫혔을 때 호출됨.
	@OnClose
	public void handleClose(){
		log.debug("client is now disconnected.");
	}

	@OnError
	public void handleError(Throwable t){
		t.printStackTrace();
	}
}
