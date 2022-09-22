package com.dbmanager.commonlibraries;

import static com.mongodb.client.model.Filters.eq;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

public class DAO {
	private final Path dbConfigFile = Paths.get("configuration", "database.conf");

	private MongoDatabase mDB;
	private MongoClient mongoClient;

	private MongoCollection<Document> strategyCollection;
	private MongoCollection<Document> groupActionCollection;
	private MongoCollection<Document> actionCollection;
	private MongoCollection<Document> actionImplCollection;
	private MongoCollection<Document> robotCollection;
	private MongoCollection<Document> robotImplCollection;
	private MongoCollection<Document> taskCollection;
	private MongoCollection<Document> architectureCollection;
	private MongoCollection<Document> variableCollection;
	private MongoCollection<Document> variableImplCollection;

	private DAO() {
		connectDB();

		strategyCollection = mDB.getCollection(DBCollections.Collection_Strategy);
		groupActionCollection = mDB.getCollection(DBCollections.Collection_GroupAction);
		actionCollection = mDB.getCollection(DBCollections.Collection_Action);
		actionImplCollection = mDB.getCollection(DBCollections.Collection_ActionImpl);
		robotCollection = mDB.getCollection(DBCollections.Collection_Robot);
		robotImplCollection = mDB.getCollection(DBCollections.Collection_RobotImpl);
		taskCollection = mDB.getCollection(DBCollections.Collection_Task);
		architectureCollection = mDB.getCollection(DBCollections.Collection_Architecture);
		variableCollection = mDB.getCollection(DBCollections.Collection_Variable);
		variableImplCollection = mDB.getCollection(DBCollections.Collection_VariableImpl);
	}

	private static class DBHolder {
		public static final DAO instance = new DAO();
	}

	public static DAO getInstance() {
		return DBHolder.instance;
	}

	private void connectDB() {
		try {
			DBConf dbConf = getDatabaseConfiguration();
			MongoCredential credential = MongoCredential.createCredential(dbConf.getUserName(),
					dbConf.getDbName(), dbConf.getPassword().toCharArray());
			mongoClient = (MongoClient) MongoClients.create(MongoClientSettings.builder()
					.applyToClusterSettings(builder -> builder.hosts(
							Arrays.asList(new ServerAddress(dbConf.getIp(), dbConf.getPort()))))
					.credential(credential).build());
			mDB = mongoClient.getDatabase(dbConf.getDbName());
			System.out.println("DB Connection OK!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DB Connetion Error!");
		}
	}

	private DBConf getDatabaseConfiguration() {
		try {
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			return mapper.readValue(dbConfigFile.toFile(), DBConf.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Document getStrategy(String strategyId) {
		try {
			Bson queryFilter = Filters.eq("StrategyId", strategyId);
			return strategyCollection.find(queryFilter).first();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Document getRobot(String robotClass) {
		try {
			Bson queryFilter = Filters.eq("RobotClass", robotClass);
			return robotCollection.find(queryFilter).first();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Document getArchitecture(String deviceName) {
		try {
			Bson queryFilter = Filters.eq("DeviceName", deviceName);
			return architectureCollection.find(queryFilter).first();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Document getTask(String taskId) {
		try {
			Bson queryFilter = Filters.eq("TaskId", taskId);
			Bson projection = Projections.exclude("TaskType");
			return taskCollection.find(queryFilter).projection(projection).first();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Document getTaskType(String taskId) {
		try {
			Bson queryFilter = Filters.eq("TaskId", taskId);
			Bson projection = Projections.include("TaskType");
			return taskCollection.find(queryFilter).projection(projection).first();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Document getGroupAction(String GroupActionName) {
		try {
			Bson queryFilter = Filters.eq("Name", GroupActionName);
			return groupActionCollection.find(queryFilter).first();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Document getAction(String actionName) {
		try {
			Bson queryFilter = Filters.eq("Name", actionName);
			return actionCollection.find(queryFilter).first();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Document getActionImpl(String robotClass, String actionName) {
		try {
			Bson queryFilter =
					Filters.and(eq("RobotClass", robotClass), eq("ActionName", actionName));
			return actionImplCollection.find(queryFilter).first();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Document getActionImpl(String actionImplId) {
		try {
			Bson queryFilter = Filters.eq("ActionImplId", actionImplId);
			return actionImplCollection.find(queryFilter).first();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Document getVariable(String variableName) {
		try {
			Bson queryFilter = Filters.eq("Name", variableName);
			return variableCollection.find(queryFilter).first();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Document getVariableImpl(String robotClass, String variableName) {
		try {
			Bson queryFilter =
					Filters.and(eq("RobotClass", robotClass), eq("VariableName", variableName));
			return variableImplCollection.find(queryFilter).first();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Document getRobotImpl(String robotId) {
		try {
			Bson queryFilter = Filters.and(eq("RobotId", robotId));
			return robotImplCollection.find(queryFilter).first();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void close() {
		mongoClient.close();
	}
}
