public class CurrentDependentVoltageSource extends Element{

    double gain;
    Element elementDependent;
    CurrentDependentVoltageSource(String name,Node positiveNode,Node negativeNode,Element elementDependent,double gain){
        this.name=name;
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.elementDependent=elementDependent;
        this.gain=gain;
        data = String.valueOf(gain) + "," + String.valueOf(elementDependent.name) ;
        setLabel(gain + "(" + elementDependent.name + ")");


    }
    @Override
    void setLabel(String label) {
        this.label = label;
    }
    @Override
    double getCurrent() {
        return elementDependent.getCurrent() * gain;
    }

}
