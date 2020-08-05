public class VoltageDependentCurrentSource extends Element {
    final double gain;
    final Node positiveDependence;
    final Node negativeDependence;

    VoltageDependentCurrentSource(String name, Node positiveNode, Node negativeNode, Node positiveDependence, Node negativeDependence, double gain) {
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        this.positiveDependence = positiveDependence;
        this.negativeDependence = negativeDependence;
        this.gain = gain;
        setLabel(provideLabel(gain) + "(" + positiveDependence.getName() + "," + negativeDependence.getName() + ")");
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
