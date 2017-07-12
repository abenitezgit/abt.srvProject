package ecc.solr.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;

import ecc.solr.model.Grabacion;
import ecc.solr.utiles.GlobalArea;
import ecc.solr.utiles.Rutinas;

public class GrabService {
	GlobalArea gDatos;
	Rutinas mylib = new Rutinas();
	
	public GrabService(GlobalArea m) {
		gDatos = m;
	}
	
	public Map<String, Grabacion> getGrabaciones(int tipoConsulta) {
		Map<String, Grabacion> mapGrab = new HashMap<>();
		
		SolrDocumentList idKeys = getSolrIds(tipoConsulta);
		
		mylib.console("Total de idKeys encontrados: "+idKeys.size());
		
		if (!idKeys.isEmpty()) {
			mapGrab = getHbGrab(idKeys);
		} else {
			mylib.console(2, "No se encontraron Keys asociados a la consulta");
		}
		
//		for (SolrDocument doc: idKeys) {
//			System.out.println(doc.getFieldValue("id"));
//		}
		
		return mapGrab;
	}
	
	private Map<String, Grabacion> getHbGrab (SolrDocumentList idKeys) {
		Configuration config = HBaseConfiguration.create();
		
        for (int i=0; i<gDatos.getLstHbConfFiles().size(); i++) {
        	//System.out.println("host: "+gDatos.getLstHbConfFiles().get(i));
            config.addResource(new Path(gDatos.getLstHbConfFiles().get(i)));
        }
        
        HTablePool pool = new HTablePool(config,1000);
        
        HTableInterface hTable = pool.getTable("grabaciones");

		
		Map<String, Grabacion> mapGrab = new HashMap<>();
		try {
			for (SolrDocument doc: idKeys) {
				String key = (String) doc.getFieldValue("id");
                Get g = new Get(Bytes.toBytes(key));
                Result rs = hTable.get(g);

                Grabacion grabacion = new Grabacion();
                for (KeyValue kv : rs.raw()) {
                    setValue(kv, grabacion);
                }
                mapGrab.put((String) doc.getFieldValue("id"), grabacion);

				//System.out.println(doc.getFieldValue("id"));
			}
			
			hTable.close();
			pool.close();
			
		} catch (Exception e) {
			mylib.console(1, "Error getHbGrab ("+e.getMessage()+")");
		}
		
		return mapGrab;
	}
	
    public void setValue(KeyValue value, Grabacion grab) {
        String cq = new String(value.getQualifier());
        String valor = new String(value.getValue());
        switch (cq) {
        	case "01":
        		grab.setAni(valor);
        		break;
        	case "02":
        		grab.setDnis(valor);
        		break;
        	case "03":
        		grab.setNumerodiscado(valor);
        		break;
        	case "04":
	            grab.setConnid(valor);
	            break;
        	case "05":
        		grab.setUniqueid(valor);
        		break;
        	case "06":
        		grab.setStarttime(valor);
        		break;
        	case "07":
        		grab.setEndtime(valor);
        		break;
        	case "08":
	            grab.setFecha(valor);
	            break;
        	case "09":
        		grab.setDurationtotal(valor);
        		break;
        	case "10":
        		grab.setStatuschannel(valor);
        		break;
        	case "11":
        		grab.setChannel(valor);
        		break;
        	case "12":
        		grab.setProveedor(valor);
        		break;
        	case "13":
        		grab.setAsteriskserver(valor);
        		break;
        	case "14":
        		grab.setServicio(valor);
        		break;
        	case "15":
        		grab.setCallsonline(valor);
        		break;
        	case "16":
        		grab.setPathRecorder(valor);
        		break;
            case "17":
                grab.setNamerecorder(valor);
                break;
        	case "18":
        		grab.setRecordnum(valor);
        		break;
        	case "19":
        		grab.setAttributethisqueue(valor);
        		break;
        	case "20":
        		grab.setAttributeotherdn(valor);
        		break;
        	case "21":
        		grab.setAttributethisdn(valor);
        		break;
        	case "22":
        		grab.setUserdata2genesys(valor);
        		break;
        	case "23":
        		grab.setVirtualqueue(valor);
        		break;
        	case "24":
        		grab.setSipcallerid(valor);
        		break;
        	case "25":
        		grab.setCodigoservicio(valor);
        		break;
        	case "26":
        		grab.setInteraction_id(valor);
        		break;
        	case "27":
        		grab.setResultado_segmento(valor);
        		break;
        	case "28":
        		grab.setTtr(valor);
        		break;
            case "29":
                grab.setUrl(valor);
                break;
            case "30":
            	grab.setTipo_interaction(valor);
            	break;
            case "31":
            	grab.setSkill(valor);
            	break;
            case "32":
            	grab.setTipo_recurso(valor);
            	break;
            case "33":
            	grab.setNombre_recurso(valor);
            	break;
            case "34":
            	grab.setRut(valor);
            	break;
            case "35":
            	grab.setNombre(valor);
            	break;
            case "36":
            	grab.setGestion(valor);
            	break;
            case "37":
            	grab.setRol_recurso(valor);
            	break;
            case "38":
            	grab.setAgente(valor);
            	break;
            case "39":
            	grab.setIdcdr(valor);
            	break;
            case "40":
            	grab.setInfogestion(valor);
            	break;
            case "41":
            	grab.setBkpftp(valor);
            	break;
            case "42":
            	grab.setPathrecorder2(valor);
            	break;
            default:
                break;
        }
    }

	
	private SolrDocumentList getSolrIds(int tipoConsulta) {
		SolrDocumentList idKeys = new SolrDocumentList();
		
    	try {
			@SuppressWarnings("deprecation")
			CloudSolrServer server = new CloudSolrServer("cloudera4:2181/solr");
			
			server.setDefaultCollection("collgrabacion");
			//SolrQuery solrQuery = new SolrQuery("*.*");
			
			ModifiableSolrParams parameters = buildSolrFilters(tipoConsulta);
			
			QueryResponse response1 = server.query(parameters);
			
			//System.out.println("Total keys: "+response1.getResults().size());
			idKeys = response1.getResults();
			
			server.close();

    	} catch (SolrServerException| IOException e) {
    		mylib.console(1,"Error en getSolrIds ("+e.getMessage());
    	} catch (IndexOutOfBoundsException ex) {
    		mylib.console(2,"Consulta limitada en respuesta.");
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			mylib.console(1,"Error en getSolrIds ("+e.getMessage());
		}
		
		return idKeys;
		
	}
	
