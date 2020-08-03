import javax.swing.*;
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

    public CircuitGraphPro(int dotsNumber, Circuit circuit, ArrayList<Node> nodes,JDialog dialog) {
        this.dotsNumber = dotsNumber;
        this.circuit = circuit;
        this.nodes = nodes;
        this.dialog = dialog;
        setDiffs();
        setLengthes();
        setLocations();
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
            nodes.get(i).setEarthConnections();
            if (nodes.get(i).getEarthConnections() > maxVerPar)
                maxVerPar = nodes.get(i).getEarthConnections();
        }
        for (int i = 1; i <nodes.size() - 1;i++){
            for (int j = i+1 ; j<nodes.size();j++){
                if (Node.elementsBetween(nodes.get(i),nodes.get(j)) > maxHorPar)
                    maxHorPar = Node.elementsBetween(nodes.get(i),nodes.get(j));
            }
        }
        verticalParrallelDistance = nodesDistance/maxVerPar;
        horizentalParrallelDistance = boundLength/maxHorPar;

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
            nodes.get(i).y = 50 + boundLength;
            nodes.get(i).x = 50 + (i-1)*(nodesDistance);
        }
    }

    private void drawEarthConnecteds() {
        Node node;
        ElementShapePro elementShapePro;
        int elementNumber = 1;
        Element element;
        JLabel label,name;
        int earthConnection;
        labels = new ArrayList<>();
        names = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++){




        }
    }

    private void drawHorizontals(){

    }

    @Override
    public void paint(Graphics g) {

    }
}
