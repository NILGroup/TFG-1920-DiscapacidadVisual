package server.ws;


import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocketendpoint")
public class WsServer {
	
	private String ori, dest;
	
	@OnOpen
    public void onOpen(){
        System.out.println("Open Connection ...");
    }
     
     
    @OnMessage
    public String onMessage(String message){
    	String echoMsg="Error";
    	List<String> splittedMessage = Arrays.asList(message.split(Pattern.quote("|")));
    	
    	  switch (splittedMessage.get(0)) {
    	    case "origen:":
    	    	ori = splittedMessage.get(1);
    	    	echoMsg = splittedMessage.get(1) + " de parte del servidor1";
    	      break;
    	    case "destino:":
    	    	dest = splittedMessage.get(1);
    	    	echoMsg = splittedMessage.get(1) + " de parte del servidor2";
    	      break;
    	  }
        System.out.println("Origen: " + ori);
        System.out.println("Destino: " + dest);
        /*String echoMsg = "Echo from the server : " + message;*/
        return echoMsg;
    }
 
    @OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }
 

}
