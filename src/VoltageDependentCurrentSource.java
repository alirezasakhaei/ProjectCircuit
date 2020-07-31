public class VoltageDependentCurrentSource extends Element{
    double gain;
    Node positiveDependence,negativeDependence;
    VoltageDependentCurrentSource(String name,Node positiveNode,Node negativeNode,Node positiveDependence,Node negativeDependence,double gain){
        this.name=name;
        this.positiveNode=positiveNode;
        this.negativeNode=negativeNode;
        this.positiveDependence=positiveDependence;
        this.negativeDependence=negativeDependence;
        this.gain=gain;
        data = gain + "," + (positiveDependence.getName()) + "," + (negativeDependence.getName());
        setLabel(gain + "(" + positiveDependence.getName() + "," + negativeDependence.getName() + ")");
    }
    @Override
    void setLabel(String label) {
        this.label = label;
    }
    @Override
    double getCurrent() {
        return (positiveDependence.getVoltage()-negativeDependence.getVoltage())*gain;
    }
}
