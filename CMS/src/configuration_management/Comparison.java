package configuration_management;
import java.io.*;
import java.lang.Runtime.*;
import java.util.List;
import java.util.ArrayList;
import java.math.BigInteger;
public class Comparison {     
     List<String> files1=new ArrayList<String>();
     List<String> files2=new ArrayList<String>();
     List<String> added_files=new ArrayList<String>();
     List<String> deleted_files=new ArrayList<String>();
     List<String> results=new ArrayList<String>();
     String mydir1=null,mydir2=null;
    public void doComparison(String dir1,String dir2){
        try{
                if(!(dir1.equalsIgnoreCase("Select File1"))&&!(dir2.equalsIgnoreCase("Select File 2"))&&!(dir1.equalsIgnoreCase("You pressed cancel"))&&!(dir2.equalsIgnoreCase("You pressed cancel")))
                {
                mydir1=dir1;
                mydir2=dir2;
                File f1=new File("C:\\Folder1");
                f1.mkdirs();
                File f2=new File("C:\\Folder2");
                f2.mkdirs();
                getFnames(dir1,files1);
                getFnames(dir2,files2);
                System.out.println("calling prepare file for v1");
                for(String file1:files1){
			prepareFile("C:\\Folder1"+file1.substring(file1.lastIndexOf("\\")),file1);                       
                }
                System.out.println("calling prepare file for v2");
                for(String file2:files2){
			prepareFile("C:\\Folder2"+file2.substring(file2.lastIndexOf("\\")),file2);
		}
                String s=null,mydir1="C:\\Folder1",mydir2="C:\\Folder2";
                int myflag=0;
                Runtime run=Runtime.getRuntime();
                String[] faFiles = new File(mydir1).list();
                System.out.println("calling diff");
                for(int v=0;v<faFiles.length;v++)
                {		
                    String[] temp = new File(mydir2).list();                   
                    for(int w=0;w<temp.length;w++)
                    {
		        if(faFiles[v].compareToIgnoreCase(temp[w])==0)
                        {
                            String cmd="diff C:\\Folder1\\"+faFiles[v]+" C:\\Folder2\\"+temp[w];
			    Process p=run.exec(cmd);
                            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                            BufferedWriter bw=new BufferedWriter(new FileWriter("diff_result.txt"));
                            while ((s = br.readLine()) != null)
                            {
                                if(!(s.startsWith("<")||s.startsWith("\\")||s.startsWith(">")||s.startsWith("-")))
                                {
                                    bw.write(s);
                                    bw.newLine();
                                }
                            }
                            bw.close();
                            p.destroy();
                            processResult(faFiles[v]);
                        }
                    }
                    
                }
                System.out.println("call to diff ended");
                System.out.println("writting results to text file");
                BufferedWriter bw=new BufferedWriter(new FileWriter("results.txt"));
                bw.write("classname,added lines,deleted lines,modified lines,total");
                bw.newLine();
                for(String line:results)
                {
                   bw.write(line);
                   bw.newLine();
                }
                bw.close();
                //to get the list of added and deleted classes.
                List<String> tempFiles1=new ArrayList<String>();
                List<String> tempFiles2=new ArrayList<String>();
                System.out.println("evaluavating no of added and deleted classes.");
                getFilenames("C:\\Folder1",tempFiles1);
                getFilenames("C:\\Folder2",tempFiles2);
                for(String file:tempFiles1){
                    file=file.substring(file.lastIndexOf("\\"));
                    int flag=1;
                    for(String file1:tempFiles2)
                    {
                        file1=file1.substring(file1.lastIndexOf("\\"));
                        if(file.compareTo(file1)==0)
                            flag=0;
                    }
                    if(flag==1)
                        deleted_files.add(file);
                }
                for(String file:tempFiles2){
                    file=file.substring(file.lastIndexOf("\\"));
                    int flag=1;
                    for(String file1:tempFiles1)
                    {
                        file1=file1.substring(file1.lastIndexOf("\\"));
                        if(file.compareTo(file1)==0)
                            flag=0;
                    }
                    if(flag==1)
                        added_files.add(file);
                }
                System.out.println("Number of added classes : "+added_files.size());
                System.out.println("Number of deleted classes : "+deleted_files.size());                
                vacateFolder("C:\\Folder1");
                vacateFolder("C:\\Folder2");
                boolean success = new File("C:\\Folder1").delete();
                success = new File("C:\\Folder2").delete();
        }
                else
                {
                    System.out.println("No File Choosen");
                    BufferedWriter bw1=new BufferedWriter(new FileWriter("myFlag"));
                   bw1.write("flag to check if no file is choosen");
                   bw1.close();
                }
        }
        catch(Exception e)
        {
            System.out.println("Exception in method doComparison");
            e.printStackTrace();
        }
    }

