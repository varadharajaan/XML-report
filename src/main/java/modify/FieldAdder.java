package modify;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import parser.ReadUtils;

/**
 * @Author Varadharajan on 10/05/20 03:51
 * @Projectname EmployeeParser
 */
public abstract class FieldAdder implements IFieldAdder {

    ReadUtils readUtils = ReadUtils.getInstance();

    public void addNode(String tagName, String value, Node parent) {

        Document dom = parent.getOwnerDocument();

        // Create a new Node with the given tag name
        Node node = dom.createElement(tagName);

        // Add the node value as a child text node
        Text nodeVal = dom.createTextNode(value);
        node.appendChild(nodeVal);

        // Add the new node structure to the parent node
        parent.appendChild(node);
    }

    public void setNodeValue(String tagName, String value, NodeList nodes) {

        Node node = readUtils.getNode(tagName, nodes);
        if ( node == null )
            return;
        // Locate the child text node and change its value
        NodeList childNodes = node.getChildNodes();
        for (int y = 0; y < childNodes.getLength(); y++ ) {
            Node data = childNodes.item(y);
            if ( data.getNodeType() == Node.TEXT_NODE ) {
                data.setNodeValue(value);
                return;
            }
        }

    }


}
