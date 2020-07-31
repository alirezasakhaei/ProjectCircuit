import javax.swing.*;
import java.awt.Graphics;

public class ElementShape extends JPanel {
    Element element;
    char type;
    boolean isHorizental;
    ElementShape (Element element){
        this.element = element;
        if (element.positiveNode.getName() == 0 || element.negativeNode.getName() == 0){
            isHorizental = false;
        }else
            isHorizental = true;
        type = element.name.charAt(0);
        System.out.println(type);
        setSize(50,10);
    }

    @Override
    public void paint(Graphics g) {
        if (isHorizental){
            switch (type){
                case 'R' :
                    g.drawLine(0,5,10,0);
                    g.drawLine(10,0,20,10);
                    g.drawLine(20,10,30,0);
                    g.drawLine(30,0,40,10);
                    g.drawLine(40,10,50,5);
            }



        }
    }
}
