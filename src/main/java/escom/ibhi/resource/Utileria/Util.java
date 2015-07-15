/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ibhi.resource.Utileria;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ivan
 */
public class Util {
    
    public static final Properties cfgEE;   //Propiedades de configuracion del entrenamiento evolutivo
    private static final Logger log = Logger.getLogger(Util.class.getName());
    private static final Random r;
    public static final Double alpha;
    public static final Double alphaC;
    public static final Double alphaMax;
    public static final Double alphaMin;
    public static final Double alphaMul;
    public static final Double alphaDiv;
    
    public static final Integer alphaFrecAct;
    
    static {
        cfgEE = new Properties();
        r = new Random(System.currentTimeMillis());
        try {
            cfgEE.load(new FileInputStream("src/main/resources/Configuracion/cfgEntrenamientoEvolutivo.properties"));
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        alpha = (Double)getPropCfgEE("ALPHA");
        alphaC = (Double)getPropCfgEE("ALPHA_C");
        alphaMax = (Double)getPropCfgEE("ALPHA_MAX");
        alphaMin = (Double)getPropCfgEE("ALPHA_MIN");
        alphaMul= (Double)getPropCfgEE("ALPHA_MUL");
        alphaDiv = (Double)getPropCfgEE("ALPHA_DIV");
        alphaFrecAct = (Integer)getPropCfgEE("ALPHA_FREC_ACT");
    }
    
    public static Object getPropCfgEE(String llave){
        String prop = cfgEE.getProperty(llave);
        if(isDouble(prop))
            return Double.parseDouble(prop);
        else if(isInteger(prop))
            return Integer.parseInt(prop);
        else
            return prop;
    }
    
    public static boolean isDouble(String param){
        try{
            Double.parseDouble(param);
            return true;
        }catch(NumberFormatException nfe){
            return false;
        }
    }
    
    public static boolean isInteger(String param){
        try{
            Integer.parseInt(param);
            return true;
        }catch(NumberFormatException nfe){
            return false;
        }
    }
    
    public static double rand_DN(double media, double desviacion){
        return media + (r.nextGaussian()*desviacion);
    }
    
}
