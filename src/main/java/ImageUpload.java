/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import escom.ibhi.deteccionsexoedad.RNEvalutiva;
import escom.ibhi.resource.Utileria.ScriptExecutor;
import escom.ibhi.resource.Utileria.Util;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.encog.ml.data.MLData;

/**
 *
 * @author rainmaker
 */
@WebServlet(urlPatterns = {"/imageupload"})
@MultipartConfig
public class ImageUpload extends HttpServlet {
    RNEvalutiva clasHM;
    RNEvalutiva clasBB;
    RNEvalutiva clasNN;
    RNEvalutiva clasJV;
    RNEvalutiva clasADU;
    RNEvalutiva clasVJ;
    MLData isHM;
    MLData[] resulEdad;
    ScriptExecutor preproceso;
    public ImageUpload(){
        clasHM= new RNEvalutiva(18, 18);
        clasBB= new RNEvalutiva(18, 18);
        clasNN= new RNEvalutiva(18, 18);
        clasJV= new RNEvalutiva(18, 18);
        clasADU= new RNEvalutiva(18, 18);
        clasVJ= new RNEvalutiva(18, 18);
        
        clasHM.cargarRN(Util.d+"WebAppSexEdad"+Util.d+"Entrenamiento"+Util.d+"HOMBRES_MUJERES.eg");
        clasBB.cargarRN(Util.d+"WebAppSexEdad"+Util.d+"Entrenamiento"+Util.d+"BB_Buena.eg");
        clasNN.cargarRN(Util.d+"WebAppSexEdad"+Util.d+"Entrenamiento"+Util.d+"NN_Buena.eg");
        clasJV.cargarRN(Util.d+"WebAppSexEdad"+Util.d+"Entrenamiento"+Util.d+"JV_Buena.eg");
        clasADU.cargarRN(Util.d+"WebAppSexEdad"+Util.d+"Entrenamiento"+Util.d+"ADULTOS_Buena.eg");
        clasVJ.cargarRN(Util.d+"WebAppSexEdad"+Util.d+"Entrenamiento"+Util.d+"OLD_buena.eg");
        resulEdad  = new MLData[5];
        
        preproceso = new ScriptExecutor();
        
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        InputStream filecontent = null;
        try (PrintWriter out = response.getWriter()) {
            String edad;
            boolean sexo;
            
            final Part filePart = request.getPart("imagen");
            
            //Este es el InputStream para el BufferedImage
            InputStream entrada =  filePart.getInputStream();
            
            System.out.println("Nombre Archivo: "+filePart.getName());
            
            File f=new File(Util.d+"WebAppSexEdad"+Util.d+"Recursos"+Util.d+"imgCargada.jpg");
            
            OutputStream salida=new FileOutputStream(f);
            byte[] buf =new byte[1024];
            int len;
            while((len=entrada.read(buf))>0){
                salida.write(buf,0,len);
            }
            salida.close();
            entrada.close();
            
            preproceso.ejecutarScript(Util.d+"WebAppSexEdad"+Util.d+"Recursos"+Util.d+"Preprocesamiento.py", "imgCargada.jpg");
            
            Image img = ImageIO.read(
                    new File(Util.d+"WebAppSexEdad"+Util.d+"Recursos"+Util.d+"imgCargada.jpg"));
            
//            Image img = ImageIO.read(
//                    new File(Util.d+"WebAppSexEdad"+Util.d+"Recursos"+Util.d+filePart.getName()+filePart.getName()));
            
            isHM = clasHM.clasificar(img);
            resulEdad[0] = clasBB.clasificar(img);
            resulEdad[1] = clasNN.clasificar(img);
            resulEdad[2] = clasJV.clasificar(img);
            resulEdad[3] = clasADU.clasificar(img);
            resulEdad[4] = clasVJ.clasificar(img);
            
            if(isHM.getData(0) > 0 )
                sexo = false;
            else{
                sexo = true;
            }
            int indexEdad = 0;
            double edadMax = -1;
            for(int i = 0; i<resulEdad.length; i++){
                System.out.println("Clasificacion: "+Arrays.toString(resulEdad[i].getData()));
                if(resulEdad[i].getData(0)> edadMax){
                    edadMax = resulEdad[i].getData(0);
                    indexEdad = i;
                }
            }
            
            switch(indexEdad){
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

            out.println("<h3>Sexo:</h3><p>" + (sexo ? "Hombre" : "Mujer") + "</p><h3>Edad:</h3><p>" + edad + "</p>");
        } finally {
                if (filecontent != null) {
                    filecontent.close();
                }
            }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
