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
    public static final Properties cfgRNSexo;   //Propiedades de configuracion de la Red nueronal para el sexo
    private static final Logger log = Logger.getLogger(Util.class.getName());
    private static final Random r;
    public static final int gamma;
    public static final double gammaC;
    public static final int gammaSum;
    public static final int gammaRes;
    public static final double alpha;
    public static final double alphaC;
    public static final double alphaMax;
    public static final double alphaMin;
    public static final double alphaMul;
    public static final double alphaDiv;
    
    public static final Integer alphaFrecAct;
    public static final Integer alphaFrecConCol; 
    
    public static final double probMut;
    public static final double probCru;
    
    static {
        cfgEE = new Properties();
        cfgRNSexo = new Properties();
        r = new Random(System.currentTimeMillis());
        try {
            cfgEE.load(new FileInputStream("src/main/resources/Configuracion/cfgEntrenamientoEvolutivo.properties"));
            cfgRNSexo.load(new FileInputStream("src/main/resources/Configuracion/cfgRNSexo.properties"));
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        gamma = getPropCfgEEInteger("GAMMA");
        gammaC = getPropCfgEEDouble("GAMMA_C");
        gammaSum = getPropCfgEEInteger("GAMMA_SUM");
        gammaRes = getPropCfgEEInteger("GAMMA_RES");
        alpha = getPropCfgEEDouble("ALPHA");
        alphaC = getPropCfgEEDouble("ALPHA_C");
        alphaMax = getPropCfgEEDouble("ALPHA_MAX");
        alphaMin = getPropCfgEEDouble("ALPHA_MIN");
        alphaMul= getPropCfgEEDouble("ALPHA_MUL");
        alphaDiv = getPropCfgEEDouble("ALPHA_DIV");
        alphaFrecAct = getPropCfgEEInteger("ALPHA_FREC_ACT");
        alphaFrecConCol = getPropCfgEEInteger("FREC_CON_COL");
        
        probCru = getPropCfgEEDouble("PROB_CRU");
        probMut = getPropCfgEEDouble("PROB_MUT");
    }
    
    public static Integer getPropCfgEEInteger(String llave){
        String prop = cfgEE.getProperty(llave);
        if(isInteger(prop))
            return Integer.parseInt(prop);
        throw new NumberFormatException("Error la propiedad: "+llave+ " no es un Entero");
    }
    
    public static double getPropCfgEEDouble(String llave){
        String prop = cfgEE.getProperty(llave);
        if(isdouble(prop))
            return Double.parseDouble(prop);
        throw new NumberFormatException("Error la propiedad: "+llave+ " no es un double");
    }
    
    public static String getPropCfgEE(String llave){
        return cfgEE.getProperty(llave);
    }
    
    public static Integer getPropCfgRNSxInteger(String llave){
        String prop = cfgRNSexo.getProperty(llave);
        if(isInteger(prop))
            return Integer.parseInt(prop);
        throw new NumberFormatException("Error la propiedad: "+llave+ " no es un Entero");
    }
    
    public static double getPropCfgRNSxDouble(String llave){
        String prop = cfgRNSexo.getProperty(llave);
        if(isdouble(prop))
            return Double.parseDouble(prop);
        throw new NumberFormatException("Error la propiedad: "+llave+ " no es un double");
    }
    
    public static String getPropCfgRNSx(String llave){
        return cfgRNSexo.getProperty(llave);
    }
    
    public static boolean isdouble(String param){
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
    
    public static int randint_N_M(int ini, int fin){
        return r.nextInt(fin - ini) + ini;
    }
    
    public static double rand_DN(double media, double desviacion){
        return media + (r.nextGaussian()*desviacion);
    }
    
    public static double rand_N_M(double ini, double fin){
        return r.nextDouble()*(fin-ini) + ini;
    }
    
    public static void sumarArreglosDouble(double[] a1, double[] a2) throws Exception{
        if(a1.length != a2.length)
            throw new Exception("Error al sumar los arreglos, tamanios distintos");
        for(int i = 0; i< a1.length; i++){
            a1[i] = a1[i] +a2[i];
        }
    }
    
    public static void multArregloEscalar(double a[], double esc){
        for(int i = 0; i<a.length; i++){
            a[i] = a[i]*esc;
        }
    }
    
}
