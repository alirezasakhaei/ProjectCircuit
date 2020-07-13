public class CurrentDependentVoltageSource extends Element{

    double gain;
    Node positiveDependence,negativeDependence;
    CurrentDependentVoltageSource(String name,Node positive,Node negative,double gain,Node positiveDependence,Node negativeDependence){
        this.name=name;
        positiveNode=positive;
        negativeNode=negative;
        this.gain=gain;
        this.positiveDependence=positiveDependence;
        this.negativeDependence=negativeDependence;
    }
}
