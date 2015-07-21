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
import java.util.Arrays;
import org.encog.ml.data.MLData;

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
        RNEvalutiva n =  new RNEvalutiva("Prueba", 2, 1, new int[]{2}, 1, 1000, 10, 0.01);
        n.initEntrenamiento(XORINPUT, XORIDEAL);
        System.out.println("Entrenando...");
        n.entrenar();
        System.out.println("Entrenando<ok>");
        System.out.println("Guardando...");
        n.guardarRN("/home/ivan/Escritorio/");
        System.out.println("Guardando<ok>");
        n.cargarRN("/home/ivan/Escritorio/Prueba.eg");
        MLData salPrueba = n.clasificar(new double[]{0,0});
        MLData salPrueba1 = n.clasificar(new double[]{0,1});
        MLData salPrueba2 = n.clasificar(new double[]{1,0});
        MLData salPrueba3 = n.clasificar(new double[]{1,1});
        System.out.println(Arrays.toString(salPrueba.getData()));
        System.out.println(Arrays.toString(salPrueba1.getData()));
        System.out.println(Arrays.toString(salPrueba2.getData()));
        System.out.println(Arrays.toString(salPrueba3.getData()));
        
    }    
}
