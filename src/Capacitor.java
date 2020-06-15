public class Capacitor extends Element {
    private double capacity;

    public Capacitor(Node positiveNode,Node negativeNode,double capacity){
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.capacity=capacity;
    }
    double getCapacity(){
        return capacity;
    }
}
