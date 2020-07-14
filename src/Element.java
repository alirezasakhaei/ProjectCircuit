import java.util.ArrayList;

public abstract class Element extends Circuit{
    Node positiveNode,negativeNode;
    double current;
    ArrayList<Double> currentsArray = new ArrayList<Double>();
    String name;

    double getVoltage(){
        return positiveNode.getVoltage()-negativeNode.getVoltage();
    }

    abstract double getCurrent();


    @Override
    public String toString() {
        return "Element{" +
                "name='" + name + '\'' +
                ", positiveNode=" + positiveNode +
                ", negativeNode=" + negativeNode +
                ", current=" + current +
                '}'+"\n";
    }
}
