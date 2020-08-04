import java.awt.*;
import java.util.ArrayList;

public class Node {
    private double voltage;
    private int union;
    private int branchsNumer;
    private boolean added;
    private final ArrayList<Integer> neighbors;
    private final ArrayList<String> positives;
    private final ArrayList<String> negatives;
    private final int name;
    private double previousVoltage;
    private final ArrayList<Double> voltagesArray;
    private int earthConnections = 0, earthConnectionsPro = 0;
    private Point location;
    int x, y;

    public int getEarthConnectionsPro() {
        return earthConnectionsPro;
    }

    public void setEarthConnectionsPro() {
        for (String positive : positives) {
            if (Circuit.getCircuit().getElements().get(positive).negativeNode.name == 0) {
                earthConnectionsPro++;
            }
        }
        for (String negative : negatives) {
            if (Circuit.getCircuit().getElements().get(negative).positiveNode.name == 0) {
                earthConnectionsPro++;
            }
        }
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getEarthConnections() {
        return earthConnections;
    }

    public void setEarthConnections() {
        for (String positive : positives) {
            if (Circuit.getCircuit().getElements().get(positive).negativeNode.name == 0) {
                earthConnections++;
            }
        }
        for (String negative : negatives) {
            if (Circuit.getCircuit().getElements().get(negative).positiveNode.name == 0) {
                earthConnections++;
            }
        }
    }

    public ArrayList<Double> getVoltagesArray() {
        return voltagesArray;
    }

    public boolean equals(Node node) {
        return name == node.name;
    }

    public Node(int name) {
        this.name = name;
        voltage = 0;
        previousVoltage = 0;
        added = false;
        neighbors = new ArrayList<>();
        positives = new ArrayList<>();
        negatives = new ArrayList<>();
        voltagesArray = new ArrayList<>();
    }

    int getName() {
        return name;
    }

    void setNeighbors(int neighbor) {
        if (!neighbors.contains(neighbor))
            neighbors.add(neighbor);
    }

    void setPositives(String name) {
        if (!positives.contains(name)) {
            branchsNumer++;
            positives.add(name);
        }
    }

    void setNegatives(String name) {
        if (!negatives.contains(name)) {
            branchsNumer++;
            negatives.add(name);
        }
    }

    ArrayList<String> getPositives() {
        return positives;
    }

    ArrayList<String> getNegatives() {
        return negatives;
    }

    ArrayList<Integer> getNeighbors() {
        return neighbors;
    }

    public int getUnion() {
        return union;
    }

    public void setUnion(int union) {
        this.union = union;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    void setVoltage(double voltage) {
        if (name != 0)
            this.voltage = voltage;
    }

    double getVoltage() {
        if (name != 0)
            return voltage;
        else return 0;
    }

    double getPreviousVoltage() {
        return previousVoltage;
    }

    void updatePreviousVoltage() {
        voltagesArray.add(voltage);
        previousVoltage = voltage;
    }

    public static int elementsBetween(Node node1, Node node2) {
        int number = 0;
        for (int i = 0; i < node1.getNegatives().size(); i++)
            if (node2.getName() == Circuit.getCircuit().getElements().get(node1.getNegatives().get(i)).positiveNode.getName()) {
                number++;
            }
        for (int i = 0; i < node1.getPositives().size(); i++)
            if (node2.getName() == Circuit.getCircuit().getElements().get(node1.getPositives().get(i)).negativeNode.getName()) {
                number++;
            }

        return number;
    }


    @Override
    public String toString() {
        return "Node{" +
                "voltage=" + voltage +
                ", union=" + union +
                ", neighbors=" + neighbors +
                ", name=" + name +
                '}';
    }
}
