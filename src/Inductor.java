public class Inductor extends Element {
    private double inductance;
    private double voltage;
    private double previousCurrent;
    private double time;

    public Inductor(String name,Node positiveNode,Node negativeNode,double inductance){
        this.name=name;
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.inductance=inductance;
        previousCurrent=0;
        time=0;
        data = String.valueOf(inductance);
    }
    double getInductance(){
        return inductance;
    }
    @Override
    double getCurrent() {
        updateVoltage();
        double current=(getDt()*voltage)/inductance + previousCurrent;
        if(getTime()>time) {
            previousCurrent = current;
            time = getTime();
        }
        return current;
    }

    private void updateVoltage(){
        voltage=positiveNode.getVoltage()-negativeNode.getVoltage();
    }
}
