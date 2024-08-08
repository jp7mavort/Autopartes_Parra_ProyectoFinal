import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.FindIterable;

import java.util.Date;

public class TransaccionDAO {
    private MongoCollection<Document> transaccionCollection;

    public TransaccionDAO(MongoDatabase database) {
        this.transaccionCollection = database.getCollection("transacciones");
    }

    public void insertTransaccion(String productoId, int cantidad, double total, String cajero, Date fecha) {
        Document transaccion = new Document("producto_id", productoId)
                .append("cantidad", cantidad)
                .append("total", total)
                .append("cajero", cajero)
                .append("fecha", fecha);
        transaccionCollection.insertOne(transaccion);
    }

    public FindIterable<Document> getTransacciones() {
        return transaccionCollection.find();
    }

    public FindIterable<Document> getTransaccionesPorCajero(String cajero) {
        return transaccionCollection.find(new Document("cajero", cajero));
    }
}
