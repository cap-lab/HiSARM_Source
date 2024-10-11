package com.dbmanager.commonlibraries;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bson.Document;
import org.lightcouch.CouchDbClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CouchDBDAO implements HiSARMDAO {
    private Path PRIVATE_KEY = Paths.get(System.getProperty("user.home"), ".ssh", "id_rsa");

    private CouchDbClient strategyDB;
    private CouchDbClient actionDB;
    private CouchDbClient actionImplDB;
    private CouchDbClient robotDB;
    private CouchDbClient robotImplDB;
    private CouchDbClient resourceDB;
    private CouchDbClient taskDB;
    private CouchDbClient architectureDB;
    private CouchDbClient variableDB;
    private CouchDbClient variableImplDB;
    private CouchDbClient groupingDB;
    private CouchDbClient leaderDB;
    private CouchDbClient simulationDeviceDB;

    private CouchDBDAO() {}

    public void initializeDB(String ip, int port, String user, String epwd, String pwd,
            String dbName) {
        Logger lightCouchLogger = Logger.getLogger("org.lightcouch");
        lightCouchLogger.setLevel(Level.SEVERE);
        connectDB(ip, port, user, epwd, pwd, dbName);
    }

    private static class DBHolder {
        public static final CouchDBDAO instance = new CouchDBDAO();
    }

    public static CouchDBDAO getInstance() {
        return DBHolder.instance;
    }

    private String decryptPassword(String pwd) {
        Security.addProvider(new BouncyCastleProvider());
        String decryptedData = null;
        try {
            PEMParser privatePemParser =
                    new PEMParser(new StringReader(Files.readString(PRIVATE_KEY)));
            PrivateKey privateKey = null;

            Object privateObject = privatePemParser.readObject();
            if (privateObject instanceof PEMKeyPair) {
                PEMKeyPair pemKeyPair = (PEMKeyPair) privateObject;
                JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
                privateKey = converter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());
            }

            // Decrypt the message using the private key
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(pwd.getBytes()));

            // Print the decrypted message
            decryptedData = new String(decrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedData;
    }

    private String getPassword(String epwd, String pwd) throws Exception {
        if (pwd != null) {
            return pwd;
        } else if (epwd != null) {
            return decryptPassword(epwd);
        } else {
            throw new Exception("no Password for DB");
        }
    }

    private void connectDB(String ip, int port, String user, String epwd, String pwd,
            String dbName) {
        try {
            strategyDB = new CouchDbClient(CouchDBList.DB_Control_Strategy, true, "http", ip, port,
                    user, getPassword(epwd, pwd));
            actionDB = new CouchDbClient(CouchDBList.DB_Action, true, "http", ip, port, user,
                    getPassword(epwd, pwd));
            actionImplDB = new CouchDbClient(CouchDBList.DB_ActionImpl, true, "http", ip, port,
                    user, getPassword(epwd, pwd));
            robotDB = new CouchDbClient(CouchDBList.DB_Robot, true, "http", ip, port, user,
                    getPassword(epwd, pwd));
            robotImplDB = new CouchDbClient(CouchDBList.DB_RobotImpl, true, "http", ip, port, user,
                    getPassword(epwd, pwd));
            resourceDB = new CouchDbClient(CouchDBList.DB_Resource, true, "http", ip, port, user,
                    getPassword(epwd, pwd));
            taskDB = new CouchDbClient(CouchDBList.DB_Task, true, "http", ip, port, user,
                    getPassword(epwd, pwd));
            architectureDB = new CouchDbClient(CouchDBList.DB_Architecture, true, "http", ip, port,
                    user, getPassword(epwd, pwd));
            variableDB = new CouchDbClient(CouchDBList.DB_Variable, true, "http", ip, port, user,
                    getPassword(epwd, pwd));
            variableImplDB = new CouchDbClient(CouchDBList.DB_VariableImpl, true, "http", ip, port,
                    user, getPassword(epwd, pwd));
            groupingDB = new CouchDbClient(CouchDBList.DB_Grouping_Algorithm, true, "http", ip,
                    port, user, getPassword(epwd, pwd));
            leaderDB = new CouchDbClient(CouchDBList.DB_Leader_Selection_Algorithm, true, "http",
                    ip, port, user, getPassword(epwd, pwd));
            simulationDeviceDB = new CouchDbClient(CouchDBList.DB_Simulation_Device, true, "http",
                    ip, port, user, getPassword(epwd, pwd));

            System.out.println("DB Connection OK!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DB Connetion Error!");
        }
    }

    public Document getStrategy(String strategyId) {
        try {
            JsonObject selector = new JsonObject();
            selector.addProperty("StrategyId", strategyId);
            JsonObject query = new JsonObject();
            query.add("selector", selector);
            JsonObject result = strategyDB.findDocs(query.toString(), JsonObject.class).get(0);
            return convertToBsonDocument(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document getRobot(String robotClass) {
        try {
            JsonObject selector = new JsonObject();
            selector.addProperty("RobotClass", robotClass);
            JsonObject query = new JsonObject();
            query.add("selector", selector);
            JsonObject result = robotDB.findDocs(query.toString(), JsonObject.class).get(0);
            return convertToBsonDocument(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document getArchitecture(String deviceName) {
        try {
            JsonObject selector = new JsonObject();
            selector.addProperty("DeviceName", deviceName);
            JsonObject query = new JsonObject();
            query.add("selector", selector);
            JsonObject result = architectureDB.findDocs(query.toString(), JsonObject.class).get(0);
            return convertToBsonDocument(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document getTask(String taskId) {
        try {
            JsonObject selector = new JsonObject();
            selector.addProperty("TaskId", taskId);
            JsonObject query = new JsonObject();
            query.add("selector", selector);
            JsonObject result = taskDB.findDocs(query.toString(), JsonObject.class).get(0);
            return convertToBsonDocument(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document getTaskType(String taskId) {
        try {
            JsonObject selector = new JsonObject();
            selector.addProperty("TaskId", taskId);
            JsonObject query = new JsonObject();
            query.add("selector", selector);
            JsonObject result = taskDB.findDocs(query.toString(), JsonObject.class).get(0);
            return convertToBsonDocument(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document getAction(String actionName) {
        try {
            JsonObject selector = new JsonObject();
            selector.addProperty("Name", actionName);
            JsonObject query = new JsonObject();
            query.add("selector", selector);
            JsonObject result = actionDB.findDocs(query.toString(), JsonObject.class).get(0);
            return convertToBsonDocument(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document getActionImpl(String robotClass, String actionName) {
        try {
            JsonObject selector = new JsonObject();
            selector.addProperty("RobotClass", robotClass);
            selector.addProperty("ActionName", actionName);
            JsonObject query = new JsonObject();
            query.add("selector", selector);
            JsonObject result = actionImplDB.findDocs(query.toString(), JsonObject.class).get(0);
            return convertToBsonDocument(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document getActionImpl(String actionImplId) {
        try {
            JsonObject selector = new JsonObject();
            selector.addProperty("ActionImplId", actionImplId);
            JsonObject query = new JsonObject();
            query.add("selector", selector);
            JsonObject result = actionImplDB.findDocs(query.toString(), JsonObject.class).get(0);
            return convertToBsonDocument(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document getVariable(String variableName) {
        try {
            JsonObject selector = new JsonObject();
            selector.addProperty("Name", variableName);
            JsonObject query = new JsonObject();
            query.add("selector", selector);
            JsonObject result = variableDB.findDocs(query.toString(), JsonObject.class).get(0);
            return convertToBsonDocument(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document getVariableImpl(String robotClass, String variableName) {
        try {
            JsonObject selector = new JsonObject();
            selector.addProperty("RobotClass", robotClass);
            selector.addProperty("VariableName", variableName);
            JsonObject query = new JsonObject();
            query.add("selector", selector);
            JsonObject result = variableImplDB.findDocs(query.toString(), JsonObject.class).get(0);
            return convertToBsonDocument(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document getRobotImpl(String robotId) {
        try {
            JsonObject selector = new JsonObject();
            selector.addProperty("RobotId", robotId);
            JsonObject query = new JsonObject();
            query.add("selector", selector);
            JsonObject result = robotImplDB.findDocs(query.toString(), JsonObject.class).get(0);
            return convertToBsonDocument(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document getResource(String robotClass, String resourceId) {
        try {
            JsonObject selector = new JsonObject();
            selector.addProperty("RobotClass", robotClass);
            selector.addProperty("ResourceId", resourceId);
            JsonObject query = new JsonObject();
            query.add("selector", selector);
            JsonObject result = resourceDB.findDocs(query.toString(), JsonObject.class).get(0);
            return convertToBsonDocument(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document getGroupingAlgorithm(String groupingId) {
        try {
            JsonObject selector = new JsonObject();
            selector.addProperty("GroupingId", groupingId);
            JsonObject query = new JsonObject();
            query.add("selector", selector);
            JsonObject result = groupingDB.findDocs(query.toString(), JsonObject.class).get(0);
            return convertToBsonDocument(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document getLeaderSelectionAlgorithm(String leaderSelectionId) {
        try {
            JsonObject selector = new JsonObject();
            selector.addProperty("LeaderSelectionId", leaderSelectionId);
            JsonObject query = new JsonObject();
            query.add("selector", selector);
            JsonObject result = leaderDB.findDocs(query.toString(), JsonObject.class).get(0);
            return convertToBsonDocument(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document getSimulationDevice(String deviceId) {
        try {
            JsonObject selector = new JsonObject();
            selector.addProperty("DeviceId", deviceId);
            JsonObject query = new JsonObject();
            query.add("selector", selector);
            JsonObject result =
                    simulationDeviceDB.findDocs(query.toString(), JsonObject.class).get(0);
            return convertToBsonDocument(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Document convertToBsonDocument(JsonObject jsonObject) {
        Document document = new Document();

        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (value.isJsonObject()) {
                document.append(key, convertToBsonDocument(value.getAsJsonObject()));
            } else if (value.isJsonArray()) {
                document.append(key, convertToBsonArray(value.getAsJsonArray()));
            } else if (value.isJsonPrimitive()) {
                document.append(key, getPrimitiveValue(value));
            } else {
                document.append(key, value.toString());
            }
        }

        return document;
    }

    private static List<Object> convertToBsonArray(JsonArray jsonArray) {
        List<Object> list = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {
            if (jsonElement.isJsonObject()) {
                list.add(convertToBsonDocument(jsonElement.getAsJsonObject()));
            } else if (jsonElement.isJsonArray()) {
                list.add(convertToBsonArray(jsonElement.getAsJsonArray()));
            } else if (jsonElement.isJsonPrimitive()) {
                list.add(getPrimitiveValue(jsonElement));
            } else {
                list.add(jsonElement.toString());
            }
        }

        return list;
    }

    private static Object getPrimitiveValue(JsonElement jsonElement) {
        if (jsonElement.getAsJsonPrimitive().isBoolean()) {
            return jsonElement.getAsBoolean();
        } else if (jsonElement.getAsJsonPrimitive().isNumber()) {
            if (jsonElement.getAsNumber().toString().contains(".")) {
                return jsonElement.getAsDouble();
            } else {
                return jsonElement.getAsInt();
            }
        } else if (jsonElement.getAsJsonPrimitive().isString()) {
            return jsonElement.getAsString();
        }
        return jsonElement.toString();
    }

    public void close() {
        strategyDB.shutdown();
        actionDB.shutdown();
        actionImplDB.shutdown();
        robotDB.shutdown();
        robotImplDB.shutdown();
        resourceDB.shutdown();
        taskDB.shutdown();
        architectureDB.shutdown();
        variableDB.shutdown();
        variableImplDB.shutdown();
        groupingDB.shutdown();
        leaderDB.shutdown();
        simulationDeviceDB.shutdown();
        System.out.println("DB Connection Closed!");
    }
}
