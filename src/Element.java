import java.util.ArrayList;
import java.util.HashMap;

public abstract class Element extends Circuit{
    Node positiveNode,negativeNode;
    String data = null;
    double current;
    ArrayList<Double> currentsArray = new ArrayList<Double>();
    String name;

    double getVoltage(){
        return positiveNode.getVoltage()-negativeNode.getVoltage();
    }

    abstract double getCurrent();
    public boolean isParallel(Element element){
        if (positiveNode.equals(element.positiveNode) && negativeNode.equals(element.negativeNode))
            return true;
        if (positiveNode.equals(element.negativeNode) && negativeNode.equals(element.positiveNode))
            return true;
        return false;
    }
    public static boolean isTheSameKind(Element elementOne, Element elementTwo){
        if (elementOne.getClass().getName() == elementTwo.getClass().getName())
            return true;
        return false;
    }



    @Override
    public String toString() {
        return "Element{" +
                "name='" + name + '\'' +
                ", positiveNode=" + positiveNode.getName() +
                ", negativeNode=" + negativeNode.getName() +
                ", current=" + current +
                '}'+"\n";
    }
}
