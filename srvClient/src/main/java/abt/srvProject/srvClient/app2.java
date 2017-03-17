package abt.srvProject.srvClient;

import java.util.LinkedList;

public class app2 {

	public static void main(String[] args) {
		
		LinkedList<String> lkd = new LinkedList<String>();
		
		lkd.add("k-2");
		lkd.add("k-5");
		lkd.add("k-1");
		
		while (!lkd.isEmpty()) {
			System.out.println(lkd.remove());
		}
		
		

	}
	
	static String getNombre(String nombre) throws dbException {
		
		throw new dbException("e1");
		//System.out.println("dentro del sp");
		//return nombre;
	}

}
