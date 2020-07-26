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
        // System.out.println("dd");
        // System.out.println(getVoltage()+"\t"+getPreviousVoltage());
        // System.out.println(capacity * (getVoltage() - getPreviousVoltage()) / Circuit.getCircuit().getDt());
        //  System.out.println("cc");
        return capacity * (getVoltage() - getPreviousVoltage()) / Circuit.getCircuit().getDt();
    }

}
