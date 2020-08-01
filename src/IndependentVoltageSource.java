import java.util.ArrayList;

public class IndependentVoltageSource extends Element {
    double offset, amplitude, frequency, phase;

    IndependentVoltageSource(String name, Node positive, Node negative, double offset, double amplitude, double frequency, double phase) {
        this.name = name;
        positiveNode = positive;
        negativeNode = negative;
        this.offset = offset;
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.phase = phase;
        data = String.valueOf(offset) + "," + String.valueOf(amplitude) + "," + String.valueOf(frequency) + "," + String.valueOf(phase);
        if (amplitude == 0)
            setLabel(Double.toString(offset));
        else
            setLabel(offset + "+" + amplitude + "sin(2PI" + frequency + "t+" + phase + ")");
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

    public double getVoltage() {
        double theta = 2 * Math.PI * frequency * Circuit.getCircuit().getTime() + phase;
        return offset + amplitude * Math.sin(theta);
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
