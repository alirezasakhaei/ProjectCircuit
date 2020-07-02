import java.util.ArrayList;

public class Node {
    private double voltage;
    private double current;
    private int union;
    private boolean added;
    private boolean isGround;
    private ArrayList<String> neighbors;
    private ArrayList<String> positives;
    private ArrayList<String> negatives;


    public Node(boolean isGround){
        voltage=0;
        added = false;
        this.isGround=isGround;
        neighbors=new ArrayList<>();
        positives=new ArrayList<>();
        negatives=new ArrayList<>();
    }

    void setNeighbors(String neighbor){
        if(!neighbors.contains(neighbor))
            neighbors.add(neighbor);
    }

    void setPositives(String name){
        if(!neighbors.contains(name))
            neighbors.add(name);
    }

    void setNegatives(String name){
        if(!neighbors.contains(name))
            neighbors.add(name);
    }

    ArrayList<String> getPositives(){
        return positives;
    }

     ArrayList<String> getNegatives(){
        return negatives;
    }

    ArrayList<String> getNeighbors(){
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
