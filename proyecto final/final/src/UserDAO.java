import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document; // Asegúrate de importar org.bson.Document
import com.mongodb.client.model.Filters;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;

public class UserDAO {
    private MongoCollection<Document> userCollection;

    public UserDAO(MongoDatabase database) {
        this.userCollection = database.getCollection("usuarios");
    }

    public Document findUserByUsername(String username) {
        System.out.println("Buscando usuario con username: " + username);
        return userCollection.find(Filters.eq("username", username)).first();
    }

    public void insertUser(String username, String password, String role) {
        Document user = new Document("username", username)
                .append("password", password)
                .append("role", role);
        userCollection.insertOne(user);
        System.out.println("Usuario insertado: " + username);
    }

    public boolean authenticate(String username, String password) {
        Document user = findUserByUsername(username);
        if (user != null) {
            String storedPassword = user.getString("password");
            System.out.println("Usuario encontrado: " + username);
            System.out.println("Contraseña almacenada: " + storedPassword);
            System.out.println("Contraseña proporcionada: " + password);
            return storedPassword.equals(password);
        }
        System.out.println("Usuario no encontrado: " + username);
        return false;
    }

    public static void main(String[] args) {
        String uri = "mongodb+srv://jp7mavort:12345@final.012d6.mongodb.net/AutoPartsXpress?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("AutoPartsXpress");

        UserDAO userDAO = new UserDAO(database);

        // Insertar usuarios de prueba
        userDAO.insertUser("joel", "parra", "admin");
        userDAO.insertUser("parra", "joel", "cajero");

        // Probar la autenticación
        boolean isAuthenticated = userDAO.authenticate("joel", "parra");
        System.out.println("Autenticación exitosa: " + isAuthenticated);
    }
}
