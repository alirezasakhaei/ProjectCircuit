import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Graphics {
    Circuit circuit;
    Graphics (Circuit circuit){
        this.circuit = circuit;
    }
    public void draw(){
        Border border = BorderFactory.createLineBorder(Color.BLACK,2,false);

        JFrame frame = new JFrame("Circuit Simulator");
        frame.setBounds(0,0,600,600);
        frame.setLayout(null);

        JPanel pText,pRun,pDraw,pLoad;
        JButton buttonRun,buttonDraw,buttonLoad;

        pText = new JPanel();
        pText.setBounds(0,300,300,300);
        pText.setBorder(border);
        pText.setLayout(null);
        frame.add(pText);

        pRun = new JPanel();
        pRun.setBounds(300,0,300,300);
        pRun.setBorder(border);
        pRun.setLayout(null);
        frame.add(pRun);

        pDraw = new JPanel();
        pDraw.setBounds(300,300,300,300);
        pDraw.setBorder(border);
        pDraw.setLayout(null);
        frame.add(pDraw);

        pLoad = new JPanel();
        pLoad.setBounds(0,0,300,300);
        pLoad.setBorder(border);
        pLoad.setLayout(null);
        frame.add(pLoad);

        buttonRun = new JButton("RUN");
        buttonRun.setBounds(100,100,100,100);
        pRun.add(buttonRun);

        buttonDraw = new JButton("Draw");
        buttonDraw.setBounds(100,100,100,100);
        pDraw.add(buttonDraw);

        buttonDraw = new JButton("Load");
        buttonDraw.setBounds(100,100,100,100);
        pLoad.add(buttonDraw);



        frame.setVisible(true);
    }

}
