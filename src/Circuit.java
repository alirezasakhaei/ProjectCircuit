import java.util.ArrayList;

public class Circuit {
    private ArrayList<Node> nodes;
    private ArrayList<Element> elements;
    private ArrayList<Integer> nodeNameQueue;
    private ArrayList<String> elementNames;
    private ArrayList<ArrayList<Node>> unions;


    double dt,dv,di;

    public Circuit() {
        nodes = new ArrayList<>();
        elements = new ArrayList<>();
        nodeNameQueue = new ArrayList<>();
        unions=new ArrayList<>();
    }

    void addNode(int name) {
        int i = nodes.size();
        if (!nodes.contains(new Node(name))&&!nodes.contains(new Node(name))) {
            if (name==0)
                nodes.add(name, new Node(name));
            else nodes.add(new Node(name));
            nodes.get(name).setUnion(i);
        }
    }


    //resistor,capacitor,inductor
    void addElement(String name, int positive, int negative, String type, double value) {
        if (!elementNames.contains(name)) {
            switch (type) {
                case "resistor":
                    elements.add(new Resistor(name,nodes.get(positive), nodes.get(negative), value));
                    break;
                case "capacitor":
                    elements.add(new Capacitor(name,nodes.get(positive), nodes.get(negative), value));
                    break;
                case "inductance":
                    elements.add(new Inductor(name,nodes.get(positive), nodes.get(negative), value));
                    break;
                default:
                    return;
            }
            nodes.get(positive).setNeighbors(negative);
            nodes.get(negative).setNeighbors(positive);
            nodes.get(positive).setPositives(name);
            nodes.get(negative).setNegatives(name);
            elementNames.add(name);
        }
    }

    //diode
    void addElement(String name, int positive, int negative, String type) {
        if (type.equals("diode") && !elementNames.contains(name)) {
            elements.add(new Diode(name,nodes.get(positive), nodes.get(negative)));
            nodes.get(positive).setNeighbors(negative);
            nodes.get(negative).setNeighbors(positive);
            nodes.get(positive).setPositives(name);
            nodes.get(negative).setNegatives(name);
            elementNames.add(name);
        }
    }

    //independent sources
    void addElement(String name, int positive, int negative, String type, double value, double offset, double amplitude, double frequency, double phase) {
        if (!elementNames.contains(name)) {
            switch (type) {
                case "independentCurrent":
                    elements.add(new IndependentCurrentSource(name,nodes.get(positive), nodes.get(negative), value, offset, amplitude, frequency, phase));
                    break;
                case "independentVoltage":
                    elements.add(new IndependentVoltageSource(name,nodes.get(positive), nodes.get(negative), value, offset, amplitude, frequency, phase));
                    break;
                default:
                    return;
            }
            nodes.get(positive).setNeighbors(negative);
            nodes.get(negative).setNeighbors(positive);
            nodes.get(positive).setPositives(name);
            nodes.get(negative).setNegatives(name);
            elementNames.add(name);
        }
    }

    //dependent sources
    void addElement(String name, int positive, int negative, String type, double gain, int positiveDepended, int negativeDepended) {
        if (!elementNames.contains(name)) {
            switch (type) {
                case "CurrentDependentCurrent":
                    elements.add(new CurrentDependentCurrentSource(name,nodes.get(positive), nodes.get(negative), gain,
                            nodes.get(positiveDepended), nodes.get(negativeDepended)));
                    break;
                case "CurrentDependentVoltage":
                    elements.add(new CurrentDependentVoltageSource(name,nodes.get(positive), nodes.get(negative), gain,
                            nodes.get(positiveDepended), nodes.get(negativeDepended)));
                    break;
                case "voltageDependentCurrent":
                    elements.add(new VoltageDependentCurrentSource(name,nodes.get(positive), nodes.get(negative), gain,
                            nodes.get(positiveDepended), nodes.get(negativeDepended)));
                    break;
                case "voltageDependentVoltage":
                    elements.add(new VoltageDependentVoltageSource(name,nodes.get(positive), nodes.get(negative), gain,
                            nodes.get(positiveDepended), nodes.get(negativeDepended)));
                    break;
                default:
                    return;
            }
            nodes.get(positive).setNeighbors(negative);
            nodes.get(negative).setNeighbors(positive);
            nodes.get(positive).setPositives(name);
            nodes.get(negative).setNegatives(name);
            elementNames.add(name);
        }
    }

