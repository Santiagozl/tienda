package tienda.proyecto_final.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tienda.proyecto_final.Model.Compras;
import tienda.proyecto_final.Model.Productos;
import tienda.proyecto_final.Model.Usuarios;
import tienda.proyecto_final.Repository.ComprasRepository;
import tienda.proyecto_final.Repository.ProductosRepository;
import tienda.proyecto_final.Repository.UsuariosRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;


@Service
public class CompraService {
    @Autowired
    private ComprasRepository comprasRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private EmailService emailService;

    public Compras realizarCompra(int idCliente, int idProducto, BigDecimal total, int cantidad) {
        Usuarios cliente = usuariosRepository.findById(idCliente).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Productos producto = productosRepository.findById(idProducto).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getCantidad_disponible() < cantidad) {
            throw new RuntimeException("Cantidad insuficiente en stock");
        }

        // Descontar el inventario
        boolean descontado = productoService.descontarInventario(idProducto, cantidad);
        if (!descontado) {
            throw new RuntimeException("No se pudo descontar el inventario");
        }

        Compras compra = new Compras();
        compra.setId_cliente(cliente);
        compra.setProductos_id_producto(producto);
        compra.setTotal(total);
        compra.setCantidad(cantidad);
        compra.setFecha(Date.valueOf(LocalDate.now()));

        comprasRepository.save(compra);

        // Enviar correo de confirmación
        emailService.enviarCorreoCompra(cliente, producto, cantidad, total);

        return compra;
    }
}

