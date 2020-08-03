import javax.swing.*;
import java.awt.Graphics;
import java.util.ArrayList;

public class CircuitGraphPro extends JPanel {
    int nodesNumber;
    Circuit circuit;
    ArrayList<Node> nodes;
    JDialog dialog;
    int boundLength;
    int nodesDistance;
    int horizentalParrallelDistance;
    int verticalParrallelDistance;
    int horizentalElementWidth;
    int horizentalElementHeight;
    int vertictalElementWidth;
    int verticalElementWidth;

    public CircuitGraphPro(int nodesNumber, Circuit circuit, ArrayList<Node> nodes,JDialog dialog) {
        this.nodesNumber = nodesNumber;
        this.circuit = circuit;
        this.nodes = nodes;
        this.dialog = dialog;
        drawEarthConnecteds();
        drawHorizontals();
    }

    private void setLengthes(){



    }

    private void drawEarthConnecteds() {

    }

    private void drawHorizontals(){

    }

    @Override
    public void paint(Graphics g) {

    }
}
