package abt.srvProject.srvRutinas;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.JSONObject;

import abt.srvProject.model.Dependence;
import abt.srvProject.model.Grupo;
import abt.srvProject.model.Mov;
import abt.srvProject.model.MovMatch;
import abt.srvProject.model.Proceso;


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
	
	public void parseaMovMatch(MovMatch movMatch, ResultSet rs) throws Exception {
		try {
        	movMatch.setMovOrder(rs.getInt("MOVORDER"));
        	movMatch.setSourceField(rs.getString("SOURCEFIELD"));
            movMatch.setSourceLength(rs.getInt("SOURCELENGTH"));
            movMatch.setSourceType(rs.getString("SOURCETYPE"));
            movMatch.setDestField(rs.getString("DESTFIELD"));
            movMatch.setDestLength(rs.getInt("DESTLENGTH"));
            movMatch.setDestType(rs.getString("DESTTYPE"));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}
	
	public void parseaMov(Mov mov, ResultSet rs) throws Exception {
		try {
            mov.setMovID(rs.getString("MOVID")); 
            mov.setMovDesc(rs.getString("MOVDESC")); 
            mov.setEnable(rs.getInt("MOVENABLE")); 
            mov.setCliDesc(rs.getString("CLIDESC")); 
            mov.setWHEREACTIVE(rs.getInt("WHEREACTIVE")); 
            mov.setQUERYBODY(rs.getString("QUERYBODY")); 
            mov.setSTBNAME(rs.getString("STBNAME")); 
        	mov.setDTBNAME(rs.getString("DTBNAME")); 
        	mov.setSIP(rs.getString("SIP")); 
        	mov.setSDBNAME(rs.getString("SDBNAME")); 
        	mov.setSDBDESC(rs.getString("SDBDESC")); 
        	mov.setSDBTYPE(rs.getString("SDBTYPE")); 
        	mov.setSDBPORT(rs.getString("SDBPORT")); 
        	mov.setSDBINSTANCE(rs.getString("SDBINSTANCE")); 
        	mov.setSDBCONF(rs.getString("SDBCONF")); 
        	mov.setSDBJDBC(rs.getString("SDBJDBC")); 
        	mov.setSUSERNAME(rs.getString("SUSERNAME")); 
        	mov.setSUSERPASS(rs.getString("SUSERPASS")); 
        	mov.setSUSERTYPE(rs.getString("SUSERTYPE")); 
        	mov.setDIP(rs.getString("DIP")); 
        	mov.setDDBDESC(rs.getString("DDBDESC")); 
        	mov.setDDBNAME(rs.getString("DDBNAME")); 
        	mov.setDDBTYPE(rs.getString("DDBTYPE")); 
        	mov.setDDBPORT(rs.getString("DDBPORT")); 
        	mov.setDDBINSTANCE(rs.getString("DDBINSTANCE")); 
        	mov.setDDBCONF(rs.getString("DDBCONF")); 
        	mov.setDDBJDBC(rs.getString("DDBJDBC")); 
        	mov.setDUSERNAME(rs.getString("DUSERNAME")); 
        	mov.setDUSERPASS(rs.getString("DUSERPASS")); 
        	mov.setDUSERTYPE(rs.getString("DUSERTYPE")); 
        	mov.setAppend(rs.getInt("APPEND")); 
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void parseaDependence(Dependence dependence, ResultSet rs) throws Exception {
		try {
			dependence.setCritical(rs.getInt("critical"));
			dependence.setGrpID(rs.getString("grpID"));
			dependence.setProcHijo(rs.getString("procHijo"));
			dependence.setProcPadre(rs.getString("procPadre"));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void parseaProceso(Proceso process, ResultSet rs) throws Exception {
		try {
			process.setCritical(rs.getInt("critical"));
			process.setnOrder(rs.getInt("nOrder"));
			process.setProcID(rs.getString("procID"));
			process.setType(rs.getString("type"));
			process.setParams("object");
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}
	
	public void parseaGrupo(Grupo grupo, ResultSet rs) throws Exception {
		try {
			grupo.setCliDesc(rs.getString("CLIDESC"));
			grupo.setCliID(rs.getString("CLIID"));
			grupo.setGrpDESC(rs.getString("GRPDESC"));
			grupo.setGrpID(rs.getString("GRPID"));
			grupo.setEnable(rs.getString("ENABLE"));
			grupo.setHorDesc(rs.getString("HORDESC"));
			grupo.setMaxTimeExec(rs.getInt("MAXTIMEEXEC"));
			grupo.setNumSecExec(rs.getString("NUMSECEXEC"));
			grupo.setTypeBalance(rs.getString("TYPEBALANCE"));
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
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
	
	public boolean isNull(String s) {
	    return s == null ;

	}
	
	public boolean isNullOrWhitespace(String s) {
	    return s == null || isWhitespace(s);

	}
	
	public boolean isNullOrEmpty(String s) {
	    return s == null || s.length() == 0;
	}
	
	public String getLoggerLevel(Logger logger) throws Exception {
		if (logger.isDebugEnabled()) {
			return "DEBUG";
		} else if (logger.isTraceEnabled()) {
				return "TRACE";
			} else if (logger.isInfoEnabled()) {
						return "INFO";
					} else {
						return "NOT FOUND";
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
    
    public String msgResponse(String status, String data) {
        JSONObject jHeader = new JSONObject();
        
        jHeader.put("data", data);
        jHeader.put("status", status);

        return jHeader.toString();
    }
    
    public String sendError(int errCode, String errMesg) {
        JSONObject jData = new JSONObject();
        JSONObject jHeader = new JSONObject();
    
        jData.put("errMesg", errMesg);
        jData.put("errCode", errCode);
        
        jHeader.put("data",jData);
        jHeader.put("result", "error");
            
        return jHeader.toString();
    }
    
    public String sendError(int errCode) {
        String errMesg;
        
        switch (errCode) {
            case 90: 
                    errMesg = "error de entrada";
                    break;
            case 80: 
                    errMesg = "servicio offlne";
                    break;
            case 60:
                    errMesg = "TX no autorizada";
                    break;
            case 61:
                errMesg = "Request no reconocido";
                break;
            default: 
                    errMesg = "error desconocido";
                    break;
        }
        
        JSONObject jData = new JSONObject();
        JSONObject jHeader = new JSONObject();
    
        jData.put("errMesg", errMesg);
        jData.put("errCode", errCode);
        
        jHeader.put("data",jData);
        jHeader.put("result", "error");
            
        return jHeader.toString();
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
