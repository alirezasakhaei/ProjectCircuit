import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.util.ArrayList;

public class Graph extends JPanel {
    double maxTime, dt, maxAmount;
    double[] fixedArray;
    int[] finalArray;
    ArrayList<Double> protoArray;
    Graph (double maxTime,double dt, double maxAmount, ArrayList<Double> protoArray){
        setSize(500,501);
        this.maxAmount = maxAmount;
        this.maxTime = maxTime;
        this.dt = dt;
        this.protoArray = protoArray;
        setFixedArray();
        setFinalArray();
    }
    private void setFixedArray(){
        fixedArray = new double[500];
        double stepTime = maxTime/500;
        int stepDt = (int) (stepTime/dt);
        for (int i =0;i<500;i++){
            fixedArray[i] = protoArray.get(i*stepDt);
        }
    }
    public void setFinalArray(){
        finalArray = new int[500];
        for (int i=0;i<500;i++){
            finalArray[i] = (int)(240*(fixedArray[i]/maxAmount));
        }
    }
    @Override
    public void paint(Graphics g) {
        g.drawLine(0,250,500,250);
        g.drawLine(0,0,0,500);
        g.drawLine(0,10,500,10);
        g.drawLine(0,490,500,490);

        for (int i =0;i<499;i++){
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setStroke(new BasicStroke(5));
            graphics2D.drawLine(i,250 - finalArray[i],i+1,250 - finalArray[i+1]);
        }
    }
}
