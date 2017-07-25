package abt.iiabt;

/**
 * Hello world!
 * @param <T>
 *
 */
public class App<T> 
{
	private Nodo<T> primero;
	
    public static void main( String[] args ) {
        
    	
    	 	
    	
    	System.out.println( "Hello World!" );
        
        System.out.println(rNum());

        
        
        
        
    }
    
    static private int rNum() {
    	return (int) (Math.random()*100);
    }
    
    
    
}
