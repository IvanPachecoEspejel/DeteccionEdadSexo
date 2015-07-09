/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ibhi.deteccionsexoedad;

import org.encog.ml.MLMethod;
import org.encog.ml.TrainingImplementationType;
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
    
    //Contiene la estructura de la red neuronal
    private final FlatNetwork currentFlatNetwork;
    
    /*
    *   @training : Contiene los valores de entrada y los valores de salida 
    *               que se deberian de obtener
    *   @network :  Contiene la estructura de la red neuronal
    */
    public EntrenamientoEvolutivo(ContainsFlat network, MLDataSet training) {
        super(TrainingImplementationType.Iterative);
        currentFlatNetwork = (FlatNetwork) network;
        setTraining(training);
        setIteration(0);
        setError(0);
        //Falta especifiar las estrategias a utilizar
    }
    
    /*
    *   Funcion que efecuta una iteración del entrenamiento de la red neuronal
    */
    @Override
    public void iteration() {
        setIteration(getIteration()+1);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
}
