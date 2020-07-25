import org.omg.Messaging.SYNC_WITH_TRANSPORT;

public class Inductor extends Element {
    private double inductance;
    private double previousCurrent;
    private double time;

    public Inductor(String name, Node positiveNode, Node negativeNode, double inductance) {
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        this.inductance=inductance;
        previousCurrent=0;
        time=0;
        data = String.valueOf(inductance);
    }

    double getInductance(){
        return inductance;
    }

    @Override
    double getCurrent() {
        double current = (Circuit.getCircuit().getDt() * getVoltage()) / inductance + previousCurrent;
        if (Circuit.getCircuit().getTime() > time) {
            previousCurrent = current;
            time = Circuit.getCircuit().getTime();
        }
        return current;
    }

}
