public class Capacitor extends Element {
    private double capacity;
    private double voltage;
    private double time;
    double previousVoltage;

    public Capacitor(String name,Node positiveNode,Node negativeNode,double capacity){
        this.name=name;
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.capacity=capacity;
        previousVoltage=0;
        voltage=0;
        time=0;
        data = String.valueOf(capacity);

    }
    double getCapacity(){
        return capacity;
    }
    @Override
    double getCurrent() {

        double current = capacity * (getVoltage() - previousVoltage) / Circuit.getCircuit().getDt();
        if (Circuit.getCircuit().getTime() > time) {

            previousVoltage = getVoltage();
            time = Circuit.getCircuit().getTime();
        }
        return current;
    }

}
