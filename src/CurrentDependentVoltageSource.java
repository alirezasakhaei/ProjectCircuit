public class CurrentDependentVoltageSource extends Element{

    double gain;
    Node positiveDependence,negativeDependence;
    CurrentDependentVoltageSource(Node positive,Node negative,double gain,Node positiveDependence,Node negativeDependence){
        positiveNode=positive;
        negativeNode=negative;
        this.gain=gain;
        this.positiveDependence=positiveDependence;
        this.negativeDependence=negativeDependence;
    }
}
