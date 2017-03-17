package abt.srvProject.dataAccess;

import abt.srvProject.model.Agenda;

public class MetaQuery {
	String dbType;
	
	public MetaQuery(String dbType) {
		this.dbType = dbType;
	}
	
	public String getSqlFindMovMatch(String MOVID) {
    	String vSQL="";
    	switch (dbType) {
    	case "mySQL":
    		vSQL = 	"select " +
                    "  MOVID, MOVORDER, SOURCEFIELD, SOURCELENGTH, SOURCETYPE, " +
                    "  DESTFIELD, DESTLENGTH, DESTTYPE " +
                    "from  " +
                    "  TB_MOVMATCH " +
                    "where " +
                    "  MOVID='"+ MOVID  +"' " +
                    "  And ENABLE=1 order by MOVORDER";
    		break;
		default:
			vSQL="";
			break;
    	}
    	
    	return vSQL;
    }

	
	public String getSqlFindMov(String procID) {
    	String vSQL="";
    	switch (dbType) {
    	case "mySQL":
    		vSQL = 	"select  cfg.MOVID as MOVID, cfg.MOVDESC as MOVDESC, cfg.MOVENABLE as MOVENABLE, cli.CLIDESC as CLIDESC, " +
                    "        cfg.QUERYWHEREACTIVE as WHEREACTIVE, cfg.QUERYBODY as QUERYBODY, cfg.APPEND as APPEND, " +
                    "        cfg.SOURCETBNAME as STBNAME,  cfg.DESTTBNAME as DTBNAME, " +
                    "        srv.SERVERIP as SIP,  " +
                    "        db.DBDESC as SDBDESC, db.DBNAME as SDBNAME, db.DBTYPE as SDBTYPE, db.DBPORT as SDBPORT, db.DBINSTANCE as SDBINSTANCE, db.DBFILECONF as SDBCONF, db.DBJDBCSTRING as SDBJDBC, " +
                    "        usr.USERNAME as SUSERNAME, usr.USERPASS as SUSERPASS, usr.USERTYPE as SUSERTYPE, " +
                    "        srvD.SERVERIP as DIP, " +
                    "        dbD.DBDESC as DDBDESC, dbD.DBNAME as DDBNAME, dbD.DBTYPE as DDBTYPE, dbD.DBPORT as DDBPORT, dbD.DBINSTANCE as DDBINSTANCE, dbD.DBFILECONF as DDBCONF, dbD.DBJDBCSTRING as DDBJDBC, " +
                    "        usrD.USERNAME as DUSERNAME, usrD.USERPASS as DUSERPASS, usrD.USERTYPE as DUSERTYPE " +
                    "from " +
                    "  tb_movtb cfg, " +
                    "  tb_server srv, " +
                    "  tb_dbase db, " +
                    "  tb_client cli, " +
                    "  tb_user usr, " +
                    "  tb_server srvD, " +
                    "  tb_dbase dbD, " +
                    "  tb_user usrD " +
                    "where " +
                    "  cfg.CLIID = cli.CLIID " +
                    "  And cfg.SourceServerID = srv.SERVERID " +
                    "  And cfg.SourceDBID = db.DBID " +
                    "  And cfg.SOURCEUSERID = usr.USERID " +
                    "  And cfg.DESTSERVERID = srvD.SERVERID " +
                    "  And cfg.DESTDBID = dbD.DBID " +
                    "  And cfg.DESTUSERID = usrD.USERID " +
                    "  And cfg.MOVID='"+ procID +"'  " +
                    "order by " +
                    "  MOVID";
    		break;
		default:
			vSQL="";
			break;
    	}
    	
    	return vSQL;
    }

	
	public String getSqlFindProcess(String vGrpID) {
    	String vSQL="";
    	switch (dbType) {
    	case "mySQL":
    		vSQL = 	" select grpID, procID, nOrder, critical, type " +
					" from " +
					"	tb_procGroup " +
					" where " +
					"	grpID='"+vGrpID+"' " +
					"	and enable='1' " +
					" order by " +
					"	norder asc ";
    		break;
		default:
			vSQL="";
			break;
    	}
    	
    	return vSQL;

    }
    
