import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;

public class CircuitGraph extends JPanel {
    int nodesNumber;
    Circuit circuit;

    public CircuitGraph(int nodesNumber, Circuit circuit) {
        this.nodesNumber = nodesNumber;
        this.circuit = circuit;
    }

    @Override
    public void paint(Graphics g) {
        g.drawLine(10,450,490,450);
        for (int i=0;i<nodesNumber;i++){
            g.fillOval(10 + 160*i,350,5,5);
        }
    }
}
