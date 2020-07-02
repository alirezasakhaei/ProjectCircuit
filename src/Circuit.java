import java.util.ArrayList;
import java.util.HashMap;

public class Circuit {
    private HashMap<String, Node> nodes;
    private HashMap<String, Element> elements;
    private ArrayList<String> nodeNameQueue;

    public Circuit() {
        nodes = new HashMap<>();
        elements = new HashMap<>();
        nodeNameQueue = new ArrayList<>();
    }

    void addNode(String name) {
        int i = nodes.size();
        if (!nodes.containsKey(name)) {
            if (name.equals("0"))
                nodes.put(name, new Node(true));
            else nodes.put(name, new Node(false));
            nodes.get(name).setUnion(i);
        }

    }

    //resistor,capacitor,inductor
    void addElement(String name, String positive, String negative, String type, double value) {
        if (!elements.containsKey(name)) {
            switch (type) {
                case "resistor":
                    elements.put(name, new Resistor(nodes.get(positive), nodes.get(negative), value));
                    break;
                case "capacitor":
                    elements.put(name, new Capacitor(nodes.get(positive), nodes.get(negative), value));
                    break;
                case "inductance":
                    elements.put(name, new Inductor(nodes.get(positive), nodes.get(negative), value));
                    break;
                default:
                    return;
            }
            nodes.get(positive).setNeighbors(negative);
            nodes.get(negative).setNeighbors(positive);
            nodes.get(positive).setPositives(name);
            nodes.get(negative).setNegatives(name);
        }
    }

    //diode
    void addElement(String name, String positive, String negative, String type) {
        if (type.equals("diode") && !elements.containsKey(name)) {
            elements.put(name, new Diode(nodes.get(positive), nodes.get(negative)));
            nodes.get(positive).setNeighbors(negative);
            nodes.get(negative).setNeighbors(positive);
            nodes.get(positive).setPositives(name);
            nodes.get(negative).setNegatives(name);
        }
    }

    //independent sources
    void addElement(String name, String positive, String negative, String type, double value, double offset, double amplitude, double frequency, double phase) {
        if (!elements.containsKey(name)) {
            switch (type) {
                case "independentCurrent":
                    elements.put(name, new IndependentCurrentSource(nodes.get(positive), nodes.get(negative), value, offset, amplitude, frequency, phase));
                    break;
                case "independentVoltage":
                    elements.put(name, new IndependentVoltageSource(nodes.get(positive), nodes.get(negative), value, offset, amplitude, frequency, phase));
                    break;
                default:
                    return;
            }
            nodes.get(positive).setNeighbors(negative);
            nodes.get(negative).setNeighbors(positive);
            nodes.get(positive).setPositives(name);
            nodes.get(negative).setNegatives(name);
        }
    }

    //dependent sources
    void addElement(String name, String positive, String negative, String type, double gain, String positiveDepended, String negativeDepended) {
        if (!elements.containsKey(name)) {
            switch (type) {
                case "CurrentDependentCurrent":
                    elements.put(name, new CurrentDependentCurrentSource(nodes.get(positive), nodes.get(negative), gain,
                            nodes.get(positiveDepended), nodes.get(negativeDepended)));
                    break;
                case "CurrentDependentVoltage":
                    elements.put(name, new CurrentDependentVoltageSource(nodes.get(positive), nodes.get(negative), gain,
                            nodes.get(positiveDepended), nodes.get(negativeDepended)));
                    break;
                case "voltageDependentCurrent":
                    elements.put(name, new VoltageDependentCurrentSource(nodes.get(positive), nodes.get(negative), gain,
                            nodes.get(positiveDepended), nodes.get(negativeDepended)));
                    break;
                case "voltageDependentVoltage":
                    elements.put(name, new VoltageDependentVoltageSource(nodes.get(positive), nodes.get(negative), gain,
                            nodes.get(positiveDepended), nodes.get(negativeDepended)));
                    break;
                default:
                    return;
            }
            nodes.get(positive).setNeighbors(negative);
            nodes.get(negative).setNeighbors(positive);
            nodes.get(positive).setPositives(name);
            nodes.get(negative).setNegatives(name);
        }
    }

    private void setAddedNodes(String name) {
        if (!nodes.get(name).isAdded()) {
            nodes.get(name).setAdded(true);
            nodeNameQueue.add(name);
        } else return;
        for (int i = 0; i < nodes.get(name).getNeighbors().size(); i++) {
            for (int j = 0; j < nodes.get(name).getPositives().size(); j++) {
                if (elements.get(nodes.get(name).getPositives().get(j)).getClass().equals(VoltageDependentCurrentSource.class) ||
                        elements.get(nodes.get(name).getPositives().get(j)).getClass().equals(VoltageDependentVoltageSource.class) ||
                        elements.get(nodes.get(name).getPositives().get(j)).getClass().equals(IndependentVoltageSource.class)) {

                    elements.get(nodes.get(name).getPositives().get(j)).negativeNode.setUnion(nodes.get(nodeNameQueue.get(i)).getUnion());
                }
            }
            for (int j = 0; j < nodes.get(name).getNegatives().size(); j++) {
                if (elements.get(nodes.get(name).getNegatives().get(j)).getClass().equals(VoltageDependentCurrentSource.class) ||
                        elements.get(nodes.get(name).getNegatives().get(j)).getClass().equals(VoltageDependentVoltageSource.class) ||
                        elements.get(nodes.get(name).getNegatives().get(j)).getClass().equals(IndependentVoltageSource.class)) {

                    elements.get(nodes.get(name).getNegatives().get(j)).positiveNode.setUnion(nodes.get(nodeNameQueue.get(i)).getUnion());
                }
            }
            setAddedNodes(nodes.get(name).getNeighbors().get(i));
        }
    }

    boolean initializeGraph() {
        nodeNameQueue.add("0");
        nodeNameQueue.addAll(nodes.get("0").getNeighbors());
        setAddedNodes("0");


        return true;
    }

}
