/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escom.ibhi.resource.Utileria;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author isaac
 */
public class ScriptExecutor {

    public void ejecutarScript(String pythonScriptPath) {
        try {
            String[] cmd = new String[2];
            cmd[0] = "python";
            cmd[1] = pythonScriptPath;

            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(cmd);

//// Recibir  info de ejecucion del script
//            BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
//            String line = "";
//            while ((line = bfr.readLine()) != null) {
//// Mostrar cada linea
//                System.out.println(line);
//            }

        } catch (Exception e) {
            System.err.println("ERROR!!!");
            e.printStackTrace();
        }

    }

}
