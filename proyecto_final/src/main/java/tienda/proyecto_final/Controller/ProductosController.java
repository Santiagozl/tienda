package tienda.proyecto_final.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tienda.proyecto_final.Model.Productos;
import tienda.proyecto_final.Service.ProductoService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductosController {

    @Autowired
    private ProductoService productoService;

    @PostMapping()
    public ResponseEntity<Productos> insertarProducto(
            @RequestParam String nombre,
            @RequestParam String tipoProducto,
            @RequestParam Integer cantidadDisponible,
            @RequestParam BigDecimal precio,
            @RequestParam MultipartFile imagen) {
        try {
            Productos producto = productoService.insertarProducto(nombre, tipoProducto, cantidadDisponible, precio, imagen);
            return ResponseEntity.ok(producto);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping()
    public ResponseEntity<ResponseData<List<Productos>>> listarProductos() {
        List<Productos> productos = productoService.listarProductos();
        if (productos != null && !productos.isEmpty()) {
            return ResponseEntity.ok(new ResponseData<>(true, "Productos encontrados", productos));
        } else {
            return ResponseEntity.ok(new ResponseData<>(false, "No se encontraron productos", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Productos> actualizarProducto(@PathVariable Integer id, @RequestBody Productos producto) {
        try {
            Productos productoActualizado = productoService.actualizarProducto(id, producto);
            if (productoActualizado != null) {
                return ResponseEntity.ok(productoActualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    public class ResponseData<T> {

        private boolean status;
        private String message;
        private T data;

        public ResponseData(boolean status, String message, T data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        // Constructor adicional
        public ResponseData(boolean status, String message) {
            this.status = status;
            this.message = message;
            this.data = null;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }

}
