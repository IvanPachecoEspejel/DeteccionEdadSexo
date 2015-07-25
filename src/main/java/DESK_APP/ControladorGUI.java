/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DESK_APP;

import escom.ibhi.deteccionsexoedad.RNEvalutiva;
import escom.ibhi.resource.Utileria.ScriptExecutor;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.encog.ml.data.MLData;

/**
 *
 * @author Isaac
 */
public class ControladorGUI {

    RNEvalutiva clasHM;
    RNEvalutiva clasBB;
    RNEvalutiva clasNN;
    RNEvalutiva clasJV;
    RNEvalutiva clasADU;
    RNEvalutiva clasVJ;
    MLData isHM;
    MLData[] resulEdad;
    ScriptExecutor preproceso;
    String fseparador;
    String rootDir;
    String dirTrain;

    public ControladorGUI() {
        clasHM = new RNEvalutiva(18, 18);
        clasBB = new RNEvalutiva(18, 18);
        clasNN = new RNEvalutiva(18, 18);
        clasJV = new RNEvalutiva(18, 18);
        clasADU = new RNEvalutiva(18, 18);
        clasVJ = new RNEvalutiva(18, 18);
        rootDir = System.getProperty("user.dir");
        fseparador = System.getProperty("file.separator");
        dirTrain = "Entrenamiento";
        
        // CARGA DE ARCHIVOS QUE CONTIENEN PARAMENTROS DE LA RED NEURONAL PARA CADA CLASE
        clasHM.cargarRN(rootDir + fseparador + dirTrain+ fseparador+ "HOMBRES_MUJERES.eg");
        clasBB.cargarRN(rootDir + fseparador + dirTrain+ fseparador+ "BB_Buena.eg");
        clasNN.cargarRN(rootDir + fseparador + dirTrain+ fseparador+ "NN_Buena.eg");
        clasJV.cargarRN(rootDir + fseparador + dirTrain+ fseparador+ "JV_Buena.eg");
        clasADU.cargarRN(rootDir + fseparador + dirTrain+ fseparador+"ADULTOS_Buena.eg");
        clasVJ.cargarRN(rootDir + fseparador + dirTrain+ fseparador+ "OLD_buena.eg");
        
        resulEdad = new MLData[5];
        preproceso = new ScriptExecutor();
        
    }

    public void clasificar(APP_DESK ventana, File imagen) {
        String edad;
        boolean sexo;
        // obtener imagen 50px b/n  con rostro de persona
        preproceso.ejecutarScript(rootDir + fseparador+"Recursos" + fseparador + "Preprocesamiento.pyc", imagen.getAbsolutePath());
        
        Image img;

        try {
            img = ImageIO.read(new File(imagen.getAbsolutePath())); //rootDir + fseparador+"Recursos" + fseparador + "imgCargada.jpg"
            isHM = clasHM.clasificar(img);
            resulEdad[0] = clasBB.clasificar(img);
            resulEdad[1] = clasNN.clasificar(img);
            resulEdad[2] = clasJV.clasificar(img);
            resulEdad[3] = clasADU.clasificar(img);
            resulEdad[4] = clasVJ.clasificar(img);
        } catch (IOException ex) {
            System.out.println("Error al abrir imagen");
            JOptionPane.showMessageDialog(ventana, "No se reconoce un rostro en la imagen, intentelo de nuevo por favor");
            return;
        }
        if (isHM.getData(0) > 0) {
            sexo = false;
        } else {
            sexo = true;
        }
        int indexEdad = 0;
        double edadMax = -1;
        for (int i = 0; i < resulEdad.length; i++) {
            System.out.println("Clasificacion: " + Arrays.toString(resulEdad[i].getData()));
            if (resulEdad[i].getData(0) > edadMax) {
                edadMax = resulEdad[i].getData(0);
                indexEdad = i;
            }
        }

        switch (indexEdad) {
            case 0:
                edad = "Bebe";
                break;
            case 1:
                edad = "Niño";
                break;
            case 2:
                edad = "Joven";
                break;
            case 3:
                edad = "Adulto";
                break;
            case 4:
                edad = "Viejo";
                break;
            default:
                edad = "No se reconocio tu edad";
        }
        if(sexo){
            ventana.setSexo("HOMBRE");
        }else{
            ventana.setSexo("MUJER");
        }
        ventana.setEdad(edad);

    }

}
