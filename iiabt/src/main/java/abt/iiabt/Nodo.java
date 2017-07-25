package abt.iiabt;

public class Nodo<T> {
	private T dato;
    private Nodo<T> next;
 
    /**
     * Constructor por defecto
     */
    public Nodo(){
        next=null;
     }
 
    /**
     * Le pasamos un dato al nodo
     * @param p 
     */
    public Nodo(T p){
        next=null;
        dato = p;
    }
 
    /**
     * Le pasamos un dato y su siguiente nodo al nodo
     * @param t Dato a insertar
     * @param next Su sisguiente nodo
     */
    public Nodo(T t, Nodo<T> next){
        this.next=next;
        dato = t;
    }
     
    public T getDato() {
        return dato;
    }
 
    public void setDato(T dato) {
        this.dato = dato;
    }
 
    public Nodo<T> getNext() {
        return next;
    }
 
    public void setNext(Nodo<T> next) {
        this.next = next;
    }
}
