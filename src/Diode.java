public class Diode extends Element {
    private boolean isOn;

    Diode(String name,Node positiveNode,Node negativeNode){
        this.name=name;
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
    }

    void updateIsOn(){
        isOn= positiveNode.getVoltage() >= negativeNode.getVoltage();
    }

    boolean isOn(){
        return isOn;
    }

    @Override
    double getCurrent() {
        if(!isOn)
        return 0;
        //to be completed...
        return 0;
    }
}
