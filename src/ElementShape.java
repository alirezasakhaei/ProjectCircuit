import javax.swing.*;
import java.awt.Graphics;

public class ElementShape extends JPanel {
    Element element;
    char type;
    boolean isHorizental;
    boolean isUpWardRightWard;
    ElementShape (Element element){
        this.element = element;
        if (element.positiveNode.getName() == 0 || element.negativeNode.getName() == 0){
            isHorizental = false;
        }else {
            isHorizental = true;
        }
        type = element.name.charAt(0);
        if (element.positiveNode.getName() > element.negativeNode.getName())
            isUpWardRightWard = true;
        else
            isUpWardRightWard = false;
    }

    @Override
    public void paint(Graphics g) {
        if (isHorizental && isUpWardRightWard){
            setSize(100,10);
            g.drawLine(25,5,0,5);
            g.drawLine(75,5,100,5);
            switch (type){
                case 'R' :
                    setSize(100,10);
                    g.drawLine(25,5,0,5);
                    g.drawLine(75,5,100,5);
                    g.drawLine(25,5,35,0);
                    g.drawLine(35,0,45,10);
                    g.drawLine(45,10,55,0);
                    g.drawLine(55,0,65,10);
                    g.drawLine(65,10,75,5);
                    break;
                case 'C' :
                    g.drawLine(40,5,0,5);
                    g.drawLine(60,5,100,5);
                    g.drawLine(40,0,40,10);
                    g.drawLine(60,0,60,10);
                    break;
                case 'L' :
                    for (int k=0;k<5;k++)
                        g.drawOval(25+10*k,3,10,10);
                    break;

                case 'V' :
                    g.drawLine(25,0,25,10);
                    g.drawLine(75,0,75,10);
                    g.drawLine(25,1,75,1);
                    g.drawLine(25,9,75,9);

                    g.drawLine(44,3,44,7);
                    g.drawLine(41,5,47,5);

                    g.drawLine(55,3,55,7);
                    break;

                case 'I' :
                    g.drawLine(25,0,25,10);
                    g.drawLine(75,0,75,10);
                    g.drawLine(25,1,75,1);
                    g.drawLine(25,9,75,9);

                    g.drawLine(30,5,70,5);
                    g.drawLine(30,5,35,0);
                    g.drawLine(30,5,35,10);
                    break;


                case 'E' :
                case 'H' :
                    g.drawLine(50,0,25,5);
                    g.drawLine(50,0,75,5);
                    g.drawLine(50,10,25,5);
                    g.drawLine(50,10,75,5);

                    g.drawLine(44,3,44,7);
                    g.drawLine(41,5,47,5);

                    g.drawLine(55,3,55,7);
                    break;

                case 'G' :
                case 'F' :
                    g.drawLine(50,0,25,5);
                    g.drawLine(50,0,75,5);
                    g.drawLine(50,10,25,5);
                    g.drawLine(50,10,75,5);

                    g.drawLine(30,5,70,5);
                    g.drawLine(30,5,35,0);
                    g.drawLine(30,5,35,10);
                    break;

                case 'D' :
                    g.drawLine(25,5,45,5);
                    g.drawLine(55,5,75,5);
                    g.drawLine(45,0,45,10);
                    g.drawLine(55,0,55,10);

                    g.drawLine(55,0,45,5);
                    g.drawLine(55,10,45,5);
                    break;
            }
        }

        if (isHorizental && !isUpWardRightWard){
            setSize(100,10);
            g.drawLine(25,5,0,5);
            g.drawLine(75,5,100,5);
            switch (type){
                case 'R' :
                    setSize(100,10);
                    g.drawLine(25,5,0,5);
                    g.drawLine(75,5,100,5);
                    g.drawLine(25,5,35,0);
                    g.drawLine(35,0,45,10);
                    g.drawLine(45,10,55,0);
                    g.drawLine(55,0,65,10);
                    g.drawLine(65,10,75,5);
                    break;
                case 'C' :
                    g.drawLine(40,5,0,5);
                    g.drawLine(60,5,100,5);
                    g.drawLine(40,0,40,10);
                    g.drawLine(60,0,60,10);
                    break;
                case 'L' :
                    for (int k=0;k<5;k++)
                        g.drawOval(25+10*k,3,10,10);
                    break;

                case 'V' :
                    g.drawLine(25,0,25,10);
                    g.drawLine(75,0,75,10);
                    g.drawLine(25,1,75,1);
                    g.drawLine(25,9,75,9);

                    g.drawLine(56,3,56,7);
                    g.drawLine(59,5,53,5);

                    g.drawLine(45,3,45,7);
                    break;

                case 'I' :
                    g.drawLine(25,0,25,10);
                    g.drawLine(75,0,75,10);
                    g.drawLine(25,1,75,1);
                    g.drawLine(25,9,75,9);

                    g.drawLine(30,5,70,5);

                    g.drawLine(70,5,65,0);
                    g.drawLine(70,5,65,10);
                    break;


                case 'E' :
                case 'H' :
                    g.drawLine(50,0,25,5);
                    g.drawLine(50,0,75,5);
                    g.drawLine(50,10,25,5);
                    g.drawLine(50,10,75,5);

                    g.drawLine(56,3,56,7);
                    g.drawLine(59,5,53,5);

                    g.drawLine(45,3,45,7);
                    break;

                case 'G' :
                case 'F' :
                    g.drawLine(50,0,25,5);
                    g.drawLine(50,0,75,5);
                    g.drawLine(50,10,25,5);
                    g.drawLine(50,10,75,5);

                    g.drawLine(30,5,70,5);
                    g.drawLine(70,5,65,0);
                    g.drawLine(70,5,65,10);
                    break;

                case 'D' :
                    g.drawLine(25,5,45,5);
                    g.drawLine(55,5,75,5);
                    g.drawLine(45,0,45,10);
                    g.drawLine(55,0,55,10);

                    g.drawLine(45,0,55,5);
                    g.drawLine(45,10,55,5);
                    break;
            }
        }

        if (!isHorizental && isUpWardRightWard){
            setSize(10,100);
            g.drawLine(5,25,5,0);
            g.drawLine(5,75,5,100);
            switch (type){
                case 'R' :
                    g.drawLine(5,25,0,35);
                    g.drawLine(0,35,10,45);
                    g.drawLine(10,45,0,55);
                    g.drawLine(0,55,10,65);
                    g.drawLine(10,65,5,75);
                    break;
                case 'C' :
                    g.drawLine(5,40,5,0);
                    g.drawLine(5,60,5,100);
                    g.drawLine(0,40,10,40);
                    g.drawLine(0,60,10,60);
                    break;
                case 'L' :
                    for (int k=0;k<5;k++)
                        g.drawOval(3,25+10*k,10,10);
                    break;
                case 'V' :
                    g.drawLine(0,25,10,25);
                    g.drawLine(0,75,10,75);
                    g.drawLine(1,25,1,75);
                    g.drawLine(9,25,9,75);

                    g.drawLine(3,44,7,44);
                    g.drawLine(5,41,5,47);

                    g.drawLine(3,55,7,55);
                    break;
                case 'I' :
                    g.drawLine(0,25,10,25);
                    g.drawLine(0,75,10,75);
                    g.drawLine(1,25,1,75);
                    g.drawLine(9,25,9,75);

                    g.drawLine(5,30,5,70);
                    g.drawLine(5,30,0,35);
                    g.drawLine(5,30,10,35);
                    break;

                case 'E' :
                case 'H' :
                    g.drawLine(0,50,5,25);
                    g.drawLine(0,50,5,75);
                    g.drawLine(10,50,5,25);
                    g.drawLine(10,50,5,75);

                    g.drawLine(3,44,7,44);
                    g.drawLine(5,41,5,47);

                    g.drawLine(3,55,7,55);
                    break;

                case 'G' :
                case 'F' :
                    g.drawLine(0,50,5,25);
                    g.drawLine(0,50,5,75);
                    g.drawLine(10,50,5,25);
                    g.drawLine(10,50,5,75);

                    g.drawLine(5,30,5,70);
                    g.drawLine(5,30,0,35);
                    g.drawLine(5,30,10,35);
                    break;

                case 'D' :
                    g.drawLine(5,25,5,45);
                    g.drawLine(5,75,5,55);
                    g.drawLine(0,45,10,45);
                    g.drawLine(0,55,10,55);

                    g.drawLine(0,45,5,55);
                    g.drawLine(10,45,5,55);
                    break;

            }
        }

        if (!isHorizental && !isUpWardRightWard){
            setSize(10,100);
            g.drawLine(5,25,5,0);
            g.drawLine(5,75,5,100);
            switch (type){
                case 'R' :
                    g.drawLine(5,25,0,35);
                    g.drawLine(0,35,10,45);
                    g.drawLine(10,45,0,55);
                    g.drawLine(0,55,10,65);
                    g.drawLine(10,65,5,75);
                    break;
                case 'C' :
                    g.drawLine(5,40,5,0);
                    g.drawLine(5,60,5,100);
                    g.drawLine(0,40,10,40);
                    g.drawLine(0,60,10,60);
                    break;
                case 'L' :
                    for (int k=0;k<5;k++)
                        g.drawOval(3,25+10*k,5,10);
                    break;
                case 'V' :
                    g.drawLine(0,25,10,25);
                    g.drawLine(0,75,10,75);
                    g.drawLine(1,25,1,75);
                    g.drawLine(9,25,9,75);

                    g.drawLine(3,56,7,56);
                    g.drawLine(5,59,5,53);

                    g.drawLine(3,45,7,45);
                    break;
                case 'I' :
                    g.drawLine(0,25,10,25);
                    g.drawLine(0,75,10,75);
                    g.drawLine(1,25,1,75);
                    g.drawLine(9,25,9,75);

                    g.drawLine(5,30,5,70);
                    g.drawLine(5,70,0,65);
                    g.drawLine(5,70,10,65);
                    break;
                case 'E' :
                case 'H' :
                    g.drawLine(0,50,5,25);
                    g.drawLine(0,50,5,75);
                    g.drawLine(10,50,5,25);
                    g.drawLine(10,50,5,75);

                    g.drawLine(3,56,7,56);
                    g.drawLine(5,59,5,53);

                    g.drawLine(3,45,7,45);
                    break;

                case 'G' :
                case 'F' :
                    g.drawLine(0,50,5,25);
                    g.drawLine(0,50,5,75);
                    g.drawLine(10,50,5,25);
                    g.drawLine(10,50,5,75);

                    g.drawLine(5,30,5,70);
                    g.drawLine(5,70,0,65);
                    g.drawLine(5,70,10,65);
                    break;
                case 'D' :
                    g.drawLine(5,25,5,45);
                    g.drawLine(5,75,5,55);
                    g.drawLine(0,45,10,45);
                    g.drawLine(0,55,10,55);

                    g.drawLine(0,55,5,45);
                    g.drawLine(10,55,5,45);
                    break;
            }
        }
    }
}
