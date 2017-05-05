package abt.srvProject.srvClient;

public class solrClient {
	GlobalArea gDatos = new GlobalArea();
	Rutinas mylib = new Rutinas();

	
	public static void main(String[] args) {
		
		String vSQL = 	"select	cfg.etlID etlID, "
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
                + "		And cfg.ETLID='"+ "ETL00001" +"'";
		
		System.out.println(vSQL);

		
		
		
	}

}
