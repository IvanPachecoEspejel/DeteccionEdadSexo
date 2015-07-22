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
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.encog.ml.data.MLData;

public class EntrenamientoOLD {
    
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
        RNEvalutiva n =  new RNEvalutiva("OLD", 2500, 4, new int[]{1000,500,100,50}, 1, 5000, 2000, 0.01);
        double [][]entradas;
        double [][]salidas;
        int numEntradas;
        int aux = 0;
        //File f_H = new File(Util.getPropCfgRNSx("PATH_H"));
        //File f_M = new File(Util.getPropCfgRNSx("PATH_M"));
        
        File f_OLD = new File(Util.getPropCfgRNSx("PATH_OLD"));
        if(!f_OLD.exists()){
            System.err.println("Directorio erroneo de Hombres");
            return;
        }
        
        File[] imgs_OLD = f_OLD.listFiles();
        numEntradas = imgs_OLD.length;
        salidas = new double[numEntradas][];
        entradas = new double[numEntradas][];
        BufferedImage imgAux;
        BufferedImage imagen;
        DataBuffer df;
        
        int indexEntradas = 0;
        while(indexEntradas < numEntradas){
            try {
               
                    imgAux = ImageIO.read(imgs_OLD[indexEntradas]);
                    imagen = new BufferedImage(imgAux.getWidth(),
                            imgAux.getHeight(), 
                            BufferedImage.TYPE_BYTE_GRAY);
                    salidas[indexEntradas] = new double[]{1};
                    System.out.println("OLD");
                
                df = imagen.getData().getDataBuffer();
                entradas[indexEntradas] = new double[df.getSize()];
                for(int j = 0; j<entradas[indexEntradas].length; j++){
                    entradas[indexEntradas][j] = df.getElemDouble(j);
                }
                indexEntradas++;
                System.out.println("num: "+indexEntradas);
            } catch (IOException ex) {
                Logger.getLogger(Entrenamiento.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        n.initEntrenamiento(entradas, salidas);
        System.out.println("Entrenando...");
        n.entrenar();
        System.out.println("Entrenando<ok>");
        System.out.println("Guardando...");
        n.guardarRN("/home/rainmaker/");
        System.out.println("Guardando<ok>");
    }    
}
