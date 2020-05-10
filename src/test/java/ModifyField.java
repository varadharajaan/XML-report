import ExternalField.AddressAdder;
import ExternalField.AgeUpdater;
import ExternalField.PhoneNumberAdder;
import modify.Printer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Author Varadharajan on 10/05/20 03:31
 * @Projectname EmployeeParser
 */
public class ModifyField {

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
    public void modifyFieldWithAddress() throws Exception {

        String beforeUpdate = printer.printAsString(doc);
        // Get the document's root XML node
        NodeList root = doc.getChildNodes();

        // Navigate down the hierarchy to get to the CEO node
        readUtils.getNode("employees", root);
        NodeList employeeList = doc.getElementsByTagName("employee");

        for (int i = 0; i < employeeList.getLength(); i++) {

            Node employee = employeeList.item(i);
            String employeeId  =  readUtils.getNodeAttr("id", employee);

            NodeList employeeNodes = employee.getChildNodes();

            String name = readUtils.getNodeValue("name", employeeNodes);

            // xmlparser.modify and add user
            if(name.equals("Rajesh"))
            {
                AddressAdder addressAdder = new AddressAdder();
                Element addr = doc.createElement("address");
                addressAdder.addNode("doorNo","23", addr);
                addressAdder.addNode("town","chennai",addr);
                addressAdder.addNode("state" ,"tamilnadu", addr);
                employee.appendChild(addr);
                printer.writeToOutputStream(doc);

            }

            if(employeeId.equals("PEER001")) {
                PhoneNumberAdder phoneNumberField = new PhoneNumberAdder();
                phoneNumberField.addNode("phonenumber","8754152743", employee);
                printer.writeToOutputStream(doc);
            }

            if(employeeId.equals("PEER001")) {
                AgeUpdater ageUpdater = new AgeUpdater();
                ageUpdater.setNodeValue("age", "35", employeeNodes);
                printer.writeToOutputStream(doc);
                ageUpdater.setNodeValue("not_found", "not_found", employeeNodes);
                ageUpdater.setNodeValue("not_found", "not_found", employee.getChildNodes().item(4).getChildNodes());
            }

        }

        final String afterUpdate = printer.printAsString(doc);

        assertFalse(beforeUpdate.contains("address") || beforeUpdate.contains("doorNo") || beforeUpdate.contains("23") ||
                beforeUpdate.contains("town") || beforeUpdate.contains("chennai") || beforeUpdate.contains("state") || beforeUpdate.contains("tamilnadu"));

        assertTrue(afterUpdate.contains("address")&& afterUpdate.contains("doorNo") && afterUpdate.contains("23") &&
                    afterUpdate.contains("town") && afterUpdate.contains("chennai") && afterUpdate.contains("state") && afterUpdate.contains("tamilnadu"));


    }

    @Test
    public void addNewField() throws Exception {

        String beforeUpdate = printer.printAsString(doc);
        // Get the document's root XML node
        NodeList root = doc.getChildNodes();

        // Navigate down the hierarchy to get to the CEO node
        readUtils.getNode("employees", root);
        NodeList employeeList = doc.getElementsByTagName("employee");

        for (int i = 0; i < employeeList.getLength(); i++) {

            Node employee = employeeList.item(i);
            String employeeId  =  readUtils.getNodeAttr("id", employee);

            if(employeeId.equals("PEER001")) {
                PhoneNumberAdder phoneNumberField = new PhoneNumberAdder();
                phoneNumberField.addNode("phonenumber","8754152743", employee);
                printer.writeToOutputStream(doc);
            }

        }

        final String afterUpdate = printer.printAsString(doc);
        assertFalse(beforeUpdate.contains("8754152743"));
        assertTrue(afterUpdate.contains("8754152743"));
    }

    @Test
    public void updateExistingField() throws Exception {

        String beforeUpdate = printer.printAsString(doc);
        // Get the document's root XML node
        NodeList root = doc.getChildNodes();

        // Navigate down the hierarchy to get to the CEO node
        readUtils.getNode("employees", root);
        NodeList employeeList = doc.getElementsByTagName("employee");

        for (int i = 0; i < employeeList.getLength(); i++) {

            Node employee = employeeList.item(i);
            NodeList employeeNodes = employee.getChildNodes();
            String employeeId  =  readUtils.getNodeAttr("id", employee);

            if(employeeId.equals("PEER001")) {
                AgeUpdater ageUpdater = new AgeUpdater();
                ageUpdater.setNodeValue("age" ,"35" , employeeNodes);
                printer.writeToOutputStream(doc);
            }

        }

        final String afterUpdate = printer.printAsString(doc);
      assertTrue(beforeUpdate.contains("25"));
      assertFalse(beforeUpdate.contains("35"));

      assertFalse(afterUpdate.contains("25"));
      assertTrue(afterUpdate.contains("35"));

    }
}
