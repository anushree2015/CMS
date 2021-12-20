package configuration_management;
import java.io.*;
import java.io.FileWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class severity_extraction {
  public void extraction(String s1,String s2) {
      try{
        //File f1 = new File("C:/Users/anushree/Desktop/configuration management/defect+severity/useful data/eclipse.tar/eclipse/CDT/severity.xml");
        //File f2 = new File("C:/Users/anushree/Desktop/configuration management/defect+severity/useful data/eclipse.tar/eclipse/CDT/component.xml");
        File f1=new File(s1);
        File f2=new File(s2);
        BufferedWriter bw1=new BufferedWriter(new FileWriter("severity-report.txt"));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(f1);
        NodeList list = doc.getElementsByTagName("report");
        int len1=list.getLength();
        String [][]severity=new String[3][len1];
        myXMLparser(f1,severity);
        doc=docBuilder.parse(f2);
        list = doc.getElementsByTagName("report");
        int len2=list.getLength();
        String [][]component=new String[3][len2];
        myXMLparser(f2,component);
        for(int i=0;i<len1;i++)
        {
            for(int j=0;j<len2;j++)
            {
                if((severity[0][i].equals(component[0][j]))&&(severity[1][i].equals(component[1][j])))  
                {
                        bw1.write(severity[0][i]+" , "+severity[2][i]+" , "+component[2][j]);
                        bw1.newLine();
                }
            }
        }
        bw1.close();
      }
      catch(Exception e)
      {
          e.printStackTrace();
      }
  }
public static void myXMLparser(File f,String [][]arr)
{
     try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(f);
        NodeList list = doc.getElementsByTagName("report");
        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element)list.item(i);
            String nodeName = element.getNodeName();
            if(nodeName.equals("report"))
            {
                NodeList upd = element.getElementsByTagName("update");
                for(int j=0;j<upd.getLength();j++)
                {
                    Element elem=(Element)upd.item(j);
                    String node=elem.getNodeName();
                    if(node.equals("update"))
                    {
                        NodeList wen=elem.getElementsByTagName("when");
                        NodeList wat=elem.getElementsByTagName("what");
                        Element e1=(Element)wen.item(0);
                        Element e2=(Element)wat.item(0);
                        arr[0][i]=element.getAttribute("id");
                        arr[1][i]=e1.getTextContent();
                        arr[2][i]=e2.getTextContent();
                    }
                }
            }
        }
	}
    catch (Exception e) {
	e.printStackTrace();
    }
}
}
