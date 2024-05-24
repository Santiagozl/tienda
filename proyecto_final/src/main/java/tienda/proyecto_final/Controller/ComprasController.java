package tienda.proyecto_final.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tienda.proyecto_final.Model.Compras;
import tienda.proyecto_final.Model.Usuarios;
import tienda.proyecto_final.Service.CompraService;
import tienda.proyecto_final.Service.UsuariosService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/compras")
public class ComprasController {
    @Autowired
    private CompraService comprasService;
    @Autowired
    private UsuariosService usuariosService;

    @PostMapping()
    public ResponseEntity<Map<String, Object>> realizarCompra(@RequestBody CompraRequest compraRequest) {
        try {
            // Llamar al método de servicio para realizar la compra
            Compras compras = comprasService.realizarCompra(compraRequest.getIdCliente(), compraRequest.getTotal(), compraRequest.getProductos());

            // Crear la respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("status", true);
            response.put("message", "Compra realizada con éxito");
            response.put("data", compras);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Manejar excepciones
            Map<String, Object> response = new HashMap<>();
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }
    public static class CompraRequest {
        private int idCliente;
        private BigDecimal total; // Cambiar de double a BigDecimal
        private List<ProductoCompraRequest> productos;

        // Getters y Setters
        public int getIdCliente() {
            return idCliente;
        }

        public void setIdCliente(int idCliente) {
            this.idCliente = idCliente;
        }

        public BigDecimal getTotal() { // Cambiar el tipo de retorno de double a BigDecimal
            return total;
        }

        public void setTotal(BigDecimal total) { // Cambiar el tipo del parámetro de double a BigDecimal
            this.total = total;
        }

        public List<ProductoCompraRequest> getProductos() {
            return productos;
        }

        public void setProductos(List<ProductoCompraRequest> productos) {
            this.productos = productos;
        }
    }

    public static class ProductoCompraRequest {
        private int idProducto;
        private int cantidad;

        // Getters y Setters
        public int getIdProducto() {
            return idProducto;
        }

        public void setIdProducto(int idProducto) {
            this.idProducto = idProducto;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }
    }

    @GetMapping()
    public ResponseEntity<Map<String, Object>> listarTodasLasCompras() {
        List<Compras> compras = comprasService.listarTodasLasCompras();
        Map<String, Object> response = new HashMap<>();

        if (compras.isEmpty()) {
            response.put("status", false);
            response.put("message", "No se encontraron compras");
            response.put("data", compras);
            return ResponseEntity.ok(response);
        }

        List<Map<String, Object>> comprasConUsuario = compras.stream().map(compra -> {
            Map<String, Object> compraConUsuario = new HashMap<>();
            Usuarios usuario = compra.getCliente();
            compraConUsuario.put("id_compra", compra.getId_compra());
            compraConUsuario.put("id_cliente", usuario.getId_cliente());
            compraConUsuario.put("nombre_cliente", usuario.getNombre());
            compraConUsuario.put("fecha", compra.getFecha());
            compraConUsuario.put("total", compra.getTotal());
            compraConUsuario.put("detalles", compra.getDetalles().stream().map(detalle -> {
                Map<String, Object> detalleMap = new HashMap<>();
                detalleMap.put("id_producto", detalle.getId_producto().getId_producto());
                detalleMap.put("nombre_producto", detalle.getId_producto().getNombre());
                detalleMap.put("cantidad", detalle.getCantidad());
                detalleMap.put("precio", detalle.getPrecio());
                return detalleMap;
            }).collect(Collectors.toList()));
            return compraConUsuario;
        }).collect(Collectors.toList());

        response.put("status", true);
        response.put("message", "Compras listadas con éxito");
        response.put("data", comprasConUsuario);
        return ResponseEntity.ok(response);
    }
}
