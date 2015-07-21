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

    public void ejecutarScript() {
        try {

            String pythonScriptPath = "/home/isaac/test1.py";
            String[] cmd = new String[2];
            cmd[0] = "python";
            cmd[1] = pythonScriptPath;

// create runtime to execute external command
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(cmd);

// retrieve output from python script
            BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line = "";
            while ((line = bfr.readLine()) != null) {
// display each output line form python script
                System.out.println(line);
            }

        } catch (Exception e) {
            System.err.println("ERROR!!!");
            e.printStackTrace();
        }

    }

}
