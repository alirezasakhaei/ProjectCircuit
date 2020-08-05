
public class IndependentVoltageSource extends Element {
    final double offset;
    final double amplitude;
    final double frequency;
    final double phase;
    int stackOverFlowed;


    IndependentVoltageSource(String name, Node positive, Node negative, double offset, double amplitude, double frequency, double phase) {
        this.name = name;
        positiveNode = positive;
        negativeNode = negative;
        this.offset = offset;
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.phase = phase;
        stackOverFlowed = 0;
        if (amplitude == 0)
            setLabel(provideLabel(offset));
        else
            setLabel(provideLabel(offset) + "+" + provideLabel(amplitude) + "sin(2PI" + provideLabel(frequency) + "t+" + provideLabel(phase) + ")");
    }

    @Override
    public void setParallelToVoltageSource(boolean b) {
        if (stackOverFlowed == 0) {
            if (b)
                stackOverFlowed = 1;
            else stackOverFlowed = -1;
        }
    }

    @Override
    void setLabel(String label) {
        this.label = label;
    }

    @Override
    double getCurrent() {
        double current = 0;
        boolean positiveIsGood = true;
        if (stackOverFlowed != 1) {
            try {

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
            } catch (StackOverflowError e) {
                return 0;
            }
        } else return 0;
    }

    @Override
    public double getVoltage() {
        double theta = 2 * Math.PI * frequency * Circuit.getCircuit().getTime() + phase;
        return offset + amplitude * Math.sin(theta);
    }

}
