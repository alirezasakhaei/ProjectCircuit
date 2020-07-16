public class ElementAdder {
    Circuit circuit;

    public ElementAdder(Circuit circuit) {
        this.circuit = circuit;
    }

    //node
    void addNode(int name) {
        int i = circuit.getNodes().size();
        if (!circuit.getNodes().containsKey(name)) {
            circuit.getNodes().put(name, new Node(name));
            circuit.getNodes().get(name).setUnion(i);
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
                    circuit.getElements().put(name, new IndependentCurrentSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative), offset, amplitude, frequency, phase));
                    break;
                case "independentVoltage":
                    circuit.getElements().put(name, new IndependentVoltageSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative), offset, amplitude, frequency, phase));
                    circuit.getVoltageSources().put(name, circuit.getElements().get(name));

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
                    circuit.getElements().put(name, new VoltageDependentCurrentSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative),
                            circuit.getNodes().get(positiveDepended), circuit.getNodes().get(negativeDepended), gain));
                    break;
                case "voltageDependentVoltage":
                    circuit.getElements().put(name, new VoltageDependentVoltageSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative),
                            circuit.getNodes().get(positiveDepended), circuit.getNodes().get(negativeDepended), gain));
                    circuit.getVoltageSources().put(name, circuit.getElements().get(name));

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
                    circuit.getElements().put(name, new CurrentDependentCurrentSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative),
                            circuit.getElements().get(elementDependent), gain));
                    break;
                case "CurrentDependentVoltage":
                    circuit.getElements().put(name, new CurrentDependentVoltageSource(name, circuit.getNodes().get(positive), circuit.getNodes().get(negative),
                            circuit.getElements().get(elementDependent), gain));
                    circuit.getVoltageSources().put(name, circuit.getElements().get(name));
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
