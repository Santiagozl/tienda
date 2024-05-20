package tienda.proyecto_final.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tienda.proyecto_final.Model.Productos;
import tienda.proyecto_final.Model.Usuarios;

import javax.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

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
        String mensaje = "¡Bienvenido a nuestra tienda!" + cliente.getNombre() + "\n\n"
                + "Tu usuario: " + cliente.getUsuario() + "\n"
                + "Tu contraseña: " + cliente.getContraseña() + "\n\n"
                + "Por favor, guarda esta información para poder acceder a nuestra tienda.";
        enviarCorreo(destino, asunto, mensaje);
    }

    public void enviarCorreoCompra(Usuarios cliente, Productos producto, int cantidad, BigDecimal total) {
        String destino = cliente.getEmail();
        String asunto = "Detalles de tu compra";
        String mensaje = "Gracias por tu compra \n" +
                "Hola " + cliente.getNombre() + " " +
                "Has comprado: " + cantidad + " x " + producto.getNombre() + " " +
                "Total: $ " + total + " " +
                "Fecha: " + Date.valueOf(LocalDate.now()) + " \n " +
                "Gracias por comprar con nosotros.";
        enviarCorreo(destino, asunto, mensaje);
    }
}
