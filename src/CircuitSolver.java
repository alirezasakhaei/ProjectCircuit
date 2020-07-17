import java.util.Map;

public class CircuitSolver {
    Circuit circuit;
    double time, timeMaximum, dv, dt, di;

    public CircuitSolver(Circuit circuit) {
        this.circuit = circuit;
        time = 0;
        timeMaximum = circuit.getMaximumTime();
        dv = circuit.getDv();
        di = circuit.getDi();
        dt = circuit.getDt();
    }

    public double iCalculator(Node node) {
        double iTotal = 0;
        Element element;
        for (int i = 0; i < node.getPositives().size(); i++) {
            element = circuit.getElements().get(node.getPositives().get(i));
            if (element.isCurrentSource())
                iTotal -= element.getCurrent();
            else
                iTotal += element.getCurrent();
        }
        for (int i = 0; i < node.getNegatives().size(); i++) {
            element = circuit.getElements().get(node.getNegatives().get(i));
            if (element.isCurrentSource())
                iTotal += element.getCurrent();
            else
                iTotal -= element.getCurrent();
        }
        return iTotal;
    }

    public void solve() {
        double iTotal1, iTotal2;
        Node node = null;
        for (time = 0; time < timeMaximum; time += dt) {
            for (int i = 0; i < 100000; i++) {
                node = circuit.getNodes().get(1);
                iTotal1 = iCalculator(node);
                node.setVoltage(node.getVoltage() + dv);
                iTotal2 = iCalculator(node);
                node.setVoltage(node.getVoltage() - dv + ((Math.abs(iTotal1) - Math.abs(iTotal2)) / di * dv));
            }
            System.out.println(time + "=" + node.getVoltage());
        }
        System.out.println(circuit.getElements().get("Iin").getVoltage());
        System.out.println(circuit.getElements().get("Iin").getCurrent());

    }
}