public class IndependentVoltageSource extends Element{
    double offset,amplitude,frequency,phase;
    IndependentVoltageSource(String name,Node positive,Node negative,double offset,double amplitude,double frequency,double phase){
        this.name=name;
        positiveNode=positive;
        negativeNode=negative;
        this.offset=offset;
        this.amplitude=amplitude;
        this.frequency=frequency;
        this.phase=phase;
    }


    @Override
    double getCurrent() {
        current=0;
        for (int i = 0; i < positiveNode.getPositives().size(); i++) {
            current-=elements.get(positiveNode.getPositives().get(i)).getCurrent();
        }
        for (int i = 0; i < negativeNode.getPositives().size(); i++) {
            current+=elements.get(negativeNode.getPositives().get(i)).getCurrent();
        }
        return current;
    }
}
