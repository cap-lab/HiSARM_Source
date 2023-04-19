package com.dbmanager.commonlibraries;

import static com.mongodb.client.model.Filters.eq;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bson.Document;
import org.bson.conversions.Bson;
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
	private MongoDatabase mDB;
	private MongoClient mongoClient;
	private Path PRIVATE_KEY = Paths.get(System.getProperty("user.home"), ".ssh", "id_rsa");

	private MongoCollection<Document> strategyCollection;
	private MongoCollection<Document> groupActionCollection;
	private MongoCollection<Document> actionCollection;
	private MongoCollection<Document> actionImplCollection;
	private MongoCollection<Document> robotCollection;
	private MongoCollection<Document> robotImplCollection;
	private MongoCollection<Document> resourceCollection;
	private MongoCollection<Document> taskCollection;
	private MongoCollection<Document> architectureCollection;
	private MongoCollection<Document> variableCollection;
	private MongoCollection<Document> variableImplCollection;
	private MongoCollection<Document> groupingCollection;
	private MongoCollection<Document> leaderCollection;

	private DAO() {}

	public void initializeDB(String ip, int port, String user, String epwd, String pwd,
			String dbName) {
		connectDB(ip, port, user, epwd, pwd, dbName);

		strategyCollection = mDB.getCollection(DBCollections.Collection_Control_Strategy);
		groupActionCollection = mDB.getCollection(DBCollections.Collection_GroupAction);
		actionCollection = mDB.getCollection(DBCollections.Collection_Action);
		actionImplCollection = mDB.getCollection(DBCollections.Collection_ActionImpl);
		robotCollection = mDB.getCollection(DBCollections.Collection_Robot);
		robotImplCollection = mDB.getCollection(DBCollections.Collection_RobotImpl);
		resourceCollection = mDB.getCollection(DBCollections.Collection_Resource);
		taskCollection = mDB.getCollection(DBCollections.Collection_Task);
		architectureCollection = mDB.getCollection(DBCollections.Collection_Architecture);
		variableCollection = mDB.getCollection(DBCollections.Collection_Variable);
		variableImplCollection = mDB.getCollection(DBCollections.Collection_VariableImpl);
		groupingCollection = mDB.getCollection(DBCollections.Collection_Grouping_Algorithm);
		leaderCollection = mDB.getCollection(DBCollections.Collection_Leader_Selection_Algorithm);
	}

	private static class DBHolder {
		public static final DAO instance = new DAO();
	}

	public static DAO getInstance() {
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
			MongoCredential credential = MongoCredential.createCredential(user, dbName,
					getPassword(epwd, pwd).replaceAll("\n", "").toCharArray());
			mongoClient = (MongoClient) MongoClients.create(MongoClientSettings.builder()
					.applyToClusterSettings(
							builder -> builder.hosts(Arrays.asList(new ServerAddress(ip, port))))
					.credential(credential).build());
			mDB = mongoClient.getDatabase(dbName);
			System.out.println("DB Connection OK!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DB Connetion Error!");
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

	public Document getResource(String robotClass, String resourceId) {
		try {
			Bson queryFilter =
					Filters.and(eq("RobotClass", robotClass), eq("ResourceId", resourceId));
			return resourceCollection.find(queryFilter).first();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Document getGroupingAlgorithm(String groupingId) {
		try {
			Bson queryFilter = Filters.eq("GroupingId", groupingId);
			return groupingCollection.find(queryFilter).first();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Document getLeaderSelectionAlgorithm(String leaderSelectionId) {
		try {
			Bson queryFilter = Filters.eq("LeaderSelectionId", leaderSelectionId);
			return leaderCollection.find(queryFilter).first();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void close() {
		mongoClient.close();
	}
}
