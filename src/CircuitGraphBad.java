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

    ArrayList<JLabel> labels;
    ArrayList<JLabel> namesElement;
    ArrayList<JLabel> namesNode;

    public CircuitGraphBad(int nodesNumber, Circuit circuit, ArrayList<Node> nodes, JDialog dialog, ArrayList<JLabel> labels, ArrayList<JLabel> namesElement, ArrayList<JLabel> namesNode) {
        this.nodesNumber = nodesNumber;
        this.circuit = circuit;
        this.nodes = nodes;
        this.dialog = dialog;
        this.labels = labels;
        this.namesElement = namesElement;
        this.namesNode = namesNode;


        drawEarthConnecteds();
        drawHorizontals();
        drawVerticals();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                if (Circuit.getCircuit().getNodes().containsKey(j + 1 + (30 - (i + 1) * 6))) {
                    Circuit.getCircuit().getNodes().get(j + 1 + (30 - (i + 1) * 6)).xBad = 50 + 100 * j;
                    Circuit.getCircuit().getNodes().get(j + 1 + (30 - (i + 1) * 6)).yBad = 50 + 100 * i;
                }
            }
        }
        nodesNames();

    }

    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                if (Circuit.getCircuit().getNodes().containsKey(j + 1 + (30 - (i + 1) * 6))) {
                    g.fillOval(48 + 100 * j, 48 + 100 * i, 5, 5);
                }
            }
        }

        for (int i=1;i<7;i++){
            if (Circuit.getCircuit().getNodes().containsKey(i)){
                if (Circuit.getCircuit().getNodes().get(i).getEarthConnections() > 1)
                    g.drawLine(100 * (Circuit.getCircuit().getNodes().get(i).getName()) - 50, 450, 100 * (Circuit.getCircuit().getNodes().get(i).getName()) - 20, 450);
                if (Circuit.getCircuit().getNodes().get(i).getEarthConnections() > 2)
                    g.drawLine(100 * (Circuit.getCircuit().getNodes().get(i).getName()) - 50, 450, 100 * (Circuit.getCircuit().getNodes().get(i).getName()) - 80, 450);
            }
        }
        for (int i=1;i<31;i++){
            int between;
            Node nodeOne,nodeTwo;
            if (Circuit.getCircuit().getNodes().containsKey(i)){
                nodeOne = Circuit.getCircuit().getNodes().get(i);
                if (Circuit.getCircuit().getNodes().containsKey(i+1)){
                    nodeTwo = Circuit.getCircuit().getNodes().get(i+1);
                    between = Node.elementsBetween(nodeOne,nodeTwo);
                    if (between > 1){
                        g.drawLine(100 * (i % 6) - 50, 500 - 100 * ((i - 1) / 6) - 75, 100 * (i % 6) - 50, 500 - 100 * ((i - 1) / 6) - 50);
                        g.drawLine(100 * ((i+1) % 6) - 50, 500 - 100 * ((i + 1 - 1) / 6) - 75, 100 * ((i+1) % 6) - 50, 500 - 100 * ((i + 1 - 1) / 6) - 50);
                    }
                    if (between > 2){
                        g.drawLine(100 * (i % 6) - 50, 500 - 100 * ((i - 1) / 6) - 50, 100 * (i % 6) - 50, 500 - 100 * ((i - 1) / 6) - 25);
                        g.drawLine(100 * ((i+1) % 6) - 50, 500 - 100 * ((i + 1 - 1) / 6) - 50, 100 * ((i+1) % 6) - 50, 500 - 100 * ((i + 1 - 1) / 6) - 25);
                    }
                }
            }
        }
        for (int i=1;i<26;i++){
            int between;
            Node nodeOne,nodeTwo;
            if (Circuit.getCircuit().getNodes().containsKey(i)){
                nodeOne = Circuit.getCircuit().getNodes().get(i);
                if (Circuit.getCircuit().getNodes().containsKey(i+6)){
                    nodeTwo = Circuit.getCircuit().getNodes().get(i+6);
                    between = Node.elementsBetween(nodeOne,nodeTwo);
                    if (between > 1){
                        g.drawLine(100 * (i % 6) - 50, 500 - 100 * ((i - 1) / 6) - 50, 100 * (i % 6) - 25, 500 - 100 * ((i - 1) / 6) - 50);
                        g.drawLine(100 * ((i+6) % 6) - 50, 500 - 100 * ((i+6 - 1) / 6) - 50, 100 * ((i+6) % 6) - 25, 500 - 100 * ((i +6 - 1) / 6) - 50);
                    }
                    if (between > 2){
                        g.drawLine(100 * (i % 6) - 50, 500 - 100 * ((i - 1) / 6) - 50, 100 * (i % 6) - 75, 500 - 100 * ((i - 1) / 6) - 50);
                        g.drawLine(100 * ((i+6) % 6) - 50, 500 - 100 * ((i+6 - 1) / 6) - 50, 100 * ((i+6) % 6) - 75, 500 - 100 * ((i +6 - 1) / 6) - 50);
                    }
                }
            }



        }





        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(25, 555, 575, 555);
        g2d.setStroke(new BasicStroke(8));
        g2d.drawLine(0, 0, 600, 0);
        g2d.drawLine(0, 0, 0, 600);
        g2d.drawLine(600, 0, 600, 600);
        g2d.drawLine(0, 600, 600, 600);
    }

    private void drawVerticals() {
        Element element;
        ElementShape elementShape;
        Node nodeOne, nodeTwo;
        boolean isBetween, flag;
        int elementsBetween, elementNumber = 1;
        JLabel label, name;
        for (int i = 1; i < 25; i++) {
            flag = true;
            if (!Circuit.getCircuit().getNodes().containsKey(i))
                flag = false;
            if (!Circuit.getCircuit().getNodes().containsKey(i + 6))
                flag = false;
            if (flag) {
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
                            dialog.add(elementShape);
                            name = new JLabel(element.name);
                            name.setFont(new Font("Arial", Font.ITALIC, 8));
                            dialog.add(name);
                            label = new JLabel(element.label);
                            label.setFont(new Font("Arial", Font.ITALIC, 8));
                            dialog.add(label);

                            namesElement.add(name);
                            labels.add(label);


                            switch (elementNumber) {
                                case 1:
                                    elementShape.setBounds(95 + 100 * (i % 6 - 1) + 0, 500 - 100 * ((i - 1) / 6) - 100, 10, 100);
                                    name.setBounds(95 + 100 * (i % 6 - 1) + 0, 495 - 100 * ((i - 1) / 6) - 100 , 100, 20);
                                    label.setBounds(95 + 100 * (i % 6 - 1) + 0, 505 - 100 * ((i - 1) / 6) - 100 , 100, 20);
                                    break;
                                case 2:
                                    elementShape.setBounds(95 + 100 * (i % 6 - 1) + 25, 500 - 100 * ((i - 1) / 6) - 100, 10, 100);
                                    name.setBounds(95 + 100 * (i % 6 - 1) + 25, 495 - 100 * ((i - 1) / 6) - 100 + 70 , 100, 20);
                                    label.setBounds(95 + 100 * (i % 6 - 1) + 25, 505 - 100 * ((i - 1) / 6) - 100 + 70, 100, 20);
                                    break;
                                case 3:
                                    elementShape.setBounds(95 + 100 * (i % 6 - 1) - 25, 500 - 100 * ((i - 1) / 6) - 100, 10, 100);
                                    name.setBounds(95 + 100 * (i % 6 - 1) - 25, 495 - 100 * ((i - 1) / 6) - 100 + 70, 100, 20);
                                    label.setBounds(95 + 100 * (i % 6 - 1) - 25, 505 - 100 * ((i - 1) / 6) - 100 + 70, 100, 20);
                                    break;
                            }
                            elementNumber++;
                        }
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

                    namesElement.add(name);
                    labels.add(label);

                    int addedY = 70;

                    switch (elementNumber) {
                        case 1:
                            addedY = 0;
                            elementShape.setBounds(95 + 100 * (node.getName() - 1), 500, 10, 100);
                            name.setBounds(95 + 100 * (node.getName() - 1), 505 + addedY, 100, 20);
                            label.setBounds(95 + 100 * (node.getName() - 1), 515 + addedY, 100, 20);
                            break;
                        case 2:
                            elementShape.setBounds(95 + 25 + 100 * (node.getName() - 1), 500, 10, 100);
                            name.setBounds(95 + 25 + 100 * (node.getName() - 1), 500 + addedY, 100, 20);
                            label.setBounds(95 + 25 + 100 * (node.getName() - 1), 510 + addedY, 100, 20);
                            break;
                        case 3:
                            elementShape.setBounds(95 - 25 + 100 * (node.getName() - 1), 500, 10, 100);
                            name.setBounds(95 - 25 + 100 * (node.getName() - 1), 500 + addedY, 100, 20);
                            label.setBounds(95 - 25 + 100 * (node.getName() - 1), 510 + addedY, 100, 20);
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

                    namesElement.add(name);
                    labels.add(label);

                    int addedY = 70;
                    switch (elementNumber) {
                        case 1:
                            addedY = 0;
                            elementShape.setBounds(95 + 100 * (node.getName() - 1), 500, 10, 100);
                            name.setBounds(95 + 100 * (node.getName() - 1), 500 + addedY, 100, 20);
                            label.setBounds(95 + 100 * (node.getName() - 1), 510 + addedY, 100, 20);
                            break;
                        case 2:
                            elementShape.setBounds(95 + 25 + 100 * (node.getName() - 1), 500, 10, 100);
                            name.setBounds(95 + 25 + 100 * (node.getName() - 1), 500 + addedY, 100, 20);
                            label.setBounds(95 + 25 + 100 * (node.getName() - 1), 510 + addedY, 100, 20);
                            break;
                        case 3:
                            elementShape.setBounds(95 - 25 + 100 * (node.getName() - 1), 500, 10, 100);
                            name.setBounds(95 - 25 + 100 * (node.getName() - 1), 500 + addedY, 100, 20);
                            label.setBounds(95 - 25 + 100 * (node.getName() - 1), 510 + addedY, 100, 20);
                            break;
                    }

                    elementNumber++;
                }
            }
        }
    }

    private void drawHorizontals() {
        Element element;
        ElementShape elementShape;
        Node nodeOne, nodeTwo;
        boolean isBetween, flag;
        int elementsBetween, elementsNumber = 1;
        JLabel label, name;
        for (int i = 1; i < 31; i++) {
            flag = true;
            if (i % 6 == 0)
                flag = false;
            if (!Circuit.getCircuit().getNodes().containsKey(i))
                flag = false;
            if (!Circuit.getCircuit().getNodes().containsKey(i + 1))
                flag = false;

            if (flag) {
                nodeOne = Circuit.getCircuit().getNodes().get(i);
                nodeTwo = Circuit.getCircuit().getNodes().get(i + 1);
                elementsBetween = Node.elementsBetween(nodeOne, nodeTwo);
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

                            name = new JLabel(element.name);
                            name.setFont(new Font("Arial", Font.ITALIC, 8));
                            label = new JLabel(element.label);
                            label.setFont(new Font("Arial", Font.ITALIC, 8));

                            dialog.add(elementShape);
                            dialog.add(name);
                            dialog.add(label);

                            namesElement.add(name);
                            labels.add(label);

                            switch (elementsNumber) {
                                case 1:
                                    elementShape.setBounds(100 + 100 * (i % 6 - 1), 495 - 100 * ((i - 1) / 6) + 0, 100, 10);
                                    name.setBounds(100 + 100 * (i % 6 - 1) + 70 * (elementsNumber % 2), 495 - 100 * ((i - 1) / 6) + 0 - 15, 100, 20);
                                    label.setBounds(100 + 100 * (i % 6 - 1) + 70 * (elementsNumber % 2), 495 - 100 * ((i - 1) / 6) + 0 + 5, 100, 20);
                                    break;
                                case 3:
                                    elementShape.setBounds(100 + 100 * (i % 6 - 1), 495 - 100 * ((i - 1) / 6) + 25, 100, 10);
                                    name.setBounds(100 + 100 * (i % 6 - 1) + 70 * (elementsNumber % 2), 495 - 100 * ((i - 1) / 6) + 25 - 15, 100, 20);
                                    label.setBounds(100 + 100 * (i % 6 - 1) + 70 * (elementsNumber % 2), 495 - 100 * ((i - 1) / 6) + 0 + 25, 100, 20);
                                    break;
                                case 2:
                                    elementShape.setBounds(100 + 100 * (i % 6 - 1), 495 - 100 * ((i - 1) / 6) - 25, 100, 10);
                                    name.setBounds(100 + 100 * (i % 6 - 1) + 70 * (elementsNumber % 2), 495 - 100 * ((i - 1) / 6) - 25 - 15, 100, 20);
                                    label.setBounds(100 + 100 * (i % 6 - 1) + 70 * (elementsNumber % 2), 495 - 100 * ((i - 1) / 6) - 25 + 5, 100, 20);
                                    break;

                            }

                            elementsNumber++;

                        }
                    }
                }
            }
        }
    }

    private void nodesNames() {
        JLabel name;
        for (int i = 1; i < 31; i++) {
            boolean contains = false;
            if (Circuit.getCircuit().getNodes().containsKey(i)) {
                name = new JLabel(String.valueOf(Circuit.getCircuit().getNodes().get(i).getName()));
                name.setBounds(Circuit.getCircuit().getNodes().get(i).xBad + 45, Circuit.getCircuit().getNodes().get(i).yBad + 35, 100, 20);
                dialog.add(name);
                for (int j = 0; j < namesNode.size(); j++) {
                    if (namesNode.get(j).getText().equals(name.getText()))
                        contains = true;
                }
                if (!contains)
                    namesNode.add(name);
            }
        }
    }
}
