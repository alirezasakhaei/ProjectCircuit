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
        data = String.valueOf(gain) + "," + String.valueOf(positiveDependence.getName()) + "," + String.valueOf(negativeDependence.getName());

    }
    @Override
    double getCurrent() {
        current=0;
        for (int i = 0; i < positiveNode.getPositives().size(); i++) {
            current-=Circuit.getCircuit().getElements().get(positiveNode.getPositives().get(i)).getCurrent();
        }
        for (int i = 0; i < negativeNode.getPositives().size(); i++) {
            current+=Circuit.getCircuit().getElements().get(positiveNode.getNegatives().get(i)).getCurrent();
        }
        return current;
    }
}
