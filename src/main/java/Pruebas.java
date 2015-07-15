/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ivan
 */
import escom.ibhi.resource.Utileria.Util;
import java.util.Random;
public class Pruebas {
    
    public static void main(String args[]){
        Random r = new Random();
        for(int i = 0; i<10; i++)
            System.out.println(i+": "+r.nextGaussian());
    }
    
}
