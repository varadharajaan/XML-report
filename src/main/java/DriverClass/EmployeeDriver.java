package DriverClass;

import ExternalField.AddressAdder;
import ExternalField.AgeUpdater;
import ExternalField.PhoneNumberAdder;
import modify.Printer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import parser.ReadUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDriver {

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = dbFactory.newDocumentBuilder();

           ReadUtils readUtils = ReadUtils.getInstance();
           Printer printer = Printer.getInstance();

            Document doc = parser.parse("report/employees.xml");
            doc.getDocumentElement().normalize();

            List<String> employeesTobeDeleted = List.of("PEER002","PEER005");
            // Get the document's root XML node
            NodeList root = doc.getChildNodes();

            // Navigate down the hierarchy to get to the CEO node
            Node comp = readUtils.getNode("employees", root);
            NodeList employeeList = doc.getElementsByTagName("employee");

            List<Node> deleteNodes = new ArrayList<>();
            for (int i = 0; i < employeeList.getLength(); i++) {

                Node employee = employeeList.item(i);
                String employeeId  =  readUtils.getNodeAttr("id", employee);

                NodeList employeeNodes = employee.getChildNodes();

                String name = readUtils.getNodeValue("name", employeeNodes);
                String age = readUtils.getNodeValue("age", employeeNodes);
                String designation = readUtils.getNodeValue("designation", employeeNodes);

                //delete user
                if(employeesTobeDeleted.contains(employeeId)){
                    deleteNodes.add(employee);
                }

                // xmlparser.modify and add user
                if(name.equals("Mohan"))
                {
                    AddressAdder addressAdder = new AddressAdder();
                    Element addr = doc.createElement("address");
                    addressAdder.addNode("door","23", addr);
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
                    ageUpdater.setNodeValue("age" ,"35" , employeeNodes);
                    printer.writeToOutputStream(doc);
                }

            }

            if(!deleteNodes.isEmpty()) {
                deleteNodes.forEach(comp::removeChild);
            }

            Node nameNode = readUtils.getNode("name", comp.getChildNodes());
            String companyName = readUtils.getNodeValue(nameNode);
            printer.writeToOutputStream(doc);

        }

}