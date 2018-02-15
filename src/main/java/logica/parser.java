/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.util.List;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author 2118270
 */
public class parser {
    public Element proceso;
    public List<Element> actores = new ArrayList<Element>();
    public List<Element> taskes = new ArrayList<Element>();
    public List<Element> events = new ArrayList<Element>();
    public List<Element> reglas = new ArrayList<Element>();
    
    /**
     * @param args the command line arguments
     */
    public  void leer (File ruta) {
        try {
        String filename = ruta.toString();
        File fXmlFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);        
        doc.getDocumentElement().normalize();
        NodeList collaboration = doc.getElementsByTagName("model:collaboration");
        Node colaboradores = collaboration.item(0);
        NodeList participantes = colaboradores.getChildNodes();
        NodeList process = doc.getElementsByTagName("model:process");
        Node componentes = process.item(0);
        NodeList todos = componentes.getChildNodes();
        
        for (int temp = 0; temp < participantes.getLength(); temp++) {
		Node nNode = participantes.item(temp);          
		
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {                    
			Element eElement = (Element) nNode;
                        if (eElement.hasAttribute("processRef")){
                            proceso=eElement;
                        }else{
                            
                            actores.add(eElement);                         
                                                  
                                               
                        }			                      
		}
	}    
        
        for (int temp = 0; temp < todos.getLength(); temp++) {
		Node nNode = todos.item(temp);	
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {                       
			Element eElement = (Element) nNode;
                        if (eElement.getNodeName().contains("Task") || eElement.getNodeName().equals("model:callActivity") ){                            
                            taskes.add(eElement);                            
                        }
                        if (eElement.getNodeName().contains("Event")){                           
                            events.add(eElement);                  
			                      
		        }
                        if (eElement.getNodeName().contains("exclusiveGateway")){                           
                            reglas.add(eElement);                  
			                      
		        }
                        
	         }
        }       
        
        } catch (Exception e){
            e.printStackTrace();
        }
        
        // TODO code application logic here
    }
    public String docuementar() {
       
        String html;
        String tempo= "";
        String documentacion="";
        html= "<html> <head> <title> BPMNDoc </title> </head> <body bgcolor='wheat'>	<h1> Proceso:";
        html+= proceso.getAttribute("name") + "</h1>";
        
        documentacion=getdocumentacion(proceso);  
        if (!documentacion.equals("")){
            html+= "<p>" + documentacion + "</p>";            
        }
       
        html+= "<h2> Actores </h2> <TABLE BORDER> <TR> <TH>Identificador</TH> <TH>Nombre del actor</TH> <TH>Descripcion corta</TH> </TR>";
        tempo="";       
       
        for (int i=0 ; i< actores.size(); ++i){            
            tempo+="<TR> <TD> Actor"+(i+1)+"</TD>";
             
            tempo+="<TD>"+actores.get(i).getAttribute("name")+"</TD>";
            documentacion=getdocumentacion(actores.get(i));          
            
            if (!documentacion.equals("")){
                
                tempo+= "<TD>" + documentacion + "</TD> </TR>";  
                
            }else {               
                tempo+="<TD> </TD> </TR>";
                
            }    
        }
       
      
        html+= tempo+ "</TABLE>  <h2> Tareas </h2> <TABLE BORDER> <TR> <TH>Identificador</TH> <TH>Nombre de la tarea</TH> <TH>Descripcion corta</TH> <TH>Tipo</TH> </TR>";
        tempo="";
        
        for (int i=0 ; i< taskes.size(); ++i){  
             
           tempo+="<TR> <TD> AC-"+(i+1)+"</TD>";
           tempo+="<TD>"+taskes.get(i).getAttribute("name")+"</TD>";
            documentacion=getdocumentacion(taskes.get(i));          
            
            if (!documentacion.equals("")){
               
                tempo+= "<TD>" + documentacion + "</TD>";             
           }else {
               tempo+="<TD> </TD>";
           }
           tempo+="<TD>" + taskes.get(i).getNodeName().replace("model:", "")+"</TD> </TR>" ;           
        }
        html+= tempo+ "</TABLE>  <h2> Eventos  </h2> <TABLE BORDER> <TR> <TH>Identificador</TH> <TH>Nombre del evento</TH> <TH>Descripcion corta</TH> <TH>Tipo</TH> </TR>";
        tempo="";        
        for (int i=0 ; i< events.size(); ++i){           
           tempo+="<TR> <TD> Ev-"+(i+1)+"</TD>";
           tempo+="<TD>"+events.get(i).getAttribute("name")+"</TD>";
           documentacion=getdocumentacion(events.get(i));         
            
            if (!documentacion.equals("")){
                
                tempo+= "<TD>" + documentacion + "</TD> ";             
           }else {
               tempo+="<TD> </TD>";
           }
           tempo+="<TD>" + events.get(i).getNodeName().replace("model:", "")+"</TD> </TR>" ;           
        }
        html+= tempo+ "</TABLE>  <h2> Reglas de negocio  </h2> <TABLE BORDER> <TR> <TH>Identificador</TH> <TH>Nombre del regla</TH> <TH>Descripcion corta</TH>  </TR>";
        
        tempo="";        
        for (int i=0 ; i< reglas.size(); ++i){           
           tempo+="<TR> <TD> RN-"+(i+1)+"</TD>";
           tempo+="<TD>"+reglas.get(i).getAttribute("name")+"</TD>";
           documentacion=getdocumentacion(reglas.get(i));         
            
            if (!documentacion.equals("")){
                
                tempo+= "<TD>" + documentacion + "</TD>  ";             
           }else {
               tempo+="<TD> </TD> </TR>";
           }                    
        }       
        
        
        html+= tempo+"</TABLE> </body> </html>";
        
        return html;
    }
    public String getdocumentacion(Element e){
        NodeList documentacion= e.getElementsByTagName("model:documentation"); 
        if (documentacion.getLength()>0){
                return documentacion.item(0).getTextContent();
                
        }
        return "";      
                
          
    }
    
}
