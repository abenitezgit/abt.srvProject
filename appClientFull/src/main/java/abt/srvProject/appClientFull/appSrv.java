package abt.srvProject.appClientFull;


import abt.srvProject.services.SrvSocket;

public class appSrv {

	public static void main(String[] args) {
		SrvSocket ss = new SrvSocket("localhost",9090);
		
		try {
			
			ss.executeRequest("getTask");
		
			
			System.out.println("status: "+ss.getStatus());
		    System.out.println("mesg: "+ss.getMesg());
		    System.out.println("response: "+ss.getResponse());
		    System.out.println("data: "+ss.getData().toString());
		    
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
