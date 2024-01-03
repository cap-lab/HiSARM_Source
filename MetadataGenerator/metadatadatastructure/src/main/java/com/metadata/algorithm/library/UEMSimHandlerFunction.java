package com.metadata.algorithm.library;

import hopes.cic.xml.YesNoType;

public class UEMSimHandlerFunction extends UEMLibraryFunction {
    public static UEMLibraryFunction makeGetFuncFromResourceForHandler() {
        UEMLibraryFunction libraryFunction = makeGetFunction("handler", null, "RemoteAPIClient*");
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncFromResourceForObject() {
        UEMLibraryFunction libraryFunction = makeGetFunction("object", null, "int64_t");
        libraryFunction.getArgument().add(makeArgument("std::string", "path", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument("std::optional<json>", "options", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncJointTargetVelocityFromResource() {
        UEMLibraryFunction libraryFunction = makeSetFunction("joint_target_velocity", null, "void");
        libraryFunction.getArgument().add(makeArgument("int64_t", "objectHandle", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("double", "targetVelocity", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncLEDFromResource() {
        UEMLibraryFunction libraryFunction = makeSetFunction("led", null, "void");
        libraryFunction.getArgument().add(makeArgument("int64_t", "lightHandle", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("int64_t", "state", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("double*", "value", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncPositionFromResource() {
        UEMLibraryFunction libraryFunction = makeGetFunction("position", null, "void");
        libraryFunction.getArgument().add(makeArgument("int64_t", "objectHandle", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument("int64_t", "relativeObjectHandle", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("double*", "buffer", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncOrientationFromResource() {
        UEMLibraryFunction libraryFunction = makeGetFunction("orientation", null, "void");
        libraryFunction.getArgument().add(makeArgument("int64_t", "objectHandle", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument("int64_t", "relativeObjectHandle", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("double*", "buffer", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncProximityFromResource() {
        UEMLibraryFunction libraryFunction = makeGetFunction("proximity", null, "int64_t");
        libraryFunction.getArgument().add(makeArgument("int64_t", "sensorHandle", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("double*", "dist", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncFromResource() {
        UEMLibraryFunction libraryFunction = makeGetFunction("function", null, "void");
        libraryFunction.getArgument().add(makeArgument("std::string", "command", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("json", "_args", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("int64_t", "size", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("void*", "buffer", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncFromResource() {
        UEMLibraryFunction libraryFunction = makeSetFunction("function", null, "void");
        libraryFunction.getArgument().add(makeArgument("std::string", "command", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("json", "_args", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncForInt32SignalFromResource() {
        UEMLibraryFunction libraryFunction = makeGetFunction("int32_signal", null, "int32_t");
        libraryFunction.getArgument().add(makeArgument("const char*", "signalName", YesNoType.YES));
        libraryFunction.getArgument().add(makeArgument("int32_t*", "buffer", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncForInt32SignalFromResource() {
        UEMLibraryFunction libraryFunction = makeSetFunction("int32_signal", null, "void");
        libraryFunction.getArgument().add(makeArgument("const char*", "signalName", YesNoType.YES));
        libraryFunction.getArgument().add(makeArgument("int64_t", "buffer", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncParentObjectFromResource() {
        UEMLibraryFunction libraryFunction = makeGetFunction("parent_object", null, "int64_t");
        libraryFunction.getArgument().add(makeArgument("int64_t", "objectHandle", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncRemoveObjectFromResource() {
        UEMLibraryFunction libraryFunction = makeSetFunction("remove_object", null, "void");
        libraryFunction.getArgument().add(makeArgument("int64_t", "objectHandle", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncRemoveObjectsFromResource() {
        UEMLibraryFunction libraryFunction = makeSetFunction("remove_objects", null, "void");
        libraryFunction.getArgument()
                .add(makeArgument("std::vector<int64_t>", "objectHandles", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncObjectsInTreeFromResource() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("objects_in_tree", null, "std::vector<int64_t>");
        libraryFunction.getArgument().add(makeArgument("int64_t", "baseObject", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncVisionSensorImgFromResource() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("vision_sensor_img", null, "std::vector<uint8_t>");
        libraryFunction.getArgument().add(makeArgument("int64_t", "sensorHandle", YesNoType.NO));
        return libraryFunction;
    }

}
