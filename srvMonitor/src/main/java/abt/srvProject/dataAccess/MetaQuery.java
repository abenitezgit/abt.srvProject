package abt.srvProject.dataAccess;

import abt.srvProject.model.Agenda;

public class MetaQuery {
	String dbType;
	
	public MetaQuery(String dbType) {
		this.dbType = dbType;
	}
	
	public String getSqlInsertGroupControl() {
		String vSQL="";
		switch (dbType) {
		case "mySQL":
			vSQL = "insert into srvConf.tb_groupControl ( "
					+ " grpID, "
					+ " numSecExec, "
					+ " procID, "
					+ " norder, "
					+ " fecIns, "
					+ " fecFinished, "
					+ " status, "
					+ " uStatus, "
					+ " errCode, "
					+ " errMesg, "
					+ " fecUpdate ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			break;
		default:
			break;
		}
		return vSQL;
	}
	
	public String getSqlInsertProcControl() {
		String vSQL="";
		switch (dbType) {
		case "mySQL":
			vSQL = "insert into srvConf.tb_procControl ( "
					+ " grpID, "
					+ " numSecExec, "
					+ " procID, "
					+ " typeProc, "
					+ " norder, "
					+ " fecIns, "
					+ " fecFinished, "
					+ " status, "
					+ " uStatus, "
					+ " errCode, "
					+ " errMesg, "
					+ " fecUpdate ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			break;
		default:
			break;
		}
		return vSQL;
	}
	
	public String getSqlUpdateProcControl(String key) {
		String vSQL="";
		String[] param = key.split(":");
		switch (dbType) {
		case "mySQL":
			
			vSQL = "update srvConf.tb_procControl set "
					+ " fecFinished = ?, "
					+ " status = ?, "
					+ " uStatus = ?, "
					+ " errCode = ?, "
					+ " errMesg = ?, "
					+ " fecUpdate = ? "
					+ "		where procID = '"+param[0]+"' and numSecExec='"+param[1]+"'";
			break;
		default:
			break;
		}
		return vSQL;
	}
	
	public String getSqlUpdateGroupControl(String key) {
		String vSQL="";
		String[] param = key.split(":");
		switch (dbType) {
		case "mySQL":
			
			vSQL = "update srvConf.tb_groupControl set "
					+ " fecFinished = ?, "
					+ " status = ?, "
					+ " uStatus = ?, "
					+ " errCode = ?, "
					+ " errMesg = ?, "
					+ " fecUpdate = ? "
					+ "		where grpID = '"+param[0]+"' and numSecExec='"+param[1]+"'";
			break;
		default:
			break;
		}
		return vSQL;
	}
	
	public String getSqlIsExistProcControl(String procID, String numSecExec) {
		String vSQL="";
		switch (dbType) {
		case "mySQL":
			vSQL = "select procID from srvConf.tb_procControl where procID='"+procID+"' and numSecExec='"+numSecExec+"'";
			break;
		default:
			break;
		}
		return vSQL;
	}
	
	public String getSqlIsExistGroupControl(String grpID, String numSecExec) {
		String vSQL="";
		switch (dbType) {
		case "mySQL":
			vSQL = "select grpID from srvConf.tb_groupControl where grpID='"+grpID+"' and numSecExec='"+numSecExec+"'";
			break;
		default:
			break;
		}
		return vSQL;
	}
	
	public String getSqlFindProcControl(String grpID, String numSecExec) {
    	String vSQL="";
    	switch (dbType) {
    	case "mySQL":
    		vSQL = 	"SELECT " + 
					" grpID, " + 
					" numSecExec, " + 
					" procID, " + 
					" typeProc, " + 
					" norder, " +
					" fecIns, " + 
					" fecUpdate, " + 
					" fecFinished, " + 
					" status, " + 
					" uStatus, " + 
					" errCode, " + 
					" errMesg " +
					" FROM srvConf.tb_procControl " +
					" where "
					+ " grpID='"+grpID+"' "
					+ " and numSecExec='"+numSecExec+"' "
					+ " order by norder asc" ; 
    		break;
		default:
			vSQL="";
			break;
    	}
    	return vSQL;
		
	}
	
