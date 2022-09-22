package com.dbmanager.datastructure.architecture;

import java.util.ArrayList;
import java.util.List;

public class Architecture {
	private String deviceName;
	private SWPlatform swPlatform;
	private CPUArchitecture CPU;
	private List<Processor> processorList;
	private Memory memory;
	private List<String> moduleList;
	private List<EnvironmentVariable> environmentVariableList;

	public Architecture() {
		processorList = new ArrayList<Processor>();
		environmentVariableList = new ArrayList<EnvironmentVariable>();
		moduleList = new ArrayList<String>();
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setOS(String swPlatform) {
		this.swPlatform = SWPlatform.valueOf(swPlatform.toUpperCase());
	}

	public SWPlatform getSWPlatform() {
		return swPlatform;
	}

	public CPUArchitecture getCPU() {
		return CPU;
	}

	public void setCPU(String CPU) {
		this.CPU = CPUArchitecture.valueOf(CPU.toUpperCase());
	}

	public void setProcessors(List<Processor> processorList) {
		this.processorList = processorList;
	}

	public List<Processor> getProcessors() {
		return processorList;
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	public Memory getMemory() {
		return memory;
	}

	public void setModuleList(List<String> moduleList) {
		this.moduleList = moduleList;
	}

	public List<String> getModuleList() {
		return moduleList;
	}

	public void setEnvironmentVariables(List<EnvironmentVariable> environmentVariableList) {
		this.environmentVariableList = environmentVariableList;
	}

	public List<EnvironmentVariable> getEnvironmentVariableList() {
		return environmentVariableList;
	}
}
