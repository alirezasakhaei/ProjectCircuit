import java.util.HashMap;

public class Circuit {
    private HashMap<String,Node> nodes;
    private HashMap<String,Element> elements;
    public Circuit(){
    }

    void addNode(String name){
        int i=nodes.size();
        if(!nodes.containsKey(name)) {
            if (name.equals("0"))
                nodes.put(name, new Node(true));
            else nodes.put(name, new Node(false));
            nodes.get(name).setUnion(i);
        }

    }
    //resistor,capacitor,inductor
    void addElement(String name,String positive,String negative,String type,double value){
        if(!elements.containsKey(name)){
            switch (type){
                case "resistor":
                    elements.put(name,new Resistor(nodes.get(positive),nodes.get(negative),value));
                    break;
                case "capacitor":
                    elements.put(name,new Capacitor(nodes.get(positive),nodes.get(negative),value));
                    break;
                case "inductance":
                    elements.put(name,new Inductor(nodes.get(positive),nodes.get(negative),value));
                    break;
            }
        }
    }
    //diode
    void addElement(String name,String positive,String negative,String type){
        if(type.equals("diode")&&!elements.containsKey(name))
            elements.put(name,new Diode(nodes.get(positive),nodes.get(negative)));
    }
    //independent sources
    void addElement(String name,String positive,String negative,String type,double value,double offset,double amplitude,double frequency,double phase){
        if(!elements.containsKey(name)){
            switch (type){
                case "independentCurrent":
                    elements.put(name,new IndependentCurrentSource(nodes.get(positive),nodes.get(negative),value,offset,amplitude,frequency,phase));
                    break;
                case "independentVoltage":
                    elements.put(name,new IndependentVoltageSource(nodes.get(positive),nodes.get(negative),value,offset,amplitude,frequency,phase));
                    break;
            }
        }
    }
    //dependent sources
    void addElement(String name,String positive,String negative,String type,double gain
            ,String positiveDepended,String negativeDepended){
        if(!elements.containsKey(name)){
            switch (type){
                case "CurrentDependentCurrent":
                    elements.put(name,new CurrentDependentCurrentSource(nodes.get(positive),nodes.get(negative),gain,
                            nodes.get(positiveDepended),nodes.get(negativeDepended)));
                    break;
                case "CurrentDependentVoltage":
                    elements.put(name,new CurrentDependentVoltageSource(nodes.get(positive),nodes.get(negative),gain,
                            nodes.get(positiveDepended),nodes.get(negativeDepended)));
                    break;
                case "voltageDependentCurrent":
                    elements.put(name,new VoltageDependentCurrentSource(nodes.get(positive),nodes.get(negative),gain,
                            nodes.get(positiveDepended),nodes.get(negativeDepended)));
                    break;
                case "voltageDependentVoltage":
                    elements.put(name,new VoltageDependentVoltageSource(nodes.get(positive),nodes.get(negative),gain,
                            nodes.get(positiveDepended),nodes.get(negativeDepended)));
                    break;
            }
        }
    }

}
