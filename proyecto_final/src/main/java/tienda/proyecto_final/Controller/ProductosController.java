package tienda.proyecto_final.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tienda.proyecto_final.Model.Productos;
import tienda.proyecto_final.Service.ProductoService;

import java.io.IOException;
import java.math.BigDecimal;

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
    @GetMapping("/{id}")
    public ResponseEntity<Productos> obtenerProducto(@PathVariable Integer id) {
        Productos producto = productoService.obtenerProducto(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Productos> actualizarProducto(
            @PathVariable Integer id,
            @RequestParam String nombre,
            @RequestParam String tipoProducto,
            @RequestParam Integer cantidadDisponible,
            @RequestParam BigDecimal precio,
            @RequestParam(required = false) MultipartFile imagen) {
        try {
            Productos producto = productoService.actualizarProducto(id, nombre, tipoProducto, cantidadDisponible, precio, imagen);
            if (producto != null) {
                return ResponseEntity.ok(producto);
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
}
