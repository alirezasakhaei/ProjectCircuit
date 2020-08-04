import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Map;

public class CircuitGraphBad extends JPanel {
    int nodesNumber;
    Circuit circuit;
    ArrayList<Node> nodes;
    JDialog dialog;

    public CircuitGraphBad(int nodesNumber, Circuit circuit, ArrayList<Node> nodes, JDialog dialog) {
        this.nodesNumber = nodesNumber;
        this.circuit = circuit;
        this.nodes = nodes;
        this.dialog = dialog;
        drawEarthConnecteds();
        drawHorizontals();
        //drawVerticals();

    }

    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                g.fillOval(50 + 100 * j, 50 + 100 * i, 5, 5);
            }
        }
        g.drawLine(50, 550, 550, 550);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(8));
        g2d.drawLine(0,0,600,0);
        g2d.drawLine(0,0,0,600);
        g2d.drawLine(600,0,600,600);
        g2d.drawLine(0,600,600,600);
    }

    private void drawVerticals() {
        Element element;
        ElementShape elementShape;
        Node nodeOne, nodeTwo;
        boolean isBetween;
        int elementsBetween, elementNumber = 1;
        JLabel label, name;
        for (int i = 1; i < 25 && i % 6 != 0 && Circuit.getCircuit().getNodes().containsKey(i) && Circuit.getCircuit().getNodes().containsKey(i + 6); i++) {
            nodeOne = Circuit.getCircuit().getNodes().get(i);
            nodeTwo = Circuit.getCircuit().getNodes().get(i + 6);
            elementsBetween = Node.elementsBetween(nodeOne, nodeTwo);
            elementNumber = 1;
            if (elementsBetween > 0) {
                for (Map.Entry elementLoop : Circuit.getCircuit().getElements().entrySet()) {
                    element = (Element) elementLoop.getValue();
                    isBetween = false;
                    if (element.positiveNode.getName() == nodeOne.getName() && element.negativeNode.getName() == nodeTwo.getName())
                        isBetween = true;
                    if (element.positiveNode.getName() == nodeTwo.getName() && element.negativeNode.getName() == nodeOne.getName())
                        isBetween = true;
                    if (isBetween) {
                        if (element.positiveNode.getName() > element.negativeNode.getName())
                            elementShape = new ElementShape(element, false, true);
                        else
                            elementShape = new ElementShape(element, false, false);

                        elementShape.setBounds(45 + 100 * (i % 6 - 1) + (elementNumber - 1) * 30, 450 - 100 * ((i - 1) / 6) - 100 , 10, 100);
                        dialog.add(elementShape);

                        name = new JLabel(element.name);
                        name.setBounds(45 + 100 * (i % 6 - 1) + (elementNumber - 1) * 30, 445 - 100 * ((i - 1) / 6) - 100 + 80 * (elementNumber % 2), 100, 20);
                        name.setFont(new Font("Arial", Font.ITALIC, 8));
                        dialog.add(name);

                        label = new JLabel(element.label);
                        label.setBounds(45 + 100 * (i % 6 - 1) + (elementNumber - 1) * 30, 455 - 100 * ((i - 1) / 6) - 100 + 80 * (elementNumber % 2), 100, 20);
                        label.setFont(new Font("Arial", Font.ITALIC, 8));
                        dialog.add(label);

                        elementNumber++;
                    }
                }
            }
        }
    }

    private void drawHorizontals() {
        Element element;
        ElementShape elementShape;
        Node nodeOne, nodeTwo;
        boolean isBetween;
        int elementsBetween, elementsNumber = 1;
        JLabel label, name;
        for (int i = 1; (i < 31) && (i%6 != 0) && Circuit.getCircuit().getNodes().containsKey(i) && Circuit.getCircuit().getNodes().containsKey(i + 1); i++) {
            nodeOne = Circuit.getCircuit().getNodes().get(i);
            nodeTwo = Circuit.getCircuit().getNodes().get(i + 1);
            elementsBetween = Node.elementsBetween(nodeOne, nodeTwo);
            System.out.println(i + " " + (i+1) + " " + elementsBetween);
            elementsNumber = 1;
            if (elementsBetween > 0) {
                for (Map.Entry elementLoop : Circuit.getCircuit().getElements().entrySet()) {
                    element = (Element) elementLoop.getValue();
                    isBetween = false;
                    if (element.positiveNode.getName() == nodeOne.getName() && element.negativeNode.getName() == nodeTwo.getName())
                        isBetween = true;
                    if (element.positiveNode.getName() == nodeTwo.getName() && element.negativeNode.getName() == nodeOne.getName())
                        isBetween = true;

                    if (isBetween) {
                        if (element.positiveNode.getName() > element.negativeNode.getName())
                            elementShape = new ElementShape(element, true, true);

                        else
                            elementShape = new ElementShape(element, true, false);

                        elementShape.setBounds(100 + 100 * (i % 6 - 1), 495 - 100 * ((i - 1) / 6) + (elementsNumber - 1) * 30, 100, 10);
                        dialog.add(elementShape);

                        name = new JLabel(element.name);
                        name.setBounds(100 + 100*(i % 6 - 1) + 70 * (elementsNumber % 2), 495 - 100 * ((i - 1) / 6) + (elementsNumber - 1) * 30 - 15, 100, 20);
                        name.setFont(new Font("Arial", Font.ITALIC, 8));
                        dialog.add(name);

                        label = new JLabel(element.label);
                        label.setBounds(100 + 100*(i % 6 - 1) + 70 * (elementsNumber % 2), 495 - 100 * ((i - 1) / 6) + (elementsNumber - 1) * 30 + 5, 100, 20);
                        label.setFont(new Font("Arial", Font.ITALIC, 8));
                        dialog.add(label);

                        elementsNumber++;

                    }
                }
            }
        }
    }

    private void drawEarthConnecteds() {
        Node node;
        ElementShape elementShape;
        int elementNumber = 1;
        Element element;
        JLabel label, name;
        for (Map.Entry nodeMap : Circuit.getCircuit().getNodes().entrySet()) {
            elementNumber = 1;
            node = (Node) nodeMap.getValue();
            for (int j = 0; j < node.getNegatives().size(); j++) {
                element = circuit.getElements().get(node.getNegatives().get(j));
                if (element.positiveNode.getName() == 0) {
                    elementShape = new ElementShape(element);
                    name = new JLabel(element.name);
                    name.setFont(new Font("Arial", Font.ITALIC, 8));

                    label = new JLabel(element.label);
                    label.setFont(new Font("Arial", Font.ITALIC, 8));

                    dialog.add(elementShape);
                    dialog.add(name);
                    dialog.add(label);

                    switch (elementNumber) {
                        case 1:
                            elementShape.setBounds(95 + 100 * (node.getName() - 1), 500, 10, 100);
                            name.setBounds(95  + 100 * (node.getName() - 1), 505 + 80 * (elementNumber % 2), 100, 20);
                            label.setBounds(95 + 100 * (node.getName() - 1), 515 + 80 * (elementNumber % 2), 100, 20);
                            break;
                        case 2:
                            elementShape.setBounds(95 + 30 + 100 * (node.getName() - 1), 500, 10, 100);
                            name.setBounds(95 + 30  + 100 * (node.getName() - 1), 505 + 80 * (elementNumber % 2), 100, 20);
                            label.setBounds(95 + 30 + 100 * (node.getName() - 1), 515 + 80 * (elementNumber % 2), 100, 20);
                            break;
                        case 3:
                            elementShape.setBounds(95 - 30 + 100 * (node.getName() - 1), 500, 10, 100);
                            name.setBounds(95 - 30 + 100 * (node.getName() - 1), 505 + 80 * (elementNumber % 2), 100, 20);
                            label.setBounds(95 - 30 + 100 * (node.getName() - 1), 515 + 80 * (elementNumber % 2), 100, 20);
                            break;
                    }


                    elementNumber++;
                }
            }
            for (int j = 0; j < node.getPositives().size(); j++) {
                element = circuit.getElements().get(node.getPositives().get(j));
                if (element.negativeNode.getName() == 0) {
                    elementShape = new ElementShape(element);
                    name = new JLabel(element.name);
                    name.setFont(new Font("Arial", Font.ITALIC, 8));

                    label = new JLabel(element.label);
                    label.setFont(new Font("Arial", Font.ITALIC, 8));

                    dialog.add(elementShape);
                    dialog.add(name);
                    dialog.add(label);

                    switch (elementNumber) {
                        case 1:
                            elementShape.setBounds(95 + 100 * (node.getName() - 1), 500, 10, 100);
                            name.setBounds(95 + 100 * (node.getName() - 1), 505 + 80 * (elementNumber % 2), 100, 20);
                            label.setBounds(95 + 100 * (node.getName() - 1), 515 + 80 * (elementNumber % 2), 100, 20);
                            break;
                        case 2:
                            elementShape.setBounds(95 + 30 + 100 * (node.getName() - 1), 500, 10, 100);
                            name.setBounds(95 + 30  + 100 * (node.getName() - 1), 505 + 80 * (elementNumber % 2), 100, 20);
                            label.setBounds(95 + 30 + 100 * (node.getName() - 1), 515 + 80 * (elementNumber % 2), 100, 20);
                            break;
                        case 3:
                            elementShape.setBounds(95 - 30 + 100 * (node.getName() - 1), 500, 10, 100);
                            name.setBounds(95 - 30 + 100 * (node.getName() - 1), 505 + 80 * (elementNumber % 2), 100, 20);
                            label.setBounds(95 - 30 + 100 * (node.getName() - 1), 515 + 80 * (elementNumber % 2), 100, 20);
                            break;
                    }

                    elementNumber++;
                }
            }
        }
    }


}
