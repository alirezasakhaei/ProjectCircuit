public class CurrentDependentCurrentSource extends Element {
    double gain;
    Element dependentElement;
    CurrentDependentCurrentSource(String name,Node positiveNode,Node negativeNode,Element dependentElement,double gain){
        this.name=name;
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.dependentElement = dependentElement;
        this.gain=gain;

    }
}