    public void getFnames(String sDir, List myFileList){
        File[] faFiles = new File(sDir).listFiles();        
        for(File file : faFiles){
            if(file.isDirectory())
                getFnames(file.getAbsolutePath(),myFileList);
            if((!file.isDirectory())&&(file.toString().endsWith("java")))
                myFileList.add(file.toString());
        }
}
    public void getFilenames(String sDir, List myFileList){        
        File[] faFiles = new File(sDir).listFiles();
        for(File file : faFiles){
                myFileList.add(file.toString());
        }
}
    private void processResult(String filename)
    {
        BigInteger modified=BigInteger.ZERO,added=BigInteger.ZERO,deleted=BigInteger.ZERO;
        //System.out.println("in method processResult");
        try
        {
            BufferedReader br = new BufferedReader (new FileReader ("diff_result.txt"));
            String s1=null;            
            while((s1=br.readLine())!=null)
            {                
                if(s1.contains("a"))
                {
                    if(s1.contains(","))
                    {
                        int i,j=0,k=0;
                        for(i=s1.indexOf('a')+1;i<s1.lastIndexOf(',');i++)
                                j=10*j+Character.getNumericValue(s1.charAt(i));
                        for(i=s1.lastIndexOf(',')+1;i<s1.length();i++)
                                k=10*k+Character.getNumericValue(s1.charAt(i));
                        added=added.add(BigInteger.valueOf(k-j+1));
                    }
                    else
                        added=added.add(BigInteger.ONE);
                }
                if(s1.contains("c"))
                { //check the code for mofified lines as it gives - several times reason being the case with 2 commas is not handled here.
                    if(s1.contains(","))
                    {
                            int i,j=0,k=0;
                            if(s1.indexOf(',')==s1.lastIndexOf(','))
                            {
                                if(s1.indexOf(',')<s1.indexOf('c'))
                                {
                                    for(i=0;i<s1.indexOf(",");i++)
                                        j=10*j+Character.getNumericValue(s1.charAt(i));
                                    for(i=s1.indexOf(",")+1;i<s1.indexOf('c');i++)
                                        k=10*k+Character.getNumericValue(s1.charAt(i));
                                    modified=modified.add(BigInteger.valueOf(k-j+1));
                                }
                                else
                                {
                                     for(i=s1.indexOf('c')+1;i<s1.lastIndexOf(',');i++)
                                        j=10*j+Character.getNumericValue(s1.charAt(i));
                                     for(i=s1.lastIndexOf(',')+1;i<s1.length();i++)
                                        k=10*k+Character.getNumericValue(s1.charAt(i));
                                     modified=modified.add(BigInteger.valueOf(k-j+1));
                                }
                            }
                            else
                            {
                                //code if it contains 2 commas
                                    for(i=s1.indexOf('c')+1;i<s1.lastIndexOf(',');i++)
                                        j=10*j+Character.getNumericValue(s1.charAt(i));
                                     for(i=s1.lastIndexOf(',')+1;i<s1.length();i++)
                                        k=10*k+Character.getNumericValue(s1.charAt(i));
                                     modified=modified.add(BigInteger.valueOf(k-j+1));
                            }
                    }
                    else
                        modified=modified.add(BigInteger.ONE);
                }
                if(s1.contains("d"))
                {
                    if(s1.contains(","))
                    {
                        int i,j=0,k=0;
                        for(i=0;i<s1.indexOf(",");i++)
                            j=10*j+Character.getNumericValue(s1.charAt(i));
                        for(i=s1.indexOf(",")+1;i<s1.indexOf('d');i++)
                            k=10*k+Character.getNumericValue(s1.charAt(i));
                        deleted=deleted.add(BigInteger.valueOf(k-j+1));
                    }
                    else                        
                        deleted=deleted.add(BigInteger.ONE);
                }
            }
            br.close();
            BigInteger total=added.add(deleted).add(modified.multiply(BigInteger.valueOf(2)));
            String temp=filename+","+added.toString()+","+deleted.toString()+","+modified.toString()+","+total.toString();
            results.add(temp);
        }
        catch(Exception e)
        {
            System.out.println("exception in method processResult : "+e);
        }
    }
    private void vacateFolder(String mydir)
    {
        try
        {
            File[] faFiles = new File(mydir).listFiles();
            for(File file : faFiles)
            {
                if(file.isDirectory())
                    vacateFolder(file.getAbsolutePath().toString());
                file.delete();
            }

        }
        catch(Exception e)
        {
            System.out.println("exception in method vacateFolder");
            e.printStackTrace();
        }
    }
    private void prepareFile(String file1,String file2)
    {
        try{            
            BufferedWriter bw1=new BufferedWriter(new FileWriter("temp.txt"));
            BufferedReader br1 = new BufferedReader (new FileReader (file2));
            String newline = System.getProperty("line.separator");
            String z="";
outer:
            while((z=br1.readLine())!=null)
            {
                while(
				z.trim().isEmpty()
				||z.trim().startsWith("//")
				||z.trim().length()==0
				||z.trim().equals(newline))
                    {
		        z=br1.readLine();
			if(z==null)break outer;
                    }
                if(z.trim().startsWith("/*"))
                    {
                        while(!(z.trim().endsWith("*/")||z.trim().contains("*/")))
                        {
                            z = br1.readLine();
                        }
                        if(z.endsWith("*/"))
                            z=br1.readLine();
                        else
                            z=z.substring(z.lastIndexOf("*/"));
                        if(z==null)break outer;
                    }
                while(z.trim().isEmpty()||z.trim().startsWith("//")||z.trim().length()==0||z.trim().equals(newline))
                {
                    z=br1.readLine();
                    if(z==null)break outer;
                }
                bw1.write(z.trim());	
                bw1.newLine();
            }
            bw1.close();
            br1.close();
            /*to arrange the code classwise*/
            BufferedReader br2=new BufferedReader (new FileReader ("temp.txt"));
            int flag=0;
            BufferedWriter[] bw2=new BufferedWriter[500];
            while((z=br2.readLine())!=null)
            {		
                if((z.contains(" class ")||z.startsWith("class "))&&!(z.contains("?")||(z.contains(";"))||(z.contains("*"))||z.contains("\"")||z.contains("(")||z.contains(")")))
                {
                    
                    String classname=z.substring(z.indexOf("class")+6).trim();
                    if(classname.contains(" "))
                         classname=classname.substring(0,classname.indexOf(" ")).trim();
                    if(classname.contains("{"))
                        classname=classname.substring(0,classname.indexOf("{")-1);
                    if(classname.contains("<"))
                        classname=classname.substring(0,classname.indexOf("<")-1);
                    if(file1.contains("Folder1")){
                            String f=file2.substring(mydir1.length()+1);
                            String f1=null,f2=null;
                            int i=0;
                            while(f.contains("\\"))
                            {
                                 i=1;
                                f2=f.substring(f.lastIndexOf("\\")+1);
                                if(f1==null)
                                    f1=f2;
                                else
                                    f1=f2.concat("-").concat(f1);
                                f=f.substring(0,f.lastIndexOf("\\"));
                            }
                            if(i==1){
                            f1=f.concat("-").concat(f1);
                            f1=f1.substring(0,f1.lastIndexOf("."));}
                           if(f1!=null)
                                bw2[flag]=new BufferedWriter(new FileWriter("C:\\Folder1\\"+f1+"-"+classname+".java"));
                            else
                                bw2[flag]=new BufferedWriter(new FileWriter("C:\\Folder1\\"+classname+".java"));			    
		    }
                    else{
                            String f=file2.substring(mydir2.length()+1);
                            String f1=null,f2=null;
                            int i=0;
                            while(f.contains("\\"))
                            {
                                i=1;
                                f2=f.substring(f.lastIndexOf("\\")+1);
                                if(f1==null)
                                    f1=f2;
                                else
                                    f1=f2.concat("-").concat(f1);
                                f=f.substring(0,f.lastIndexOf("\\"));
                            }
                            if(i==1){
                                f1=f.concat("-").concat(f1);
                            f1=f1.substring(0,f1.lastIndexOf("."));   }
                            if(f1!=null)
                            bw2[flag]=new BufferedWriter(new FileWriter("C:\\Folder2\\"+f1+"-"+classname+".java"));
                            else
                                bw2[flag]=new BufferedWriter(new FileWriter("C:\\Folder2\\"+classname+".java"));
		    }
                    flag++;
                }
                if(flag>0)
                {
                    bw2[flag-1].write(z);
                    bw2[flag-1].newLine();
                }
            }
            br2.close();
            for(int i=0;i<flag;i++)
                bw2[i].close();
        }
        catch(Exception e)
        {
            System.out.println("Exception in method prepareFile"+file1);
            e.printStackTrace();
        }
    }
}