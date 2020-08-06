import javax.swing.*;
import java.awt.Graphics;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class CircuitGraphEdited extends JPanel {
    final int nodesNumber;
    final Circuit circuit;
    final ArrayList<Node> nodes;
    final JDialog dialog;
    ArrayList<Integer> locations;
    ArrayList<JLabel> labels;
    ArrayList<JLabel> namesElement;
    ArrayList<JLabel> namesNode;

    public CircuitGraphEdited(int nodesNumber, Circuit circuit, ArrayList<Node> nodes, JDialog dialog, ArrayList<JLabel> labels, ArrayList<JLabel> namesElement, ArrayList<JLabel> namesNode) {
        this.nodesNumber = nodesNumber;
        this.circuit = circuit;
        this.nodes = nodes;
        this.dialog = dialog;
        this.labels = labels;
        this.namesElement = namesElement;
        this.namesNode = namesNode;

        locations = new ArrayList<>();
        int location;
        locations.add(0);
        for (int i = 1; i < nodes.size(); i++) {
            location = 100;
            nodes.get(i).setEarthConnections();
            if (nodes.get(i).getEarthConnections() > 0)
                locations.add(i, 0);
            else {
                for (int j = 1; j < nodes.size() && j != i; j++) {
                    if (Node.elementsBetween(nodes.get(i), nodes.get(j)) > 0 && Math.abs(i - j) < location)
                        location = Math.abs(i - j);
                }
                locations.add(i, location);
            }
        }
        JLabel label;

        for (int i = 1; i < nodes.size(); i++) {
            label = new JLabel(String.valueOf(nodes.get(i).getName()));
            label.setBounds(45 + (i - 1) * 100, 415 - locations.get(i)*50, 50, 50);
            dialog.add(label);
            namesNode.add(label);
        }

        drawEarthConnecteds();
        drawHorizontals();
    }

    @Override
    public void paint(Graphics g) {
        int earth;
        JLabel label;
        for (int i = 1; i < nodes.size(); i++) {
            g.fillOval(50 + (i - 1) * 100, 450 - locations.get(i) * 50, 5, 5);
            label = new JLabel(String.valueOf(nodes.get(i).getName()));
            label.setBounds(45 + (i - 1) * 100, 455, 50, 50);
            dialog.add(label);
            nodes.get(i).setEarthConnections();
            earth = nodes.get(i).getEarthConnections();
            if (earth > 1) {
                g.drawLine(50 + (i - 1) * 100, 450, 50 + (i-1) * 100 + 15*(earth - 1), 450);
            }
        }
        int parralles;
        for (int i = 1; i < nodes.size() - 1; i++) {
            for (int j = (i + 1); j < nodes.size(); j++) {
                parralles = Node.elementsBetween(nodes.get(i), nodes.get(j));
                if (parralles > 0) {
                    g.drawLine(50 + 100 * (i - 1), 450 - 50*locations.get(i), 50 + 100 * (i - 1), 450 - 50 * (j - i) -3*i);
                    g.drawLine(50 + 100 * (j - 1), 450 - 50*locations.get(j), 50 + 100 * (j - 1), 450 - 50 * (j - i) -3*i);
                }
                for (int k = 0; k < parralles; k++) {
                    g.drawLine(50 + 100 * (i - 1) + 100, 450 - 50 * (j - i) + k * 15 - 3*i, 50 + 100 * (j - 1), 450 - 50 * (j - i) + k * 15 - 3*i);
                    //if (Math.abs(i-j) == locations.get(j))
                    //    g.drawLine(50 + 100 * (j - 1), 450 - 50*locations.get(j), 50 + 100 * (j - 1), 450 - 50*locations.get(j) + 15);
                  //  if (Math.abs(i-j) == locations.get(i))
                    //    g.drawLine(50 + 100 * (i - 1), 450 - 50*locations.get(i), 50 + 100 * (i - 1), 450 - 50*locations.get(i) + 15);
                }
            }
        }


        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(5));
        g.drawLine(50, 552, 1000, 552);
        g2d.setStroke(new BasicStroke(8));
        g2d.drawLine(5, 50, 1080, 50);
        g2d.drawLine(5, 50, 5, 560);
        g2d.drawLine(1080, 50, 1080, 560);
        g2d.drawLine(5, 560, 1080, 560);


    }

    private void drawHorizontals() {
        Element element;
        ElementShape elementShape;
        boolean isBetween;
        int parralles;
        JLabel label, name;
        for (int i = 1; i < nodes.size() - 1; i++) {
            for (int j = (i + 1); j < nodes.size(); j++) {
                parralles = 0;
                for (Map.Entry<String, Element> elementLoop : Circuit.getCircuit().getElements().entrySet()) {
                    element = elementLoop.getValue();
                    isBetween = element.positiveNode.getName() == nodes.get(j).getName() && element.negativeNode.getName() == nodes.get(i).getName();
                    if (element.positiveNode.getName() == nodes.get(i).getName() && element.negativeNode.getName() == nodes.get(j).getName())
                        isBetween = true;
                    if (isBetween) {
                        elementShape = new ElementShape(element);
                        int diff = j - i;
                        elementShape.setBounds(100 * i - 50, 445 - 50 * (j - i) + parralles * 15 - 3 * (i), 100, 10);
                        dialog.add(elementShape);

                        name = new JLabel(element.name);
                        name.setBounds(100 * i + 70 * (parralles % 2) - 50, 445 - 50 * (j - i) + parralles * 15 - 15 - 3 * (i), 100, 20);
                        name.setFont(new Font("Arial", Font.ITALIC, 8));
                        namesElement.add(name);
                        dialog.add(name);

                        label = new JLabel(element.label);
                        label.setBounds(100 * i + 70 * (parralles % 2) - 50, 445 - 50 * (j - i) + parralles * 15 + 5 - 3*(i), 100, 20);
                        label.setFont(new Font("Arial", Font.ITALIC, 8));
                        labels.add(label);
                        dialog.add(label);

                        parralles++;

                    }
                }
            }
        }
    }

    private void drawEarthConnecteds() {
        Node node;
        ElementShape elementShape;
        int elementNumber;
        Element element;
        JLabel label, name;
        for (int i = 1; i < nodes.size(); i++) {
            elementNumber = 1;
            node = nodes.get(i);
            for (int j = 0; j < node.getNegatives().size(); j++) {
                element = circuit.getElements().get(node.getNegatives().get(j));
                if (element.positiveNode.getName() == 0) {
                    elementShape = new ElementShape(element);
                    elementShape.setBounds((45 + 100 * (i - 1)) + 15 * (elementNumber - 1), 450, 10, 100);
                    dialog.add(elementShape);

                    name = new JLabel(element.name);
                    name.setBounds((45 + 100 * (i - 1)) + 15 * (elementNumber - 1) - 5, 445 + 80 * (elementNumber % 2), 100, 20);
                    name.setFont(new Font("Arial", Font.ITALIC, 8));
                    namesElement.add(name);
                    dialog.add(name);

                    label = new JLabel(element.label);
                    label.setBounds((45 + 100 * (i - 1)) + 15 * (elementNumber - 1) - 5, 455 + 80 * (elementNumber % 2), 100, 20);
                    label.setFont(new Font("Arial", Font.ITALIC, 8));
                    labels.add(label);
                    dialog.add(label);

                    elementNumber++;
                }
            }
            for (int j = 0; j < node.getPositives().size(); j++) {
                element = circuit.getElements().get(node.getPositives().get(j));
                if (element.negativeNode.getName() == 0) {
                    elementShape = new ElementShape(element);
                    elementShape.setBounds((45 + 100 * (i - 1)) + 15 * (elementNumber - 1), 450, 10, 100);
                    dialog.add(elementShape);

                    name = new JLabel(element.name);
                    name.setBounds((45 + 100 * (i - 1)) + 15 * (elementNumber - 1) - 5, 445 + 80 * (elementNumber % 2), 100, 20);
                    name.setFont(new Font("Arial", Font.ITALIC, 8));
                    dialog.add(name);

                    label = new JLabel(element.label);
                    label.setBounds((45 + 100 * (i - 1)) + 15 * (elementNumber - 1) - 5, 455 + 80 * (elementNumber % 2), 100, 20);
                    label.setFont(new Font("Arial", Font.ITALIC, 8));
                    dialog.add(label);


                    elementNumber++;
                }
            }
        }


    }


}
