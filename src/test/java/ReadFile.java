
import DriverClass.EmployeeDriver;
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author Varadharajan on 10/05/20 02:57
 * @Projectname EmployeeParser
 */
public class ReadFile {

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
    public void readFileSuccessFully() throws Exception {

        NodeList root = doc.getChildNodes();

        Node comp = readUtils.getNode("employees", root);
        NodeList employeeList = doc.getElementsByTagName("employee");

        for (int i = 0; i < employeeList.getLength(); i++) {

            Node employee = employeeList.item(i);
            String employeeId  =  readUtils.getNodeAttr("id", employee);
            assertNotNull(employeeId);
            readUtils.getNodeAttr("employee", "id" ,employeeList );

            NodeList employeeNodes = employee.getChildNodes();

            String name = readUtils.getNodeValue("name", employeeNodes);
            assertNotNull(name);
            String age = readUtils.getNodeValue("age", employeeNodes);
            assertNotNull(age);
            String designation = readUtils.getNodeValue("designation", employeeNodes);
            assertNotNull(designation);
        }

        Node nameNode = readUtils.getNode("name", comp.getChildNodes());
        String companyName = readUtils.getNodeValue(nameNode);
        assertEquals(companyName,"Peer Island");
        final String s = printer.printAsString(doc);
        assertTrue(s.contains("Anitha"));
        assertTrue(s.contains("PEER003"));

        EmployeeDriver em = new EmployeeDriver();
        em.main(null);

        Node not_found_node = readUtils.getNode("Not_Found" , employeeList);

        assertNull(not_found_node);

        String not_found = readUtils.getNodeValue("not_found", employeeList.item(2).getChildNodes().item(2).getChildNodes());

        assertEquals(not_found,"");

        not_found = readUtils.getNodeAttr("not_found",nameNode);

        assertEquals(not_found,"");

        not_found = readUtils.getNodeAttr("not_found", "not_found_value" , employeeList );

        assertEquals(not_found,"");

    }

    @Test
    public void fieldnotFoundNullPointer() {
        assertThrows(NullPointerException.class, () -> {

            NodeList root = doc.getChildNodes();

             Node comp =readUtils.getNode("users", root);
            NodeList employeeList = doc.getElementsByTagName("employee");

            for (int i = 0; i < employeeList.getLength(); i++) {

                Node employee = employeeList.item(i);

                NodeList employeeNodes = employee.getChildNodes();

               readUtils.getNodeValue("name", employeeNodes);

                Node nameNode = readUtils.getNode("name", comp.getChildNodes());

            }
        });

    }
}
