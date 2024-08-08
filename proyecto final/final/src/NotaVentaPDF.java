import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class NotaVentaPDF {
    private static final AtomicInteger counter = new AtomicInteger(1);

    public void generarNotaVenta(String nombreProducto, int cantidad, double total, String cajero, String imagenUrl) throws DocumentException, FileNotFoundException {
        Document document = new Document();

        // Generar un nombre de archivo único
        String filename = "NotaVenta" + counter.getAndIncrement() + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();

        // Título del documento
        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph titulo = new Paragraph("Nota de Venta - AutoPartsXpress", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        // Espacio entre el título y el contenido
        document.add(new Paragraph(" "));

        // Tabla para la información de la venta
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Bordes de las celdas
        PdfPCell cell;

        cell = new PdfPCell(new Phrase("Producto"));
        cell.setBorderWidth(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(nombreProducto));
        cell.setBorderWidth(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Cantidad"));
        cell.setBorderWidth(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(String.valueOf(cantidad)));
        cell.setBorderWidth(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Total"));
        cell.setBorderWidth(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("$" + total));
        cell.setBorderWidth(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Cajero"));
        cell.setBorderWidth(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(cajero));
        cell.setBorderWidth(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Fecha"));
        cell.setBorderWidth(2);
        table.addCell(cell);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        cell = new PdfPCell(new Phrase(dateFormat.format(new Date())));
        cell.setBorderWidth(2);
        table.addCell(cell);

        document.add(table);

        // Espacio entre la tabla y la imagen
        document.add(new Paragraph(" "));

        // Imagen del producto
        try {
            Image imagen = Image.getInstance(new URL(imagenUrl));
            imagen.scaleToFit(150, 150);
            imagen.setAlignment(Element.ALIGN_CENTER);
            document.add(imagen);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();
    }
}
