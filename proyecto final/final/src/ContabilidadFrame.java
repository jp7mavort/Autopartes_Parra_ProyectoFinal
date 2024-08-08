import javax.swing.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.FindIterable;

public class ContabilidadFrame extends JFrame {
    private MongoDatabase database;
    private JTextArea textArea;

    public ContabilidadFrame(MongoDatabase database) {
        this.database = database;
        setTitle("Contabilidad");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(10, 10, 560, 340);
        add(scrollPane);

        calcularActivos();
    }

    private void calcularActivos() {
        MongoCollection<Document> productos = database.getCollection("productos");
        MongoCollection<Document> transacciones = database.getCollection("transacciones");

        double totalActivos = 0.0;
        double totalVentas = 0.0;

        FindIterable<Document> productosDocs = productos.find();
        for (Document producto : productosDocs) {
            double precio = producto.getDouble("precio");
            int stock = producto.getInteger("stock");
            totalActivos += precio * stock;
        }

        FindIterable<Document> transaccionesDocs = transacciones.find();
        for (Document transaccion : transaccionesDocs) {
            totalVentas += transaccion.getDouble("total");
        }

        double activosNetos = totalActivos - totalVentas;

        textArea.append("Total de Activos: $" + totalActivos + "\n");
        textArea.append("Total de Ventas: $" + totalVentas + "\n");
        textArea.append("Activos Netos: $" + activosNetos + "\n");
    }
}
