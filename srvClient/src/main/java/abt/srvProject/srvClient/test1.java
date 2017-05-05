package abt.srvProject.srvClient;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;

public class test1 {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		
		
		Map<String, task> map1 = new HashMap<>();
		task tsk = new task();
		tsk.setStatus("PENDING");
		
		map1.put("val1", tsk);
		
		
		
		//Operaciones
//		Map<String, String> map2 = new HashMap<>();
//		map2.putAll(map1);
//		map2.remove("val1");
		
		//Operaciones2
//		Map<String, String> map2 = new HashMap<>();
//		map2 = map1;
//		map2.remove("val1");
		
		//Operaciones3
		Map<String, task> map2 = new HashMap<>(map1);

		//Muestra
		System.out.println("Map1:");
		task tsk2 = new task();
		for (Map.Entry<String, task> entry : map1.entrySet()) {
			System.out.println(entry.getKey()+":"+entry.getValue().getStatus());
			tsk2 = entry.getValue().getClass().newInstance();
			tsk2.setStatus(null);
			map2.put(entry.getKey(),tsk2);
		}
		
		System.out.println("\nMap1 :");
		for (Map.Entry<String, task> entry : map1.entrySet()) {
			System.out.println(entry.getKey()+":"+entry.getValue().getStatus());
		}

		System.out.println("\nMap2 :");
		for (Map.Entry<String, task> entry : map2.entrySet()) {
			System.out.println(entry.getKey()+":"+entry.getValue().getStatus());
		}
		
		
	}
	
	static class task {
		String status;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
		
		
	}

}
