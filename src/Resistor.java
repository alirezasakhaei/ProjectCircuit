public class Resistor extends Element{
    private double resistance;

    public Resistor(String name,Node positiveNode,Node negativeNode,double resistance){
        this.name=name;
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.resistance=resistance;
        data = String.valueOf(resistance);
    }
    double getResistance(){
        return resistance;
    }

    @Override
    double getCurrent() {
        return getVoltage()/resistance;
    }
}
