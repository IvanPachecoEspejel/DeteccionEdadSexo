/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ibhi.deteccionsexoedad;

import escom.ibhi.resource.Utileria.Util;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.encog.ml.MLMethod;
import org.encog.ml.TrainingImplementationType;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.train.BasicTraining;
import org.encog.neural.flat.FlatNetwork;
import org.encog.neural.networks.ContainsFlat;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.TrainingContinuation;

/**
 *
 * @author ivan
 */
public class EntrenamientoEvolutivo extends BasicTraining implements Train{
    
    private static final Logger log = Logger.getLogger(EntrenamientoEvolutivo.class.getName());
    
    //Contiene la estructura de la red neuronal
    private final FlatNetwork currentFlatNetwork;
    private final Poblacion pob;
    
    private double[] salidaS = null;
    private Sujeto sujetoM;
    private double errS;
    /*
    *   @training : Contiene los valores de entrada y los valores de salida 
    *               que se deberian de obtener
    *   @network :  Contiene la estructura de la red neuronal
    */
    public EntrenamientoEvolutivo(ContainsFlat network, MLDataSet training) {
        super(TrainingImplementationType.Iterative);
        currentFlatNetwork = network.getFlat();
        salidaS = new double[training.getIdealSize()];
        setTraining(training);
        setIteration(0);
        setError(0);
        pob = new Poblacion(Util.getPropCfgEEInteger("TAM_POBLACION"), currentFlatNetwork.getEncodeLength());
        for(Sujeto s : pob.getSujetos()){
            evaluarSujeto(s);
            pob.setMejor(s);
        }
        setError(pob.getMejor().getError());
        //Falta especifiar las estrategias a utilizar
    }
    
    /*
    *   Funcion que efecuta una iteración del entrenamiento de la red neuronal
    */
    @Override
    public void iteration() {
        setIteration(getIteration()+1);
        //System.out.println("********** Iteracion : "+getIteration()+" **********");
        for(int i = 0; i< getPob().getTamPob(); i++){
            sujetoM = getPob().mutaSujeto(i);
            getPob().addIteracionAt(i);
            evaluarSujeto(sujetoM);
            if(getPob().getSujetoAt(i).getError() > sujetoM.getError()){
                getPob().addExitoAt(i);
                getPob().setSujetoAt(sujetoM, i);
            }
            if(getIteration() % ((int)Util.alphaFrecAct) == 0){
                getPob().modificaAlpha(i);
            }
        }
        setError(getPob().getMejor().getError());
        //System.out.println("~~~~~~~~~~ Iteracion : "+getIteration()+" <OK> ~~~~~~~~~~");
    }
    
    public double calcError(final double[] salidas,final double[] ideales,final double[] pesos){
        double error = 0.0;
        double sumaPesos = 0.0;
        for(int i = 0; i < salidas.length; i++){
            error += (ideales[i])*Math.log(salidas[i]) - (1-ideales[i])*Math.log(1-salidas[i]);
        }
        
        error = Math.abs(error);
        return error;
    }
    
    public final void evaluarSujeto(Sujeto s){
        currentFlatNetwork.setWeights(s.getCromosoma());
        errS = 0;
        for(MLDataPair par : getTraining()){
            currentFlatNetwork.compute(par.getInput().getData(), salidaS);
            errS += calcError(salidaS, par.getIdealArray(), s.getCromosoma());
        }
        s.setError(errS);
    }
    
    /*
    *   Funcion para verificar si el proceso de entrenamiento de la red
    *   neuronal puede ser pausado, para despues coontinuar
    *   true sí se puede pausar el entrenamiento
    */
    @Override
    public boolean canContinue() {
        //Regresando false siempre, numca se ocupa la funcionalidad de pausar
        //el entrenamiento para luego reanudar
        return false;
    }
    
    /*
    *   Funcion que pausa al hijo que esta ejecutando el entrenamiento
    */
    @Override
    public TrainingContinuation pause() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*
    *   Funcion que reanuda el entrenamiento despertando al hilo que se 
    *   encarga del entrenamiento
    */

    @Override
    public void resume(TrainingContinuation tc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*
    *   Regresa la estrategia que se esta ejecutando en la iteracion presente,
    *   el valor que tiene la variable iteration
    *   Strategy, Se utilizan para saber si la red neuronal ha convergido, y
    *   para saber el estado actual de la red neuronal, estras estrategias ya
    *   estan implementadas solo hay que usarlas, hay demasiadas estrategias
    *   que nos pueden ayudar realizar un mejor entrenamiento para ver todas las
    *   estrategias ir a la pagina http://heatonresearch-site.s3-website-us-east-1.amazonaws.com/javadoc/encog-3.3/org/encog/ml/train/strategy/Strategy.html
    */
    @Override
    public MLMethod getMethod() {
        return (MLMethod) getStrategies().get(getIteration());
    }

    /**
     * @return the pob
     */
    public Poblacion getPob() {
        return pob;
    }
}
