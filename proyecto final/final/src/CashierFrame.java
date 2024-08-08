import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Date;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.IOException;

public class CashierFrame extends JFrame {
    private JTextField txtProducto;
    private JTextField txtCantidad;
    private JTextField txtBuscarProducto;
    private JButton btnComprar;
    private JButton btnBuscarProducto;
    private JButton btnConfirmarCompra;
    private JButton btnRegresar;
    private JLabel lblImagen;
    private ProductoDAO productoDAO;
    private TransaccionDAO transaccionDAO;
    private String cajero;

    public CashierFrame(String cajero) {
        this.cajero = cajero;
        setTitle("Cajero");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        // Fondo azul claro
        getContentPane().setBackground(new Color(173, 216, 230));

        // Configuración del layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // Título principal
        JLabel lblTitulo = new JLabel("Panel de Cajero");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridy = 1;
        JLabel lblProducto = new JLabel("Producto:");
        add(lblProducto, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        txtProducto = new JTextField(20);
        add(txtProducto, gbc);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblCantidad = new JLabel("Cantidad:");
        add(lblCantidad, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        txtCantidad = new JTextField(20);
        add(txtCantidad, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        lblImagen = new JLabel();
        add(lblImagen, gbc);

        gbc.gridy = 4;
        btnComprar = new JButton("Comprar");
        add(btnComprar, gbc);

        btnComprar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreProducto = txtProducto.getText();
                Document producto = productoDAO.findProductoByNombre(nombreProducto);
                if (producto != null) {
                    String imagenUrl = producto.getString("imagen");
                    try {
                        URL url = new URL(imagenUrl);
                        Image image = ImageIO.read(url);
                        lblImagen.setIcon(new ImageIcon(image));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    btnConfirmarCompra.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Producto no encontrado.");
                }
            }
        });

        gbc.gridy = 5;
        btnConfirmarCompra = new JButton("Confirmar Compra");
        btnConfirmarCompra.setEnabled(false);
        add(btnConfirmarCompra, gbc);

        btnConfirmarCompra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreProducto = txtProducto.getText();
                int cantidad = Integer.parseInt(txtCantidad.getText());
                Document producto = productoDAO.findProductoByNombre(nombreProducto);
                if (producto != null && producto.getInteger("stock") >= cantidad) {
                    double precio = producto.getDouble("precio");
                    double total = precio * cantidad;
                    String imagenUrl = producto.getString("imagen");

                    productoDAO.updateStock(nombreProducto, cantidad);
                    transaccionDAO.insertTransaccion(producto.getObjectId("_id").toString(), cantidad, total, cajero, new Date());
                    try {
                        new NotaVentaPDF().generarNotaVenta(nombreProducto, cantidad, total, cajero, imagenUrl);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    JOptionPane.showMessageDialog(null, "Compra realizada con éxito!");
                } else {
                    JOptionPane.showMessageDialog(null, "Stock insuficiente o producto no encontrado.");
                }
                btnConfirmarCompra.setEnabled(false);
            }
        });

        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel lblBuscarProducto = new JLabel("Buscar Producto:");
        add(lblBuscarProducto, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        txtBuscarProducto = new JTextField(20);
        add(txtBuscarProducto, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.CENTER;
        btnBuscarProducto = new JButton("Buscar");
        add(btnBuscarProducto, gbc);

        btnBuscarProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreProducto = txtBuscarProducto.getText();
                Document producto = productoDAO.findProductoByNombre(nombreProducto);
                if (producto != null) {
                    String infoProducto = "Nombre: " + producto.getString("nombre") + "\n"
                            + "Descripción: " + producto.getString("descripcion") + "\n"
                            + "Precio: $" + producto.getDouble("precio") + "\n"
                            + "Stock: " + producto.getInteger("stock") + "\n"
                            + "Imagen URL: " + producto.getString("imagen");
                    JOptionPane.showMessageDialog(null, infoProducto, "Información del Producto", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gbc.gridy = 8;
        btnRegresar = new JButton("Regresar");
        add(btnRegresar, gbc);

        btnRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });

        // Conectar a la base de datos
        String uri = "mongodb+srv://jp7mavort:12345@final.012d6.mongodb.net/AutoPartsXpress?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("AutoPartsXpress");
        productoDAO = new ProductoDAO(database);
        transaccionDAO = new TransaccionDAO(database);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CashierFrame("cajero").setVisible(true);
            }
        });
    }
}
