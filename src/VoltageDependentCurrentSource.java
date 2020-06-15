public class VoltageDependentCurrentSource extends Element{
    double gain;
    Node positiveDependence,negativeDependence;
    VoltageDependentCurrentSource(Node positive,Node negative,double gain,Node positiveDependence,Node negativeDependence){
        positiveNode=positive;
        negativeNode=negative;
        this.gain=gain;
        this.positiveDependence=positiveDependence;
        this.negativeDependence=negativeDependence;
    }
}
