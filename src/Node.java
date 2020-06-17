import java.util.HashMap;

public class Node {
    private double voltage;

    private int union;
    private boolean added;

    public Node(){
        voltage=0;
        added = false;
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
        this.voltage=voltage;
    }
    double getVoltage(){
        return voltage;
    }
}
