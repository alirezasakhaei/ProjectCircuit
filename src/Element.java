public abstract class Element extends Circuit{
    Node positiveNode,negativeNode;
    double current;

    double getVoltage(){
        return positiveNode.getVoltage()-negativeNode.getVoltage();
    }

//    abstract double getCurrent();

}
