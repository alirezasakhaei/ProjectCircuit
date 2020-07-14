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
    }
}
