package tienda.proyecto_final.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tienda.proyecto_final.Model.CompraDetalle;
import tienda.proyecto_final.Model.Compras;
import tienda.proyecto_final.Model.Productos;
import tienda.proyecto_final.Model.Usuarios;

import javax.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void enviarCorreo(String destino, String asunto, String mensaje) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setTo(destino);
            helper.setSubject(asunto);
            helper.setText(mensaje);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    public void enviarCorreoNuevoCliente(Usuarios cliente) {
        String destino = cliente.getEmail();
        String asunto = "Bienvenido " + cliente.getNombre();
        String mensaje = "¡Bienvenido a nuestra tienda, " + cliente.getNombre() + "!\n\n"
                + "Tu usuario: " + cliente.getUsuario() + "\n"
                + "Tu contraseña: " + cliente.getContraseña() + "\n\n"
                + "Por favor, guarda esta información para poder acceder a nuestra tienda.";
        enviarCorreo(destino, asunto, mensaje);
    }

    public void enviarCorreoCompra(Usuarios cliente, Compras compra) {
        String destino = cliente.getEmail();
        String asunto = "Detalles de tu compra";

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Gracias por tu compra\n");
        mensaje.append("Hola ").append(cliente.getNombre()).append(",\n\n");
        mensaje.append("Aquí están los detalles de tu compra:\n\n");

        List<CompraDetalle> detalles = compra.getDetalles(); // Obtener los detalles de la compra

        // Verificar si hay detalles antes de recorrerlos
        if (detalles == null || detalles.isEmpty()) {
            mensaje.append("No hay detalles disponibles para esta compra.\n");
        } else {
            for (CompraDetalle detalle : detalles) {
                Productos producto = detalle.getId_producto();
                mensaje.append("Producto: ").append(producto.getNombre())
                        .append("\nCantidad: ").append(detalle.getCantidad())
                        .append("\nPrecio unitario: $").append(detalle.getPrecio())
                        .append("\nSubtotal: $").append(detalle.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad())))
                        .append("\n\n");
            }
        }

        // Agregar el total de la compra al mensaje
        mensaje.append("Total de la compra: $").append(compra.getTotal()).append("\n");
        mensaje.append("Fecha: ").append(compra.getFecha()).append("\n\n");
        mensaje.append("Gracias por comprar con nosotros.");

        enviarCorreo(destino, asunto, mensaje.toString());
    }
}
