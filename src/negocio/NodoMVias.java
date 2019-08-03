/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 * @param <T>
 */
public class NodoMVias<T>{

    private final List<T> listaDeDatos;
    private final List< NodoMVias<T>> listaDeHijos;
    private final List<String> listaRelacion;
    private int orden;

    public NodoMVias(int orden,String[] rela) {
        this.orden = orden;
        listaDeDatos = new ArrayList<>();
        listaDeHijos = new ArrayList<>();
        listaRelacion = new ArrayList<>();
        for (int i = 0; i < orden; i++) {
            if (i < (orden - 1)) {
                this.listaDeDatos.add((T) NodoMVias.datoVacio());
            }
            this.listaDeHijos.add(NodoMVias.nodoVacio());
            if(rela.length > 0){
                this.listaRelacion.add(rela[i]);
            }
        }
    }

    public NodoMVias(int orden, T primerDato, String[] rela) {
        this(orden,rela);
        this.listaDeDatos.set(0, primerDato);
    }

    /**
     * Retorna el dato de una posicion en la lista de dato. PRE: La posicion es
     * valida
     *
     * @param posicion
     * @return Un dato de la lista de datos
     */
    public T getDato(int posicion) {
        return this.listaDeDatos.get(posicion);
    }
    

    public void setDato(int posicion, T unDato) {
        this.listaDeDatos.set(posicion, unDato);
    }

    public void setDatoVacio(int posicion) {
        this.listaDeDatos.set(posicion, (T) NodoMVias.datoVacio());
    }

    public boolean esDatoVacio(int posicion) {
        return this.listaDeDatos.get(posicion) == NodoMVias.datoVacio();
    }

    public NodoMVias<T> getHijo(int posicion) {
        return this.listaDeHijos.get(posicion);
    }
    
     public String getRelacion(int posicion) {
        return this.listaRelacion.get(posicion);
    }

    public void setHijo(int posicion, NodoMVias<T> unHijo) {
        this.listaDeHijos.set(posicion, unHijo);
    }

    public boolean esHijoVacio(int posicion) {
        return this.listaDeHijos.get(posicion) == NodoMVias.nodoVacio();
    }
    public int getOrden(){
        return this.orden;
    }

    //metodos compartidos por todas las clases (static) 
    //por lo tanto no pueden ser genericos
    public static NodoMVias nodoVacio() {
        return null;
    }

    public static Object datoVacio() {
        return null;
    }

    public static boolean esNodoVacio(NodoMVias nodoActual){
        return NodoMVias.nodoVacio() == nodoActual;
    }
    
    //** metodos adicionales**//
    public boolean esHoja() {
        
        return 0 == listaRelacion.size();
    }

    public boolean estanLlenosTodosLosDatos() {
        for (int i = 0; i < this.listaDeDatos.size(); i++) {
            if (this.esDatoVacio(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean estanTodosLosDatosVacios() {
        for (int i = 0; i < this.listaDeDatos.size(); i++) {
            if (!this.esDatoVacio(i)) {
                return false;
            }
        }
        return true;
    }

    public int cantidadDeDatosNoVacios() {
        int cantidadDeDatos = 0;
        for (int i = 0; i < this.listaDeDatos.size(); i++) {
            if (!this.esDatoVacio(i)) {
                cantidadDeDatos++;
            }
        }
        return cantidadDeDatos;
    }

    public int cantidadDeHijosNoVacios() {
        int cantidadDeHijos = 0;
        for (int i = 0; i < this.listaDeHijos.size(); i++) {
            if (!this.esHijoVacio(i)) {
                cantidadDeHijos++;
            }
        }
        return cantidadDeHijos;
    }
    public int getLenRelaciones(){
        return this.listaRelacion.size();
    }

    @Override
    public String toString() {
        return "NodoMVias{" + "listaDeDatos=" + listaDeDatos + '}';
    }

    
}
