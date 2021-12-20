/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package configuration_management;

import java.io.*;

/**
 *
 * @author anushree
 */
public class metricsCalculation{
public String result="class WMC DIT NOC CBO RFC LCOM Ce NPM\n";

public void writeresult()
    {
    try{
            BufferedWriter bw=new BufferedWriter(new FileWriter("metrics.csv"));
            bw.write(result);
            bw.close();
        }
    catch(Exception e)
    {
        e.printStackTrace();
    }

}
public void callforcalculation(String dir)
    {
    File[] faFiles = new File(dir).listFiles();
        for(File file : faFiles){
            if(file.isDirectory())
                callforcalculation(file.getAbsolutePath());
            if(!(file.isDirectory())&&file.toString().endsWith(".class"))
                docalculation(file.toString());
        }
    }

public void docalculation(String dir)
    {
    try
    {
        Runtime run=Runtime.getRuntime();
        String cmd="java -jar C:\\Users\\anushree\\Documents\\NetBeansProjects\\ckjm-1.9\\build\\ckjm-1.9.jar "+dir;
        String s=null;
        Process p=run.exec(cmd);
        
        BufferedReader br1 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        BufferedReader br2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((s = br1.readLine()) != null)
        {
            System.out.println(s);
        }
        String s1=" ";
        while ((s1 = br2.readLine()) != null)
        {            
            result=result.concat(s1).concat("\n");
        }
        p.destroy();
        //System.out.println("final result: "+result);
        br1.close();
        br2.close();
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}
}
