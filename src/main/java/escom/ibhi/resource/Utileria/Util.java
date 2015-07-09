/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ibhi.resource.Utileria;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ivan
 */
public class Util {
    
    public static final Properties cfgEE;   //Propiedades de configuracion del entrenamiento evolutivo
    private static final Logger log = Logger.getLogger(Util.class.getName());
    
    static {
        cfgEE = new Properties();
        try {
            cfgEE.load(new FileInputStream("src/main/resources/Configuracion/cfgEntrenamientoEvolutivo.properties"));
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }
    
    public static String getPropCfgEE(String llave){
        return cfgEE.getProperty(llave);
    }
    
}
