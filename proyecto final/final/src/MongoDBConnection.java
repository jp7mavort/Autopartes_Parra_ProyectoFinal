import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static final String CONNECTION_STRING = "mongodb+srv://jp7mavort:12345@final.012d6.mongodb.net/";

    public static MongoDatabase getDatabase(String databaseName) {
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        return mongoClient.getDatabase(databaseName);
    }
}
