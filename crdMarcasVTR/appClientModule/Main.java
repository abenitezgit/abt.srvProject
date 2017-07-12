import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main {
	static String fecStrHoy;
	static String fecStrAyer;
	static String fecStrHoy2;
	static String fecStrAyer2;
	
	public static void main(String[] args) {
		
		Calendar calhoy = Calendar.getInstance();
		Calendar calayer = Calendar.getInstance();
		
		calhoy.add(Calendar.DATE, -19);
		Date fecHoy = calhoy.getTime();
		
		calayer.add(Calendar.DATE, -20);
		Date fecAyer = calayer.getTime();
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		
		fecStrHoy = df.format(fecHoy)+"000000";
		fecStrAyer = df.format(fecAyer)+"000000";
		
		fecStrHoy2 = df2.format(fecHoy)+" 00:00:00";
		fecStrAyer2 = df2.format(fecAyer)+" 00:00:00";
		
		System.out.println("Fechas a Procesar: ");
		System.out.println("Desde1  : "+fecStrAyer);
		System.out.println("Hasta1  : "+fecStrHoy);
		System.out.println("Desde2  : "+fecStrAyer2);
		System.out.println("Hasta2  : "+fecStrHoy2);

		//System.exit(0);
		
		execMov1(); //cdr_detail_v2 ODS
		
		execMov2(); //cdr_ext_v2 ODS
		
		//execMov3(); //cdr_ext_v2 VTR
		
		//execMov4(); //cdr_detail_v2 VTR
	}
	
	static void execMov4() {
		try {
		
			System.out.println("Iniciando copia tabla cdr_detail_v2 (VTR)...Hora: "+getDateNow());
			
			mysqlDB sconn = new mysqlDB("172.17.232.84","navegacion","3306","userNav","u341700");
			sconn.conectar();
			
			System.out.println("Estado de Conexión DBSource: "+sconn.getConnStatus());
			
			sqlDB dconn = new sqlDB("172.17.69.8","Ods_Genesys","1433","user_econtact","Vtr.@contact2016");
			dconn.conectar();
			
			System.out.println("Estado de Conexion DBDest (VTR): "+dconn.getConnStatus());
			
			String intIni = fecStrAyer;
			String intFin = fecStrHoy;
			
			System.out.println("Intervalo inicial: "+intIni);
			System.out.println("Intervalo Final  : "+intFin);
			
			if (sconn.getConnStatus()&&dconn.getConnStatus()) {
				
			
				StringBuilder vsql = new StringBuilder();
				
				vsql.append("select ");
				vsql.append("cdrd_uniqueid,");
				vsql.append("cdrd_connid,");
				vsql.append("cdrd_secuencia,");
				vsql.append("cdrd_starttime,");
				vsql.append("cdrd_appname,");
				vsql.append("cdrd_opcion_ivr,");
				vsql.append("cdrd_rc,");
				vsql.append("cdrd_duration_ms,");
				vsql.append("cdrd_data,");
				vsql.append("cdrd_msg");
				vsql.append(" from cdr_detail_v2 ");
				vsql.append(" where cdrd_startTime >= UNIX_TIMESTAMP('"+intIni+"') ");
				vsql.append(" and cdrd_startTime <    UNIX_TIMESTAMP('"+intFin+"') ");
				
				ResultSet rs = sconn.consultar(vsql.toString());
			
			
				int rowsRead=0;
				int rowsLoad=0;
				int rowShow=2000;
				
				while (rs.next()) {
					rowsRead++;
					//System.out.println(rs.getString(1));
					
					StringBuilder cols = new StringBuilder();
					cols.append("cdrd_uniqueid,");
					cols.append("cdrd_connid,");
					cols.append("cdrd_secuencia,");
					cols.append("cdrd_starttime,");
					cols.append("cdrd_appname,");
					cols.append("cdrd_opcion_ivr,");
					cols.append("cdrd_rc,");
					cols.append("cdrd_duration_ms,");
					cols.append("cdrd_data,");
					cols.append("cdrd_msg");
					
					
					StringBuilder Variables = new StringBuilder();
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?");
					
					PreparedStatement psInsertar = null;
		
					psInsertar =  dconn.getConexion().prepareStatement(	"insert into cdr_detail_v2 (" + cols + ") VALUES (" + Variables	+ ")");
					
					//psInsertar.setString(1, (char)34 + rs.getString(1)+(char)34);
					psInsertar.setString(1, rs.getString(1));
					psInsertar.setString(2, rs.getString(2));
					psInsertar.setInt(3, rs.getInt(3));
					psInsertar.setInt(4, rs.getInt(4));
					psInsertar.setString(5, rs.getString(5));
					psInsertar.setString(6, rs.getString(6));
					psInsertar.setInt(7, rs.getInt(7));
					psInsertar.setInt(8, rs.getInt(8));
					psInsertar.setString(9, rs.getString(9));
					psInsertar.setString(10, rs.getString(10));
					
					
					if (psInsertar.executeUpdate()==1)  {
						rowsLoad++;
					}
					
					
					if (rowsRead==rowShow) {
						System.out.println("...Filas Leidas: "+rowsRead);
						System.out.println("...Filas Cargadas: "+rowsLoad);
						rowShow=rowShow+2000;
					}
					
				}
			
				System.out.println("Total Filas Leidas: "+rowsRead);
				System.out.println("Total Filas Cargadas: "+rowsLoad);
				
				System.out.println("Terminando copia tabla cdr_detail_v2 (VTR)...Hora: "+getDateNow());
				
			} else {
				System.out.println("No hay conexion a alguna BD");
			}
		} catch (Exception e) {
			System.out.println("Error general: "+e.getMessage());
		}
	}

	
	static void execMov3() {
		try {
			System.out.println("Iniciando copia tabla cdr_ext_v2 (VTR)...Hora: "+getDateNow());
			
			mysqlDB sconn = new mysqlDB("172.17.232.84","navegacion","3306","userNav","u341700");
			sconn.conectar();
			
			System.out.println("Estado de Conexión DBSource: "+sconn.getConnStatus());
			
			sqlDB dconn = new sqlDB("172.17.69.8","Ods_Genesys","1433","user_econtact","Vtr.@contact2016");
			dconn.conectar();
			
			System.out.println("Estado de Conexión DBDest (VTR): "+dconn.getConnStatus());
			
			
			if (sconn.getConnStatus()&&dconn.getConnStatus()) {
			
			
				StringBuilder vsql = new StringBuilder();
				
				vsql.append("select ");
				vsql.append("calldate,");
				vsql.append("clid,");
				vsql.append("src,");
				vsql.append("dst,");
				vsql.append("dcontext,");
				vsql.append("channel,");
				vsql.append("dstchannel,");
				vsql.append("lastapp,");
				vsql.append("lastdata,");
				vsql.append("duration,");
				vsql.append("billsec,");
				vsql.append("disposition,");
				vsql.append("amaflags,");
				vsql.append("accountcode,");
				vsql.append("userfield,");
				vsql.append("uniqueid,");
				vsql.append("did,");
				vsql.append("hangupcause,");
				vsql.append("systemname,");
				vsql.append("dialstatus,");
				vsql.append("causecode,");
				vsql.append("epcs_mercado,");
				vsql.append("epcs_segmento,");
				vsql.append("epcs_loghangup,");
				vsql.append("epcs_custom1,");
				vsql.append("epcs_custom2,");
				vsql.append("epcs_custom3,");
				vsql.append("custom4,");
				vsql.append("custom5,");
				vsql.append("custom6,");
				vsql.append("custom7,");
				vsql.append("custom8,");
				vsql.append("custom9,");
				vsql.append("custom10,");
				vsql.append("transferto,");
				vsql.append("nav_appname,");
				vsql.append("nav_opcion_ivr,");
				vsql.append("nav_rc,");
				vsql.append("nav_duration_ms,");
				vsql.append("answer_time,");
				vsql.append("callid01,");
				vsql.append("callid02,");
				vsql.append("callid03");
				vsql.append(" from cdr_ext_v2 ");
				vsql.append(" where calldate >= '"+fecStrAyer2+"' " );
				vsql.append(" and calldate < '"+fecStrHoy2+"' " );
				
				ResultSet rs = sconn.consultar(vsql.toString());
				
				
				int rowsRead=0;
				int rowsLoad=0;
				int rowShow=2000;
				
				while (rs.next()) {
					rowsRead++;
					//System.out.println(rs.getString(1));
					
					StringBuilder cols = new StringBuilder();
					cols.append("calldate,");
					cols.append("clid,");
					cols.append("src,");
					cols.append("dst,");
					cols.append("dcontext,");
					cols.append("channel,");
					cols.append("dstchannel,");
					cols.append("lastapp,");
					cols.append("lastdata,");
					cols.append("duration,");
					cols.append("billsec,");
					cols.append("disposition,");
					cols.append("amaflags,");
					cols.append("accountcode,");
					cols.append("userfield,");
					cols.append("uniqueid,");
					cols.append("did,");
					cols.append("hangupcause,");
					cols.append("systemname,");
					cols.append("dialstatus,");
					cols.append("causecode,");
					cols.append("nav_mercado,");
					cols.append("nav_segmento,");
					cols.append("nav_loghangup,");
					cols.append("nav_custom1,");
					cols.append("nav_custom2,");
					cols.append("nav_custom3,");
					cols.append("custom4,");
					cols.append("custom5,");
					cols.append("custom6,");
					cols.append("custom7,");
					cols.append("custom8,");
					cols.append("custom9,");
					cols.append("custom10,");
					cols.append("transferto,");
					cols.append("nav_appname,");
					cols.append("nav_opcion_ivr,");
					cols.append("nav_rc,");
					cols.append("nav_duration_ms,");
					cols.append("answer_time,");
					cols.append("callid01,");
					cols.append("callid02,");
					cols.append("callid03");
					
					
					StringBuilder Variables = new StringBuilder();
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?");
					
					PreparedStatement psInsertar = null;
	
					psInsertar =  dconn.getConexion().prepareStatement(	"insert into cdr_ext_v2 (" + cols + ") VALUES (" + Variables	+ ")");
					
					psInsertar.setDate(1, rs.getDate(1));
					psInsertar.setString(2, rs.getString(2));
					psInsertar.setString(3, rs.getString(3));
					psInsertar.setString(4, rs.getString(4));
					psInsertar.setString(5, rs.getString(5));
					psInsertar.setString(6, rs.getString(6));
					psInsertar.setString(7, rs.getString(7));
					psInsertar.setString(8, rs.getString(8));
					psInsertar.setString(9, rs.getString(9));
					psInsertar.setInt(10, rs.getInt(10));
					psInsertar.setInt(11, rs.getInt(11));
					psInsertar.setString(12, rs.getString(12));
					psInsertar.setInt(13, rs.getInt(13));
					psInsertar.setString(14, rs.getString(14));
					psInsertar.setString(15, rs.getString(15));
					psInsertar.setString(16, rs.getString(16));
					psInsertar.setString(17, rs.getString(17));
					psInsertar.setString(18, rs.getString(18));
					psInsertar.setString(19, rs.getString(19));
					psInsertar.setString(20, rs.getString(20));
					psInsertar.setString(21, rs.getString(21));
					psInsertar.setString(22, rs.getString(22));
					psInsertar.setString(23, rs.getString(23));
					psInsertar.setString(24, rs.getString(24));
					psInsertar.setString(25, rs.getString(25));
					psInsertar.setString(26, rs.getString(26));
					psInsertar.setString(27, rs.getString(27));
					psInsertar.setString(28, rs.getString(28));
					psInsertar.setString(29, rs.getString(29));
					psInsertar.setString(30, rs.getString(30));
					psInsertar.setString(31, rs.getString(31));
					psInsertar.setString(32, rs.getString(32));
					psInsertar.setString(33, rs.getString(33));
					psInsertar.setString(34, rs.getString(34));
					psInsertar.setString(35, rs.getString(35));
					psInsertar.setString(36, rs.getString(36));
					psInsertar.setString(37, rs.getString(37));
					psInsertar.setInt(38, rs.getInt(38));
					psInsertar.setInt(39, rs.getInt(39));
					psInsertar.setDate(40, rs.getDate(40));
					psInsertar.setString(41, rs.getString(41));
					psInsertar.setString(42, rs.getString(42));
					psInsertar.setString(43, rs.getString(43));
					
					
					if (psInsertar.executeUpdate()==1)  {
						rowsLoad++;
					}
					
					
					if (rowsRead==rowShow) {
						System.out.println("...Filas Leidas: "+rowsRead);
						System.out.println("...Filas Cargadas: "+rowsLoad);
						rowShow=rowShow+2000;
					}
					
				}
				
				System.out.println("Total Filas Leidas: "+rowsRead);
				System.out.println("Total Filas Cargadas: "+rowsLoad);
				
				System.out.println("Terminando copia tabla cdr_ext_v2 (VTR)...Hora: "+getDateNow());
				
			} else {
				System.out.println("No hay conexion a alguna BD");
			}
		} catch (Exception e) {
			System.out.println("Error general: "+e.getMessage());
		}
	}

	
	static void execMov2() {
		try {
			System.out.println("Iniciando copia tabla cdr_ext_v2...Hora: "+getDateNow());
			
			mysqlDB sconn = new mysqlDB("172.17.233.65","navegacion","3306","userNav","u341700");
			sconn.conectar();
			
			System.out.println("Estado de Conexión DBSource: "+sconn.getConnStatus());
			
			sqlDB dconn = new sqlDB("s1-odsnod01-vtr.callcenter.vtr.cl","VTR_ODS1","1433","sqlAdmin","SQLadmin1");
			dconn.conectar();
			
			System.out.println("Estado de Conexión DBDest: "+dconn.getConnStatus());
			
			
			if (sconn.getConnStatus()&&dconn.getConnStatus()) {
			
			
				StringBuilder vsql = new StringBuilder();
				
				vsql.append("select ");
				vsql.append("calldate,");
				vsql.append("clid,");
				vsql.append("src,");
				vsql.append("dst,");
				vsql.append("dcontext,");
				vsql.append("channel,");
				vsql.append("dstchannel,");
				vsql.append("lastapp,");
				vsql.append("lastdata,");
				vsql.append("duration,");
				vsql.append("billsec,");
				vsql.append("disposition,");
				vsql.append("amaflags,");
				vsql.append("accountcode,");
				vsql.append("userfield,");
				vsql.append("uniqueid,");
				vsql.append("did,");
				vsql.append("hangupcause,");
				vsql.append("systemname,");
				vsql.append("dialstatus,");
				vsql.append("causecode,");
				vsql.append("epcs_mercado,");
				vsql.append("epcs_segmento,");
				vsql.append("epcs_loghangup,");
				vsql.append("epcs_custom1,");
				vsql.append("epcs_custom2,");
				vsql.append("epcs_custom3,");
				vsql.append("custom4,");
				vsql.append("custom5,");
				vsql.append("custom6,");
				vsql.append("custom7,");
				vsql.append("custom8,");
				vsql.append("custom9,");
				vsql.append("custom10,");
				vsql.append("transferto,");
				vsql.append("nav_appname,");
				vsql.append("nav_opcion_ivr,");
				vsql.append("nav_rc,");
				vsql.append("nav_duration_ms,");
				vsql.append("answer_time,");
				vsql.append("callid01,");
				vsql.append("callid02,");
				vsql.append("callid03");
				vsql.append(" from cdr_ext_v2 ");
				vsql.append(" where calldate >= '"+fecStrAyer2+"' " );
				vsql.append(" and calldate < '"+fecStrHoy2+"' " );
				
				ResultSet rs = sconn.consultar(vsql.toString());
				
				
				int rowsRead=0;
				int rowsLoad=0;
				int rowShow=2000;
				
				while (rs.next()) {
					rowsRead++;
					//System.out.println(rs.getString(1));
					
					StringBuilder cols = new StringBuilder();
					cols.append("calldate,");
					cols.append("clid,");
					cols.append("src,");
					cols.append("dst,");
					cols.append("dcontext,");
					cols.append("channel,");
					cols.append("dstchannel,");
					cols.append("lastapp,");
					cols.append("lastdata,");
					cols.append("duration,");
					cols.append("billsec,");
					cols.append("disposition,");
					cols.append("amaflags,");
					cols.append("accountcode,");
					cols.append("userfield,");
					cols.append("uniqueid,");
					cols.append("did,");
					cols.append("hangupcause,");
					cols.append("systemname,");
					cols.append("dialstatus,");
					cols.append("causecode,");
					cols.append("epcs_mercado,");
					cols.append("epcs_segmento,");
					cols.append("epcs_loghangup,");
					cols.append("epcs_custom1,");
					cols.append("epcs_custom2,");
					cols.append("epcs_custom3,");
					cols.append("custom4,");
					cols.append("custom5,");
					cols.append("custom6,");
					cols.append("custom7,");
					cols.append("custom8,");
					cols.append("custom9,");
					cols.append("custom10,");
					cols.append("transferto,");
					cols.append("nav_appname,");
					cols.append("nav_opcion_ivr,");
					cols.append("nav_rc,");
					cols.append("nav_duration_ms,");
					cols.append("answer_time,");
					cols.append("callid01,");
					cols.append("callid02,");
					cols.append("callid03");
					
					
					StringBuilder Variables = new StringBuilder();
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?");
					
					PreparedStatement psInsertar = null;
	
					psInsertar =  dconn.getConexion().prepareStatement(	"insert into cdr_ext_v2 (" + cols + ") VALUES (" + Variables	+ ")");
					
					psInsertar.setDate(1, rs.getDate(1));
					psInsertar.setString(2, rs.getString(2));
					psInsertar.setString(3, rs.getString(3));
					psInsertar.setString(4, rs.getString(4));
					psInsertar.setString(5, rs.getString(5));
					psInsertar.setString(6, rs.getString(6));
					psInsertar.setString(7, rs.getString(7));
					psInsertar.setString(8, rs.getString(8));
					psInsertar.setString(9, rs.getString(9));
					psInsertar.setInt(10, rs.getInt(10));
					psInsertar.setInt(11, rs.getInt(11));
					psInsertar.setString(12, rs.getString(12));
					psInsertar.setInt(13, rs.getInt(13));
					psInsertar.setString(14, rs.getString(14));
					psInsertar.setString(15, rs.getString(15));
					psInsertar.setString(16, rs.getString(16));
					psInsertar.setString(17, rs.getString(17));
					psInsertar.setString(18, rs.getString(18));
					psInsertar.setString(19, rs.getString(19));
					psInsertar.setString(20, rs.getString(20));
					psInsertar.setString(21, rs.getString(21));
					psInsertar.setString(22, rs.getString(22));
					psInsertar.setString(23, rs.getString(23));
					psInsertar.setString(24, rs.getString(24));
					psInsertar.setString(25, rs.getString(25));
					psInsertar.setString(26, rs.getString(26));
					psInsertar.setString(27, rs.getString(27));
					psInsertar.setString(28, rs.getString(28));
					psInsertar.setString(29, rs.getString(29));
					psInsertar.setString(30, rs.getString(30));
					psInsertar.setString(31, rs.getString(31));
					psInsertar.setString(32, rs.getString(32));
					psInsertar.setString(33, rs.getString(33));
					psInsertar.setString(34, rs.getString(34));
					psInsertar.setString(35, rs.getString(35));
					psInsertar.setString(36, rs.getString(36));
					psInsertar.setString(37, rs.getString(37));
					psInsertar.setInt(38, rs.getInt(38));
					psInsertar.setInt(39, rs.getInt(39));
					psInsertar.setDate(40, rs.getDate(40));
					psInsertar.setString(41, rs.getString(41));
					psInsertar.setString(42, rs.getString(42));
					psInsertar.setString(43, rs.getString(43));
					
					
					if (psInsertar.executeUpdate()==1)  {
						rowsLoad++;
					}
					
					
					if (rowsRead==rowShow) {
						System.out.println("...Filas Leidas: "+rowsRead);
						System.out.println("...Filas Cargadas: "+rowsLoad);
						rowShow=rowShow+2000;
					}
					
				}
				
				System.out.println("Total Filas Leidas: "+rowsRead);
				System.out.println("Total Filas Cargadas: "+rowsLoad);
				
				System.out.println("Terminando copia tabla cdr_ext_v2...Hora: "+getDateNow());
				
			} else {
				System.out.println("No hay conexion a alguna BD");
			}
		} catch (Exception e) {
			System.out.println("Error general: "+e.getMessage());
		}
	}
	
	
	static void execMov1() {
		try {
		
			System.out.println("Iniciando copia tabla cdr_detail_v2...Hora: "+getDateNow());
			
			mysqlDB sconn = new mysqlDB("172.17.233.65","navegacion","3306","userNav","u341700");
			//mysqlDB sconn = new mysqlDB("172.17.233.65","navegacion","3306","abenitez","maca3108");
			sconn.conectar();
			
			System.out.println("Estado de Conexión DBSource: "+sconn.getConnStatus());
			
			sqlDB dconn = new sqlDB("s1-odsnod01-vtr.callcenter.vtr.cl","VTR_ODS1","1433","sqlAdmin","SQLadmin1");
			dconn.conectar();
			
			System.out.println("Estado de Conexion DBDest: "+dconn.getConnStatus());
			
			String intIni = fecStrAyer;
			String intFin = fecStrHoy;
			
			System.out.println("Intervalo inicial: "+intIni);
			System.out.println("Intervalo Final  : "+intFin);
			
			if (sconn.getConnStatus()&&dconn.getConnStatus()) {
			
				StringBuilder vsql = new StringBuilder();
				
				vsql.append("select ");
				vsql.append("cdrd_uniqueid,");
				vsql.append("cdrd_connid,");
				vsql.append("cdrd_secuencia,");
				vsql.append("cdrd_starttime,");
				vsql.append("cdrd_appname,");
				vsql.append("cdrd_opcion_ivr,");
				vsql.append("cdrd_rc,");
				vsql.append("cdrd_duration_ms,");
				vsql.append("cdrd_data,");
				vsql.append("cdrd_msg");
				vsql.append(" from cdr_detail_v2 ");
				vsql.append(" where cdrd_startTime >= UNIX_TIMESTAMP('"+intIni+"') ");
				vsql.append(" and cdrd_startTime <    UNIX_TIMESTAMP('"+intFin+"') ");
				
				ResultSet rs = sconn.consultar(vsql.toString());
			
			
				int rowsRead=0;
				int rowsLoad=0;
				int rowShow=2000;
				
				while (rs.next()) {
					rowsRead++;
					//System.out.println(rs.getString(1));
					
					StringBuilder cols = new StringBuilder();
					cols.append("cdrd_uniqueid,");
					cols.append("cdrd_connid,");
					cols.append("cdrd_secuencia,");
					cols.append("cdrd_starttime,");
					cols.append("cdrd_appname,");
					cols.append("cdrd_opcion_ivr,");
					cols.append("cdrd_rc,");
					cols.append("cdrd_duration_ms,");
					cols.append("cdrd_data,");
					cols.append("cdrd_msg");
					
					
					StringBuilder Variables = new StringBuilder();
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?,");
					Variables.append("?");
					
					PreparedStatement psInsertar = null;
		
					psInsertar =  dconn.getConexion().prepareStatement(	"insert into cdr_detail_v2 (" + cols + ") VALUES (" + Variables	+ ")");
					
					//psInsertar.setString(1, (char)34 + rs.getString(1)+(char)34);
					psInsertar.setString(1, rs.getString(1));
					psInsertar.setString(2, rs.getString(2));
					psInsertar.setInt(3, rs.getInt(3));
					psInsertar.setInt(4, rs.getInt(4));
					psInsertar.setString(5, rs.getString(5));
					psInsertar.setString(6, rs.getString(6));
					psInsertar.setInt(7, rs.getInt(7));
					psInsertar.setInt(8, rs.getInt(8));
					psInsertar.setString(9, rs.getString(9));
					psInsertar.setString(10, rs.getString(10));
					
					
					if (psInsertar.executeUpdate()==1)  {
						rowsLoad++;
					}
					
					
					if (rowsRead==rowShow) {
						System.out.println("...Filas Leidas: "+rowsRead);
						System.out.println("...Filas Cargadas: "+rowsLoad);
						rowShow=rowShow+2000;
					}
					
				}
			
				System.out.println("Total Filas Leidas: "+rowsRead);
				System.out.println("Total Filas Cargadas: "+rowsLoad);
				
				System.out.println("Terminando copia tabla cdr_detail_v2...Hora: "+getDateNow());
				
			} else {
				System.out.println("No hay conexion a alguna BD");
			}
		} catch (Exception e) {
			System.out.println("Error general: "+e.getMessage());
		}
	}
	
    static String getDateNow() {
        try {
            //Extrae Fecha de Hoy
            //
            Date today;
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            today = new Date();
            return formatter.format(today);  
        } catch (Exception e) {
            return null;
        }
    }


}