public class CurrentDependentVoltageSource extends Element {

    double gain;
    String elementDependent;
    int stackOverFlowed;


    CurrentDependentVoltageSource(String name, Node positiveNode, Node negativeNode, String elementDependent, double gain) {
        this.name = name;
        this.positiveNode = positiveNode;
        this.negativeNode = negativeNode;
        this.elementDependent = elementDependent;
        this.gain = gain;
        data = gain + "," + elementDependent;
        setLabel(gain + "(" + elementDependent + ")");


    }

    @Override
    void setLabel(String label) {
        this.label = label;
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
        return Circuit.getCircuit().getElements().get(elementDependent).getCurrent() * gain;
    }
}
