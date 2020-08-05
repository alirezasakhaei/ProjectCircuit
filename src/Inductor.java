
public class Inductor extends Element {
    private final double inductance;
    private double previousCurrent;

    public Inductor(String name, Node positiveNode, Node negativeNode, double inductance) {
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        this.inductance=inductance;
        previousCurrent=0;
        data = String.valueOf(inductance);
        setLabel(Double.toString(inductance));
    }

    @Override
    double getCurrent() {
        return (Circuit.getCircuit().getDt() * getVoltage()) / inductance + previousCurrent;
    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void updateTime() {
        super.updateTime();
        previousCurrent = getCurrent();
    }

}
