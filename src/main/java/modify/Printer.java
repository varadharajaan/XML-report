package modify;

import org.w3c.dom.Document;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;

/**
 * @Author Varadharajan on 10/05/20 01:42
 * @Projectname EmployeeParser
 */
public class Printer {

    private Printer() {
    }

    private static volatile Printer instance;

    public static Printer getInstance() {
        if (instance == null) {
            synchronized (Printer .class) {
                if (instance == null) {
                    instance = new Printer();
                }
            }
        }
        return instance;
    }

    public void writeToOutputStream(Document doc) throws Exception {
        printToConsole(doc);
        printToFile(doc);
        printAsString(doc);
    }

    public String printAsString(Document doc) throws TransformerException {
        DOMSource docSource = new DOMSource(doc);
        doc.setXmlStandalone(true);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter sw = new StringWriter();
        StreamResult string = new StreamResult(sw);
        transformer.transform(docSource, string);
        return sw.getBuffer().toString();

    }

    public void printToFile(Document doc) throws TransformerException, FileNotFoundException {
        DOMSource docSource = new DOMSource(doc);
        doc.setXmlStandalone(true);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        OutputStream outputStream = new FileOutputStream("report/create_emp1"+System.currentTimeMillis()+".xml");
        StreamResult result = new StreamResult(outputStream);
        transformer.transform(docSource, result);
    }

    public void printToConsole(Document doc) throws TransformerException {


        DOMSource docSource = new DOMSource(doc);
        doc.setXmlStandalone(true);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StreamResult console = new StreamResult(System.out);
        transformer.transform(docSource, console);
    }


}
