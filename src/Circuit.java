import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

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
    private ArrayList<Integer> nodeNameQueue;
    private ArrayList<String> elementNames;
    private ArrayList<ArrayList<Node>> unions;
    private double dt = 0, dv = 0, di = 0, time, maximumTime;
    ArrayList<Double> timeArray;

    public Circuit() {
        nodes = new HashMap<>();
        elements = new HashMap<>();
        nodeNameQueue = new ArrayList<>();
        elementNames = new ArrayList<>();
        unions = new ArrayList<>();
        timeArray = new ArrayList<>();
    }

    /////////////////// Get-set codeBox

    public ArrayList<Double> getTimeArray() {
        return timeArray;
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

    /*   protected void setAddedNodes(int name) {
           if (!nodes.get(name).isAdded()) {
               nodes.get(name).setAdded(true);
               nodeNameQueue.add(name);
           } else return;
           for (int j = 0; j < nodes.get(name).getPositives().size(); j++) {
               if (elements.get(nodes.get(name).getPositives().get(j)).isVoltageSource()&&!elements.get(nodes.get(name).getPositives().get(j)).negativeNode.isAdded()) {
                   elements.get(nodes.get(name).getPositives().get(j)).negativeNode.setUnion(nodes.get(name).getUnion());
               }
           }
           for (int j = 0; j < nodes.get(name).getNegatives().size(); j++) {
               if (elements.get(nodes.get(name).getNegatives().get(j)).isVoltageSource()&&!elements.get(nodes.get(name).getNegatives().get(j)).positiveNode.isAdded()) {
                   elements.get(nodes.get(name).getNegatives().get(j)).positiveNode.setUnion(nodes.get(name).getUnion());
               }
           }
           for (int i = 0; i < nodes.get(name).getNeighbors().size(); i++) {
               setAddedNodes(nodes.get(name).getNeighbors().get(i));
           }
       }
   */
    protected void setAddedNodes(int name) {
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(name);
        nodes.get(name).setAdded(true);
        nodeNameQueue.add(name);
        while (queue.size() > 0) {
            Node node = nodes.get(queue.poll());
            for (int j = 0; j < node.getPositives().size(); j++) {
                if (elements.get(node.getPositives().get(j)).isVoltageSource()) {
                    elements.get(node.getPositives().get(j)).negativeNode.setUnion(node.getUnion());
                }
            }
            for (int j = 0; j < node.getNegatives().size(); j++) {
                if (elements.get(node.getNegatives().get(j)).isVoltageSource()) {
                    elements.get(node.getNegatives().get(j)).positiveNode.setUnion(node.getUnion());
                }
            }

            for (int i = 0; i < node.getNeighbors().size(); i++) {
                if (!nodes.get(node.getNeighbors().get(i)).isAdded()) {
                    queue.add(node.getNeighbors().get(i));
                    nodes.get(node.getNeighbors().get(i)).setAdded(true);
                    nodeNameQueue.add(node.getNeighbors().get(i));
                }
            }
        }

    }


    void reconstructUnions() {
        for (int i = 0; i < nodeNameQueue.size(); i++) {
            nodes.get(nodeNameQueue.get(i)).setAdded(false);
            nodes.get(nodeNameQueue.get(i)).setUnion(i);
        }
        for (int i = 0; i < elementNames.size(); i++) {
            if (elementNames.get(i).charAt(0) == 'D') {
                if (elements.get(elementNames.get(i)).positiveNode.getVoltage() < elements.get(elementNames.get(i)).negativeNode.getVoltage() || (elements.get(elementNames.get(i)).getVoltage() == 0 && elements.get(elementNames.get(i)).getCurrent() <= 0)) {
                    Element temp = elements.remove(elementNames.get(i));
                    IndependentCurrentSource d = new IndependentCurrentSource(elementNames.get(i), temp.positiveNode, temp.negativeNode, 0, 0, 0, 0);
                    d.setCurrentSource(true);
                    d.setCurrentsArray(temp.getCurrentsArray());
                    d.setVoltagesArray(temp.getVoltagesArray());
                    d.setPowersArray(temp.getPowersArray());
                    elements.put(elementNames.get(i), d);
                } else {
                    Element temp = elements.remove(elementNames.get(i));
                    IndependentVoltageSource d = new IndependentVoltageSource(elementNames.get(i), temp.positiveNode, temp.negativeNode, 0, 0, 0, 0);
                    d.setVoltageSource(true);
                    d.setCurrentsArray(temp.getCurrentsArray());
                    d.setVoltagesArray(temp.getVoltagesArray());
                    d.setPowersArray(temp.getPowersArray());
                    elements.put(elementNames.get(i), d);
                }
            }
        }
        nodeNameQueue.clear();
        setAddedNodes(0);
        initializeUnions();
    }


    private void setVoltagesInUnion(int i) {
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

    void solveCircuit() {
        double i1, i2;

        for (time = 0; time <= maximumTime; time += dt) {
            reconstructUnions();

            for (int i = 0; i < unions.size(); i++) {
                setVoltagesInUnion(i);
                unions.get(i).get(0).setVoltage(unions.get(i).get(0).getVoltage() - dv);
                setVoltagesInUnion(i);

                i1 = obtainCurrent(unions.get(i));
                unions.get(i).get(0).setVoltage(unions.get(i).get(0).getVoltage() + 2 * dv);
                setVoltagesInUnion(i);
                i2 = obtainCurrent(unions.get(i));
                unions.get(i).get(0).setVoltage(unions.get(i).get(0).getPreviousVoltage() + dv * (Math.abs(i1) - Math.abs(i2)) / di / 2);
                setVoltagesInUnion(i);
            }

            for (int p = 0; p < elementNames.size(); p++) {
                elements.get(elementNames.get(p)).updateTime();
            }
            for (int p = 0; p < nodeNameQueue.size(); p++) {
                nodes.get(nodeNameQueue.get(p)).updatePreviousVoltage();
            }
            timeArray.add(time);
        }

        for (int i = 0; i < elementNames.size(); i++) {
            if (elementNames.get(i).charAt(0) == 'D') {
                Element temp = elements.remove(elementNames.get(i));
                Diode d = new Diode(temp.name, temp.positiveNode, temp.negativeNode);
                d.setCurrentsArray(temp.getCurrentsArray());
                d.setVoltagesArray(temp.getVoltagesArray());
                d.setPowersArray(temp.getPowersArray());
                elements.put(elementNames.get(i), d);
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
                if (elements.get(union.get(j).getNegatives().get(i)).positiveNode.getUnion() != union.get(j).getUnion()) {
                    current += elements.get(union.get(j).getNegatives().get(i)).getCurrent();
                }
            }
        }
        return current;
    }


    private void initializeUnions() {
        ArrayList<Integer> seenUnions = new ArrayList<>();
        unions.clear();
        for (int i = 0; i < nodeNameQueue.size(); i++) {
            if (!seenUnions.contains(nodes.get(nodeNameQueue.get(i)).getUnion())) {
                ArrayList<Node> temp = new ArrayList<>();
                temp.add(nodes.get(nodeNameQueue.get(i)));
                unions.add(temp);
                seenUnions.add(nodes.get(nodeNameQueue.get(i)).getUnion());
            } else {
                unions.get(seenUnions.indexOf(nodes.get(nodeNameQueue.get(i)).getUnion())).add(nodes.get(nodeNameQueue.get(i)));
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


    String getOutput() {
        StringBuilder output = new StringBuilder();
        int[] nodeNames = new int[nodeNameQueue.size()];
        for (int i = 0; i < nodeNames.length; i++) {
            nodeNames[i] = nodeNameQueue.get(i);
        }
        Arrays.sort(nodeNames);
        for (int i = 1; i < nodeNames.length; i++) {
            output.append(nodeNames[i]);
            for (int j = 0; j < nodes.get(nodeNames[i]).getVoltagesArray().size(); j++) {
                output.append(" ");
                output.append(nodes.get(nodeNames[i]).getVoltagesArray().get(j));
            }
            output.append("\n");
        }
        output.append("\n");
        for (int i = 0; i < elementNames.size(); i++) {
            output.append(elementNames.get(i));
            for (int j = 0; j < elements.get(elementNames.get(i)).getVoltagesArray().size(); j++) {
                output.append(" ");
                output.append(elements.get(elementNames.get(i)).getVoltagesArray().get(j));
                output.append(" ");
                output.append(elements.get(elementNames.get(i)).getCurrentsArray().get(j));
                output.append(" ");
                output.append(elements.get(elementNames.get(i)).getPowersArray().get(j));
            }
            output.append("\n");
        }
        return output.toString();
    }
}
