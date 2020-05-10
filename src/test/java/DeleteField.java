
import ExternalField.AgeUpdater;
import modify.FieldAdder;
import modify.Printer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import parser.ReadUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Author Varadharajan on 10/05/20 03:19
 * @Projectname EmployeeParser
 */
public class DeleteField {

    DocumentBuilderFactory dbFactory ;
    DocumentBuilder parser ;
    ReadUtils readUtils ;
    Printer printer ;
    Document doc ;

    @BeforeEach
    public void setUp() throws ParserConfigurationException, IOException, SAXException {

        dbFactory = DocumentBuilderFactory.newInstance();
        parser = dbFactory.newDocumentBuilder();
        readUtils = ReadUtils.getInstance();
        printer = Printer.getInstance();
        doc = parser.parse("report/employees.xml");
        doc.getDocumentElement().normalize();
    }

    @Test
    public  void deletefieldPresent() throws Exception {

        List<String> employeesTobeDeleted = List.of("PEER002","PEER001");

        // Get the document's root XML node
        NodeList root = doc.getChildNodes();

        // Navigate down the hierarchy to get to the CEO node
        Node comp = readUtils.getNode("employees", root);
        NodeList employeeList = doc.getElementsByTagName("employee");

        int countBeforeDelete = employeeList.getLength();
        List<Node> deleteNodes = new ArrayList<>();
        for (int i = 0; i < employeeList.getLength(); i++) {

            Node employee = employeeList.item(i);
            String employeeId  =  readUtils.getNodeAttr("id", employee);

            NodeList employeeNodes = employee.getChildNodes();

            String name = readUtils.getNodeValue("name", employeeNodes);

            //delete user
            if(employeesTobeDeleted.contains(employeeId)){
                deleteNodes.add(employee);
            }
        }

        if(!deleteNodes.isEmpty()) {
            deleteNodes.forEach(comp::removeChild);
        }

        Node nameNode = readUtils.getNode("name", comp.getChildNodes());
        String companyName = readUtils.getNodeValue(nameNode);
        System.out.println(companyName);

        FieldAdder adder = new AgeUpdater();
        adder.addNode("doorNo","23",comp);

        int countAfterDelete = employeeList.getLength();
        final String s =printer.printAsString(doc);

        assertEquals(countBeforeDelete,3);
        assertEquals(countAfterDelete,1);

        assertTrue(s.contains("Rajesh"));

    }

    @Test
    public  void deleteFieldNotPresent() throws TransformerException {

        List<String> employeesTobeDeleted = List.of("PEER006","PEER008");
        // Get the document's root XML node
        NodeList root = doc.getChildNodes();

        // Navigate down the hierarchy to get to the CEO node
        Node comp = readUtils.getNode("employees", root);
        NodeList employeeList = doc.getElementsByTagName("employee");

        int countBeforeDelete = employeeList.getLength();
        List<Node> deleteNodes = new ArrayList<>();
        for (int i = 0; i < employeeList.getLength(); i++) {

            Node employee = employeeList.item(i);
            String employeeId  =  readUtils.getNodeAttr("id", employee);

            NodeList employeeNodes = employee.getChildNodes();

            String name = readUtils.getNodeValue("name", employeeNodes);

            //delete user
            if(employeesTobeDeleted.contains(employeeId)){
                deleteNodes.add(employee);
            }
        }

        if(!deleteNodes.isEmpty()) {
            deleteNodes.forEach(comp::removeChild);
        }

        Node nameNode = readUtils.getNode("name", comp.getChildNodes());
        String companyName = readUtils.getNodeValue(nameNode);
        System.out.println(companyName);

        int countAfterDelete = employeeList.getLength();
        final String s =printer.printAsString(doc);

        assertEquals(countBeforeDelete,3);
        assertEquals(countAfterDelete,3);

        assertTrue(s.contains("Anitha")&&s.contains("Rajesh") && s.contains("Mohan"));

    }


}
