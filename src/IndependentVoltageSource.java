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

}
