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
    
    private double[] cromosoma;
    private double error;
    
    public Sujeto(double[] cromosoma){
        this.cromosoma = cromosoma;
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
    
    public void setGetAt(double gen, int indexGen){
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
        this.cromosoma = cromosoma;
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
    }
    
    @Override
    public String toString(){
        return Arrays.toString(cromosoma) + " -> Error: "+error;
    }
    
}
