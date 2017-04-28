package ecc.solr.utiles;

import java.util.ArrayList;
import java.util.List;

import ecc.solr.model.DataRequest;

public class GlobalArea {
    Rutinas mylib = new Rutinas();

    DataRequest dr = new DataRequest();
    List<String> lstHbConfFiles = new ArrayList<>();
    List<String> lstSolrServers = new ArrayList<>();
    String stringSolrServers;
    boolean flagInitComponents;
    
    //Getter and Setter
    
    public boolean isFlagInitComponents() {
        return flagInitComponents;
    }

    public String getStringSolrServers() {
		return stringSolrServers;
	}

	public void setStringSolrServers(String stringSolrServers) {
		this.stringSolrServers = stringSolrServers;
	}

	public void setFlagInitComponents(boolean flagInitComponents) {
        this.flagInitComponents = flagInitComponents;
    }

    public List<String> getLstHbConfFiles() {
        return lstHbConfFiles;
    }

    public void setLstHbConfFiles(List<String> lstHbConfFiles) {
        this.lstHbConfFiles = lstHbConfFiles;
    }

    public List<String> getLstSolrServers() {
        return lstSolrServers;
    }

    public void setLstSolrServers(List<String> lstSolrServers) {
        this.lstSolrServers = lstSolrServers;
    }

	public DataRequest getDr() {
		return dr;
	}
	
	public void setDr(DataRequest dr) {
		this.dr = dr;
	}

    /**
     * Procedures
     */
    
    public void initComponents(String dataInput) {
        try {
            /**
             * fill Lista SolrServers
             */
            fillLstSolrServers();
            
            /**
             * fill Lista Hbase Conf Files
             */
            fillLstHbConfFiles();
            
            /**
             * fill Global DataRequest
             */
            fillDataRequest(dataInput);
            
            flagInitComponents = true;
        } catch (Exception e) {
            flagInitComponents = false;
        }
    }
    
    public void fillLstSolrServers() throws Exception {
        lstSolrServers.add("hwk23:2181");
        lstSolrServers.add("hwk24:2181");
        lstSolrServers.add("hwk25:2181");
        StringBuilder str = new StringBuilder();
        
        str.append("");
        
        for (String lst : lstSolrServers) {
            if (str.equals("")) {
                str.append(lst);
            } else {
                str.append(","+lst);
            }
        }
        
        stringSolrServers = str.toString();
    }
    
    public void fillLstHbConfFiles() throws Exception {
        lstHbConfFiles.add("/usr/local/hbase/conf/ecoprod01/hbase-site.xml");
        lstHbConfFiles.add("/usr/local/hbase/conf/ecoprod01/core-site.xml");
        lstHbConfFiles.add("/usr/local/hbase/conf/ecoprod01/hdfs-site.xml");
    }
    
    public void fillDataRequest(String dataInput) throws Exception {
        dr = (DataRequest) mylib.serializeJSonStringToObject(dataInput, DataRequest.class);
    }
    
