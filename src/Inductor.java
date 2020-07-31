
public class Inductor extends Element {
    private double inductance;
    private double previousCurrent;
    private double time;
    double current;

    public Inductor(String name, Node positiveNode, Node negativeNode, double inductance) {
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        this.inductance=inductance;
        previousCurrent=0;
        time = 0;
        current = 0;
        data = String.valueOf(inductance);
        setLabel(Double.toString(inductance));
    }

    double getInductance() {
        return inductance;
    }

    @Override
    double getCurrent() {
        current = (Circuit.getCircuit().getDt() * getVoltage()) / inductance + previousCurrent;
        return current;
    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void updateTime() {
        currentsArray.add(getCurrent());
        voltagesArray.add(getVoltage());
        powersArray.add(getVoltage() * getCurrent());
        previousCurrent = current;
        time = Circuit.getCircuit().getTime();
    }

}
