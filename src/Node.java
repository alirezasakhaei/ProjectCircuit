public class Node {
    private double voltage;
    private double current;
    private int union;
    private boolean added;
    private boolean isGround;

    public Node(boolean isGround){
        voltage=0;
        added = false;
        this.isGround=isGround;
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
