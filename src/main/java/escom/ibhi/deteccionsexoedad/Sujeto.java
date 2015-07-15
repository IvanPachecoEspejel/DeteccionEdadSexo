/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ibhi.deteccionsexoedad;

import escom.ibhi.resource.Utileria.Util;

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
        double desviacion = (double)Util.getPropCfgEE("VAL_MAX_SUJ") - (double)Util.getPropCfgEE("VAL_MIN_SUJ");
        desviacion /= 2.0;
        double media = (double)Util.getPropCfgEE("VAL_MIN_SUJ") + desviacion;
        if(opCrom == 1)
            for(double gen : cromosoma){
                gen = Util.rand_DN(media, desviacion);
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
    
}
