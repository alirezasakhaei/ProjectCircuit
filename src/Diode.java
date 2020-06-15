public class Diode extends Element {
    private boolean isOn;

    Diode(Node positiveNode,Node negativeNode){
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
    }

    void updateIsOn(){
        isOn= positiveNode.getVoltage() >= negativeNode.getVoltage();
    }

    boolean isOn(){
        return isOn;
    }
}
