public class Inductor extends Element {
    private double inductance;

    public Inductor(String name,Node positiveNode,Node negativeNode,double inductance){
        this.name=name;
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.inductance=inductance;
    }
    double getInductance(){
        return inductance;
    }
}
