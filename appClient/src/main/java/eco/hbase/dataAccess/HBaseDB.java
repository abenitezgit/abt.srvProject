package eco.hbase.dataAccess;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import abt.srvProject.srvRutinas.Rutinas;
import eco.hbase.model.RowModel;



public class HBaseDB {
	Rutinas mylib = new Rutinas();
	Configuration hcfg  = HBaseConfiguration.create();
	String tbName;

	public void setConfig(String filePropertiesPath, String HBProperty) throws Exception {
		try {
			Properties fileProperties = new Properties();
			String [] confFiles;
			String pathFiles;
			
			fileProperties.load(new FileInputStream(filePropertiesPath));
			confFiles = fileProperties.getProperty(HBProperty+".conf").split(",");
			pathFiles = fileProperties.getProperty(HBProperty+".path");
            
            for (int i=0; i< confFiles.length; i++  ) {
            	System.out.println("add file: "+pathFiles+"/"+confFiles[i]);
                hcfg.addResource(new Path(pathFiles+"/"+confFiles[i]));
            }
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void setConfig(String filePropertiesPath, String HBProperty, String tbName ) throws Exception {
		try {
			Properties fileProperties = new Properties();
			String [] confFiles;
			String pathFiles;
			
			fileProperties.load(new FileInputStream(filePropertiesPath));
			confFiles = fileProperties.getProperty(HBProperty+".conf").split(",");
			pathFiles = fileProperties.getProperty(HBProperty+".path");
            
            for (int i=0; i< confFiles.length; i++  ) {
            	System.out.println("add file: "+pathFiles+"/"+confFiles[i]);
                hcfg.addResource(new Path(pathFiles+"/"+confFiles[i]));
            }
            this.tbName = tbName;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public boolean isTableAvailable() throws Exception {
    	try {

    		Connection conn = ConnectionFactory.createConnection(hcfg);
    		Admin admin = conn.getAdmin();
    		
    		boolean exist = admin.isTableAvailable(TableName.valueOf(tbName));
    		
    		admin.close();
    		conn.close();
    		return exist;
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
	
	public boolean isTableAvailable(String tableName) throws Exception {
    	try {

    		Connection conn = ConnectionFactory.createConnection(hcfg);
    		Admin admin = conn.getAdmin();
    		
    		boolean exist = admin.isTableAvailable(TableName.valueOf(tableName));
    		
    		admin.close();
    		conn.close();
    		return exist;
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }

	public boolean isRowFound(String key) throws Exception {
		try {
			Connection conn = ConnectionFactory.createConnection(hcfg);
			
			Table table = conn.getTable(TableName.valueOf(tbName));
			
			Get get = new Get(key.getBytes());
			
			Result r = table.get(get);
			
			boolean isFound;
			
			if (r.isEmpty()) {
				isFound = false;
			} else {
				isFound = true;
			}
			
			table.close();
			conn.close();
			
            return isFound;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void deleteKeys(List<String> lstKeys) throws Exception {
		try {
			Connection conn = ConnectionFactory.createConnection(hcfg);
			
			Table table = conn.getTable(TableName.valueOf(tbName));
			List<Delete> lstDel = new ArrayList<>();
			
			mylib.console("Generando lista de Deletes para "+ lstKeys.size() + " keys...");
			
			int rowsDel=0;
			for (int i=0; i<lstKeys.size(); i++) {
				Delete delKey = new Delete(lstKeys.get(i).getBytes());
				lstDel.add(delKey);
				rowsDel++;
			}
			
			mylib.console("Ejecutando borrado batch en Hbase....");
			
			table.delete(lstDel);

			table.close();
			conn.close();
			
			mylib.console("Se eliminaron "+ rowsDel + " filas desde Hbase");
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean isRowFound(String key, String tbName) throws Exception {
		try {
			Connection conn = ConnectionFactory.createConnection(hcfg);
			
			Table table = conn.getTable(TableName.valueOf(tbName));
			
			Get get = new Get(key.getBytes());
			
			Result r = table.get(get);
			
			boolean isFound;
			
			if (r.isEmpty()) {
				isFound = false;
			} else {
				isFound = true;
			}
			
			table.close();
			conn.close();
			
            return isFound;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	
	@SuppressWarnings("deprecation")
	public void putRow(Map<String, List<RowModel>> mapRows) throws Exception {
		try {
			Connection conn = ConnectionFactory.createConnection(hcfg);
			
			Table table = conn.getTable(TableName.valueOf(tbName));
			List<Put> lstPut = new ArrayList<>();
			
			mylib.console("Se recuperaron: "+mapRows.size()+ " filas desde SQLServer");
			
			mylib.console("Generando Lista de puts...");
			for (Entry<String, List<RowModel>> entry : mapRows.entrySet()) {
				Put p = new Put(Bytes.toBytes(entry.getKey()));
				
				//mylib.console("Copiando key: " + entry.getKey());
				
				for (int i=0; i<entry.getValue().size(); i++) {
					String cf = entry.getValue().get(i).getFamily();
					String cq = entry.getValue().get(i).getColumn();
					String vu = entry.getValue().get(i).getValue();
					p.add(Bytes.toBytes(cf), Bytes.toBytes(cq),Bytes.toBytes(vu));
				}
				
				//table.put(p);
				lstPut.add(p);
			}
			
			mylib.console("Insertando List put masivo a tabla hbase...");
			
			table.put(lstPut);
			
			mylib.console("Termino de put masivo...");
			
			table.close();
			conn.close();
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public HashMap<String, HashMap<String,String>> getRows(String key, String[] family) throws Exception {
		try {
			Connection conn = ConnectionFactory.createConnection(hcfg);
			
			Table table = conn.getTable(TableName.valueOf(tbName));
			
			Get get = new Get(key.getBytes());
			
			//Add family filters
			for (String cf : family) {
				get.addFamily(cf.getBytes());
			}
			
			Result r = table.get(get);
			
			HashMap<String, String> mapRowValue = new HashMap<>();
			HashMap<String, HashMap<String, String>> mapKeyValue = new HashMap<>();
			
			for (Cell cell : r.listCells()) {
				mapRowValue.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
			}
			
			mapKeyValue.put(key, mapRowValue);
			
			return mapKeyValue;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public List<String> getAllKeys() throws Exception {
		try {
			Connection conn = ConnectionFactory.createConnection(hcfg);
			Table table = conn.getTable(TableName.valueOf(tbName));
			
			List<String> lstKeys = new ArrayList<>();
			
			Scan scan = new Scan();
			
			scan.setCaching(10000);
			scan.setMaxVersions(1);
			
			ResultScanner rs = table.getScanner(scan);
			
			Result r = rs.next();

			while (r!=null) {
				String key = Bytes.toString(r.getRow());
				lstKeys.add(key);
				r = rs.next();
			}

			return lstKeys;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public HashMap<String, HashMap<String,String>> scan(String[] family, int limit) throws Exception {
		try {
			Connection conn = ConnectionFactory.createConnection(hcfg);
			
			Table table = conn.getTable(TableName.valueOf(tbName));
			
			Scan scan = new Scan();
			
			scan.setCaching(10000);
			scan.setMaxVersions(1);
			
			//Add filters
			FilterList list = new FilterList(FilterList.Operator.MUST_PASS_ALL);
			
			SingleColumnValueFilter colValFilter1 = new SingleColumnValueFilter(Bytes.toBytes("f"), Bytes.toBytes("fechagrab")
	                ,CompareOp.GREATER_OR_EQUAL, new BinaryComparator(Bytes.toBytes("20170105_0000")));

			SingleColumnValueFilter colValFilter2 = new SingleColumnValueFilter(Bytes.toBytes("f"), Bytes.toBytes("fechagrab")
	                ,CompareOp.LESS_OR_EQUAL, new BinaryComparator(Bytes.toBytes("20170106_0000")));
			
			
			list.addFilter(colValFilter1);
			list.addFilter(colValFilter2);
			
			scan.setFilter(list);
			
			//Add family filters
			for (String cf : family) {
				scan.addFamily(cf.getBytes());
			}

			ResultScanner rs = table.getScanner(scan);
			
			Result r = rs.next();
			
			HashMap<String, HashMap<String, String>> mapKeyValue = new HashMap<>();
			int it = 0;

			while (r!=null && it<limit) {
			
				HashMap<String, String> mapRowValue = new HashMap<>();
				String key = null;
				
				for (Cell cell : r.listCells()) {
					key = new String(CellUtil.cloneRow(cell));
					mapRowValue.put("id", key);
					mapRowValue.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
				}
				
				mapKeyValue.put(key, mapRowValue);
				
				it++;
				r = rs.next();
			
			}
			
			return mapKeyValue;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

}
