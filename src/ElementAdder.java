import javafx.scene.image.Image;

public class ElementAdder {
    Circuit circuit;

    public ElementAdder(Circuit circuit) {
        this.circuit = circuit;
    }

    //node
    void addNode(int name) {
        int i = circuit.getNodes().size();
        if (!circuit.getNodes().containsKey(0)) {
            i++;
        }
        if (!circuit.getNodes().containsKey(name)) {
            if (name == 0) {
                circuit.getNodes().put(name, new Node(name));
                circuit.getNodes().get(name).setUnion(0);
            } else {
                circuit.getNodes().put(name, new Node(name));
                circuit.getNodes().get(name).setUnion(i);
            }
        }

    }

    //resistor,capacitor,inductor
    void addElement(String name, int positive, int negative, String type, double value) {
        if (!circuit.getElementNames().contains(name)) {
            switch (type) {
                case "resistor":
                    circuit.getElements().put(name, new Resistor(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative), value));
                    break;
                case "capacitor":
                    circuit.getElements().put(name, new Capacitor(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative), value));
                    break;
                case "inductance":
                    circuit.getElements().put(name, new Inductor(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative), value));
                    break;
                default:
                    return;
            }
            circuit.getNodes().get(positive).setNeighbors(negative);
            circuit.getNodes().get(negative).setNeighbors(positive);
            circuit.getNodes().get(positive).setPositives(name);
            circuit.getNodes().get(negative).setNegatives(name);
            circuit.getElementNames().add(name);
        }
    }

    //diode
    void addElement(String name, int positive, int negative, String type) {
        if (type.equals("diode") && !circuit.getElementNames().contains(name)) {
            circuit.getElements().put(name, new Diode(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative)));
            circuit.getNodes().get(positive).setNeighbors(negative);
            circuit.getNodes().get(negative).setNeighbors(positive);
            circuit.getNodes().get(positive).setPositives(name);
            circuit.getNodes().get(negative).setNegatives(name);
            circuit.getElementNames().add(name);
        }
    }

    //independent sources
    void addElement(String name, int positive, int negative, String type, double offset, double amplitude, double frequency, double phase) {
        if (!circuit.getElementNames().contains(name)) {
            switch (type) {
                case "independentCurrent":
                    IndependentCurrentSource independentCurrentSource = new IndependentCurrentSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative), offset, amplitude, frequency, phase);
                    independentCurrentSource.setCurrentSource(true);
                    circuit.getElements().put(name, independentCurrentSource);
                    break;
                case "independentVoltage":
                    IndependentVoltageSource independentVoltageSource = new IndependentVoltageSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative), offset, amplitude, frequency, phase);
                    independentVoltageSource.setVoltageSource(true);
                    circuit.getElements().put(name, independentVoltageSource);

                    break;
                default:
                    return;
            }
            circuit.getNodes().get(positive).setNeighbors(negative);
            circuit.getNodes().get(negative).setNeighbors(positive);
            circuit.getNodes().get(positive).setPositives(name);
            circuit.getNodes().get(negative).setNegatives(name);
            circuit.getElementNames().add(name);
        }
    }

    //voltage dependent sources
    void addElement(String name, int positive, int negative, String type, int positiveDepended, int negativeDepended, double gain) {
        if (!circuit.getElementNames().contains(name)) {
            switch (type) {
                case "voltageDependentCurrent":
                    VoltageDependentCurrentSource voltageDependentCurrentSource = new VoltageDependentCurrentSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative),
                            circuit.getNodes().get(positiveDepended), circuit.getNodes().get(negativeDepended), gain);
                    voltageDependentCurrentSource.setCurrentSource(true);
                    circuit.getElements().put(name, voltageDependentCurrentSource);
                    break;
                case "voltageDependentVoltage":
                    VoltageDependentVoltageSource voltageDependentVoltageSource = new VoltageDependentVoltageSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative),
                            circuit.getNodes().get(positiveDepended), circuit.getNodes().get(negativeDepended), gain);
                    voltageDependentVoltageSource.setVoltageSource(true);
                    circuit.getElements().put(name, voltageDependentVoltageSource);
                    break;
                default:
                    return;
            }
            circuit.getNodes().get(positive).setNeighbors(negative);
            circuit.getNodes().get(negative).setNeighbors(positive);
            circuit.getNodes().get(positive).setPositives(name);
            circuit.getNodes().get(negative).setNegatives(name);
            circuit.getElementNames().add(name);
        }
    }

    //current dependent source
    void addElement(String name, int positive, int negative, String type, String elementDependent, double gain) {
        if (!circuit.getElementNames().contains(name)) {
            switch (type) {
                case "CurrentDependentCurrent":
                    CurrentDependentCurrentSource currentDependentCurrentSource = new CurrentDependentCurrentSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative),
                            circuit.getElements().get(elementDependent), gain);
                    currentDependentCurrentSource.setCurrentSource(true);
                    circuit.getElements().put(name, currentDependentCurrentSource);
                    break;
                case "CurrentDependentVoltage":
                    CurrentDependentVoltageSource currentDependentVoltageSource = new CurrentDependentVoltageSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative),
                            circuit.getElements().get(elementDependent), gain);
                    currentDependentVoltageSource.setVoltageSource(true);
                    circuit.getElements().put(name, currentDependentVoltageSource);
                    break;
                default:
                    return;
            }
            circuit.getNodes().get(positive).setNeighbors(negative);
            circuit.getNodes().get(negative).setNeighbors(positive);
            circuit.getNodes().get(positive).setPositives(name);
            circuit.getNodes().get(negative).setNegatives(name);
            circuit.getElementNames().add(name);
        }
    }
}
