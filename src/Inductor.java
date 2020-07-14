public class Inductor extends Element {
    private double inductance;
    private double voltage;
    private double history;

    public Inductor(String name,Node positiveNode,Node negativeNode,double inductance){
        this.name=name;
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.inductance=inductance;
        history=0;
    }
    double getInductance(){
        return inductance;
    }
    @Override
    double getCurrent() {
        updateVoltage();
        double current=(getDt()*voltage+history)/inductance;
        history+=voltage*getDt();
        return current;
    }

    private void updateVoltage(){
        voltage=positiveNode.getVoltage()-negativeNode.getVoltage();
    }
}
