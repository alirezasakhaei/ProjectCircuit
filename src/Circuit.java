import java.util.ArrayList;
import java.util.HashMap;

public class Circuit {
    private static Circuit circuit;

    public static void setCircuit(Circuit circuit) {
        Circuit.circuit = circuit;
    }

    public static Circuit getCircuit() {
        return circuit;
    }

    private HashMap<Integer, Node> nodes;
    private HashMap<String, Element> elements;
    private HashMap<String, Element> voltageSources;
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


    public HashMap<String, Element> getVoltageSources() {
        return voltageSources;
    }

    public HashMap<Integer, Node> getNodes() {
        return nodes;
    }

    public HashMap<String, Element> getElements() {
        return elements;
    }

    public void setMaximumTime(double maximumTime) {
        this.maximumTime = maximumTime;
    }

    public double getMaximumTime() {
        return maximumTime;
    }

    public ArrayList<Integer> getNodeNameQueue() {
        return nodeNameQueue;
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

    public ArrayList<String> getElementNames() {
        return elementNames;
    }

    /////////////////// End of get-set codeBox


    int initializeGraph() {

        String validatedNodes = checkLoopValidation("0", "", 0);
        for (int i : nodes.keySet()) {
            if (!validatedNodes.contains(String.valueOf(i))) {
                return 5;
            }
        }
        initializeUnions();
        return 0;
    }

    protected void setAddedNodes(int name) {
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
