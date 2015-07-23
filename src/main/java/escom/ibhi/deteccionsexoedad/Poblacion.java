/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ibhi.deteccionsexoedad;

import escom.ibhi.resource.Utileria.Util;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 *
 * @author ivan
 */
public class Poblacion {
    
    private static final Logger log = Logger.getLogger(Poblacion.class.getName());
    
    private int tamPob;
    private final int tamSuj;
    
    private final Sujeto []sujetos;
    private final double []alphas;
    private final int []ks;
    private final int []exitosAlphas;
    private final int []iteracionesAlphas;
    private final int []exitosKs;
    private final int []iteracionesKs;
    private Sujeto mejor;
    private Sujeto peor;
    

    public Poblacion(int tamPoblacion, int tamSujeto) {
        this.tamPob = tamPoblacion;
        this.tamSuj = tamSujeto;
        sujetos = new Sujeto[tamPoblacion];
        alphas = new double[tamPoblacion];
        exitosAlphas = new int[tamPoblacion];
        iteracionesAlphas = new int[tamPoblacion];
        exitosKs = new int[tamPoblacion];
        iteracionesKs = new int[tamPoblacion];
        ks = new int[tamPoblacion];
        mejor = null;
        for(int i = 0; i< tamPoblacion; i++){
            sujetos[i] = new Sujeto(tamSujeto, 1);
            alphas[i] = Util.alpha;
            ks[i] = Util.gamma;
            ks[i] = Util.randint_N_M(0, tamPob-1);
            exitosAlphas[i] = 0;
            iteracionesAlphas[i] = 0;
            exitosKs[i] = 0;
            iteracionesKs[i] = 0;
        }
    }
    
    public Sujeto cruzarSuejtos(int indexSujeto){
        return sujetos[indexSujeto].cruzar(sujetos[seleccionRuleta()], ks[indexSujeto]);
    }
    
    public int seleccionRuleta(){
        double sumaApt = 0;
        double aptAcu = 0;
        int indexSujeto;
        for(Sujeto s : sujetos){
            sumaApt += getAptitudDe(s);
        }
        double propSelec = Util.rand_N_M(0, sumaApt);
        for(indexSujeto = 0; indexSujeto < sujetos.length; indexSujeto++){
            aptAcu += getAptitudDe(sujetos[indexSujeto]);
            if(aptAcu >= propSelec)
                return indexSujeto;
        }
        return -1;
    }
    
    public double getAptitudDe(Sujeto s){
        return s.getPreAptitud() + peor.getError();
    }
    
    public void modificaAlpha(int indexSujeto){
        if(alphas[indexSujeto]<(Util.alphaMin)){
            System.out.println("~~~~~~ Reset ALPHA ~~~~~~");
            alphas[indexSujeto] = Util.alpha;
        }else if(alphas[indexSujeto]> (Util.alphaMax)){
            System.out.println("~~~~~~ Reset ALPHA ~~~~~~");
            alphas[indexSujeto] = Util.alpha;
        }else{
            double relacionExito = exitosAlphas[indexSujeto]/iteracionesAlphas[indexSujeto];
            if(relacionExito < Util.alphaC){
                System.out.println("****** ALPHA ******");
                System.out.println("relacionExito: "+ relacionExito);
                System.out.println("ALPHA: "+ alphas[indexSujeto]);
                alphas[indexSujeto] *= Util.alphaMul;
                System.out.println("****** ALPHA ******");
            }else if(relacionExito > Util.alphaC){
                System.out.println("////// ALPHA //////");
                System.out.println("relacionExito: "+ relacionExito);
                System.out.println("ALPHA: "+ alphas[indexSujeto]);
                alphas[indexSujeto] /= Util.alphaMul;
                System.out.println("////// ALPHA //////");
            }
        }
        System.out.println("Nvo ALPHA: "+alphas[indexSujeto]);
        exitosAlphas[indexSujeto] = 0;
        iteracionesAlphas[indexSujeto] = 0;
    }
    
    public void modificaGamma(int indexSujeto){
        double relacionExito = exitosKs[indexSujeto]/iteracionesKs[indexSujeto];
        if(relacionExito < Util.gammaC){
            System.out.println("****** GAMMA ******");
            System.out.println("relacionExito: "+ relacionExito);
            System.out.println("GAMMA: "+ ks[indexSujeto]);
            ks[indexSujeto] += Util.gammaSum;
            if(ks[indexSujeto] >= tamSuj)
                ks[indexSujeto] = tamSuj-1;
            System.out.println("****** GAMMA ******");
        }else if(relacionExito > Util.gammaC){
            System.out.println("////// GAMMA //////");
            System.out.println("relacionExito: "+ relacionExito);
            System.out.println("GAMMA: "+ ks[indexSujeto]);
            ks[indexSujeto] -= Util.gammaRes;
            if(ks[indexSujeto] < 0)
                ks[indexSujeto] = 0;
            System.out.println("////// GAMMA //////");
        }
        System.out.println("Nvo K: "+ks[indexSujeto]);
        exitosKs[indexSujeto] = 0;
        iteracionesKs[indexSujeto] = 0;
    }
    
    public Sujeto recolectarConocimeintoColectivo(){
        double conCol[] = new double[tamSuj];
        Arrays.fill(conCol, 0);
        try{
            for(int i = 2; i<tamPob; i++){
                Util.sumarArreglosDouble(conCol,sujetos[i].getCromosoma());
            }
            Util.multArregloEscalar(conCol, tamPob);
        }catch(Exception e){
            Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
        }
        Sujeto s = new Sujeto(conCol);
        return s;
    }
    
    public void conocimeintoColectivoExitoso(Sujeto mejSuj){
        System.out.println("*************Resulto*************");
        for(Sujeto s : sujetos){
            s.copiar(mejSuj);
        }
    }
    
    public void resetAlpha(double alpha){
        alpha = Util.alpha;
    }
    
    public void addExitoAt(int indexSujeto){
        exitosAlphas[indexSujeto] = exitosAlphas[indexSujeto]+1;
        exitosKs[indexSujeto] = exitosKs[indexSujeto]+1;
    }
    
    public void addIteracionAt(int indexSujeto){
        iteracionesAlphas[indexSujeto] = iteracionesAlphas[indexSujeto] +1;
        iteracionesKs[indexSujeto] = iteracionesKs[indexSujeto] +1;
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
        System.arraycopy(sujetos, 0, this.sujetos, 0, tamPob);
    }
    
    public void setSujetoAt(Sujeto sujeto, int posicion){
        sujetos[posicion] = sujeto;
        setMejor(sujeto);
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

    /**
     * @return the mejor
     */
    public Sujeto getMejor() {
        return mejor;
    }
    
    public void setMejor(Sujeto sujeto){
        if(mejor == null || sujeto.getError()< mejor.getError() )
            mejor = sujeto;
        if(peor == null || sujeto.getError() > peor.getError())
            peor = sujeto;
    }
    
    @Override
    public String toString(){
        String str = "";
        for(Sujeto s :sujetos)
            str += s.toString() +"\n";
        return str;
    }
    
}
