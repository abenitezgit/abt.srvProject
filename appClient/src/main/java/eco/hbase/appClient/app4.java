package eco.hbase.appClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class app4 {

	
	public static void main(String args[])
	{
		JSONObject head = new JSONObject();
		JSONObject joId = new JSONObject();
		JSONArray jaData = new JSONArray();
		JSONArray jaRows = new JSONArray();
		
		
		
		jaData.put("100");
		jaData.put("A Time to Kill");
		jaData.put("John Grisham");
		
		joId.put("id", 100);
		joId.put("data", jaData);
		
		jaRows.put(joId);
		
		head.put("rows", jaRows);
		
		
		System.out.println("> "+joId.toString());
		
		System.out.println("> "+jaRows.toString());
		
		System.out.println("> "+head.toString());
	
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date fecha = cal.getTime();
		String strFecha = dateFormat.format(fecha);
		System.out.println(strFecha);
		
	
	}
 }
