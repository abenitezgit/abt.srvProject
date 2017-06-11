package abt.srvProject.services;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.codehaus.jettison.json.JSONObject;

public class SrvSocket {
	/**
	 * Datos del Socket
	 */
	Socket client = null;
	OutputStream outStr = null;
	ObjectOutputStream objOutput = null;
	InputStream inpStr = null;
	ObjectInputStream objInput = null;

	/**
	 * Parametros de entrada y salida
	 */
	String ip;
	int port;
	String msgRequest;
	String method;
	String response;
	int status;
	String mesg;
	JSONObject data;
	
	public SrvSocket(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public String getResponse() {
		return response;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getMesg() {
		return mesg;
	}
	
	public JSONObject getData() {
		return data;
	}
	
	public String getMsgRequest() {
		return msgRequest;
	}
	
    private String getMsgRequest(String method) throws Exception {
    	try {
	        JSONObject jHeader = new JSONObject();
	        JSONObject jData = new JSONObject();
	        
	        jHeader.put("data", jData);
	        jHeader.put("auth", "qwerty0987");
	        jHeader.put("request", method);
	        
	        return jHeader.toString();
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    
    public void close() throws Exception {
    	try {
    		objInput.close();
    		inpStr.close();
    		objOutput.close();
    		outStr.close();
    		client.close();
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
	
    public void executeRequest(String method)  {
    	
        try {
        	this.msgRequest = getMsgRequest(method);
        	
        	client = new Socket(ip,port);
            
            outStr = client.getOutputStream(); 
            objOutput = new ObjectOutputStream(outStr);
            
            objOutput.writeObject(msgRequest);

            inpStr = client.getInputStream();
            objInput = new ObjectInputStream(inpStr);

            response =  (String) objInput.readObject();
            
            JSONObject jHeader = new JSONObject(response);
            JSONObject jData = new JSONObject(jHeader.getString("data"));
            
            status = jHeader.getInt("status");
            mesg = jHeader.getString("mesg");
            data = jData;
        } catch (Exception e) {
        	status=99;
        	mesg = e.getMessage();
        }
    }

}
