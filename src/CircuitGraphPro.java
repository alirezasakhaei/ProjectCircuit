import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

public class CircuitGraphPro extends JPanel {
    int dotsNumber;
    Circuit circuit;
    ArrayList<Node> nodes;
    JDialog dialog;
    HashMap<Integer,Integer> diffsMap = new HashMap<>();
    int boundLength;
    int nodesDistance;
    int maxHorPar = 0;
    int maxVerPar = 0;
    int horizentalParrallelDistance;
    int verticalParrallelDistance;
    int horizentalElementWidth;
    int horizentalElementHeight;
    int verticalElementWidth;
    int verticalElementHeight;
    int diffs = 0;
    ArrayList<JLabel> labels,names;
    Rectangle rectangle;

    public CircuitGraphPro(int dotsNumber, Circuit circuit, ArrayList<Node> nodes,JDialog dialog) {
        this.dotsNumber = dotsNumber;
        this.circuit = circuit;
        this.nodes = nodes;
        this.dialog = dialog;
        rectangle = dialog.getBounds();
        setDiffs();
        setLengthes();
        setLocations();

        drawEarthConnecteds();
    }

    private void setDiffs(){
        boolean flag = true;
        for (int i = 1 ; i < (dotsNumber) ; i++){
            for (int j = 1; j < (dotsNumber - i + 1) && flag ; j++){
                if (Node.elementsBetween(nodes.get(j),nodes.get(j+i)) > 0){
                    diffs++;
                    flag = false;
                    diffsMap.put(i,diffs);
                }
            }
            flag = true;
        }
    }

    private void setLengthes(){
        if (diffs <= 4)
            boundLength = 100;
        else
            boundLength = 500/(diffs + 1);
        if (dotsNumber <= 11)
            nodesDistance = 100;
        else
            nodesDistance = 1000/(dotsNumber - 1);
        for (int i=1;i<nodes.size();i++) {
            nodes.get(i).setEarthConnectionsPro();
            if (nodes.get(i).getEarthConnectionsPro() > maxVerPar)
                maxVerPar = nodes.get(i).getEarthConnectionsPro();
        }
        for (int i = 1; i <nodes.size() - 1;i++){
            for (int j = i+1 ; j<nodes.size();j++){
                if (Node.elementsBetween(nodes.get(i),nodes.get(j)) > maxHorPar)
                    maxHorPar = Node.elementsBetween(nodes.get(i),nodes.get(j));
            }
        }
        verticalParrallelDistance = nodesDistance/maxVerPar;
        if (maxHorPar > 0)
            horizentalParrallelDistance = boundLength/maxHorPar;
        else
            horizentalParrallelDistance = 0;

        verticalElementHeight = boundLength;
        horizentalElementWidth = nodesDistance;

        horizentalElementHeight = Math.min(horizentalParrallelDistance -  5,20);
        verticalElementWidth = Math.min(verticalParrallelDistance -  5,20);

        ElementShapePro.setHorizentalElementHeight(horizentalElementHeight);
        ElementShapePro.setHorizentalElementWidth(horizentalElementWidth);
        ElementShapePro.setVerticalElementHeight(verticalElementHeight);
        ElementShapePro.setVerticalElementWidth(verticalElementWidth);
    }

    private void setLocations(){
        for (int i = 1;i<nodes.size();i++){
            nodes.get(i).y = rectangle.height - (50 + boundLength);
            nodes.get(i).x = 50 + (i-1)*(nodesDistance);
        }
    }
    @Override
    public void paint(Graphics g) {
        g.drawLine(0,0,100,100);

    }
    private void drawEarthConnecteds() {
        Node node;
        ElementShapePro elementShapePro;
        int elementNumber = 1;
        Element element;
        JLabel label,name;
        labels = new ArrayList<>();
        names = new ArrayList<>();
        for (int i=1;i<nodes.size();i++){
            elementNumber = 1;
            node = nodes.get(i);
            for (int j=0; j<node.getNegatives().size(); j++){
                element = circuit.getElements().get(node.getNegatives().get(j));
                if (element.positiveNode.getName() == 0){
                    elementShapePro = new ElementShapePro(element);
                    elementShapePro.setBounds(node.x - verticalElementWidth/2 + verticalParrallelDistance*(elementNumber - 1), node.y, verticalElementWidth, verticalElementHeight) ;
                    dialog.add(elementShapePro);

                    name = new JLabel(element.name);
                    name.setBounds(node.x - verticalElementWidth/2 + verticalParrallelDistance*(elementNumber - 1), node.y +  verticalElementHeight/5, 100, 20);
                    name.setFont(new Font("Arial",Font.ITALIC,8));
                    names.add(name);
                    dialog.add(name);

                    label = new JLabel(element.label);
                    label.setBounds(node.x - verticalElementWidth/2 + verticalParrallelDistance*(elementNumber - 1), node.y +  4*verticalElementHeight/5, 100, 20);
                    label.setFont(new Font("Arial",Font.ITALIC,8));
                    labels.add(label);
                    dialog.add(label);

                    elementNumber++;
                }
            }
            for (int j=0; j<node.getPositives().size(); j++){
                element = circuit.getElements().get(node.getPositives().get(j));
                if (element.negativeNode.getName() == 0){
                    elementShapePro = new ElementShapePro(element);
                    elementShapePro.setBounds(node.x - verticalElementWidth/2 + verticalParrallelDistance*(elementNumber - 1), node.y, verticalElementWidth, verticalElementHeight) ;
                    dialog.add(elementShapePro);

                    name = new JLabel(element.name);
                    name.setBounds(node.x - verticalElementWidth/2 + verticalParrallelDistance*(elementNumber - 1), node.y +  verticalElementHeight/5, 100, 20);
                    name.setFont(new Font("Arial",Font.ITALIC,8));
                    names.add(name);
                    dialog.add(name);

                    label = new JLabel(element.label);
                    label.setBounds(node.x - verticalElementWidth/2 + verticalParrallelDistance*(elementNumber - 1), node.y +  4*verticalElementHeight/5, 100, 20);
                    label.setFont(new Font("Arial",Font.ITALIC,8));
                    labels.add(label);
                    dialog.add(label);


                    elementNumber++;
                }
            }
        }
    }

    private void drawHorizontals(){

    }


}
