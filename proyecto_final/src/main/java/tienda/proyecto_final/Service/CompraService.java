package tienda.proyecto_final.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tienda.proyecto_final.Controller.ComprasController;
import tienda.proyecto_final.Model.CompraDetalle;
import tienda.proyecto_final.Model.Compras;
import tienda.proyecto_final.Model.Productos;
import tienda.proyecto_final.Model.Usuarios;
import tienda.proyecto_final.Repository.CompraDetallesRepository;
import tienda.proyecto_final.Repository.ComprasRepository;
import tienda.proyecto_final.Repository.ProductosRepository;
import tienda.proyecto_final.Repository.UsuariosRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class CompraService {

    @Autowired
    private ComprasRepository comprasRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private CompraDetallesRepository compraDetallesRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private EmailService emailService;


    public Compras realizarCompra(int idCliente, BigDecimal total, List<ComprasController.ProductoCompraRequest> productos) {
        Usuarios cliente = usuariosRepository.findById(idCliente).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Validar que el total no sea nulo ni negativo
        if (total == null || total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El total de la compra debe ser un valor positivo");
        }

        Compras compra = new Compras();
        compra.setCliente(cliente);
        compra.setFecha(Date.valueOf(LocalDate.now()));

        // Establecer el total proporcionado desde el frontend
        compra.setTotal(total);

        // Guardar la compra primero
        compra = comprasRepository.save(compra);

        BigDecimal totalCompra = BigDecimal.ZERO;

        for (ComprasController.ProductoCompraRequest productoRequest : productos) {
            Productos producto = productosRepository.findById(productoRequest.getIdProducto()).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getCantidad_disponible() < productoRequest.getCantidad()) {
                throw new RuntimeException("Cantidad insuficiente en stock para el producto " + producto.getNombre());
            }

            // Calcular el precio total del producto multiplicando el precio unitario por la cantidad
            BigDecimal precioTotalProducto = producto.getPrecio().multiply(BigDecimal.valueOf(productoRequest.getCantidad()));

            // Descontar el inventario
            boolean descontado = productoService.descontarInventario(productoRequest.getIdProducto(), productoRequest.getCantidad());
            if (!descontado) {
                throw new RuntimeException("No se pudo descontar el inventario del producto " + producto.getNombre());
            }

            CompraDetalle detalle = new CompraDetalle();
            detalle.setId_producto(producto);
            detalle.setCantidad(productoRequest.getCantidad());
            detalle.setPrecio(precioTotalProducto); // Se establece el precio total del producto
            detalle.setId_compra(compra);
            compraDetallesRepository.save(detalle);

            totalCompra = totalCompra.add(precioTotalProducto); // Se agrega al total de la compra
        }

        emailService.enviarCorreoCompra(cliente, compra);

        return compra;
    }

    public List<Compras> listarTodasLasCompras() {
        return comprasRepository.findAll();
    }
}