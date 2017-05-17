package eco.hbase.appClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.commons.codec.binary.Base64;


import com.google.gson.Gson;

@SuppressWarnings("deprecation")
public class AppGoogle {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
        Object resultado=null;
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();
		BufferedReader br;
		int responseCode;

		Map<String, Object> header = new HashMap<String, Object>();
		Map<String, Object> data1 = new HashMap<String, Object>();
		Map<String, Object> data2 = new HashMap<String, Object>();
        
        //localaudio = Thread.currentThread().getContextClassLoader().getResource("/").getPath();
        
        String localaudio = "/Users/admin/01_20161219_164318_94410985934309__1482176597.15546.wav";

        File audio = new File(localaudio);
        String inFile = audio.getAbsolutePath();
        System.out.println("infile: "+inFile);
        
    	String output = encodeFileToBase64Binary(localaudio);
    	
    	System.out.println("Audio convertido a B64: "+localaudio);
    	//System.out.println(output);

    	data1.put("encoding", "MULAW");
    	data1.put("sampleRate", "8000");
    	data1.put("languageCode", "es-CL");
    	data2.put("content", output);
    	
    	header.put("config", data1);
    	header.put("audio", data2);
    	
    	
		String params = new Gson().toJson(header);


		String user="formacion";
		String pass="6XxUw3sLz1LBK";
		String authorization = user + ":" + pass;
		byte[] encodedBytes;
		//String encodedBytes;
		//encodedBytes = Base64.encodeBase64(authorization.getBytes());
		//String encondeString = new String(encodedBytes);
		//authorization = "Basic " + encondeString;
		//Base64.encodeBytes(authorization.getBytes(), 0);
		HttpPost sendPost = new HttpPost("https://speech.googleapis.com/v1/speech:recognize?key=AIzaSyDOy4MoLsYevrcHfn0W2A4cTlJchYN5dp0");
		StringEntity httpPostParams =new StringEntity(params,"UTF-8");
		httpPostParams.setContentType("application/json");
		sendPost.addHeader("content-type", "application/json");
		sendPost.addHeader("Accept", "*/*");
		//sendPost.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		//sendPost.addHeader("Accept-Language", "en-US,en;q=0.8");
		//sendPost.addHeader("Authorization", authorization);
		sendPost.setEntity(httpPostParams);

		System.out.println("Ejecutando API...");
		
        HttpResponse responsePost = client.execute(sendPost);
        responseCode = responsePost.getStatusLine().getStatusCode();

        if (responsePost.getStatusLine().getStatusCode() == 200 || responsePost.getStatusLine().getStatusCode() == 204) {

            br = new BufferedReader(
                    new InputStreamReader((responsePost.getEntity().getContent())));

                responseBuffer.setLength(0);
                while ((inputLine = br.readLine()) != null) {
                        System.out.println("line: "+inputLine);
                        responseBuffer.append(inputLine);
                }

                resultado = new Gson().toJson(responseBuffer.toString());
                System.out.println(resultado);
        }
        else{
            System.out.println(responsePost.getStatusLine().getStatusCode());

            throw new RuntimeException("Failed : HTTP error code : "
                    + responsePost.getStatusLine().getStatusCode() + responsePost.getStatusLine().getReasonPhrase());
        }




	}
	
	 private static byte[] loadFile(File file) throws IOException {
	        byte[] bytes;
	        try (InputStream is = new FileInputStream(file)) {
	            long length = file.length();
	            if (length > Integer.MAX_VALUE) {
	                // File is too large
	            }   bytes = new byte[(int)length];
	            int offset = 0;
	            int numRead = 0;
	            while (offset < bytes.length
	                    && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	                offset += numRead;
	            }   if (offset < bytes.length) {
	                throw new IOException("Could not completely read file "+file.getName());
	            }
	        }
	        return bytes;
	    }    
	    
	    private static String encodeFileToBase64Binary(String fileName)
	                    throws IOException {

	            File file = new File(fileName);
	            byte[] bytes = loadFile(file);
	            byte[] encoded = Base64.encodeBase64(bytes);
	            String encodedString = new String(encoded);

	            return encodedString;
	    }    
	    


}
