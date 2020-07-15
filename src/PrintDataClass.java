import java.util.Map;

public class PrintDataClass {
    Circuit circuit;

    public PrintDataClass(Circuit circuit) {
        this.circuit = circuit;
    }
    public void printData(){
        printNodesData();
        printElementData();
        printDData();
        printMaxTimeData();
    }

    public void printNodesData(){
        for (Map.Entry node : circuit.getNodes().entrySet())
            System.out.println(node.getValue().toString());
    }
    public void printElementData(){
        for (Map.Entry element : circuit.getElements().entrySet())
            System.out.println(element.getValue().toString());
    }
    public void printDData(){
        System.out.println("dt = " + circuit.getDt());
        System.out.println("dv = " + circuit.getDv());
        System.out.println("di = " + circuit.getDi());
    }
    public void printMaxTimeData(){
        System.out.println("Tran = " + circuit.getMaximumTime());
    }
}
