package abt.srvProject.utiles;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;


public class Rutinas {
	
	public String getUpdateString(String oldS, String newS, int flag) {
		switch (flag) {
			case 0:
				//si oldS = null or oldS = blanco actualiza con newS
				if (isNullOrEmpty(oldS)) {
					oldS = newS;
				}
				break;
			case 1:
				//actualiza siempre que newS sea != null
				if (!isNullOrEmpty(newS)) {
					oldS = newS;
				}
				break;
			default:
				break;
			}
		return oldS;
	}
	
	public boolean isWhitespace(String s) {
	    int length = s.length();
	    if (length > 0) {
	        for (int start = 0, middle = length / 2, end = length - 1; start <= middle; start++, end--) {
	            if (s.charAt(start) > ' ' || s.charAt(end) > ' ') {
	                return false;
	            }
	        }
	        return true;
	    }
	    return false;
	}
	
	public String nvlString(String obj) {
		if (obj==null) {
			return "";
		} else {
			return obj;
		}
	}
	
	public boolean isNull(String s) {
	    return s == null ;

	}
	
	public boolean isNull(Object o) {
	    return o == null;
	}
	
	public boolean isNullOrWhitespace(String s) {
	    return s == null || isWhitespace(s);

	}
	
	public boolean isNullOrEmpty(String s) {
	    return s == null || s.length() == 0;
	}
	
	public java.sql.Timestamp getSqlTimestamp(Date fecha)  {
		try {
//			java.util.Calendar cal = Calendar.getInstance();
//			cal.setTime(fecha);
			java.sql.Timestamp sqlDate = new java.sql.Timestamp(fecha.getTime()); // your sql date

			return sqlDate;
//	        SimpleDateFormat formatter;
//	        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	        return (java.sql.Date) formatter.parse(formatter.format(fecha));
		} catch (Exception e) {
			return null;
		}
	}
	
	
	public Date getDate() throws Exception {
		try {
	        Date today;
	        SimpleDateFormat formatter;
	        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        today = new Date();
	        return formatter.parse(formatter.format(today));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Date getDateAddDays(Date fecha, int days) throws Exception {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fecha);
			calendar.add(Calendar.DAY_OF_MONTH, days);
			return calendar.getTime();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Date getDate(String fecha) throws Exception {
		try {
			SimpleDateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return formatter.parse(fecha);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public int getDaysDiff(Date fecFin, Date fecIni) throws Exception {
		try {
			int dias=(int) ((fecFin.getTime()-fecIni.getTime())/86400000);
			return dias;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Date getDate(String fecha, String xformat) throws Exception {
		try {
			SimpleDateFormat formatter;
			formatter = new SimpleDateFormat(xformat);
			return formatter.parse(fecha);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public String getDateString(String fecha, String formatIni, String formatFin) {
		try {
	    	SimpleDateFormat formatterIni;
	    	SimpleDateFormat formatterFin;
	    	formatterIni = new SimpleDateFormat(formatIni);
	    	formatterFin = new SimpleDateFormat(formatFin);
	    	Date tmp = formatterIni.parse(fecha);
	    	
	    	return formatterFin.format(tmp);
		} catch (Exception e) {
			return "";
		}
	}
	
    public String getDateNow() throws Exception {
        Date today;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        today = new Date();
        return formatter.format(today);  
    }
    
    public String getDateNow(String xformat) throws Exception {
        Date today;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat(xformat);
        today = new Date();
        return formatter.format(today);  
    }
    
    public String getStringDate(Date date, String format) throws Exception {
    	SimpleDateFormat formatter;
    	formatter = new SimpleDateFormat(format);
    	return formatter.format(date);
    }
    
    public boolean fileExist(String pathFile) {
    	File f = new File(pathFile);
    	if (f.exists()) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public void console (int type, String msg) {
    	String stype = "INFO";
    	switch (type) {
	    	case 0:
	    		stype = "INFO";
	    		break;
	    	case 1:
	    		stype = "ERROR";
	    		break;
    	}
    	try {
    		System.out.println(getDateNow()+" "+stype+" Console - " + msg);
    	} catch (Exception e) {
    		System.out.println(stype+" Console - " + msg);
    	}
    }

    public void console (String msg) {
    	String stype = "INFO";
    	try {
	    	System.out.println(getDateNow()+" "+stype+" Console - " + msg);
    	} catch (Exception e) {
    		System.out.println(stype+" Console - " + msg);
    	}
    }
    
    public String serializeObjectToJSon (Object object, boolean formated) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, formated);

            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            System.out.println("Error serializeObjectToJson: "+e.getMessage());
            return null;
        }
    }
    
    public Object serializeJSonStringToObject (String parseJson, Class<?> className) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(parseJson, className);
        } catch (Exception e) {
        	System.out.println("Error serializeJSonStringToObject: "+ parseJson   + e.getMessage());
            return null;
        }
    }      
    
}
