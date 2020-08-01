public class CurrentDependentCurrentSource extends Element {
    double gain;
    String dependentElement;

    CurrentDependentCurrentSource(String name, Node positiveNode, Node negativeNode, String dependentElement, double gain) {
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        this.dependentElement = dependentElement;
        this.gain = gain;
        data = String.valueOf(gain) + "," + String.valueOf(dependentElement);
        setLabel(gain + "(" + dependentElement + ")");

    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }
    @Override
    double getCurrent() {
        return Circuit.getCircuit().getElements().get(dependentElement).getCurrent() * gain;
    }
}
