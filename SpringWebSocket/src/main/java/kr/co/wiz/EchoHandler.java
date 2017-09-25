package kr.co.wiz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class EchoHandler extends TextWebSocketHandler {

	private Logger logger = LoggerFactory.getLogger(EchoHandler.class);

	private List<WebSocketSession> connectedUsers;

	public EchoHandler() {
		connectedUsers = new ArrayList<WebSocketSession>();
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		Map<String, Object> map = session.getAttributes();
		String userId = (String) map.get("userId");
		
		logger.info("[" + userId + "] : " + message.getPayload());

		for (WebSocketSession webSocketSession : connectedUsers) {
			StringBuffer sb = new StringBuffer();

			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
			String currentTime = formatter.format(new java.util.Date());

			if (session.getId().equals(webSocketSession.getId())) {
				sb.append("<li class=\"right clearfix\"><span class=\"chat-img pull-right\">");
				sb.append("<img src=\"/bootstrap/images/me.png\" alt=\"User Avatar\" class=\"img-circle\">");
				sb.append("</span>");
				sb.append("<div class=\"chat-body clearfix\">");
				sb.append("<div class=\"header\">");
				sb.append("<small class=\"text-muted\"><span class=\"glyphicon glyphicon-time\"></span>");
				sb.append(currentTime);
				sb.append("</small>");
				sb.append("<strong class=\"pull-right primary-font\">");
				sb.append(userId);
				sb.append("</strong>");
				sb.append("</div>");
				sb.append("<p>");
				sb.append(message.getPayload());
				sb.append("</p>");
				sb.append("</div>");
				sb.append("</li>");
			} else {
				sb.append("<li class=\"left clearfix\"><span class=\"chat-img pull-left\">");
				sb.append("<img src=\"/bootstrap/images/u.png\" alt=\"User Avatar\" class=\"img-circle\">");
				sb.append("</span>");
				sb.append("<div class=\"chat-body clearfix\">");
				sb.append("<div class=\"header\">");
				sb.append("<strong class=\"primary-font\">");
				sb.append(userId);
				sb.append("</strong>");
				sb.append("<small class=\"pull-right text-muted\"><span class=\"glyphicon glyphicon-time\"></span>");
				sb.append(currentTime);
				sb.append("</small>");
				sb.append("</div>");
				sb.append("<p>");
				sb.append(message.getPayload());
				sb.append("</p>");
				sb.append("</div>");
				sb.append("</li>");
			}
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("msg", message.getPayload());
			jsonObj.put("html", sb.toString());
			String sendMsg = jsonObj.toJSONString();
			
			webSocketSession.sendMessage(new TextMessage(sendMsg));
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		connectedUsers.add(session);

		Map<String, Object> map = session.getAttributes();
		String userId = (String) map.get("userId");
		String connectMsg = userId + " 님이 입장했습니다.";
		logger.info(connectMsg);

		for (WebSocketSession webSocketSession : connectedUsers) {
			StringBuffer sb = new StringBuffer();
			sb.append("<li class=\"left clearfix\">");
			sb.append("<div class=\"alert alert-success text-center\"><strong>");
			sb.append(connectMsg);
			sb.append("</strong></div>");
			sb.append("</li>");
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("msg", connectMsg);
			jsonObj.put("html", sb.toString());
			String sendMsg = jsonObj.toJSONString();
			
			webSocketSession.sendMessage(new TextMessage(sendMsg));
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		connectedUsers.remove(session);

		Map<String, Object> map = session.getAttributes();
		String userId = (String) map.get("userId");
		String connectMsg = userId + " 님이 퇴장했습니다.";
		logger.info(connectMsg);

		for (WebSocketSession webSocketSession : connectedUsers) {
			if (!session.getId().equals(webSocketSession.getId())) {
				StringBuffer sb = new StringBuffer();
				sb.append("<li class=\"left clearfix\">");
				sb.append("<div class=\"alert alert-danger text-center\"><strong>");
				sb.append(connectMsg);
				sb.append("</strong></div>");
				sb.append("</li>");
				
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("msg", connectMsg);
				jsonObj.put("html", sb.toString());
				String sendMsg = jsonObj.toJSONString();
				
				webSocketSession.sendMessage(new TextMessage(sendMsg));
			}
		}
	}

}
