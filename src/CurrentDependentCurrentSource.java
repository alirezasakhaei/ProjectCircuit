public class CurrentDependentCurrentSource extends Element {
    double gain;
    Element dependentElement;
    CurrentDependentCurrentSource(String name,Node positiveNode,Node negativeNode,Element dependentElement,double gain){
        this.name=name;
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.dependentElement = dependentElement;
        this.gain=gain;
        data = String.valueOf(gain) + "," + String.valueOf(dependentElement.name) ;
        setLabel(gain + "(" + dependentElement.name + ")");

    }
    @Override
    void setLabel(String label) {
        this.label = label;
    }
    @Override
    double getCurrent() {
        return dependentElement.getCurrent()*gain;
    }
}
