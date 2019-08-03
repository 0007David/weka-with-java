/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;


/**
 *
 * @author David Torrez
 */
public class ObjWeka implements Comparable< ObjWeka> {
    private String nro;
    private String value;

    public ObjWeka(String nro,String value) {
        this.nro = nro;
        this.value = value;
        
    }

    public String getNro() {
        return nro;
    }
    

    public void setNro(String nro) {
        this.nro = nro;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    


    @Override
    public String toString() {
        return "Object{ \n" + "nro=" + nro + ", value=" + value +"\n }";
    }

    

    @Override
    public int compareTo(ObjWeka o) {
        return this.value.compareTo(o.value);
    }
   
    
}
