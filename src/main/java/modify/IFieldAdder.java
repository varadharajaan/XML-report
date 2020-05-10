package modify;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @Author Varadharajan on 10/05/20 02:08
 * @Projectname EmployeeParser
 */
public interface IFieldAdder {

    void addNode(String tagName, String value, Node parent);

    void setNodeValue(String tagName, String value, NodeList nodes);
}
