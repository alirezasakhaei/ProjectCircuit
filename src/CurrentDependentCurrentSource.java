public class CurrentDependentCurrentSource extends Element {
    double gain;
    Node positiveDependence,negativeDependence;
    CurrentDependentCurrentSource(String name,Node positive,Node negative,double gain,Node positiveDependence,Node negativeDependence){
        this.name=name;
        positiveNode=positive;
        negativeNode=negative;
        this.gain=gain;
        this.positiveDependence=positiveDependence;
        this.negativeDependence=negativeDependence;
    }
}
