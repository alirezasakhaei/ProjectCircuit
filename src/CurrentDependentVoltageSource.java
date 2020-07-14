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

    @Override
    double getCurrent() {
        current=0;
        for (int i = 0; i < positiveNode.getPositives().size(); i++) {
            current-=elements.get(positiveNode.getPositives().get(i)).getCurrent();
        }
        for (int i = 0; i < negativeNode.getPositives().size(); i++) {
            current+=elements.get(positiveNode.getNegatives().get(i)).getCurrent();
        }
        return current;
    }

}
