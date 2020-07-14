public class VoltageDependentVoltageSource extends Element{
    double gain;
    Node positiveDependence,negativeDependence;
    VoltageDependentVoltageSource(String name,Node positiveNode,Node negativeNode,Node positiveDependence,Node negativeDependence,double gain){
        this.name=name;
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.positiveDependence=positiveDependence;
        this.negativeDependence=negativeDependence;
        this.gain=gain;

    }
    @Override
    double getCurrent() {
        current=0;
        for (int i = 0; i < positiveNode.getPositives().size(); i++) {
            current-=elements.get(positiveNode.getPositives().get(i)).getCurrent();
        }
        for (int i = 0; i < negativeNode.getPositives().size(); i++) {
            current+=elements.get(negativeNode.getPositives().get(i)).getCurrent();
        }
        return current;
    }
}
