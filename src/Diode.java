import java.util.ArrayList;

public class Diode extends Element {
    private boolean isOn;

    Diode(String name, Node positiveNode, Node negativeNode) {
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        data = null;
    }

    @Override
    double getCurrent() {
        return 0;
    }

    public void setVoltagesArray(ArrayList<Double> voltages) {
        voltagesArray = voltages;
    }

    public void setCurrentsArray(ArrayList<Double> currents) {
        currentsArray = currents;
    }

    public void setPowersArray(ArrayList<Double> powers) {
        powersArray = powers;
    }
}
