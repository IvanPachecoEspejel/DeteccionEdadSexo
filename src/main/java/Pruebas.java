/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ivan
 */
import escom.ibhi.deteccionsexoedad.EntrenamientoEvolutivo;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.layers.Layer;
public class Pruebas {
    
    public
    static double XORINPUT[][] = {{ 0.0 , 0.0 } ,
                                  { 1.0 , 0.0 } ,
                                  { 0.0 , 1.0 } ,
                                  { 1.0 , 1.0 }} ;

    public static double XORIDEAL [][] = {  { 0.0 } ,
                                            { 1.0 } ,
                                            { 1.0 } ,
                                            { 0.0 } } ;
    public static double MEJ_SOL[] ={45.042387293235336,
                                    -32.58878843164396, 
                                    -28.464587049814877, 
                                    -4.725892464725167, 
                                    -42.43986990671029,
                                    45.0158961266909, 
                                    -59.53985250945689, 
                                    -73.20877796148959, 
                                    23.13650927220143};

    public static void main(String args[]){
        BasicNetwork n = new BasicNetwork();
        n.addLayer(new BasicLayer(null, true, 2));
        n.addLayer(new BasicLayer(new ActivationSigmoid(), true, 2));
        n.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
        n.getStructure().finalizeStructure();
        n.reset();
//        n.getFlat().setWeights(MEJ_SOL);
//        
        MLDataSet traningSet = new BasicMLDataSet(XORINPUT, XORIDEAL);
//        for(MLDataPair par : traningSet){
//            MLData salida = n.compute(par.getInput());
//            System.out.println("Salidas: "+salida.toString());
//        }
//        
        EntrenamientoEvolutivo ee =  new EntrenamientoEvolutivo(n, traningSet);
        
        int epoch = 1 ;
        do {
            ee.iteration();
            System.out.println(" Epoch #"+epoch+" Error: "+ee.getError());
            epoch++;
        } while (ee.getError() > 0.01 );
        System.out.println("Mejor solucion: "+ee.getPob().getMejor().toString());
        
    }
    
    
}
