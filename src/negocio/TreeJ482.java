/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.trees.J48;
import weka.core.Instances;

/**
 *
 * @author Juan Torrez
 * @param <T>
 */
public class TreeJ482<T extends Comparable<T>> {

    protected NodoMVias<T> raiz;

    public TreeJ482() {
        
    }
    public boolean insertarNodo(int orden, T dato,String rela[],String nro) {
        NodoMVias<T> newNodo = new NodoMVias<>(orden,dato,rela);
        if(this.isEmpty()){
            this.raiz = newNodo; 
            return true;
        }
        Queue<NodoMVias<T>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);
        
        while (!colaDeNodos.isEmpty()) {
            
            NodoMVias<T> nodoActual = colaDeNodos.poll();
            if (nodoActual.esHoja() || estaEnLaListaDeRelacion(nodoActual,nro)) {
           
                if (this.obtenerPosicionPorDondeBaja(nodoActual,nro) >= 0 && estaEnLaListaDeRelacion(nodoActual,nro)) {
                    nodoActual.setHijo(obtenerPosicionPorDondeBaja(nodoActual,nro), newNodo);
                    break;
                }
            }
            int n = nodoActual.cantidadDeHijosNoVacios();
            for (int i = 0; i < n; i++) {
                if (!nodoActual.esHijoVacio(i)) {
                    colaDeNodos.offer(nodoActual.getHijo(i));
                }
            }
        } //fin del while
        return true;
    }
    
    public boolean validarCredito(HashMap<String, String> hasp){
        NodoMVias<T> nodoAux = this.raiz; boolean salida = false;
        while(!nodoAux.esHoja()){
            ObjWeka o =  (ObjWeka)nodoAux.getDato(0);
            String valor = o.getValue();
            String relacionHasp = hasp.get(valor);
            int pos = obtenerPosicionPorDondeBajar(nodoAux,relacionHasp);
            
            nodoAux = nodoAux.getHijo(pos);
            
            if(nodoAux.esHoja()){
              o =  (ObjWeka)nodoAux.getDato(0);
              if( o.getValue().contains("good"))
                  salida= true;
            }
            
        }
        return salida;
    }
    public HashMap<String, String> cargarDatos(Instances datos ,List<String> value){ // 
        HashMap<String, String> hasp = new HashMap<>(); int len = datos.numAttributes() - 1;
        
        for (int i = 0; i < len; i++) {
            hasp.put(datos.attribute(i).name(),value.get(i));   
//            hasp.put(datos.attribute(i).name(),value[i]);
        }
        
        return hasp;
    }
    
    public boolean addNewInstace(List<String> datos){//String[] datos
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src//datos//test.arff"));
            String toWrite = "";
            String line = null;
            while ((line = reader.readLine()) != null) {
                toWrite += line +"\n";
            }
            String instace ="";
            for (int i = 0; i < datos.size(); i++) {
//                instace += datos[i]+ ","; 
                if(datos.get(i).contains(" ")){
                    instace += "\'"+datos.get(i)+"\'"+ ","; 
                }else{
                    instace += datos.get(i)+ ","; 
                }
                
            }
            reader.close();
            // borro lo que habia
            
            BufferedWriter bw = new BufferedWriter( new FileWriter("src//datos//test.arff"));
            bw.write("");
            bw.close();
           
            instace = instace.substring(0, instace.length()-1);
            toWrite = toWrite + instace+"\n";
            
            FileWriter fw = new FileWriter("src//datos//test.arff",true);
            fw.write(toWrite);
            fw.close();
            
        } catch (IOException e) {
            
            System.out.println("error");
            return false;
        }
        
        return true;
    
    }
    
    public void algoritmoJ48(J48 cls,Instances data){
        
        try {
            data.setClassIndex(data.numAttributes() - 1);
            cls.buildClassifier(data);
            String arbol = cls.graph();
            System.out.println(arbol);
            List<String> l = new ArrayList<>();
            l = TreeJ482.getNodosTree(arbol);

        //     String[] n = t.getRelaciones("N0",l);
        // algoritmo que inserta los nodos
            String value,nro,cadena = "", relacion[];int i=0, orden=0, g=0; 

            do{ 
                cadena = l.remove(0);
                nro = cadena.substring(0, cadena.indexOf('=')-1); // N0
                value = cadena.substring(cadena.indexOf('=')+1); // checking_status
                if( !value.contains("good") && !value.contains("bad") ){
                    i++;
                }

                orden = getNroOrden(nro,l);
                relacion = new String[orden];
                relacion = getRelaciones(nro,l);
                orden = (orden == 0)? 2 : orden;
                ObjWeka o = new ObjWeka(nro, value);

                insertarNodo(orden, (T) o, relacion, nro);
//                System.out.println("orden: "+orden + "  nro:"+ nro+"  value:"+ value);
            }while( l.size() > 0);
        
        } catch (Exception ex) {
            System.out.println("error en el algoritmo");
            Logger.getLogger(TreeJ482.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public static void main(String Arg[]) throws FileNotFoundException, IOException, Exception{
         J48 cls = new J48(); String ruta =  "src//datos//test.arff";
     Instances data = new Instances(new BufferedReader(new FileReader(ruta)));
       
        TreeJ482<ObjWeka> t = new TreeJ482<>();
        t.algoritmoJ48(cls,data);
        
        HashMap<String, String> hasp = new HashMap<>();
        String[] Datos = new String[21];

        Datos[0] = "no checking";
        Datos[1] = "12";
        Datos[2] = "existing paid";
        Datos[3] = "radio/tv";
        Datos[4] = "5650";
        Datos[5] = "<100";
        Datos[6] = "1<=X<4";
        Datos[7] = "4";
        Datos[8] = "male single";
        Datos[9] = "none";
        Datos[10] = "4";
        Datos[11] = "car";
        Datos[12] = "38";
        Datos[13] = "none";
        Datos[14] = "own";
        Datos[15] = "1";
        Datos[16] = "skilled";
        Datos[17] = "1";
        Datos[18] = "none";
        Datos[19] = "yes";
        Datos[20] = "good";
//        t.addNewInstace(Datos);

//        hasp = t.cargarDatos(data, Datos);
//        boolean b = t.validarCredito(hasp);
////        System.out.println("Salida: "+ b);
//
////            
//        for (String string : l) {
//            System.out.println(string);
//        }
//           
        // codigo para agregar un linea al archivo .arff
//        BufferedReader reader = new BufferedReader(new FileReader("instances.txt"));
//        String toWrite = "";
//        String line = null;
//        while ((line = reader.readLine()) != null) {
//            toWrite += line;
//        }
//        FileWriter fw = new FileWriter("testinstances.arff",true);
//        fw.write(toWrite);
//        fw.close();
           
          
        
      
    }

    private boolean estaEnLaListaDeRelacion(NodoMVias<T> nodoActual, String nro) {
        int orden = nodoActual.getLenRelaciones();
        String cad1;
        boolean salida = false;
        for (int i = 0; i < orden && !salida; i++) {
            cad1 = nodoActual.getRelacion(i).substring(0, nodoActual.getRelacion(i).indexOf('=')-1);
            if(cad1.equals(nro)){
                salida=true;
            }
            
        }
        return salida;
    }
     public int obtenerPosicionPorDondeBaja(NodoMVias<T> root,String nro){
        int orden = root.getLenRelaciones();
        return obtenerPosicionDeNROenListRelation(orden,root,nro);
    }
     
    private int obtenerPosicionDeNROenListRelation(int orden, NodoMVias<T> root, String nro) {
        int pos =-1;
        for (int i = 0; i < orden; i++) {
            if(root.getRelacion(i).contains(nro)){
                pos= i;
                break;
            }
            
        }
        return pos;
    }
    public int obtenerPosicionPorDondeBaja(NodoMVias<T> root,String nro,int pos){
        int n = root.cantidadDeHijosNoVacios(); int resul=0;
        for (int i = pos; i < n; i++) {
            if( !root.esHijoVacio(i)){
                resul=i;
            }
        }
        return resul;
    }
    
    private String obtenerNroNodo(String cadena){
        return cadena.substring(cadena.indexOf('>')+1,cadena.indexOf('='));
    }
    
    public int getNroOrden(String nodo, List<String> lista){
        String nodo2 = nodo + "->";
        int contador =0;
        for (String string : lista) {
            if(string.contains(nodo2)){
                contador++;
            }
        }
        return contador;
    }
    
    public String[] getRelaciones(String nodo, List<String> lista){
        String value,nodo2 = nodo + "->";
        String vect[] = new String[this.getNroOrden(nodo,lista)];
        int contador =0; int i =0; String rela[] = new String[this.getNroOrden(nodo,lista)];
        for (String string : lista) {
            if(string.contains(nodo2)){
                value = string.substring(string.indexOf('='));
                value = obtenerNroNodo(string)+ value;
                vect[i] = value;
                rela[i] = string;
                i++;
            }
        }
        for (int j = 0; j < rela.length; j++) {
            lista.remove(rela[j]);
        }
        return vect;
    }
    public boolean esRelacion(String cadena){
        return cadena.indexOf("->") > 0;
    }
    public static List<String> getNodosTree(String s){
        String  cadena = s.substring(s.indexOf("\n")+1, s.length()-2);
//        cadena = cadena.replace(" ", ""); 
        cadena = cadena.replace("\"]", ""); cadena = cadena.replace(" ]", "");
        cadena = cadena.replace("[label", ""); cadena = cadena.replace("\"", "");
        cadena = cadena.replace("shape=box style=filled","");
//        System.out.println(cadena);
        List<String> l = new ArrayList<>();
        int cantSaltLine = countLines(cadena);
        for (int i = 0; i < cantSaltLine; i++) {
            String x = cadena.substring(0, cadena.indexOf("\n"));
            cadena = cadena.substring(x.length()+1, cadena.length());
            l.add(x);
        }
        return l;
    } 
    private static int countLines(String str) {
        int count = 0;
        int total = str.length();
        for (int i = 0; i < total; ++i) {
            char letter = str.charAt(i);
            if (letter ==  '\n') ++count; 
        }
        return count;
    }
    public boolean isEmpty(){
        return this.raiz == NodoMVias.nodoVacio();
    }

    private int obtenerPosicionPorDondeBajar(NodoMVias<T> nodoAux, String relacionHasp) {
        int len = nodoAux.getLenRelaciones(); int pos =-1; boolean esNumero= this.isNumeric(relacionHasp);
        
        for (int i = 0; i < len; i++) {
            if(!esNumero){
                if(nodoAux.getRelacion(i).contains(relacionHasp)){
                   pos = i; i= len;
                }
            }else{
                String cadena = (String) nodoAux.getRelacion(i).trim().replace("=","");
                cadena =cadena.replace(" ", "");
                cadena = cadena.substring(cadena.indexOf("<")+1);
                Integer nroEntrada = Integer.parseInt(relacionHasp);
                Integer nroArbol = Integer.parseInt(cadena);
                
                if(nroEntrada >  nroArbol){
                    pos= i+1;
                }else{
                    pos=i;    
                }
                
                i=len;
            }
            
            
        }
        return pos;
    }
    
    private boolean isNumeric(String cadena){
	try {
		Integer.parseInt(cadena);
		return true;
	} catch (NumberFormatException nfe){
		return false;
	}
    }
    
}
