import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.FindIterable;

public class AdminFrame extends JFrame {
    private JTextField txtProducto;
    private JTextField txtDescripcion;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JTextField txtImagen;
    private JTextField txtCajero;
    private JTextField txtBuscarProducto;
    private JButton btnAgregarProducto;
    private JButton btnAgregarCajero;
    private JButton btnVerVentas;
    private JButton btnVerVentasPorCajero;
    private JButton btnBuscarProducto;
    private JButton btnVerContabilidad;
    private JButton btnRegresar;
    private ProductoDAO productoDAO;
    private UserDAO userDAO;
    private TransaccionDAO transaccionDAO;
    private MongoDatabase database;

    public AdminFrame() {
        setTitle("Admin");
        setSize(600, 700);
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
        JLabel lblTitulo = new JLabel("Panel de Administración");
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
        JLabel lblDescripcion = new JLabel("Descripción:");
        add(lblDescripcion, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        txtDescripcion = new JTextField(20);
        add(txtDescripcion, gbc);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblPrecio = new JLabel("Precio:");
        add(lblPrecio, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        txtPrecio = new JTextField(20);
        add(txtPrecio, gbc);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lblStock = new JLabel("Stock:");
        add(lblStock, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        txtStock = new JTextField(20);
        add(txtStock, gbc);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel lblImagen = new JLabel("Imagen URL:");
        add(lblImagen, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        txtImagen = new JTextField(20);
        add(txtImagen, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        btnAgregarProducto = new JButton("Agregar Producto");
        add(btnAgregarProducto, gbc);

        btnAgregarProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = txtProducto.getText();
                String descripcion = txtDescripcion.getText();
                double precio = Double.parseDouble(txtPrecio.getText());
                int stock = Integer.parseInt(txtStock.getText());
                String imagen = txtImagen.getText();

                productoDAO.insertProducto(nombre, descripcion, precio, stock, imagen);
                JOptionPane.showMessageDialog(null, "Producto agregado con éxito!");
            }
        });

        gbc.gridy = 7;
        btnAgregarCajero = new JButton("Agregar Cajero");
        add(btnAgregarCajero, gbc);

        btnAgregarCajero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog("Ingrese el nombre del cajero:");
                String password = JOptionPane.showInputDialog("Ingrese la contraseña del cajero:");

                userDAO.insertUser(username, password, "cajero");
                JOptionPane.showMessageDialog(null, "Cajero agregado con éxito!");
            }
        });

        gbc.gridy = 8;
        btnVerVentas = new JButton("Ver Ventas");
        add(btnVerVentas, gbc);

        btnVerVentas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ventas = obtenerVentas();
                JTextArea textArea = new JTextArea(ventas);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(380, 300));
                JOptionPane.showMessageDialog(null, scrollPane, "Ventas Realizadas", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel lblCajero = new JLabel("Cajero:");
        add(lblCajero, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        txtCajero = new JTextField(20);
        add(txtCajero, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.CENTER;
        btnVerVentasPorCajero = new JButton("Ver Ventas por Cajero");
        add(btnVerVentasPorCajero, gbc);

        btnVerVentasPorCajero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cajero = txtCajero.getText();
                String ventas = obtenerVentasPorCajero(cajero);
                JTextArea textArea = new JTextArea(ventas);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(380, 300));
                JOptionPane.showMessageDialog(null, scrollPane, "Ventas Realizadas por " + cajero, JOptionPane.INFORMATION_MESSAGE);
            }
        });

        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel lblBuscarProducto = new JLabel("Buscar Producto:");
        add(lblBuscarProducto, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        txtBuscarProducto = new JTextField(20);
        add(txtBuscarProducto, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 12;
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

        gbc.gridy = 13;
        btnVerContabilidad = new JButton("Ver Contabilidad");
        add(btnVerContabilidad, gbc);

        btnVerContabilidad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ContabilidadFrame(database).setVisible(true);
            }
        });

        gbc.gridy = 14;
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
        database = mongoClient.getDatabase("AutoPartsXpress");
        productoDAO = new ProductoDAO(database);
        userDAO = new UserDAO(database);
        transaccionDAO = new TransaccionDAO(database);
    }

    private String obtenerVentas() {
        StringBuilder ventas = new StringBuilder();
        FindIterable<Document> transacciones = transaccionDAO.getTransacciones();
        for (Document transaccion : transacciones) {
            ventas.append("Producto ID: ").append(transaccion.getString("producto_id")).append("\n");
            ventas.append("Cantidad: ").append(transaccion.getInteger("cantidad")).append("\n");
            ventas.append("Total: $").append(transaccion.getDouble("total")).append("\n");
            ventas.append("Cajero: ").append(transaccion.getString("cajero")).append("\n");
            ventas.append("Fecha: ").append(transaccion.getDate("fecha")).append("\n\n");
        }
        return ventas.toString();
    }

    private String obtenerVentasPorCajero(String cajero) {
        StringBuilder ventas = new StringBuilder();
        FindIterable<Document> transacciones = transaccionDAO.getTransaccionesPorCajero(cajero);
        for (Document transaccion : transacciones) {
            ventas.append("Producto ID: ").append(transaccion.getString("producto_id")).append("\n");
            ventas.append("Cantidad: ").append(transaccion.getInteger("cantidad")).append("\n");
            ventas.append("Total: $").append(transaccion.getDouble("total")).append("\n");
            ventas.append("Cajero: ").append(transaccion.getString("cajero")).append("\n");
            ventas.append("Fecha: ").append(transaccion.getDate("fecha")).append("\n\n");
        }
        return ventas.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminFrame().setVisible(true);
            }
        });
    }
}
