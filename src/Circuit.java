import java.util.ArrayList;
import java.util.Arrays;
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


    void initializeGraph() {
        initializeUnions();
    }

    protected void setAddedNodes(int name) {
        if (!nodes.get(name).isAdded()) {
            nodes.get(name).setAdded(true);
            nodeNameQueue.add(name);
        } else return;

        for (int j = 0; j < nodes.get(name).getPositives().size(); j++) {
            if (elements.get(nodes.get(name).getPositives().get(j)).isVoltageSource()) {

                elements.get(nodes.get(name).getPositives().get(j)).negativeNode.setUnion(nodes.get(name).getUnion());
            }
        }
        for (int j = 0; j < nodes.get(name).getNegatives().size(); j++) {
            if (elements.get(nodes.get(name).getNegatives().get(j)).isVoltageSource()) {

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
            for (int l = 0; l < 1000; l++) {
                for (int i = 0; i < unions.size(); i++) {
                    i1 = obtainCurrent(unions.get(i));
                    unions.get(i).get(0).setVoltage(unions.get(i).get(0).getVoltage() + dv);
                    for (int j = 1; j < unions.get(i).size(); j++) {
                        for (int k = 0; k < unions.get(i).get(j).getPositives().size(); k++) {
                            int q = unions.get(i).indexOf(elements.get(unions.get(i).get(j).getPositives().get(k)).negativeNode);
                            if (elements.get(unions.get(i).get(j).getPositives().get(k)).isVoltageSource() && q < j) {
                                unions.get(i).get(j).setVoltage(unions.get(i).get(q).getVoltage() + elements.get(unions.get(i).get(j).getPositives().get(k)).getVoltage());
                            }
                        }

                        for (int k = 0; k < unions.get(i).get(j).getNegatives().size(); k++) {
                            int q = unions.get(i).indexOf(elements.get(unions.get(i).get(j).getNegatives().get(k)).positiveNode);
                            if (elements.get(unions.get(i).get(j).getNegatives().get(k)).isVoltageSource() && q < j) {
                                unions.get(i).get(j).setVoltage(unions.get(i).get(q).getVoltage() - elements.get(unions.get(i).get(j).getNegatives().get(k)).getVoltage());
                            }
                        }
                    }
                    i2 = obtainCurrent(unions.get(i));
                    unions.get(i).get(0).setVoltage(unions.get(i).get(0).getVoltage() - dv + dv * (Math.abs(i1) - Math.abs(i2)) / di);
                    for (int j = 1; j < unions.get(i).size(); j++) {
                        for (int k = 0; k < unions.get(i).get(j).getPositives().size(); k++) {
                            int q = unions.get(i).indexOf(elements.get(unions.get(i).get(j).getPositives().get(k)).negativeNode);
                            if (elements.get(unions.get(i).get(j).getPositives().get(k)).isVoltageSource() && q < j) {
                                unions.get(i).get(j).setVoltage(unions.get(i).get(q).getVoltage() + elements.get(unions.get(i).get(j).getPositives().get(k)).getVoltage());
                            }
                        }

                        for (int k = 0; k < unions.get(i).get(j).getNegatives().size(); k++) {
                            int q = unions.get(i).indexOf(elements.get(unions.get(i).get(j).getNegatives().get(k)).positiveNode);
                            if (elements.get(unions.get(i).get(j).getNegatives().get(k)).isVoltageSource() && q < j) {
                                unions.get(i).get(j).setVoltage(unions.get(i).get(q).getVoltage() - elements.get(unions.get(i).get(j).getNegatives().get(k)).getVoltage());
                            }
                        }
                    }
                }
            }

            System.out.println(Circuit.getCircuit().getTime());
            for (int i = 0; i < elementNames.size(); i++) {
                System.out.println(elementNames.get(i) + " " + elements.get(elementNames.get(i)).getCurrent());
            }


            System.out.println(nodes.get(1).getVoltage() + " " + nodes.get(0).getVoltage());
            System.out.println();
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
        ArrayList<Integer> seenUnions = new ArrayList<>();
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


    protected void checkLoopValidation(ArrayList<String> elementsUsed, ArrayList<Integer> nodesPassed, int currentNode) {
        if (nodesPassed.size() > 1 && currentNode == 0) {
            for (int i = 0; i < nodesPassed.size(); i++) {
                nodes.get(nodesPassed.get(i)).setAdded(true);
            }
            return;
        }

        for (int i = 0; i < nodes.get(currentNode).getPositives().size(); i++) {
            if (!elementsUsed.contains(nodes.get(currentNode).getPositives().get(i))) {
                nodesPassed.add(elements.get(nodes.get(currentNode).getPositives().get(i)).negativeNode.getName());
                elementsUsed.add(nodes.get(currentNode).getPositives().get(i));
                checkLoopValidation(elementsUsed, nodesPassed, elements.get(nodes.get(currentNode).getPositives().get(i)).negativeNode.getName());
                nodesPassed.remove(nodesPassed.size() - 1);
                elementsUsed.remove(elementsUsed.size() - 1);
            }
        }

        for (int i = 0; i < nodes.get(currentNode).getNegatives().size(); i++) {
            if (!elementsUsed.contains(nodes.get(currentNode).getNegatives().get(i))) {
                nodesPassed.add(elements.get(nodes.get(currentNode).getNegatives().get(i)).positiveNode.getName());
                elementsUsed.add(nodes.get(currentNode).getNegatives().get(i));
                checkLoopValidation(elementsUsed, nodesPassed, elements.get(nodes.get(currentNode).getNegatives().get(i)).positiveNode.getName());
                nodesPassed.remove(nodesPassed.size() - 1);
                elementsUsed.remove(elementsUsed.size() - 1);
            }
        }
    }


}
