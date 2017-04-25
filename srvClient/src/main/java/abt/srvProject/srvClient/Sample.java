package abt.srvProject.srvClient;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Sample {
	
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }
	
	public static void main(String[] args) throws IOException {
			
	       Document doc = Jsoup.connect("http://grabacionesclaro.e-contact.cl/2011/2016102014").get();
	       //Document doc = Jsoup.connect("http://grabacionesclaro.e-contact.cl/2011/OREKA60/2016/11/28/22/2_20161128_222646_989101268_11000_1001516334M0161129").get();
	        //for (Element file : doc.select("td.right td 2")) {
	       int total = 0;
	       //for (Element file : doc.select("img[src~=?.(png|jpe?g)]")) {
	       Elements media = doc.select("[href]");
	       //print("\nMedia: (%d)", media.size());
	       
	       for (Element src : media) {
	    	   System.out.println(src.attr("href"));
	    	   total++;
	       }
	       print("\nMedia: (%d)", media.size());
	       	       
//	       for (Element file : doc.select("[src]")) {
//    	   //for (Element file : doc.select("a[href]")) {
//        		//System.out.println(file.childNodeSize());
//	            System.out.println(file.attr("href"));
//	            total++;
//	        }
//        	System.out.println("total: "+total);
		
	}

}