    public ModifiableSolrParams buildSolrFilters(int tipoConsulta) throws Exception {
        ModifiableSolrParams parameters = new ModifiableSolrParams();
        String q="*:*";
        
        switch (tipoConsulta) {
        	case 1:
        		/**
        		 * Connid sin rango de fechas
        		 */
        		q = buildConnidQuery();
        		break;
        	case 2:
        		/**
        		 * Connid con rango de fechas
        		 */
        		q = buildConnidFechasQuery();
        		break;
        	case 3:
        		/**
        		 * UniqueID sin rango de fechas
        		 */
        		q = buildUniqueIDQuery();
        		break;
        	case 4:
        		/**
        		 * Agente sin rango de fechas
        		 */
        		q = buildAgenteIDQuery();
        		break;
        	case 5:
        		/**
        		 * Ani con rango de fechas
        		 */
        		q = buildAniFechasQuery();
        		break;
        	case 6:
        		/**
        		 * Ani sin rango de fechas
        		 */
        		q = buildAniIDQuery();
        		break;
        	case 7:
        		/**
        		 * Dnis con rango de fechas
        		 */
        		q = buildDnisFechasQuery();
        		break;
        	case 8:
        		/**
        		 * Dnis sin rango de fechas
        		 */
        		q = buildDnisIDQuery();
        		break;
            case 9:
            	/*
            	 * Busquedas masivas por fechas
            	 */
                q = buildSkillFechasQuery();
                break;
            default:
            break;
        }
        
        parameters.set("q", q);
        parameters.set("start", 0);
        parameters.set("rows", gDatos.getDr().getLimit());
        
        mylib.console("Filtro consulta q: "+q);
        
        return parameters;
    }
    
    private String buildDnisIDQuery() {
    	List<String> skills = gDatos.getDr().getLstSkill();
    	String dnis = gDatos.getDr().getDnis();
    	
        List<String> newList = new ArrayList<>();
        for (String sk : skills) {
            newList.add(String.format("id:+%s+*", sk));
        }
        String filter1 = StringUtils.join(newList, " OR ");
        String filter2 = String.format("dnis:%s", dnis);
        String filters = String.format("(%s) AND (%s)", filter1, filter2);

        return filters;
    }

    private String buildAniIDQuery() {
    	List<String> skills = gDatos.getDr().getLstSkill();
    	String ani = gDatos.getDr().getAni();
    	
        List<String> newList = new ArrayList<>();
        for (String sk : skills) {
            newList.add(String.format("id:+%s+*", sk));
        }
        String filter1 = StringUtils.join(newList, " OR ");
        String filter2 = String.format("ani:%s", ani);
        String filters = String.format("(%s) AND (%s)", filter1, filter2);

        return filters;
    }

