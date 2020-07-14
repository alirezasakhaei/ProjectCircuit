public class Inductor extends Element {
    private double inductance;
    private double voltage;
    private double history;
    private double time;

    public Inductor(String name,Node positiveNode,Node negativeNode,double inductance){
        this.name=name;
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.inductance=inductance;
        history=0;
        time=0;
    }
    double getInductance(){
        return inductance;
    }
    @Override
    double getCurrent() {
        updateVoltage();
        double current=(getDt()*voltage+history)/inductance;
        if(getTime()>time) {
            history += voltage * getDt();
            time = getTime();
        }
        return current;
    }

    private void updateVoltage(){
        voltage=positiveNode.getVoltage()-negativeNode.getVoltage();
    }
}
