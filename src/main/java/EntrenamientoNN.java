/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ivan
 */
import escom.ibhi.deteccionsexoedad.RNEvalutiva;
import escom.ibhi.resource.Utileria.Util;
import java.awt.Image;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;

public class EntrenamientoNN {

    public static void main(String args[]) {
        //Se crea la red neuronal Sol se conideraron 2 capas ocultas
        //Parametros
        //1.-Nombre de la red neuronal
        //2.-Neuronas de la capa oculta 1
        //3.-Neuronas de la capa oculta 2
        //4.-Cada cuantas iteraciones se va hacer un respaldo de la red neuronal
        //5.-Cada cuantas iteraciones se va mostrar el error
        //6.-Error minimo a alcanzaar
        //7.-Factor al que se normaliza y escalan las imagenes a lo alto
        //8.-Factor al que se normaliza y escalan las imagenes a lo ancho
        RNEvalutiva n = new RNEvalutiva("NNLennon", 32, 16, 1000, 1, 0.01, 8, 8);

        //Se cargan las imagenes (PROCURAR QUE EL NUMERO DE AMBOS TIPOS DE IMAGENES SEA EL MISMO) 
        //las sobrantes utilizar para hacer pruebas
        File f_NN = new File(Util.getPropCfgRNSx("PATH_NN"));
        if (!f_NN.exists()) {
            System.err.println("Directorio erroneo de NN");
            return;
        }

        File[] files;

        MLData saldata;

        files = f_NN.listFiles();
        saldata = new BasicMLData(new double[]{1});//Mujeres

        for (File f : files) {
            try {
                Image imgMasFre = ImageIO.read(f);
                n.addEntrada(imgMasFre, saldata);
            } catch (Exception e) {
                Logger.getLogger(Entrenamiento.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        //Se utiliza para poder normalizar las entradas
        n.finalizarProcesoEntrada();
        //Se utiliza para crear la red neuronal segun las neuronas de entradas que se generaron en la normalizacion
        n.initRN();
        //Se inicializa el algoritmo de entrenamiento (Pueden cambiarlo si desean)
        //Para modificar los valores de configuracion del entrenamiento evolutivo esta en el archivo src/main/resource/Configuracion/cfgEntrenameintoEvolutivo.properties
        //En este link hay un ejemplo de encog con imagenes 
        //https://github.com/encog/encog-java-examples/blob/master/src/main/java/org/encog/examples/neural/image/ImageNeuralNetwork.java
        n.initEntrenamiento();
        System.out.println("Entrenando...");
        n.entrenar();
        System.out.println("Entrenando<ok>");
        System.out.println("Guardando...");
        n.guardarRN("/home/rainmaker/faces/");
        System.out.println("Guardando<ok>");

    }
}