    public String getSqlFindDependences(String vGrpID) {
    	String vSQL="";
    	switch (dbType) {
    	case "mySQL":
    		vSQL = 	" select " +
                    "  grpID, procHijo, procPadre, critical " + 
                    " from  " +
                    "  tb_procDepend " +
                    " where " +
                    "  GRPID='"+ vGrpID  +"' " +
                    " order by PROCHIJO, PROCPADRE ";
    		break;
		default:
			vSQL="";
			break;
    	}
    	
    	return vSQL;

    }

	
	public String getSqlFindAgeActive(String iteratorMinute, String posmonth, String posdayOfMonth, String posdayOfWeek, String posweekOfYear, String posweekOfMonth, String posIteratorHour, String posIteratorMinute) {
    	String vSQL=null;
    	switch (dbType) {
    		case "ORA":
	            vSQL = "select "+iteratorMinute+" horaAgenda,ageID, month, dayOfMonth, dayOfWeek, weekOfYear, weekOfMonth, hourOfDay from tb_diary where "
	                    + "     ageEnable=1 "
	                    + "     and substr(month,"+posmonth+",1) = '1'"
	                    + "     and substr(dayOfMonth,"+posdayOfMonth+",1) = '1'"
	                    + "     and substr(dayOfWeek,"+posdayOfWeek+",1) = '1'"
	                    + "     and substr(weekOfYear,"+posweekOfYear+",1) = '1'"
	                    + "     and substr(weekOfMonth,"+posweekOfMonth+",1) = '1'"
	                    + "     and substr(hourOfDay,"+posIteratorHour +",1) = '1'"
	                    + "     and substr(minute,"+posIteratorMinute +",1) = '1'";
    			break;
    		case "SQL":
	            vSQL = "select "+iteratorMinute+" horaAgenda,ageID, month, dayOfMonth, dayOfWeek, weekOfYear, weekOfMonth, hourOfDay from tb_diary where "
	                    + "     ageEnable=1 "
	                    + "     and substr(month,"+posmonth+",1) = '1'"
	                    + "     and substr(dayOfMonth,"+posdayOfMonth+",1) = '1'"
	                    + "     and substr(dayOfWeek,"+posdayOfWeek+",1) = '1'"
	                    + "     and substr(weekOfYear,"+posweekOfYear+",1) = '1'"
	                    + "     and substr(weekOfMonth,"+posweekOfMonth+",1) = '1'"
	                    + "     and substr(hourOfDay,"+posIteratorHour +",1) = '1'"
	                    + "     and substr(minute,"+posIteratorMinute +",1) = '1'";
    			break;
    		case "mySQL":
	            vSQL = "select "+iteratorMinute+" horaAgenda,ageID, month, dayOfMonth, dayOfWeek, weekOfYear, weekOfMonth, hourOfDay from tb_diary where "
	                    + "     ageEnable=1 "
	                    + "     and substr(month,"+posmonth+",1) = '1'"
	                    + "     and substr(dayOfMonth,"+posdayOfMonth+",1) = '1'"
	                    + "     and substr(dayOfWeek,"+posdayOfWeek+",1) = '1'"
	                    + "     and substr(weekOfYear,"+posweekOfYear+",1) = '1'"
	                    + "     and substr(weekOfMonth,"+posweekOfMonth+",1) = '1'"
	                    + "     and substr(hourOfDay,"+posIteratorHour +",1) = '1'"
	                    + "     and substr(minute,"+posIteratorMinute +",1) = '1'";
    			break;
    		default:
    			break;
    	}
    	return vSQL;
    }
	
	 public String getSqlFindGroup(Agenda agenda) {
	    	String vSQL="";
	    	switch (dbType) {
	    	case "mySQL":
	            vSQL =  "select "+agenda.getNumSecExec()+" as numSecExec, gr.GRPID, gr.GRPDESC, gr.ENABLE, gr.CLIID, cl.cliDesc, ho.horDesc, gr.maxTimeExec, gr.typeBalance " +
	                    "from " +
	                    "  tb_group gr, " +
	                    "  tb_schedDiary ha, " +
	                    "  tb_client cl, " +
	                    "  tb_schedule ho, " +
	                    "  tb_diary ag " +
	                    "where " +
	                    "  gr.HORID = ho.HORID " +
	                    "  and ha.HORID = ho.HORID " +
	                    "  and ha.AGEID = ag.AGEID " +
	                    "  and gr.CLIID = cl.CLIID " +
	                    "  and gr.HORID = ha.HORID " +
	                    "  and ha.HORINCLUSIVE=1 " +
	                    "  and gr.ENABLE =1 " +
	                    "  and ha.AGEID='"+agenda.getAgeID()+"' " +
	                    "  and cl.ENABLE = 1 " +
	                    "  and ag.AGEENABLE = 1 " +
	                    "  and ho.HORENABLE = 1 " +
	                    "  and ha.HORENABLE = 1 ";
	    		break;
			default:
				vSQL="";
				break;
	    	}
	    	return vSQL;
	    }

}
