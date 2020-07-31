import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Map;

public class CircuitGraph extends JPanel {
    int nodesNumber;
    Circuit circuit;
    ArrayList<Node> nodes;

    public CircuitGraph(int nodesNumber, Circuit circuit, ArrayList<Node> nodes) {
        this.nodesNumber = nodesNumber;
        this.circuit = circuit;
        this.nodes = nodes;
    }

    @Override
    public void paint(Graphics g) {

        g.drawLine(100,500,500,500);
        for (int i=0;i<nodesNumber;i++){
            g.fillOval(100 + 100*i,400,5,5);
        }
        ArrayList<Node> nodes = new ArrayList<>(0);
        nodes.add(0,circuit.getNodes().get(0));
        for (Map.Entry node : circuit.getNodes().entrySet())
            nodes.add((Node) node.getValue());
        Node node;
        for(int i=1;i<nodes.size();i++){
            node = nodes.get(i);
            node.setEarthConnections();
            if (node.getEarthConnections() > 1){
                g.drawLine(100*(i-1),400,100*(i-1) + 30*(node.getEarthConnections() - 1),400);
            }
        }
    }
}