    private void setAddedNodes(int name) {
        if (!nodes.get(name).isAdded()) {
            nodes.get(name).setAdded(true);
            nodeNameQueue.add(name);
        } else return;

            for (int j = 0; j < nodes.get(name).getPositives().size(); j++) {
                if (elements.get(nodes.get(name).getPositives().get(j)).getClass().equals(VoltageDependentCurrentSource.class) ||
                        elements.get(nodes.get(name).getPositives().get(j)).getClass().equals(VoltageDependentVoltageSource.class) ||
                        elements.get(nodes.get(name).getPositives().get(j)).getClass().equals(IndependentVoltageSource.class)) {

                    elements.get(nodes.get(name).getPositives().get(j)).negativeNode.setUnion(nodes.get(nodeNameQueue.get(i)).getUnion());
                }
            }
            for (int j = 0; j < nodes.get(name).getNegatives().size(); j++) {
                if (elements.get(nodes.get(name).getNegatives().get(j)).getClass().equals(VoltageDependentCurrentSource.class) ||
                        elements.get(nodes.get(name).getNegatives().get(j)).getClass().equals(VoltageDependentVoltageSource.class) ||
                        elements.get(nodes.get(name).getNegatives().get(j)).getClass().equals(IndependentVoltageSource.class)) {

                    elements.get(nodes.get(name).getNegatives().get(j)).positiveNode.setUnion(nodes.get(nodeNameQueue.get(i)).getUnion());
                }
            }
        for (int i = 0; i <nodes.get(name).getNeighbors().size() ; i++) {
            setAddedNodes(nodes.get(name).getNeighbors().get(i));
        }
    }

    private void initializeUnions(){
        int currentUnion=0;
        for (int i = 0; i <nodeNameQueue.size() ; i++) {
            if(nodes.get(nodeNameQueue.get(i)).getUnion()>=currentUnion){
                ArrayList<Node> temp=new ArrayList<>();
                currentUnion++;
                temp.add(nodes.get(nodeNameQueue.get(i)));
                unions.add(temp);
            }else {
                unions.get(nodes.get(nodeNameQueue.get(i)).getUnion()).add(nodes.get(nodeNameQueue.get(i)));
            }
        }
    }

    String checkLoopValidation(String s,String validated,int currentNode){
        if(s.charAt(s.length()-1)=='0'&&s.length()>3){
            return s;
        }
        if(s.indexOf(String.valueOf(currentNode))==s.lastIndexOf(String.valueOf(currentNode))){
            for (int i = 0; i < nodes.get(currentNode).getNeighbors().size(); i++) {
                validated=validated.concat(checkLoopValidation(s.concat(nodes.get(currentNode).getNeighbors().get(i))
                        ,validated,nodes.get(currentNode).getNeighbors().get(i)));
            }
        }
        return validated;
    }

    boolean initializeGraph() {
        if(nodes.get(0).name!=0){
            //No Ground
        }
        nodeNameQueue.add(0);
        setAddedNodes(0);
        if(nodeNameQueue.size()<nodes.size()){
            //Error5
        }
        String validatedNodes=checkLoopValidation("0","",0);
        for (int i = 0; i < nodes.size(); i++) {
            if(!validatedNodes.contains(nodes.get(i).name)){
                //Error5
            }
        }
        initializeUnions();

        return true;
    }

}
