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
        return (positiveDependence.getVoltage() - negativeDependence.getVoltage()) * gain;
    }
}
