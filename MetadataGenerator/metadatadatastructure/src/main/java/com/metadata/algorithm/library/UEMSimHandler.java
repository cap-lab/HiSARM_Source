package com.metadata.algorithm.library;

import java.nio.file.Path;
import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.YesNoType;

public class UEMSimHandler extends UEMLibrary {
    private String robotId;
    private String simIP;
    private int simPort;

    public UEMSimHandler(String robotId, Path taskServer) {
        super(robotId);
        this.robotId = robotId;
        setName(robotId + "_" + AlgorithmConstant.SIMULATION);
        setType(robotId + "_" + getName());
        setCflags("-I/usr/local/include");
        setLdflags("-L/usr/local/lib -lRemoteAPIClient -lzmq -lsimExtZMQ");
        setFile(robotId + "_" + AlgorithmConstant.SIMULATION_FILE
                + AlgorithmConstant.LIBRARY_FILE_EXTENSION);
        setHeader(robotId + "_" + AlgorithmConstant.SIMULATION_FILE
                + AlgorithmConstant.LIBRARY_HEADER_EXTENSION);
        getExtraHeader().add(AlgorithmConstant.SIMULATION_HEADER);
        getExtraHeader().add(AlgorithmConstant.SIMULATION_REMOTE_API_CLIENT_HEADER);
        getExtraHeader().add(AlgorithmConstant.SIMULATION_REMOTE_API_CLIENT_WRAPPER_HEADER);
        getExtraSource().add(AlgorithmConstant.SIMULATION_REMOTE_API_CLIENT_WRAPPER_SOURCE);
        setIsHardwareDependent(YesNoType.NO);
        setLanguage(AlgorithmConstant.LANGUAGE_CPP);
        setFunctionList();
    }

    public String getRobotId() {
        return robotId;
    }

    public void setSimInfo(String simIP, int simPort) {
        this.simIP = simIP;
        this.simPort = simPort;
    }

    public String getSimIP() {
        return simIP;
    }

    public int getSimPort() {
        return simPort;
    }

    private void setFunctionList() {
        getFunction().add(UEMSimHandlerFunction.makeGetFuncFromResourceForObject());
        getFunction().add(UEMSimHandlerFunction.makeSetFuncJointTargetVelocityFromResource());
        getFunction().add(UEMSimHandlerFunction.makeSetFuncLEDFromResource());
        getFunction().add(UEMSimHandlerFunction.makeGetFuncPositionFromResource());
        getFunction().add(UEMSimHandlerFunction.makeGetFuncOrientationFromResource());
        getFunction().add(UEMSimHandlerFunction.makeGetFuncProximityFromResource());
        getFunction().add(UEMSimHandlerFunction.makeGetFuncFromResource());
        getFunction().add(UEMSimHandlerFunction.makeSetFuncFromResource());
        getFunction().add(UEMSimHandlerFunction.makeGetFuncForInt32SignalFromResource());
        getFunction().add(UEMSimHandlerFunction.makeSetFuncForInt32SignalFromResource());
        getFunction().add(UEMSimHandlerFunction.makeGetFuncParentObjectFromResource());
        getFunction().add(UEMSimHandlerFunction.makeSetFuncRemoveObjectFromResource());
        getFunction().add(UEMSimHandlerFunction.makeSetFuncRemoveObjectsFromResource());
        getFunction().add(UEMSimHandlerFunction.makeGetFuncObjectsInTreeFromResource());
        getFunction().add(UEMSimHandlerFunction.makeGetFuncVisionSensorImgFromResource());
    }
}
