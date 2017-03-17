package abt.srvProject.srvClient;

public class dbException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public dbException(String message) {
		System.out.println("Error direccionado: "+message);
	}

}
