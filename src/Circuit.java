import java.text.DecimalFormat;
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

    private final HashMap<Integer, Node> nodes;
    private final HashMap<String, Element> elements;
    private final ArrayList<Integer> nodeNameQueue;
    private final ArrayList<String> elementNames;
    private final ArrayList<ArrayList<Node>> unions;
    private double dt = 0, dv = 0, di = 0, time, maximumTime;

    public Circuit() {
        nodes = new HashMap<>();
        elements = new HashMap<>();
        nodeNameQueue = new ArrayList<>();
        elementNames = new ArrayList<>();
        unions = new ArrayList<>();
    }

    /////////////////// Get-set codeBox


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

    protected void setAddedNodes() {
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(0);
        nodes.get(0).setAdded(true);
        nodeNameQueue.add(0);
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

    boolean checkForVoltageLoop() {
        ArrayList<Element> voltageSources = new ArrayList<>();
        for (String elementName : elementNames) {
            if (elements.get(elementName).isVoltageSource())
                voltageSources.add(elements.get(elementName));
        }
        int i, previousSize;
        do {
            previousSize = voltageSources.size();
            i = 0;

            while (i < voltageSources.size() - 1) {
                for (int j = i + 1; j < voltageSources.size(); j++) {
                    if ((!(voltageSources.get(i).positiveNode == voltageSources.get(j).positiveNode || voltageSources.get(i).positiveNode == voltageSources.get(j).negativeNode))
                            || (!(voltageSources.get(i).negativeNode == voltageSources.get(j).positiveNode || voltageSources.get(i).negativeNode == voltageSources.get(j).negativeNode))) {

                        voltageSources.remove(voltageSources.get(i));
                    }
                }
                i++;
            }

        } while (i < previousSize - 1);
        if (voltageSources.size() < 2)
            return true;
        double voltage = 0;
        Node currentNode = voltageSources.get(0).positiveNode;
        i = 0;
        previousSize = voltageSources.size();
        while (voltageSources.size() > 0) {

            if (previousSize == voltageSources.size()) {
                i++;
                if (i == voltageSources.size()) {
                    if (Math.abs(voltage) >= dv)
                        return false;
                    else {
                        i = 0;
                        currentNode = voltageSources.get(0).positiveNode;
                    }
                }
            } else {
                i = 0;
                previousSize = voltageSources.size();
            }
            if (currentNode.equals(voltageSources.get(i).positiveNode)) {
                voltage += voltageSources.get(i).getVoltage();
                currentNode = voltageSources.get(i).negativeNode;
                voltageSources.remove(i);
            } else if (currentNode.equals(voltageSources.get(i).negativeNode)) {
                voltage -= voltageSources.get(i).getVoltage();
                currentNode = voltageSources.get(i).positiveNode;
                voltageSources.remove(i);
            }
        }
        return Math.abs(voltage) < dv;

    }

    boolean checkForCurrentNode() {
        boolean allElementsAreCurrentSource;
        double current = 0;
        for (Integer integer : nodeNameQueue) {
            allElementsAreCurrentSource = true;
            for (int j = 0; j < nodes.get(integer).getPositives().size(); j++) {
                if (elements.get(nodes.get(integer).getPositives().get(j)).isCurrentSource()) {
                    current -= elements.get(nodes.get(integer).getPositives().get(j)).getCurrent();
                } else allElementsAreCurrentSource = false;

            }
            for (int j = 0; j < nodes.get(integer).getPositives().size(); j++) {
                if (elements.get(nodes.get(integer).getPositives().get(j)).isCurrentSource()) {
                    current += elements.get(nodes.get(integer).getPositives().get(j)).getCurrent();
                } else allElementsAreCurrentSource = false;
            }
            if (allElementsAreCurrentSource && Math.abs(current) >= di)
                return false;
        }

        return true;
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

    int solveCircuit() {
        double i1, i2;
        int isKCLmet;
        Node currentNode;
        ArrayList<Node> currentUnion;
        ErrorFinder errorFinder = new ErrorFinder(circuit);

        for (int i = 0; i < unions.size(); i++) {
            setVoltagesInUnion(i);
        }
        for (time = 0; time <= maximumTime; time += dt) {
            isKCLmet = 0;
            if (time / maximumTime > 0.0005) {
                if (!errorFinder.isCurrentSourceSeries()) {
                    return -2;
                }
                if (!errorFinder.isVoltageSourcesParallel())
                    return -3;
                if (!checkForVoltageLoop()) {
                    return -3;
                }
                if (!checkForCurrentNode())
                    return -2;
            }
            for (int j = 0; j < 5000 && isKCLmet < unions.size(); j++) {
                isKCLmet = 0;
                for (int i = 0; i < unions.size(); i++) {
                    currentUnion = unions.get(i);
                    currentNode = currentUnion.get(0);

                    i1 = obtainCurrent(currentUnion);
                    currentNode.setVoltage(currentNode.getVoltage());
                    setVoltagesInUnion(i);
                    currentNode.setVoltage(currentNode.getVoltage() + dv);
                    setVoltagesInUnion(i);
                    i2 = obtainCurrent(currentUnion);
                    currentNode.setVoltage(currentNode.getVoltage() - dv + dv * (i1 - i2) / di);
                    setVoltagesInUnion(i);
                    if (obtainCurrent(currentUnion) < Math.sqrt(di)) {
                        isKCLmet++;
                    }

                }
            }
            helpConvergence();

            for (String elementName : elementNames) {
                elements.get(elementName).updateTime();
            }
            for (Integer integer : nodeNameQueue) {
                nodes.get(integer).updatePreviousVoltage();
            }
        }
        return 0;
    }


    private void helpConvergence() {
        for (Integer integer : nodeNameQueue) {
            if (Math.abs(nodes.get(integer).getVoltage() - nodes.get(integer).getPrePreviousVoltage()) < dv)
                nodes.get(integer).setVoltage((nodes.get(integer).getVoltage() + nodes.get(integer).getPreviousVoltage()) / 2);
        }
    }

    private double obtainCurrent(ArrayList<Node> union) {
        double current = 0;
        for (Node node : union) {
            for (int i = 0; i < node.getPositives().size(); i++) {
                if (elements.get(node.getPositives().get(i)).negativeNode.getUnion() != node.getUnion()) {
                    current -= elements.get(node.getPositives().get(i)).getCurrent();
                }
            }
            for (int i = 0; i < node.getNegatives().size(); i++) {
                if (elements.get(node.getNegatives().get(i)).positiveNode.getUnion() != node.getUnion()) {
                    current += elements.get(node.getNegatives().get(i)).getCurrent();
                }
            }
        }
        return Math.abs(current);
    }


    private void initializeUnions() {
        ArrayList<Integer> seenUnions = new ArrayList<>();
        unions.clear();
        for (Integer integer : nodeNameQueue) {
            if (!seenUnions.contains(nodes.get(integer).getUnion())) {
                ArrayList<Node> temp = new ArrayList<>();
                temp.add(nodes.get(integer));
                unions.add(temp);
                seenUnions.add(nodes.get(integer).getUnion());
            } else {
                unions.get(seenUnions.indexOf(nodes.get(integer).getUnion())).add(nodes.get(integer));
            }
        }
    }


    protected void checkLoopValidation(ArrayList<String> elementsUsed, ArrayList<Integer> nodesPassed, int currentNode) {

        if (nodesPassed.size() > 1 && currentNode == 0) {
            for (Integer integer : nodesPassed) {
                nodes.get(integer).setAdded(true);
            }
            return;
        }

        for (int i = 0; i < nodes.get(currentNode).getPositives().size(); i++) {
            if (!elementsUsed.contains(nodes.get(currentNode).getPositives().get(i))) {
                nodesPassed.add(elements.get(nodes.get(currentNode).getPositives().get(i)).negativeNode.getName());
                elementsUsed.add(nodes.get(currentNode).getPositives().get(i));
                checkLoopValidation(new ArrayList<>(elementsUsed), new ArrayList<>(nodesPassed), elements.get(nodes.get(currentNode).getPositives().get(i)).negativeNode.getName());
                nodesPassed.remove(nodesPassed.size() - 1);
                elementsUsed.remove(elementsUsed.size() - 1);
            }
        }

        for (int i = 0; i < nodes.get(currentNode).getNegatives().size(); i++) {
            if (!elementsUsed.contains(nodes.get(currentNode).getNegatives().get(i))) {
                nodesPassed.add(elements.get(nodes.get(currentNode).getNegatives().get(i)).positiveNode.getName());
                elementsUsed.add(nodes.get(currentNode).getNegatives().get(i));
                checkLoopValidation(new ArrayList<>(elementsUsed), new ArrayList<>(nodesPassed), elements.get(nodes.get(currentNode).getNegatives().get(i)).positiveNode.getName());
                nodesPassed.remove(nodesPassed.size() - 1);
                elementsUsed.remove(elementsUsed.size() - 1);
            }
        }
    }


    String getOutput() {
        StringBuilder output = new StringBuilder();
        try {
            int[] nodeNames = new int[nodeNameQueue.size()];
            for (int i = 0; i < nodeNames.length; i++) {
                nodeNames[i] = nodeNameQueue.get(i);
            }
            Arrays.sort(nodeNames);
            DecimalFormat decimalFormat = new DecimalFormat("0.###E0");
            for (int i = 1; i < nodeNames.length; i++) {
                output.append(nodeNames[i]);
                for (int j = 0; j < nodes.get(nodeNames[i]).getVoltagesArray().size(); j++) {
                    output.append(" ");
                    output.append(decimalFormat.format(nodes.get(nodeNames[i]).getVoltagesArray().get(j)).replace("E0", ""));
                }
                output.append("\n");
            }
            output.append("\n");
            for (String elementName : elementNames) {
                output.append(elementName);
                for (int j = 0; j < elements.get(elementName).getVoltagesArray().size(); j++) {
                    output.append(" ");
                    output.append(decimalFormat.format(elements.get(elementName).getVoltagesArray().get(j)).replace("E0", ""));
                    output.append(" ");
                    output.append(decimalFormat.format(elements.get(elementName).getCurrentsArray().get(j)).replace("E0", ""));
                    output.append(" ");
                    output.append(decimalFormat.format(elements.get(elementName).getVoltagesArray().get(j) * elements.get(elementName).getCurrentsArray().get(j)).replace("E0", ""));
                }
                output.append("\n");
            }
            return output.toString();
        } catch (OutOfMemoryError e) {
            return "Out of Memory!\ntry increasing dt or reducing maximum time.";
        }
    }
}


