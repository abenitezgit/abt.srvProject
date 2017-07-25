package eco.hbase.appClient;

import java.sql.ResultSet;

import common.dataAccess.sqlDB;

public class AppMonSql {

	public static void main(String[] args) throws Exception {
		
		//sqlDB sqlConn = new sqlDB("172.17.233.193","master","1433","sqlAdmin","SQLadmin1",10);
		//sqlDB sqlConn = new sqlDB("eco-sql10","master","1433","sqlAdmin","adminSQL01",10);
		sqlDB sqlConn = new sqlDB("eco-sql-002","master","1433","sqlAdmin","adminSQL01",10);
		//sqlDB sqlConn = new sqlDB("eco-sql-004","master","1433","sqlAdmin","adminSQL01",10);
		
		sqlConn.open();
		
		if (sqlConn.isConnected()) {
			System.out.println("Conected");
			
			//Recupera bases de datos
			//state: 0 - ONLINE
			//state: 1 - RESTORING
			String vSql = "select * from sys.databases where name not in ('tempdb') and state=0";
			if (sqlConn.executeQuery(vSql)) {
				ResultSet rs = sqlConn.getQuery();
				
				while (rs.next()) {
					System.out.println("DB: "+rs.getString(1)+" created: "+rs.getString(5));
					System.out.println("use "+rs.getString(1));
					String vSqlDB = "use ["+rs.getString(1)+"]";
					sqlConn.executeQuery(vSqlDB);

					//DataFiles
					String vSqlDF = "select * from sys.database_files";
					if (sqlConn.executeQuery(vSqlDF)) {
						ResultSet rsDF = sqlConn.getQuery();
						while (rsDF.next()) {
							System.out.println("DF_type: "+rsDF.getString(3)+" name: "+rsDF.getString(6)+" phys: "+rsDF.getString(7)+" size: "+rsDF.getInt(10));
							
						}
						rsDF.close();
					}
					
					//Tablas
					
					String vSqlTables = "SELECT " +
									"    t.NAME AS TableName, " +
									"    s.Name AS SchemaName, " +
									"    p.rows AS RowCounts, " +
									"    SUM(a.total_pages) * 8 AS TotalSpaceKB, " + 
									"    CAST(ROUND(((SUM(a.total_pages) * 8) / 1024.00), 2) AS NUMERIC(36, 2)) AS TotalSpaceMB, " +
									"    SUM(a.used_pages) * 8 AS UsedSpaceKB,  " +
									"    CAST(ROUND(((SUM(a.used_pages) * 8) / 1024.00), 2) AS NUMERIC(36, 2)) AS UsedSpaceMB, " + 
									"    (SUM(a.total_pages) - SUM(a.used_pages)) * 8 AS UnusedSpaceKB, " +
									"    CAST(ROUND(((SUM(a.total_pages) - SUM(a.used_pages)) * 8) / 1024.00, 2) AS NUMERIC(36, 2)) AS UnusedSpaceMB " +
									"FROM  " +
									"    sys.tables t " +
									"INNER JOIN      " +
									"    sys.indexes i ON t.OBJECT_ID = i.object_id " +
									"INNER JOIN " +
									"    sys.partitions p ON i.object_id = p.OBJECT_ID AND i.index_id = p.index_id " +
									"INNER JOIN " + 
									"    sys.allocation_units a ON p.partition_id = a.container_id " +
									"LEFT OUTER JOIN " +
									"    sys.schemas s ON t.schema_id = s.schema_id " +
									"WHERE " +
									"    t.NAME NOT LIKE 'dt%' " + 
									"    AND t.is_ms_shipped = 0 " +
									"    AND i.OBJECT_ID > 255 " + 
									"GROUP BY  " +
									"    t.Name, s.Name, p.Rows " +
									"ORDER BY " +
									"    t.Name ";
					if (sqlConn.executeQuery(vSqlTables)) {
						ResultSet rsTables = sqlConn.getQuery();
						while (rsTables.next()) {
							System.out.println("table: "+rsTables.getString(1)+" rows: "+rsTables.getString(3)+" MB: "+rsTables.getInt(5));
							
						}
						
						
						rsTables.close();
					}
				}
				
				rs.close();
			}
			
			String vSqlSess = " SELECT " + 
							"	DB_NAME(dbid) as DBName, " + 
							"	COUNT(dbid) as NumberOfConnections, " +
							"	loginame as LoginName " +
							"	FROM " +
							"		sys.sysprocesses " +
							"		WHERE  " +
							"		dbid > 0 " +
							"		GROUP BY " +
							"		dbid, loginame ";
			if (sqlConn.executeQuery(vSqlSess)) {
				ResultSet rsSes = sqlConn.getQuery();
				while (rsSes.next()) {
					System.out.println("dbName: "+ rsSes.getString(1)+ " numConn: "+ rsSes.getString(2)+ " loginName: "+ rsSes.getString(3));
				}
				rsSes.close();
			}
			
			
			
			
			sqlConn.close();
		} else {
			System.out.println("NO Conected");
		}
		
		System.out.println("Exit...");
		

	}

}
