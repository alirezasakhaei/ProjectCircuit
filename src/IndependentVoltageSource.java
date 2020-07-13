public class IndependentVoltageSource extends Element{
    double voltage,offset,amplitude,frequency,phase;
    IndependentVoltageSource(String name,Node positive,Node negative,double voltage,double offset,double amplitude,double frequency,double phase){
        this.name=name;
        positiveNode=positive;
        negativeNode=negative;
        this.voltage=voltage;
        this.offset=offset;
        this.amplitude=amplitude;
        this.frequency=frequency;
        this.phase=phase;
    }

}
