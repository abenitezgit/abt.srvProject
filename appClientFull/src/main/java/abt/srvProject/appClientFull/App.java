package abt.srvProject.appClientFull;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String cadena = "2"+"0"+"2_20170106_084627_9034_11002_3001431237V0170106.wav";
        StringBuilder strID = new StringBuilder();
        
        char[] arreglo = cadena.toCharArray();
        for (char caracter : arreglo){
            if ( Character.isDigit(caracter) ) {
                System.out.print(caracter);
            	strID.append(caracter);
            }
        }   	
        
        System.out.println("");
        System.out.println(strID.toString()); 
        
        //long id = Long.valueOf(strID.toString(),30);
        //long id = Long.parseLong(strID.toString());
        double id = Double.parseDouble(strID.toString());
        
        System.out.println("");
        System.out.println(id);
        
        
    }
}
