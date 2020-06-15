public class Resistor extends Element{
    private double resistance;

    public Resistor(Node positiveNode,Node negativeNode,double resistance){
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.resistance=resistance;
    }
    double getResistance(){
        return resistance;
    }


//    @Override
    double getCurrent() {
        return getVoltage()/resistance;
    }
}
