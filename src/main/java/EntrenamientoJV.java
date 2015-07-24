/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author isaac
 */
import escom.ibhi.deteccionsexoedad.RNEvalutiva;
import escom.ibhi.resource.Utileria.Util;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;

public class EntrenamientoJV {
    
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
        RNEvalutiva n =  new RNEvalutiva("JV_Buena", 100, 50, 1000, 10, 0.01, 18, 18);
        
        //Se cargan las imagenes (PROCURAR QUE EL NUMERO DE AMBOS TIPOS DE IMAGENES SEA EL MISMO) 
        //las sobrantes utilizar para hacer pruebas
        
        File []f_BB = new File(Util.getPropCfgRNSx("PATH_BB")).listFiles();
        File []f_NN = new File(Util.getPropCfgRNSx("PATH_NN")).listFiles();
        File []f_H = new File(Util.getPropCfgRNSx("PATH_H")).listFiles();
        File []f_M = new File(Util.getPropCfgRNSx("PATH_M")).listFiles();
        File []f_OLD = new File(Util.getPropCfgRNSx("PATH_OLD")).listFiles();
        File []f_JV = new File(Util.getPropCfgRNSx("PATH_JV")).listFiles();
        
        
        File []otros = new File[f_NN.length + f_H.length +f_M.length+ f_OLD.length + f_BB.length];
        //System.out.println("Otros: "+otros.length);
        
        System.arraycopy(f_NN, 0, otros, 0, f_NN.length);
        System.arraycopy(f_H, 0, otros,f_NN.length,f_H.length);
        System.arraycopy(f_M , 0 , otros , f_NN.length +f_H.length , f_M.length);
        System.arraycopy(f_OLD , 0 , otros , f_NN.length +f_H.length+f_M.length, f_OLD.length);
        System.arraycopy(f_BB , 0 , otros , f_NN.length +f_H.length+f_M.length + f_OLD.length, f_BB.length);
        
        
        File[] filMenFre;
        File[] filMasFre;
        int factorEquidad;
        int indexEntradas = 0;
        int auxOscilador = 0;
        int indexFilesMasFrec = 0;
        int indexFilesMenFrec = 0;
        MLData salIdealMasFrec;
        MLData salIdealMenFrec;
        
        if(f_JV.length < otros.length){
            filMasFre = otros;
            filMenFre = f_JV;
            salIdealMasFrec =  new BasicMLData(new double[]{-1});//Mujeres
            salIdealMenFrec =  new BasicMLData(new double[]{1});//Hombres

        }else{
            filMasFre = f_JV;
            filMenFre = otros;
            salIdealMasFrec =  new BasicMLData(new double[]{1});//Mujeres
            salIdealMenFrec =  new BasicMLData(new double[]{-1});//Hombres
        }
        factorEquidad = filMasFre.length/filMenFre.length;
        //System.out.println("filMasFrec: "+filMasFre.length);
        while(indexFilesMasFrec < filMasFre.length){
            try {
                if(auxOscilador == 0 || indexFilesMenFrec >= filMenFre.length){
                    Image imgMasFre = ImageIO.read(filMasFre[indexFilesMasFrec]);
                    n.addEntrada(imgMasFre, salIdealMasFrec);
                    if((indexEntradas+1) % factorEquidad ==0){
                        auxOscilador -= 1;
                        auxOscilador = Math.abs(auxOscilador);
                    }
                    //System.out.println("Otros: "+indexFilesMasFrec);
                    indexFilesMasFrec++;
                }else {
                    Image imgMenFre = ImageIO.read(filMenFre[indexFilesMenFrec]);
                    n.addEntrada(imgMenFre, salIdealMenFrec);
                    auxOscilador -= 1;
                    auxOscilador = Math.abs(auxOscilador);
                    indexFilesMenFrec++;
                    //System.out.println("Interes: "+indexFilesMenFrec);
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
        n.initEntrenamientoResilentPropagation();
        System.out.println(">>>ENTRENANDO JOVENES<<<");
        n.entrenarResilentPropagation();
        System.out.println("Entrenando<ok>");
        System.out.println("Guardando...");
        n.guardarRN("/home/isaac/Escritorio/");
        System.out.println("Guardando<ok>");
        
    }    
}
