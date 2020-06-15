import java.util.HashMap;

public class Node {
    private double voltage;

    public Node(){
        voltage=0;
    }

    void setVoltage(double voltage){
        this.voltage=voltage;
    }
    double getVoltage(){
        return voltage;
    }
}