    private String buildAgenteIDQuery() {
    	List<String> skills = gDatos.getDr().getLstSkill();
    	String agente = gDatos.getDr().getAgente();
    	
        List<String> newList = new ArrayList<>();
        for (String sk : skills) {
            newList.add(String.format("id:+%s+*", sk));
        }
        String filter1 = StringUtils.join(newList, " OR ");
        String filter2 = String.format("agente:%s", agente);
        String filters = String.format("(%s) AND (%s)", filter1, filter2);

        return filters;
    }

    private String buildUniqueIDQuery() {
    	List<String> skills = gDatos.getDr().getLstSkill();
    	String uniqueid = gDatos.getDr().getUniqueid();
    	
        List<String> newList = new ArrayList<>();
        for (String sk : skills) {
            newList.add(String.format("id:+%s+*", sk));
        }
        String filter1 = StringUtils.join(newList, " OR ");
        String filter2 = String.format("uniqueid:%s", uniqueid);
        String filters = String.format("(%s) AND (%s)", filter1, filter2);

        return filters;
    }

    
    private String buildConnidQuery() {
    	List<String> skills = gDatos.getDr().getLstSkill();
    	String connid = gDatos.getDr().getConnid();
    	
        List<String> newList = new ArrayList<>();
        for (String sk : skills) {
            newList.add(String.format("id:+%s+*", sk));
        }
        String filter1 = StringUtils.join(newList, " OR ");
        String filter2 = String.format("connid:%s", connid);
        String filters = String.format("(%s) AND (%s)", filter1, filter2);

        return filters;
    }
    
    private String buildDnisFechasQuery() {
    	List<String> skills = gDatos.getDr().getLstSkill();
    	String fechaDesde = gDatos.getDr().getFechaDesde();
    	String fechaHasta = gDatos.getDr().getFechaHasta();
    	String dnis = gDatos.getDr().getDnis();
    	
    	List<String> newList = new ArrayList<>();
    	for (String sk : skills) {
    		newList.add(String.format("id:[+%s+%s+ TO +%s+%s+]", 
    				sk, fechaDesde, 
    				sk, fechaHasta));
    	}
    	String filter1 = StringUtils.join(newList, " OR ");
    	String filter2 = String.format("dnis:%s", dnis);
    	String filters = String.format("(%s) AND (%s)", filter1, filter2);
    	return filters;
    }

    private String buildAniFechasQuery() {
    	List<String> skills = gDatos.getDr().getLstSkill();
    	String fechaDesde = gDatos.getDr().getFechaDesde();
    	String fechaHasta = gDatos.getDr().getFechaHasta();
    	String ani = gDatos.getDr().getAni();
    	
    	List<String> newList = new ArrayList<>();
    	for (String sk : skills) {
    		newList.add(String.format("id:[+%s+%s+ TO +%s+%s+]", 
    				sk, fechaDesde, 
    				sk, fechaHasta));
    	}
    	String filter1 = StringUtils.join(newList, " OR ");
    	String filter2 = String.format("ani:%s", ani);
    	String filters = String.format("(%s) AND (%s)", filter1, filter2);
    	return filters;
    }

    private String buildConnidFechasQuery() {
    	List<String> skills = gDatos.getDr().getLstSkill();
    	String fechaDesde = gDatos.getDr().getFechaDesde();
    	String fechaHasta = gDatos.getDr().getFechaHasta();
    	String connid = gDatos.getDr().getConnid();
    	
    	List<String> newList = new ArrayList<>();
    	for (String sk : skills) {
    		newList.add(String.format("id:[+%s+%s+ TO +%s+%s+]", 
    				sk, fechaDesde, 
    				sk, fechaHasta));
    	}
    	String filter1 = StringUtils.join(newList, " OR ");
    	String filter2 = String.format("connid:%s", connid);
    	String filters = String.format("(%s) AND (%s)", filter1, filter2);
    	return filters;
    }

    
    private String buildSkillFechasQuery() {
    	List<String> skills = gDatos.getDr().getLstSkill();
    	String fechaDesde = gDatos.getDr().getFechaDesde();
    	String fechaHasta = gDatos.getDr().getFechaHasta();
    	
    	List<String> newList = new ArrayList<>();
    	for (String sk : skills) {
    		newList.add(String.format("id:[+%s+%s+ TO +%s+%s+]", 
    				sk, fechaDesde, 
    				sk, fechaHasta));
    	}
    	return StringUtils.join(newList, " OR ");
    }
    
    @SuppressWarnings("unused")
	private String buildSkillQuery(List<String> skills) {
        List<String> newList = new ArrayList<>();
        for (String sk : skills) {
            newList.add(String.format("id:+%s+*", sk));
        }

        return StringUtils.join(newList, " OR ");
    }

}
