public class Capacitor extends Element {
    private final double capacity;

    public Capacitor(String name,Node positiveNode,Node negativeNode,double capacity){
        this.name=name;
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.capacity=capacity;
        data = String.valueOf(capacity);
        setLabel(Double.toString(capacity));
    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }

    @Override
    double getCurrent() {

        return capacity * (getVoltage() - getPreviousVoltage()) / Circuit.getCircuit().getDt();
    }


}
