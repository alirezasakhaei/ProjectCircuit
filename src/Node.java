import java.util.ArrayList;

public class Node {
    private double voltage;
    private int union;
    private int branchsNumer;
    private boolean added;
    private ArrayList<Integer> neighbors;
    private ArrayList<String> positives;
    private ArrayList<String> negatives;
    private int name;
    private double previousVoltage;
    private double time;

    public int getBranchsNumer() {
        return branchsNumer;
    }

    public boolean equals(Node node) {
        if (name == node.name)
            return true;
        return false;
    }
    public Node(int name) {
        this.name = name;
        voltage = 0;
        previousVoltage = 0;
        time = 0;
        added = false;
        neighbors = new ArrayList<>();
        positives = new ArrayList<>();
        negatives = new ArrayList<>();
    }

    int getName(){
        return name;
    }

    void setNeighbors(int neighbor){
        if(!neighbors.contains(neighbor))
            neighbors.add(neighbor);
    }

    void setPositives(String name){
        if(!positives.contains(name)) {
            branchsNumer++;
            positives.add(name);
        }
    }

    void setNegatives(String name){
        if(!negatives.contains(name)) {
            branchsNumer++;
            negatives.add(name);
        }
    }

    ArrayList<String> getPositives(){
        return positives;
    }

    ArrayList<String> getNegatives(){
        return negatives;
    }

    ArrayList<Integer> getNeighbors(){
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
        time = Circuit.getCircuit().getTime();
        previousVoltage = voltage;
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
