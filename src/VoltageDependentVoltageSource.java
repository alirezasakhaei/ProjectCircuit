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
        setLabel(gain + "(" + positiveDependence.getName() + "," + negativeDependence.getName() + ")");
    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }

    @Override
    double getCurrent() {
        double current = 0;
        boolean positiveIsGood = true;
        for (int i = 0; i < positiveNode.getPositives().size(); i++) {
            if (!positiveNode.getPositives().get(i).equals(this.name)) {
                if (Circuit.getCircuit().getElements().get(positiveNode.getPositives().get(i)).isVoltageSource()) {
                    positiveIsGood = false;
                    break;
                }
                current -= Circuit.getCircuit().getElements().get(positiveNode.getPositives().get(i)).getCurrent();
            }
        }
        if (positiveIsGood) {
            for (int i = 0; i < positiveNode.getNegatives().size(); i++) {
                current += Circuit.getCircuit().getElements().get(positiveNode.getNegatives().get(i)).getCurrent();
            }
        } else {
            current = 0;
            for (int i = 0; i < negativeNode.getPositives().size(); i++) {
                current += Circuit.getCircuit().getElements().get(negativeNode.getPositives().get(i)).getCurrent();
            }
            for (int i = 0; i < negativeNode.getNegatives().size(); i++) {
                if (!negativeNode.getNegatives().get(i).equals(this.name)) {
                    current -= Circuit.getCircuit().getElements().get(negativeNode.getNegatives().get(i)).getCurrent();
                }
            }
        }
        return current;
    }

    @Override
    public double getVoltage() {

        return (positiveDependence.getPreviousVoltage() - negativeDependence.getPreviousVoltage()) * gain;
    }
}
