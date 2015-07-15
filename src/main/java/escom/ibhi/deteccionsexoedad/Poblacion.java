/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ibhi.deteccionsexoedad;

import escom.ibhi.resource.Utileria.Util;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ivan
 */
public class Poblacion {
    
    private static final Logger log = Logger.getLogger(Poblacion.class.getName());
    
    private int tamPob;
    private final int tamSuj;
    
    private Sujeto []sujetos;
    private final Double []alphas;
    private final int []exitos;
    private final int []iteraciones;
    private Sujeto mejor;
    

    public Poblacion(int tamPoblacion, int tamSujeto) {
        this.tamPob = tamPoblacion;
        this.tamSuj = tamSujeto;
        sujetos = new Sujeto[tamPoblacion];
        alphas = new Double[tamPoblacion];
        exitos = new int[tamPoblacion];
        iteraciones = new int[tamPoblacion];
        mejor = null;
        for(int i = 0; i< tamPoblacion; i++){
            sujetos[i] = new Sujeto(tamSujeto, 1);
            alphas[i] = Util.alpha;
            exitos[i] = 0;
            iteraciones[i] = 0;
        }
    }
    
    public Sujeto mutaSujeto(int indexSujeto){
        Sujeto sujetoMRes = new Sujeto(tamSuj, 0);  //Sujeto Mutado
        Sujeto sujAMut = sujetos[indexSujeto];
        for(int i = 0; i<tamSuj; i++){
            sujetoMRes.setGetAt(sujAMut.getGetAt(i) + alphas[i]*Util.rand_DN(0, 1), 1);
        }
        return sujetoMRes;
    }
    
    public void modificaAlpha(int indexSujeto){
        if(alphas[indexSujeto].compareTo(Util.alphaMin) < 0 ){
            log.log(Level.INFO, "~~~~~~ Reset ALPHA ~~~~~~");
            resetAlpha(alphas[indexSujeto]);
        }else if(alphas[indexSujeto].compareTo(Util.alphaMax) > 0 ){
            log.log(Level.INFO, "~~~~~~ Reset ALPHA ~~~~~~");
            resetAlpha(alphas[indexSujeto]);
        }else{
            double relacionExito = exitos[indexSujeto]/iteraciones[indexSujeto];
            if(relacionExito < Util.alphaC){
                log.log(Level.INFO,"****** ALPHA ******");
                log.log(Level.INFO, "relacionExito: {0}", relacionExito);
                log.log(Level.INFO, "ALPHA: {0}", alphas[indexSujeto]);
                alphas[indexSujeto] *= Util.alphaMul;
                log.log(Level.INFO,"****** ALPHA ******");
            }else if(relacionExito > Util.alphaC){
                log.log(Level.INFO,"////// ALPHA //////");
                log.log(Level.INFO, "relacionExito: {0}", relacionExito);
                log.log(Level.INFO, "ALPHA: {0}", alphas[indexSujeto]);
                alphas[indexSujeto] /= Util.alphaMul;
                log.log(Level.INFO,"////// ALPHA //////");
            }
        }
        log.log(Level.INFO, "Nvo ALPHA: {0}", alphas[indexSujeto]);
    }
    
    
    public void resetAlpha(Double alpha){
        alpha = Util.alpha;
    }
    
    public void addExitoAt(int indexSujeto){
        exitos[indexSujeto]++;
    }
    
    public void addIteracionAt(int indexSujeto){
        iteraciones[indexSujeto]++;
    }
    
    public Sujeto getSujetoAt(int indexSujeto){
        return sujetos[indexSujeto];
    }

    /**
     * @return the sujetos
     */
    public Sujeto[] getSujetos() {
        return sujetos;
    }

    /**
     * @param sujetos the sujetos to set
     */
    public void setSujetos(Sujeto[] sujetos) {
        this.sujetos = sujetos;
    }
    
    public void setSujetoAt(Sujeto sujeto, int posicion){
        sujetos[posicion] = sujeto;
        if(mejor == null || sujeto.getError()< mejor.getError() )
            mejor = sujeto;
    }

    /**
     * @return the tamPob
     */
    public int getTamPob() {
        return tamPob;
    }

    /**
     * @param tamPob the tamPob to set
     */
    public void setTamPob(int tamPob) {
        this.tamPob = tamPob;
    }
    
}
