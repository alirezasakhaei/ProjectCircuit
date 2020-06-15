public class Inductor extends Element {
    private double inductance;

    public Inductor(Node positiveNode,Node negativeNode,double inductance){
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.inductance=inductance;
    }
    double getInductance(){
        return inductance;
    }
}
