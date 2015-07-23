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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;

public class EntrenamientoNNOLD {
    
    public static void main(String args[]){
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
        RNEvalutiva n =  new RNEvalutiva("NNOLD", 32, 16, 1000, 10, 0.01, 8, 8);
        
        //Se cargan las imagenes (PROCURAR QUE EL NUMERO DE AMBOS TIPOS DE IMAGENES SEA EL MISMO) 
        //las sobrantes utilizar para hacer pruebas
        File f_NN = new File(Util.getPropCfgRNSx("PATH_NN"));
        File f_OLD = new File(Util.getPropCfgRNSx("PATH_OLD"));
        if(!f_NN.exists()){
            System.err.println("Directorio erroneo de NN");
            return;
        }
        if(!f_OLD.exists()){
            System.err.println("Directorio erroneo de NN");
            return;
        }
        
        File[] filMasFre;
        File[] filMenFre;
        int factorEquidad;
        int indexEntradas = 0;
        int auxOscilador = 0;
        int indFilMasFrec = 0;
        int indFilMenFrec = 0;
        MLData salIdeMasFrec;
        MLData salIdeMenFrec;
        
        if(f_NN.listFiles().length < f_OLD.listFiles().length){
            filMasFre = f_OLD.listFiles();
            filMenFre = f_NN.listFiles();
            salIdeMasFrec =  new BasicMLData(new double[]{1});//Mujeres
            salIdeMenFrec =  new BasicMLData(new double[]{0});//Hombres

        }else{
            filMasFre = f_NN.listFiles();
            filMenFre = f_OLD.listFiles();
            salIdeMasFrec =  new BasicMLData(new double[]{0});//Hombres
            salIdeMenFrec =  new BasicMLData(new double[]{1});//Mujeres
        }
        factorEquidad = filMasFre.length/filMenFre.length;
        
        while(indFilMasFrec < filMasFre.length){
            try {                
                if(auxOscilador == 0 || indFilMenFrec >= filMenFre.length){
                    Image imgMasFre = ImageIO.read(filMasFre[indFilMasFrec]);
                    n.addEntrada(imgMasFre, salIdeMasFrec);
                    if((indexEntradas+1) % factorEquidad ==0){
                        auxOscilador -= 1;
                        auxOscilador = Math.abs(auxOscilador);
                    }
                    indFilMasFrec++;
                }else {
                    Image imgMenFre = ImageIO.read(filMenFre[indFilMenFrec]);
                    n.addEntrada(imgMenFre, salIdeMenFrec);
                    auxOscilador -= 1;
                    auxOscilador = Math.abs(auxOscilador);
                    indFilMenFrec++;
                }
                indexEntradas++;
            } catch (IOException ex) {
                Logger.getLogger(Entrenamiento.class.getName()).log(Level.SEVERE, null, ex);
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
