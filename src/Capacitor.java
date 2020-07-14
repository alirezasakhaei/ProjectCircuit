public class Capacitor extends Element {
    private double capacity;
    private double previousVoltage;
    private double voltage;

    public Capacitor(String name,Node positiveNode,Node negativeNode,double capacity){
        this.name=name;
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.capacity=capacity;
        previousVoltage=0;
        voltage=0;
    }
    double getCapacity(){
        return capacity;
    }
    @Override
    double getCurrent() {
        updateVoltage();
        return capacity*(voltage-previousVoltage)/getDt();
    }

    private void updateVoltage(){
        voltage=positiveNode.getVoltage()-negativeNode.getVoltage();
    }
}
