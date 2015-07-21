/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ibhi.deteccionsexoedad;


import java.io.File;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.persist.EncogDirectoryPersistence;

/**
 *
 * @author ivan
 */
public class RNEvalutiva {
    
    private BasicNetwork n;
    private EntrenamientoEvolutivo ee;
    private MLDataSet traningSet;
    
    private String nomRN;
    private final int numNeuEnt;
    private final int numCapOcu;
    private final int[] numNeuPCapOcu;
    private final int numNeuSal;
    private double errMin;
    
    private int frecGuar;
    private int frecMonErr;
    
    
    public RNEvalutiva(String nombre, int numNeuronasEntrada, int numCapasOcultas,
            int []numeroNuerunasPorCapaOculta, int numNeuronasSalida, 
            int frecuencaRespaldoRN, int frecuenciaMonitoreoError, double errorMinimo){
        
        this.nomRN = nombre;
        this.numNeuEnt = numNeuronasEntrada;
        this.numCapOcu = numCapasOcultas;
        this.numNeuPCapOcu = numeroNuerunasPorCapaOculta;
        this.numNeuSal = numNeuronasSalida;
        this.frecGuar = frecuencaRespaldoRN;
        this.frecMonErr = frecuenciaMonitoreoError;        
        this.errMin = errorMinimo;
        System.out.println("Error minimo: "+errMin);
        
        initRN();
        
        
    }
    
    public final void initRN(){
        n = new BasicNetwork();
        getN().addLayer(new BasicLayer(null, true, getNumNeuEnt()));
        for(int i = 0; i < getNumCapOcu(); i++){
            getN().addLayer(new BasicLayer(new ActivationSigmoid(),
                    true, 
                    getNumNeuPCapOcu()[i]));
        }
        getN().addLayer(new BasicLayer(new ActivationSigmoid(), false, getNumNeuSal()));
        getN().getStructure().finalizeStructure();
        getN().reset();
    }
    
    public final void initEntrenamiento(double[][] entradas, double[][] salidasIdeales){
        traningSet = new BasicMLDataSet(entradas, salidasIdeales);
        ee =  new EntrenamientoEvolutivo(getN(), getTraningSet());
    }
    
    public void entrenar(){
        if(ee == null){
            System.err.println("Antes de entrenar la red neuronal se debe de inicializar los valores de entrada (initEntrenamiento())");
        }
        int iter = 1 ;
        do {
            getEe().iteration();
            if(iter % getFrecMonErr() == 0)
                System.out.println("Iter #"+iter+" Error: "+getEe().getError());
            iter++;
            if(iter % getFrecGuar() == 0){
                guardarRN(System.getProperty("user.dir")+System.getProperty("file.separator")+"Entrenamiento"+System.getProperty("file.separator")+nomRN+"_"+iter+".eg");
            }
        } while(getEe().getError() > getErrMin() );
        System.out.println("Iter: "+iter+" Mejor solucion: "+getEe().getPob().getMejor().toString());
    }
    
    public MLData clasificar(double[] in){
        MLData entrada = new BasicMLData(in);
        return getN().compute(entrada);
    }
    
    public void guardarRN(String pat){
        pat = pat.trim();
        int aux;
        if(!pat.endsWith(".eg")){
            if((aux = pat.lastIndexOf(System.getProperty("file.separator"))) > 0)
                pat = pat.substring(0, aux);
            pat += System.getProperty("file.separator")+nomRN+".eg";    
        }
        getN().getFlat().setWeights(getEe().getPob().getMejor().getCromosoma());
        EncogDirectoryPersistence.saveObject(new File( pat ) , getN()) ;
    }
    
    public void cargarRN(String pat){
        pat = pat.trim();
        if(!pat.endsWith(".eg")){
            System.err.println("Error archivo no reconocido");
        }
        n = (BasicNetwork)EncogDirectoryPersistence.loadObject(new File(pat));
    }

    /**
     * @return the n
     */
    public BasicNetwork getN() {
        return n;
    }

    /**
     * @return the ee
     */
    public EntrenamientoEvolutivo getEe() {
        return ee;
    }

    /**
     * @return the traningSet
     */
    public MLDataSet getTraningSet() {
        return traningSet;
    }

    /**
     * @return the nomRN
     */
    public String getNomRN() {
        return nomRN;
    }

    /**
     * @return the numNeuEnt
     */
    public int getNumNeuEnt() {
        return numNeuEnt;
    }

    /**
     * @return the numCapOcu
     */
    public int getNumCapOcu() {
        return numCapOcu;
    }

    /**
     * @return the numNeuPCapOcu
     */
    public int[] getNumNeuPCapOcu() {
        return numNeuPCapOcu;
    }

    /**
     * @return the numNeuSal
     */
    public int getNumNeuSal() {
        return numNeuSal;
    }

    /**
     * @return the frecGuar
     */
    public int getFrecGuar() {
        return frecGuar;
    }

    /**
     * @param nomRN the nomRN to set
     */
    public void setNomRN(String nomRN) {
        this.nomRN = nomRN;
    }

    /**
     * @param frecGuar the frecGuar to set
     */
    public void setFrecGuar(int frecGuar) {
        this.frecGuar = frecGuar;
    }

    /**
     * @return the errMin
     */
    public double getErrMin() {
        return errMin;
    }

    /**
     * @param errMin the errMin to set
     */
    public void setErrMin(double errMin) {
        this.errMin = errMin;
    }

    /**
     * @return the frecMonErr
     */
    public int getFrecMonErr() {
        return frecMonErr;
    }

    /**
     * @param frecMonErr the frecMonErr to set
     */
    public void setFrecMonErr(int frecMonErr) {
        this.frecMonErr = frecMonErr;
    }
}
