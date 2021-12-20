package configuration_management;
import java.io.*;
public class getLogFile {   
    getLogFile(String url,String module)
    {
        try
        {
                    Runtime run1=Runtime.getRuntime();
                    Runtime run2=Runtime.getRuntime();
                    String s=null,filename="logs.txt";
                    String cmd1="cvs -z3 -d:pserver:anonymous@"+url+" co -P "+module;
                    String cmd2="cvs -d:pserver:anonymous@"+url+" log "+module;
                    Process p1=run1.exec(cmd1);
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
                    System.out.println("checking in from repository");
                    while ((s = br1.readLine()) != null)
                    {
                        System.out.println(s);
                    }
                    System.out.println("check in completed");
                    br1.close();
                    p1.destroy();
                    Process p2=run2.exec(cmd2);
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
                    BufferedWriter bw2=new BufferedWriter(new FileWriter(filename));
                    while ((s = br2.readLine()) != null)
                    {
                        s=s.toLowerCase();
                        if(s.contains(" bug")||s.contains("fix")||s.startsWith("rcs file")||s.startsWith("bug"))
                        {
                            bw2.write(s);
                            bw2.newLine();
                            System.out.println(s);
                        }
                    }                    
                    br2.close();
                    bw2.close();
                    System.out.println("logs written in "+filename);
                    p2.destroy();
                    BufferedReader br3=new BufferedReader(new FileReader(filename));
                    BufferedWriter bw3=new BufferedWriter(new FileWriter("bug-data.txt"));
                    String s1=null;
                    int count=0;
                    s=null;
                    bw3.write("classname,v,bugcount");
                    bw3.newLine();
                    while((s=br3.readLine())!=null)
                    {
                        if(s.contains("rcs file"))
                        {
                            if(s1!=null){
                            s1=s1.concat(",")+count;
                            bw3.write(s1);
                            bw3.newLine();}
                            s1=s;
                            count=0;
                        }
                        else
                            count++;
                    }
                    br3.close();
                    bw3.close();
                    }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}


