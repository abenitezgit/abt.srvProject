package abt.iiabt;

public class NodoDoble<T> {
	private T dato;
    private NodoDoble<T> next;
    private NodoDoble<T> before;
 
    /**
     * Constructor por defecto
     */
    public NodoDoble(){
    	before=null;
        next=null;
     }
 
    /**
     * Le pasamos un dato al nodo
     * @param p 
     */
    public NodoDoble(T p){
        next=null;
        dato = p;
    }
 
    /**
     * Le pasamos un dato y su siguiente nodo al nodo
     * @param t Dato a insertar
     * @param next Su sisguiente nodo
     * @param next Su sisguiente nodo
     */
    public NodoDoble(T t, NodoDoble<T> next){
        this.next=next;
        dato = t;
    }
     
    public T getDato() {
        return dato;
    }
 
    public void setDato(T dato) {
        this.dato = dato;
    }
    
    public NodoDoble<T> getBefore() {
    	return before;
    }
 
    public NodoDoble<T> getNext() {
        return next;
    }
    
    public void setNext(NodoDoble<T> next) {
        this.next = next;
    }
}
