/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ibhi.deteccionsexoedad;

import java.awt.Image;
import java.io.File;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.train.strategy.ResetStrategy;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.platformspecific.j2se.data.image.ImageMLData;
import org.encog.platformspecific.j2se.data.image.ImageMLDataSet;
import org.encog.util.downsample.Downsample;
import org.encog.util.downsample.SimpleIntensityDownsample;
import org.encog.util.simple.EncogUtility;

/**
 *
 * @author ivan
 */
public class RNEvalutiva {

    private BasicNetwork n;
    private EntrenamientoEvolutivo ee;
    private ResilientPropagation eResilentPropagation;
    private final ImageMLDataSet traningSet;
    private final Downsample downsample;
    private final int escalamientoAlto;
    private final int escalamientoAncho;

    private String nomRN;
    private final int numNeuCapOcu1;
    private final int numNeuCapOcu2;
    private double errMin;

    private int frecGuar;
    private int frecMonErr;

    public RNEvalutiva(String nombre,
            int numNeuronasCapaOculta1,
            int numNeuronasCapaOculta2,
            int frecuencaRespaldoRN,
            int frecuenciaMonitoreoError,
            double errorMinimo,
            int escalamientoNormalizadoAlto,
            int escalamientoNormalizadoAncho) {

        this.nomRN = nombre;
        this.numNeuCapOcu1 = numNeuronasCapaOculta1;
        this.numNeuCapOcu2 = numNeuronasCapaOculta2;
        this.frecGuar = frecuencaRespaldoRN;
        this.frecMonErr = frecuenciaMonitoreoError;
        this.errMin = errorMinimo;
        this.escalamientoAlto = escalamientoNormalizadoAlto;
        this.escalamientoAncho = escalamientoNormalizadoAncho;
        downsample = new SimpleIntensityDownsample();
        traningSet = new ImageMLDataSet(downsample, false, 1, 0);
    }

    public void initRN() {
        n = EncogUtility.simpleFeedForward(traningSet.getInputSize(),
                numNeuCapOcu1,
                numNeuCapOcu2,
                this.traningSet.getIdealSize(),
                true);
    }

    public final void initEntrenamiento() {
        ee = new EntrenamientoEvolutivo(getN(), getTraningSet());
    }
    public final void initEntrenamientoResilentPropagation() {
        eResilentPropagation = new ResilientPropagation(getN(), traningSet);
        eResilentPropagation.addStrategy(new ResetStrategy(0.25, 50));
    }

    public void addEntrada(Image img, MLData salidaIdeal) {
        traningSet.add(new ImageMLData(img), salidaIdeal);
    }

    public void finalizarProcesoEntrada() {
        traningSet.downsample(escalamientoAlto, escalamientoAncho);
    }

    public void entrenar() {
        if (ee == null) {
            System.err.println("Antes de entrenar la red neuronal se debe de inicializar los valores de entrada (initEntrenamiento())");
        }
        int iter = 1;
        do {
            getEe().iteration();
            if (iter % getFrecMonErr() == 0) {
                System.out.println("Iter #" + iter + " Error: " + getEe().getError());
            }
            iter++;
            if (iter % getFrecGuar() == 0) {
                guardarRN(System.getProperty("user.dir") + System.getProperty("file.separator") + "Entrenamiento" + System.getProperty("file.separator") + nomRN + "_" + iter + ".eg");
            }
        } while (getEe().getError() > getErrMin());
        System.out.println("Iter: " + iter + " Mejor solucion: " + getEe().getPob().getMejor().toString());
    }
    
    public void entrenarResilentPropagation(){
        EncogUtility.trainConsole(eResilentPropagation, getN(), traningSet, 10);
    }

    public MLData clasificar(Image img) {
        final ImageMLData entrada = new ImageMLData(img);
        entrada.downsample(this.downsample, false, escalamientoAlto, escalamientoAncho, 1, 0);
        return getN().compute(entrada);
    }

    public MLData clasificarBNN(Image img, BasicNetwork NN) {
        final ImageMLData entrada = new ImageMLData(img);
        entrada.downsample(this.downsample, false, escalamientoAlto, escalamientoAncho, 1, 0);
        return NN.compute(entrada);
    }

    public void guardarRN(String pat) {
        pat = pat.trim();
        int aux;
        if (!pat.endsWith(".eg")) {
            if ((aux = pat.lastIndexOf(System.getProperty("file.separator"))) > 0) {
                pat = pat.substring(0, aux);
            }
            pat += System.getProperty("file.separator") + nomRN + ".eg";
        }
        getN().getFlat().setWeights(getEe().getPob().getMejor().getCromosoma());
        EncogDirectoryPersistence.saveObject(new File(pat), getN());
    }

    public static BasicNetwork cargarRN(String pat) {
        pat = pat.trim();
        if (!pat.endsWith(".eg")) {
            System.err.println("Error archivo no reconocido");
        }
        return (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(pat));

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
     * @return the numNeuCapOcu1
     */
    public int getNumNeuCapOcu1() {
        return numNeuCapOcu1;
    }

    /**
     * @return the numNeuCapOcu2
     */
    public int getNumNeuCapOcu2() {
        return numNeuCapOcu2;
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
