import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Circuit {
    protected HashMap<Integer, Node> nodes;
    protected HashMap<String, Element> elements;
    private ArrayList<Integer> nodeNameQueue;
    private ArrayList<String> elementNames;
    private ArrayList<ArrayList<Node>> unions;
    private double dt = 0, dv = 0, di = 0, time, maximumTime;

    public Circuit() {
        nodes = new HashMap<>();
        elements = new HashMap<>();
        nodeNameQueue = new ArrayList<>();
        elementNames = new ArrayList<>();
        unions = new ArrayList<>();
    }

    /////////////////// Get-set codeBox

    public void setMaximumTime(double maximumTime) {
        this.maximumTime = maximumTime;
    }

    public double getMaximumTime() {
        return maximumTime;
    }

    public void setDt(double dt) {
        this.dt = dt;
    }

    public void setDv(double dv) {
        this.dv = dv;
    }

    public void setDi(double di) {
        this.di = di;
    }

    protected double getDt() {
        return dt;
    }

    protected double getDv() {
        return dv;
    }

    protected double getDi() {
        return di;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    /////////////////// End of get-set codeBox

    /////////////////// Adding codeBox
    void addNode(int name) {
        int i = nodes.size();
        if (!nodes.containsKey(name)) {
            nodes.put(name, new Node(name));
            nodes.get(name).setUnion(i);
        }
    }

    //resistor,capacitor,inductor
    void addElement(String name, int positive, int negative, String type, double value) {
        if (!elementNames.contains(name)) {
            switch (type) {
                case "resistor":
                    elements.put(name, new Resistor(name, nodes.get(positive), nodes.get(negative), value));
                    break;
                case "capacitor":
                    elements.put(name, new Capacitor(name, nodes.get(positive), nodes.get(negative), value));
                    break;
                case "inductance":
                    elements.put(name, new Inductor(name, nodes.get(positive), nodes.get(negative), value));
                    break;
                default:
                    return;
            }
            nodes.get(positive).setNeighbors(negative);
            nodes.get(negative).setNeighbors(positive);
            nodes.get(positive).setPositives(name);
            nodes.get(negative).setNegatives(name);
            elementNames.add(name);
        }
    }

    //diode
    void addElement(String name, int positive, int negative, String type) {
        if (type.equals("diode") && !elementNames.contains(name)) {
            elements.put(name, new Diode(name, nodes.get(positive), nodes.get(negative)));
            nodes.get(positive).setNeighbors(negative);
            nodes.get(negative).setNeighbors(positive);
            nodes.get(positive).setPositives(name);
            nodes.get(negative).setNegatives(name);
            elementNames.add(name);
        }
    }

    //independent sources
    void addElement(String name, int positive, int negative, String type, double offset, double amplitude, double frequency, double phase) {
        if (!elementNames.contains(name)) {
            switch (type) {
                case "independentCurrent":
                    elements.put(name, new IndependentCurrentSource(name, nodes.get(positive), nodes.get(negative), offset, amplitude, frequency, phase));
                    break;
                case "independentVoltage":
                    elements.put(name, new IndependentVoltageSource(name, nodes.get(positive), nodes.get(negative), offset, amplitude, frequency, phase));
                    break;
                default:
                    return;
            }
            nodes.get(positive).setNeighbors(negative);
            nodes.get(negative).setNeighbors(positive);
            nodes.get(positive).setPositives(name);
            nodes.get(negative).setNegatives(name);
            elementNames.add(name);
        }
    }

    //voltage dependent sources
    void addElement(String name, int positive, int negative, String type, int positiveDepended, int negativeDepended, double gain) {
        if (!elementNames.contains(name)) {
            switch (type) {

                case "voltageDependentCurrent":
                    elements.put(name, new VoltageDependentCurrentSource(name, nodes.get(positive), nodes.get(negative),
                            nodes.get(positiveDepended), nodes.get(negativeDepended), gain));
                    break;
                case "voltageDependentVoltage":
                    elements.put(name, new VoltageDependentVoltageSource(name, nodes.get(positive), nodes.get(negative),
                            nodes.get(positiveDepended), nodes.get(negativeDepended), gain));
                    break;
                default:
                    return;
            }
            nodes.get(positive).setNeighbors(negative);
            nodes.get(negative).setNeighbors(positive);
            nodes.get(positive).setPositives(name);
            nodes.get(negative).setNegatives(name);
            elementNames.add(name);
        }
    }

    //current dependent source
    void addElement(String name, int positive, int negative, String type, String elementDependent, double gain) {
        if (!elementNames.contains(name)) {
            switch (type) {
                case "CurrentDependentCurrent":
                    elements.put(name, new CurrentDependentCurrentSource(name, nodes.get(positive), nodes.get(negative),
                            elements.get(elementDependent), gain));
                    break;
                case "CurrentDependentVoltage":
                    elements.put(name, new CurrentDependentVoltageSource(name, nodes.get(positive), nodes.get(negative),
                            elements.get(elementDependent), gain));
                    break;
                default:
                    return;
            }
            nodes.get(positive).setNeighbors(negative);
            nodes.get(negative).setNeighbors(positive);
            nodes.get(positive).setPositives(name);
            nodes.get(negative).setNegatives(name);
            elementNames.add(name);
        }
    }
    /////////////////// End of adding codeBox

    /////////////////// Print the data codeBox
    public void printData(){
        printNodesData();
        printElementData();
        printDData();
        printMaxTimeData();
    }

    public void printNodesData(){
        for (Map.Entry node : nodes.entrySet())
            System.out.println(node.getValue().toString());
    }
    public void printElementData(){
        for (Map.Entry element : elements.entrySet())
            System.out.println(element.getValue().toString());
    }
    public void printDData(){
        System.out.println("dt = " + dt);
        System.out.println("dv = " + dv);
        System.out.println("di = " + di);
    }
    public void printMaxTimeData(){
        System.out.println("Tran = " + maximumTime);
    }

    /////////////////// End of printing Data codeBox


    int initializeGraph() {
        if (!nodes.containsKey(0)) {
            return 4;
        }
        setAddedNodes(0);
        if (nodeNameQueue.size() < nodes.size()) {
            return 5;
        }
        String validatedNodes = checkLoopValidation("0", "", 0);
        for (int i : nodes.keySet()) {
            if (!validatedNodes.contains(String.valueOf(i))) {
                return 5;
            }
        }
        initializeUnions();
        return 0;
    }
    private void setAddedNodes(int name) {
        if (!nodes.get(name).isAdded()) {
            nodes.get(name).setAdded(true);
            nodeNameQueue.add(name);
        } else return;

        for (int j = 0; j < nodes.get(name).getPositives().size(); j++) {
            if (elements.get(nodes.get(name).getPositives().get(j)).getClass().equals(VoltageDependentCurrentSource.class) ||
                    elements.get(nodes.get(name).getPositives().get(j)).getClass().equals(VoltageDependentVoltageSource.class) ||
                    elements.get(nodes.get(name).getPositives().get(j)).getClass().equals(IndependentVoltageSource.class)) {

                elements.get(nodes.get(name).getPositives().get(j)).negativeNode.setUnion(nodes.get(name).getUnion());
            }
        }
        for (int j = 0; j < nodes.get(name).getNegatives().size(); j++) {
            if (elements.get(nodes.get(name).getNegatives().get(j)).getClass().equals(VoltageDependentCurrentSource.class) ||
                    elements.get(nodes.get(name).getNegatives().get(j)).getClass().equals(VoltageDependentVoltageSource.class) ||
                    elements.get(nodes.get(name).getNegatives().get(j)).getClass().equals(IndependentVoltageSource.class)) {

                elements.get(nodes.get(name).getNegatives().get(j)).positiveNode.setUnion(nodes.get(name).getUnion());
            }
        }
        for (int i = 0; i < nodes.get(name).getNeighbors().size(); i++) {
            setAddedNodes(nodes.get(name).getNeighbors().get(i));
        }
    }

    void solveCircuit() {
        double i1, i2;
        for (time = 0; time <= maximumTime; time += dt) {
            for (int l = 0; l <100 ; l++) {
                for (int i = 1; i < unions.size(); i++) {
                    for (int j = 0; j < unions.get(i).size(); j++) {
                        i1 = obtainCurrent(unions.get(i));
                        unions.get(i).get(j).setVoltage(unions.get(i).get(j).getVoltage() + dv);
                        i2 = obtainCurrent(unions.get(i));
                        unions.get(i).get(j).setVoltage(unions.get(i).get(j).getVoltage() - dv + dv * (Math.abs(i1) - Math.abs(i2)) / di);
                    }
                }
            }


        }

    }


    private double obtainCurrent(ArrayList<Node> union) {
        double current = 0;
        for (int j = 0; j < union.size(); j++) {
            for (int i = 0; i < union.get(j).getPositives().size(); i++) {
                if (elements.get(union.get(j).getPositives().get(i)).negativeNode.getUnion() != union.get(j).getUnion()) {
                    current -= elements.get(union.get(j).getPositives().get(i)).getCurrent();
                }
            }
            for (int i = 0; i < union.get(j).getNegatives().size(); i++) {
                if (elements.get(union.get(j).getNegatives().get(i)).positiveNode.getUnion() != union.get(j).getUnion())
                    current += elements.get(union.get(j).getNegatives().get(i)).getCurrent();
            }
        }
        return current;
    }


    private void initializeUnions() {
        ArrayList<Integer> seenUnions=new ArrayList<>();
        for (int i = 0; i < nodeNameQueue.size(); i++) {
            if (!seenUnions.contains(nodes.get(nodeNameQueue.get(i)).getUnion())) {
                ArrayList<Node> temp = new ArrayList<>();
                temp.add(nodes.get(nodeNameQueue.get(i)));
                unions.add(temp);
                seenUnions.add(nodes.get(nodeNameQueue.get(i)).getUnion());
            } else {
                unions.get(nodes.get(nodeNameQueue.get(i)).getUnion()).add(nodes.get(nodeNameQueue.get(i)));
            }
        }
    }

    private String checkLoopValidation(String s, String validated, int currentNode) {
        if (s.charAt(s.length() - 1) == '0' && s.length() > 3) {
            return s;
        }
        if (s.indexOf(String.valueOf(currentNode)) == s.lastIndexOf(String.valueOf(currentNode))) {
            for (int i = 0; i < nodes.get(currentNode).getNeighbors().size(); i++) {
                validated = validated.concat(checkLoopValidation(s.concat(String.valueOf(nodes.get(currentNode).getNeighbors().get(i)))
                        , validated, nodes.get(currentNode).getNeighbors().get(i)));
            }
        }
        return validated;
    }


}