    public int getTipoConsulta() {
        try {
            /**
             * Se analizan las combinaciones de datos para establecer
             * el tipo de consulta a realizar
             * Setea la variable global dr (DataRequest)
             * 
             * Los tipos de resultados son (Todos con al menos un SKILL de consulta):
             * return 1: ConnID sin Fechas
             * return 2: ConnID con rango de fechas
             * return 3: UniqueID
             * return 4: Agente
             * return 5: ANI con rango de fechas
             * return 6: Ani sin fechas
             * return 7: Dnis con rango de fechas
             * return 8: Dnis sin fechas
             * return 9: Busqueda masiva por fechas
             * return 97: Error: Debe ingresar al menos un rango de fechas si es que no ha seleccionado ningun otro valor
             * return 98: Error: Debe ingresar al menos un SKILL
             * return 99: Error de Ejecución
             */
            List<String> lstSkill = new ArrayList<>();
            lstSkill = dr.getLstSkill();

            /**
             * Primara validacion: Debe venir al menos un SKILL
             */
            if (lstSkill.size()>0) {
                    if (dr.getConnid()!=null && !dr.getConnid().equals("")) {
                            /**
                             * Busqueda con ConnID
                             */
                            if (dr.getFechaDesde()!=null && !dr.getFechaDesde().equals("")) {
                                    if (dr.getFechaHasta()!=null && !dr.getFechaHasta().equals("")) {
                                            /**
                                             * Busqueda ConnID con FechaDesde y FechaHasta (Ingresada)
                                             */
                                            return 2;
                                    } else {
                                            /**
                                             * Busqueda ConnID con FechaDesde y FechaHasta (completada con getNow)
                                             */
                                            dr.setFechaHasta(mylib.getDateNow("YYYYMMDDHHmmss"));
                                            return 2;
                                    }
                            } else {
                                    /**
                                     * Busqueda ConnID SIN Fechas
                                     */
                                    return 1;
                            }
                    } else {
                            /**
                             * Busqueda SIN connid
                             */
                            if (dr.getUniqueid()!=null && !dr.getUniqueid().equals("")) {
                                    /**
                                     * Busqueda por UniqueID
                                     */
                                    return 3;
                            } else {
                                    if (dr.getAgente()!=null && !dr.getAgente().equals("")) {
                                            /**
                                             * Busqueda por Agente
                                             */
                                            return 4;
                                    } else {
                                            if (dr.getAni()!=null && !dr.getAni().equals("")) {
                                                    /**
                                                     * Busqueda por ANI
                                                     * Se evaluara si se suma por fecha o no
                                                     */
                                                    if (dr.getFechaDesde()!=null && !dr.getFechaDesde().equals("")) {
                                                            /**
                                                             * Busqueda de ANI por Fecha
                                                             */
                                                            if (dr.getFechaHasta()!=null && !dr.getFechaHasta().equals("")) {
                                                                    return 5;
                                                            } else {
                                                                    dr.setFechaHasta(mylib.getDateNow("YYYYMMDDHHmmss"));
                                                                    return 5;
                                                            }
                                                    } else {
                                                            /**
                                                             * Busqueda por ANI sin fecha
                                                             */
                                                            return 6;
                                                    }
                                            } else {
                                                    if (dr.getDnis()!=null && !dr.getDnis().equals("")) {
                                                            /**
                                                             * Busqueda por DNIS
                                                             * Se evaluara si se suma por fecha o no
                                                             */
                                                            if (dr.getFechaDesde()!=null && !dr.getFechaDesde().equals("")) {
                                                                    /**
                                                                     * Busqueda de DNIS por Fecha
                                                                     */
                                                                    if (dr.getFechaHasta()!=null && !dr.getFechaHasta().equals("")) {
                                                                            return 7;
                                                                    } else {
                                                                            dr.setFechaHasta(mylib.getDateNow("YYYYMMDDHHmmss"));
                                                                            return 7;
                                                                    }
                                                            } else {
                                                                    /**
                                                                     * Busqueda por DNIS sin fecha
                                                                     */
                                                                    return 8;
                                                            }
                                                    } else {
                                                            /**
                                                             * Busqueda Masiva por Fechas
                                                             */
                                                            if (dr.getFechaDesde()!=null && !dr.getFechaDesde().equals("")) {
                                                                    /**
                                                                     * Consulta por fecha
                                                                     * Determinar si completa fecha Hasta
                                                                     */
                                                                    if (dr.getFechaHasta()!=null && !dr.getFechaHasta().equals("")) {
                                                                            return 9;
                                                                    } else {
                                                                            dr.setFechaHasta(mylib.getDateNow("YYYYMMDDHHmmss"));
                                                                            return 9;
                                                                    }
                                                            } else {
                                                                    mylib.console(1,"Debe ingresar un rango de fechas ");
                                                                    return 97;
                                                            } 
                                                    }
                                            }
                                    }
                            }
                    }

            } else {
                    mylib.console(1,"Debe ingresar al menos un Skill ");
                    return 98;
            }
        } catch (Exception e) {
            mylib.console(1,"Error en getTipoConsulta ("+e.getMessage()+")");
            return 99;
        }
    }
}
