public class Resistor extends Element{
    private final double resistance;

    public Resistor(String name,Node positiveNode,Node negativeNode,double resistance){
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        this.resistance = resistance;
        setLabel(provideLabel(resistance));
    }
    @Override
    void setLabel(String label) {
        this.label = label;
    }

    @Override
    double getCurrent() {
        return getVoltage()/resistance;
    }
}
