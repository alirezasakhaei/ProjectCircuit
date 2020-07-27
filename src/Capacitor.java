public class Capacitor extends Element {
    private double capacity;

    public Capacitor(String name,Node positiveNode,Node negativeNode,double capacity){
        this.name=name;
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.capacity=capacity;
        data = String.valueOf(capacity);
    }
    double getCapacity(){
        return capacity;
    }
    @Override
    double getCurrent() {

        return capacity * (getVoltage() - getPreviousVoltage()) / Circuit.getCircuit().getDt();
    }


}