	public String getSqlFindGroupControl() {
    	String vSQL="";
    	switch (dbType) {
    	case "mySQL":
    		vSQL = 	"SELECT " + 
					" grpID, " + 
					" numSecExec, " + 
					" procID, " + 
					" norder, " +
					" fecIns, " + 
					" fecUpdate, " + 
					" fecFinished, " + 
					" status, " + 
					" uStatus, " + 
					" errCode, " + 
					" errMesg " +
					" FROM srvConf.tb_groupControl " +
					" where status<>'FINISHED'" ; 
    		break;
		default:
			vSQL="";
			break;
    	}
    	return vSQL;
		
	}
	
	public String getSqlFindServices() {
    	String vSQL="";
    	switch (dbType) {
    	case "mySQL":
    		vSQL = 	"select " +
                    "  srvID, srvDesc, srvEnable, srvTypeProc, orderBalance, pctBalance " +
                    "from  " +
                    "  tb_services " +
                    "order by " +
                    "  srvID asc";
    		break;
		default:
			vSQL="";
			break;
    	}
    	return vSQL;
	}
	
	public String getSqlFindMovMatch(String MOVID) {
    	String vSQL="";
    	switch (dbType) {
    	case "mySQL":
    		vSQL = 	"select " +
                    "  MOVID, MOVORDER, SOURCEFIELD, SOURCELENGTH, SOURCETYPE, " +
                    "  DESTFIELD, DESTLENGTH, FIELDTYPE " +
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
	
    public String getSqlFindMOVMatch(String movID) {
    	String vSQL="";
    	switch (dbType) {
    	case "mySQL":
    		vSQL = 	"select " +
                    "  MOVID, MOVORDER, SOURCEFIELD, SOURCELENGTH, SOURCETYPE, " +
                    "  DESTFIELD, DESTLENGTH, FIELDTYPE " +
                    "from  " +
                    "  TB_MOVMATCH " +
                    "where " +
                    "  MOVID='"+ movID  +"' " +
                    "  And ENABLE=1 order by MOVORDER";
    		break;
		default:
			vSQL="";
			break;
    	}
    	
    	return vSQL;
    }
    
    public String getSqlFindEtlMatch(String etlID) {
    	String vSQL="";
    	switch (dbType) {
    	case "mySQL":
    		vSQL = 	"select " +
                    "  ETLORDER, ETLSOURCEFIELD, ETLSOURCELENGTH, ETLSOURCETYPE, " +
                    "  ETLDESTFIELD, ETLDESTLENGTH, ETLDESTTYPE, ETLENABLE " +
                    "from  " +
                    "  tb_etlMatch " +
                    "where " +
                    "  ETLID='"+ etlID  +"' " +
                    "  And ETLENABLE=1 order by ETLORDER";
    		break;
		default:
			vSQL="";
			break;
    	}
    	
    	return vSQL;
    }

	public String getSqlFindEtl(String procID) {
    	String vSQL="";
    	switch (dbType) {
    	case "mySQL":
    		vSQL = 	"select	cfg.etlID etlID, "
    				+ "		cfg.etlDesc etlDesc,"
    				+ "		cfg.etlEnable etlEnable, "
    				+ "		cli.cliDesc cliDesc, "
                    + "		cfg.etlIntervalFieldKey fieldKey, "
                    + " 	cfg.etlIntervalFieldKeyType fieldType, "
                    + " 	cfg.etlIntervalTimeGap timeGap, "
                    + "		cfg.etlIntervalTimeGenInterval timeGen, "
                    + "		cfg.etlIntervalTimePeriod timePeriod, "
                    + "		cfg.etlIntervalUnitMeasure unitMeasure, "
                    + "		cfg.etlQueryWhereActive whereActive, "
                    + "		cfg.etlQueryBody queryBody, "
                    + "		cfg.etlSourceTbName sTbName,  "
                    + "		cfg.etlDestTbName dTbName, "
                    + "		cfg.etlLastNumSecExec lastNumSecExec, "
                    + "		srv.serverIp sIp,  "
                    + "		db.DbDesc sDbDesc, "
                    + "		db.DbName sDbName, "
                    + "		db.dbType sDbType, "
                    + "		db.dbPort sDbPort, "
                    + "		db.dbInstance sDbInstance, "
                    + "		db.dbFileConf sDBConf, "
                    + "		db.dbJDBCString sDbJDBC, "
                    + "		usr.userName sUserName, "
                    + "		usr.userPass sUserPass, "
                    + "		usr.userType sUserType, "
                    + "		srvD.serverIp dIp, "
                    + "		dbD.DbDesc dDbDesc, "
                    + "		dbD.DbName dDbName, "
                    + "		dbD.DbType dDbType, "
                    + "		dbD.DbPort dDbPort, "
                    + "		dbD.DbInstance dDbInstance, "
                    + "		dbD.DbFileConf dDbConf, "
                    + "		dbD.DbJDBCString dDbJDBC, "
                    + "		usrD.userName dUserName, "
                    + "		usrD.userPass dUserPass, "
                    + "		usrD.UserType dUserType "
                    + "	from "
                    + "		tb_etl cfg, "
                    + "		tb_server srv, "
                    + "		tb_dbase db, "
                    + "		tb_client cli, "
                    + "		tb_user usr, "
                    + "		tb_server srvD, "
                    + "		tb_dbase dbD, "
                    + "		tb_user usrD "
                    + "	where "
                    + "		cfg.etlCliID = cli.CLIID "
                    + "		And cfg.etlSourceServerID = srv.ServerID "
                    + "		And cfg.ETLSourceDBID = db.DBID "
                    + "		And cfg.ETLSOURCEUSERID = usr.USERID "
                    + "		And cfg.ETLDESTSERVERID = srvD.SERVERID "
                    + "		And cfg.ETLDESTDBID = dbD.DBID "
                    + "		And cfg.ETLDESTUSERID = usrD.USERID "
                    + "		And cfg.ETLID='"+ procID +"'";
    		break;
		default:
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
                    "        cfg.SOURCETBNAME as STBNAME,  cfg.DESTTBNAME as DTBNAME, cfg.OFFSET as OFFSET, cfg.CREATEDEST as CREATEDEST, cfg.MAXROWSERROR as MAXROWSERROR, cfg.MAXPCTERROR as MAXPCTERROR, cfg.ROLLBACKONERROR as ROLLBACKONERROR, " +
                    "        srv.SERVERIP as SIP,  " +
                    "		 usOwn.USERNAME as SDBOWNER, udOwn.USERNAME as DDBOWNER, " +	
                    "        db.DBDESC as SDBDESC, db.DBNAME as SDBNAME, db.DBTYPE as SDBTYPE, db.DBPORT as SDBPORT, db.DBINSTANCE as SDBINSTANCE, db.DBFILECONF as SDBCONF, db.DBJDBCSTRING as SDBJDBC, " +
                    "        usr.USERNAME as SUSERNAME, usr.USERPASS as SUSERPASS, usr.USERTYPE as SUSERTYPE, " +
                    "        srvD.SERVERIP as DIP, " +
                    "        dbD.DBDESC as DDBDESC, dbD.DBNAME as DDBNAME, dbD.DBTYPE as DDBTYPE, dbD.DBPORT as DDBPORT, dbD.DBINSTANCE as DDBINSTANCE, dbD.DBFILECONF as DDBCONF, dbD.DBJDBCSTRING as DDBJDBC, " +
                    "        usrD.USERNAME as DUSERNAME, usrD.USERPASS as DUSERPASS, usrD.USERTYPE as DUSERTYPE " +
                    "from " +
                    "  tb_movtb cfg, " +
                    "  tb_user usOwn, " +
                    "  tb_user udOwn, " +
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
                    "  And cfg.DDBOWNER = udOwn.USERID " +
                    "  And cfg.SDBOWNER = usOwn.USERID " +
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
