public class CurrentDependentVoltageSource extends Element{

    double gain;
    Element elementDependent;
    CurrentDependentVoltageSource(String name,Node positiveNode,Node negativeNode,Element elementDependent,double gain){
        this.name=name;
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.elementDependent=elementDependent;
        this.gain=gain;
    }
}
