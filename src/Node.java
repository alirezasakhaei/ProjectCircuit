import java.util.ArrayList;

public class Node {
    private double voltage;
    private double current;
    private int union;
    private boolean added;
    private boolean isGround;
    private ArrayList<Integer> neighbors;
    private ArrayList<String> positives;
    private ArrayList<String> negatives;
    int name;;


    public Node(int name,boolean isGround){
        this.name=name;
        voltage=0;
        added = false;
        this.isGround=isGround;
        neighbors=new ArrayList<>();
        positives=new ArrayList<>();
        negatives=new ArrayList<>();
    }

    void setNeighbors(int neighbor){
        if(!neighbors.contains(neighbor))
            neighbors.add(neighbor);
    }

    void setPositives(String name){
        if(!positives.contains(name))
            positives.add(name);
    }

    void setNegatives(String name){
        if(!negatives.contains(name))
            negatives.add(name);
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

    void setVoltage(double voltage){
        if(!isGround)
        this.voltage=voltage;
    }
    double getVoltage(){
        if(!isGround)
        return voltage;
        else return 0;
    }

    void setCurrent(double current){
        this.current=current;
    }
    double getCurrent(){
        return current;
    }
}
