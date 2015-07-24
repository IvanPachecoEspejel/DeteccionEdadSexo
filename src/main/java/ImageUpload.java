/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import escom.ibhi.deteccionsexoedad.RNEvalutiva;
import escom.ibhi.resource.Utileria.ScriptExecutor;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
    RNEvalutiva clasM;
    RNEvalutiva clasBB;
    RNEvalutiva clasNN;
    RNEvalutiva clasJV;
    RNEvalutiva clasADU;
    RNEvalutiva clasVJ;
    MLData isHom, isMuj;
    MLData[] resulEdad;
    ScriptExecutor preproceso;
    public ImageUpload(){
        clasHM= new RNEvalutiva();
        clasBB= new RNEvalutiva();
        clasNN= new RNEvalutiva();
        clasJV= new RNEvalutiva();
        clasADU= new RNEvalutiva();
        clasVJ= new RNEvalutiva();
        
        clasHM.cargarRN(System.getProperty("user.dir")+
                    System.getProperty("file.separator")+
                    "Entrenamiento"+
                    System.getProperty("file.separator")+
                    "HOMBRES_MUJERES.eg");
        clasBB.cargarRN(System.getProperty("user.dir")+
                    System.getProperty("file.separator")+
                    "Entrenamiento"+
                    System.getProperty("file.separator")+
                    "BB_Buena.eg");
        clasNN.cargarRN(System.getProperty("user.dir")+
                    System.getProperty("file.separator")+
                    "Entrenamiento"+
                    System.getProperty("file.separator")+
                    "NN_Buena.eg");
        clasJV.cargarRN(System.getProperty("user.dir")+
                    System.getProperty("file.separator")+
                    "Entrenamiento"+
                    System.getProperty("file.separator")+
                    "JV_Buena.eg");
        clasADU.cargarRN(System.getProperty("user.dir")+
                    System.getProperty("file.separator")+
                    "Entrenamiento"+
                    System.getProperty("file.separator")+
                    "ADULTOS_Buena.eg");
        clasVJ.cargarRN(System.getProperty("user.dir")+
                    System.getProperty("file.separator")+
                    "Entrenamiento"+
                    System.getProperty("file.separator")+
                    "OLD_buena.eg");
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
            
            File f=new File(System.getProperty("user.dir")+
                    System.getProperty("file.separator")+
                    "Recursos"+
                    System.getProperty("file.separator")+
                    filePart.getName());
            
            OutputStream salida=new FileOutputStream(f);
            byte[] buf =new byte[1024];
            int len;
            while((len=entrada.read(buf))>0){
                salida.write(buf,0,len);
            }
            salida.close();
            entrada.close();
            
            preproceso.ejecutarScript(System.getProperty("user.dir")+
                    System.getProperty("file.separator")+
                    "Recursos"+
                    System.getProperty("file.separator")+
                    filePart.getName());
            
            Image img = ImageIO.read(new File(System.getProperty("user.dir")+
                    System.getProperty("file.separator")+
                    "Recursos"+
                    System.getProperty("file.separator")+
                    filePart.getName()));
            
            isHom = clasHM.clasificar(img);
            isMuj = clasHM.clasificar(img);
            resulEdad[0] = clasBB.clasificar(img);
            resulEdad[1] = clasNN.clasificar(img);
            resulEdad[2] = clasJV.clasificar(img);
            resulEdad[3] = clasADU.clasificar(img);
            resulEdad[4] = clasVJ.clasificar(img);
            
            sexo = isHom.getData()[0]>isMuj.getData()[0];
            int indexEdad = 0;
            double edadMax = -1;
            for(int i = 0; i<resulEdad.length; i++){
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
