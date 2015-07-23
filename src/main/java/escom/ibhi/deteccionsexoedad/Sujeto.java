/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ibhi.deteccionsexoedad;

import escom.ibhi.resource.Utileria.Util;
import java.util.Arrays;

/**
 *
 * @author ivan
 */
public class Sujeto {
    
    private final double[] cromosoma;
    private double error;
    private double preAptitud;
    
    public Sujeto(double[] cromosoma){
        this.cromosoma = new double[cromosoma.length];
        System.arraycopy(cromosoma, 0, this.cromosoma, 0, cromosoma.length);
        error = 0;
    }
    
    public Sujeto(int tamSujeto, int opCrom){
        cromosoma = new double[tamSujeto];
        if(opCrom == 1){
            double desviacion = Util.getPropCfgEEDouble("VAL_MAX_SUJ") - 
                                Util.getPropCfgEEDouble("VAL_MIN_SUJ");
            desviacion /= 2.0;
            double media = Util.getPropCfgEEDouble("VAL_MIN_SUJ") + desviacion;
            for(int i = 0; i < tamSujeto; i++){
                cromosoma[i] = Util.rand_DN(media, desviacion);
            }
        }
            
        error = 0;
    }
    
    public Sujeto cruzar(Sujeto pareja, int k){
        Sujeto hijo = new Sujeto(getCromosoma().length, 0);
        System.arraycopy(getCromosoma(), 0, hijo.getCromosoma(), 0, k);
        System.arraycopy(pareja.getCromosoma(), k, hijo.getCromosoma(), k, getCromosoma().length-k);
        return hijo;
    }
    
    public void mutar(double probMut, double alpha){
        for(int i = 0; i<cromosoma.length; i++){
            if(Util.rand_DN(0, 1) < probMut)
                setGenAt(getGetAt(i) + alpha*Util.rand_DN(0, 1), i);
        }
    }
    
    public void copiar(Sujeto mejSuj){
        setCromosoma(mejSuj.getCromosoma());
        error = mejSuj.getError();
    }
    
    public void setGenAt(double gen, int indexGen){
        cromosoma[indexGen] = gen;
    }
    
    public double getGetAt(int indexGen){
        return cromosoma[indexGen];
                
    }

    /**
     * @return the cromosoma
     */
    public double[] getCromosoma() {
        return cromosoma;
    }

    /**
     * @param cromosoma the cromosoma to set
     */
    public void setCromosoma(double[] cromosoma) {
        System.arraycopy(cromosoma, 0, this.cromosoma, 0, cromosoma.length);
    }

    /**
     * @return the error
     */
    public double getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(double error) {
        this.error = error;
        this.preAptitud = -error;
    }
    
    public double getPreAptitud(){
        return preAptitud;
    }
    
    @Override
    public String toString(){
        return Arrays.toString(cromosoma) + " -> Error: "+error;
    }
    
}
