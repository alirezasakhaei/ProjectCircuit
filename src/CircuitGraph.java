import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Map;

public class CircuitGraph extends JPanel {
    final int nodesNumber;
    final Circuit circuit;
    final ArrayList<Node> nodes;
    final JDialog dialog;

    public CircuitGraph(int nodesNumber, Circuit circuit, ArrayList<Node> nodes, JDialog dialog) {
        this.nodesNumber = nodesNumber;
        this.circuit = circuit;
        this.nodes = nodes;
        this.dialog = dialog;
        drawEarthConnecteds();
        drawHorizontals();
    }

    @Override
    public void paint(Graphics g) {

        g.drawLine(100,550,500,550);
        for (int i=0;i<nodesNumber;i++){
            g.fillOval(100 + 100*i,450,5,5);
            nodes.get(i+1).setLocation(new Point(100 + 100*i,450));
        }
        Node node;
        for(int i=1;i<nodes.size();i++){
            node = nodes.get(i);
            node.setEarthConnections();
            if (node.getEarthConnections() > 1){
                g.drawLine(100*(i),450,100*(i) + 30*(node.getEarthConnections() - 1),450);
            }
        }
        int parralles;
        for(int i=1;i<nodes.size() - 1;i++){
            for (int j = (i+1);j<nodes.size();j++){
                parralles = Node.elementsBetween(nodes.get(i),nodes.get(j));
                if (parralles > 0){
                    g.drawLine(nodes.get(i).getLocation().x , nodes.get(i).getLocation().y, nodes.get(i).getLocation().x, nodes.get(i).getLocation().y - 100*(j-i));
                    g.drawLine(nodes.get(j).getLocation().x , nodes.get(j).getLocation().y, nodes.get(j).getLocation().x, nodes.get(j).getLocation().y - 100*(j-i));
                }
                for (int k = 0; k<parralles ; k++){
                    g.drawLine(nodes.get(i).getLocation().x + 100,nodes.get(j).getLocation().y - 100*(j-i) + k*30 ,nodes.get(j).getLocation().x , nodes.get(j).getLocation().y - 100*(j-i) + k*30);
                }
            }
        }
    }
    private void drawEarthConnecteds(){
        Node node;
        ElementShape elementShape;
        int elementNumber;
        Element element;
        JLabel label,name;
        for (int i=1;i<nodes.size();i++){
            elementNumber = 1;
            node = nodes.get(i);
            for (int j=0; j<node.getNegatives().size(); j++){
                element = circuit.getElements().get(node.getNegatives().get(j));
                if (element.positiveNode.getName() == 0){
                    elementShape = new ElementShape(element);
                    elementShape.setBounds(145 + 30*(elementNumber-1) + 100*(i-1) ,500,10,100);
                    dialog.add(elementShape);

                    name = new JLabel(element.name);
                    name.setBounds(130 + 30*(elementNumber-1) + 100*(i-1) ,495 + 80*(elementNumber%2),100,20);
                    name.setFont(new Font("Arial",Font.ITALIC,8));
                    dialog.add(name);

                    label = new JLabel(element.label);
                    label.setBounds(130 + 30*(elementNumber-1) + 100*(i-1) ,505 + 80*(elementNumber%2),100,20);
                    label.setFont(new Font("Arial",Font.ITALIC,8));
                    dialog.add(label);

                    elementNumber++;
                }
            }
            for (int j=0; j<node.getPositives().size(); j++){
                element = circuit.getElements().get(node.getPositives().get(j));
                if (element.negativeNode.getName() == 0){
                    elementShape = new ElementShape(element);
                    elementShape.setBounds(145 + 30*(elementNumber-1) + 100*(i-1) ,500,10,100);
                    dialog.add(elementShape);

                    name = new JLabel(element.name);
                    name.setBounds(130 + 30*(elementNumber-1) + 100*(i-1) ,495 + 80*(elementNumber%2),100,20);
                    name.setFont(new Font("Arial",Font.ITALIC,8));
                    dialog.add(name);

                    label = new JLabel(element.label);
                    label.setBounds(130 + 30*(elementNumber-1) + 100*(i-1) ,505 + 80*(elementNumber%2),100,20);
                    label.setFont(new Font("Arial",Font.ITALIC,8));
                    dialog.add(label);


                    elementNumber++;
                }
            }
        }



    }
    private void drawHorizontals(){
        Element element;
        ElementShape elementShape;
        boolean isBetween;
        int parralles;
        JLabel label,name;
        for(int i=1;i<nodes.size() - 1;i++){
            for (int j = (i+1);j<nodes.size();j++){
                parralles = 0;
                for (Map.Entry elementLoop : Circuit.getCircuit().getElements().entrySet()){
                    element = (Element) elementLoop.getValue();
                    isBetween = element.positiveNode.getName() == nodes.get(i).getName() && element.negativeNode.getName() == nodes.get(j).getName();
                    if (element.positiveNode.getName() == nodes.get(j).getName() && element.negativeNode.getName() == nodes.get(i).getName())
                        isBetween = true;
                    if (isBetween){
                        elementShape = new ElementShape(element);
                        elementShape.setBounds(50 + 100*i,495 - 100*(j-i) + parralles*30,100,10);
                        dialog.add(elementShape);

                        name = new JLabel(element.name);
                        name.setBounds(50 + 100*i +70*(parralles%2) ,495 - 100*(j-i) + parralles*30 - 15 ,100,20);
                        name.setFont(new Font("Arial",Font.ITALIC,8));
                        dialog.add(name);

                        label = new JLabel(element.label);
                        label.setBounds(50 + 100*i+70*(parralles%2) ,495 - 100*(j-i) + parralles*30 + 5 ,100,20);
                        label.setFont(new Font("Arial",Font.ITALIC,8));
                        dialog.add(label);

                        parralles++;

                    }
                }
            }
        }


    }
}
