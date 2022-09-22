package com.dbmanager.datastructure.architecture;

public enum ProcessorModel {
    I7("i7"), XEONPHI("xeonPhi"), CUDA_CPU("cuda_cpu"), CUDA_GPU_SM20("cuda_gpu_sm20"), ARM926EJ_S(
            "arm926ej-s"), ARM926EJ_S_DSP("arm926ej-s-dsp"), A7("A7"), A15(
                    "A15"), ATMEGA328P_16MHZ("atmega328p_16Mhz"), ATMEGA328P_8MHZ(
                            "atmega328p_8Mhz"), ARM_CORETEX_M7("ARM_cortex-m7");

    private String value;

    private ProcessorModel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ProcessorModel getEnumFromString(String value) {
        if (value.equals(ProcessorModel.ARM926EJ_S.value)) {
            return ProcessorModel.ARM926EJ_S;
        } else if (value.equals(ProcessorModel.ARM926EJ_S_DSP.value)) {
            return ProcessorModel.ARM926EJ_S_DSP;
        } else if (value.equals(ProcessorModel.ARM_CORETEX_M7.value)) {
            return ProcessorModel.ARM_CORETEX_M7;
        } else {
            return ProcessorModel.valueOf(value.toUpperCase());
        }
    }
}
