import javax.swing.*;
import java.awt.Graphics;

public class ElementShape extends JPanel {
    Element element;
    char type;
    boolean isHorizental;
    boolean isUpwardRightWard;
    ElementShape (Element element){
        this.element = element;
        if (element.positiveNode.getName() == 0 || element.negativeNode.getName() == 0){
            System.out.println("moz" + element.name);
            isHorizental = false;
        }else {
            System.out.println("bitch");
            isHorizental = true;
        }
        type = element.name.charAt(0);
    }

    @Override
    public void paint(Graphics g) {
        if (isHorizental){
            switch (type){
                case 'R' :
                    setSize(50,10);
                    g.drawLine(0,5,10,0);
                    g.drawLine(10,0,20,10);
                    g.drawLine(20,10,30,0);
                    g.drawLine(30,0,40,10);
                    g.drawLine(40,10,50,5);
                    System.out.println("hor" + element.name);
                    break;
            }
        }
        if (!isHorizental){
            switch (type){
                case 'R' :
                    setSize(10,50);
                    g.drawLine(5,0,10,10);
                    g.drawLine(10,10,0,20);
                    g.drawLine(0,20,10,30);
                    g.drawLine(10,30,0,40);
                    g.drawLine(0,40,5,50);
                    System.out.println("ver" + element.name);
                    break;
            }
        }
    }
}
