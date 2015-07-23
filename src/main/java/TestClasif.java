
import escom.ibhi.deteccionsexoedad.RNEvalutiva;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import org.encog.ml.data.MLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.platformspecific.j2se.data.image.ImageMLData;
import org.encog.util.downsample.Downsample;
import org.encog.util.downsample.SimpleIntensityDownsample;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author isaac
 */
public class TestClasif {
 
    public static void main(String[] args) throws IOException {
        BasicNetwork rn = RNEvalutiva.cargarRN("/home/isaac/Escritorio/EntrenamientosLennon/YNOLD_96000.eg");
        
        Image img = ImageIO.read(new File("/home/isaac/Escritorio/IMG_ENTRENAMIENTO/JV/JV0.jpg"));
        Image im2 = ImageIO.read(new File("/home/isaac/Escritorio/IMG_ENTRENAMIENTO/OLD/OLD16.jpg"));
        Image im3 = ImageIO.read(new File("/home/isaac/Escritorio/IMG_ENTRENAMIENTO/BB/BB1.jpg"));
        
        TestClasif tC = new TestClasif();
        MLData resul =  tC.clasificarBNN(img, rn);
        MLData resul2 =  tC.clasificarBNN(im2, rn);
        MLData resul3 =  tC.clasificarBNN(im3, rn);
        
        System.out.println("Resul" + Arrays.toString(resul.getData()));
        System.out.println("Resul2"+ Arrays.toString(resul2.getData()));
        System.out.println("Resul3"+ Arrays.toString(resul3.getData()));
    }
 
    
    public MLData clasificarBNN(Image img, BasicNetwork NN){
        Downsample ds = new SimpleIntensityDownsample();
        
        
        final ImageMLData entrada = new ImageMLData(img);
        entrada.downsample(ds, false,8, 8, 1, 0);
        return NN.compute(entrada);
    }
    
    
    
}

