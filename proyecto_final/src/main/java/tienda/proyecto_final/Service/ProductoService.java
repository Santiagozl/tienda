package tienda.proyecto_final.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tienda.proyecto_final.Model.Productos;
import tienda.proyecto_final.Repository.ProductosRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductoService {
    @Autowired
    private ProductosRepository productoRepository;

    private static final String UPLOAD_DIR = "tienda/proyecto_final/Util/img";

    public List<Productos> listarProductos() {
        return productoRepository.findAll();
    }

    public Productos insertarProducto(String nombre, String tipoProducto, Integer cantidadDisponible, BigDecimal precio, MultipartFile imagen) throws IOException {
        Productos producto = new Productos();
        producto.setNombre(nombre);
        producto.setTipo_producto(tipoProducto);
        producto.setCantidad_disponible(cantidadDisponible);
        producto.setPrecio(precio);

        if (!imagen.isEmpty()) {
            String fileName = imagen.getOriginalFilename();
            String relativePath = UPLOAD_DIR + fileName;
            Path path = Paths.get(relativePath);
            Files.createDirectories(path.getParent());
            Files.write(path, imagen.getBytes());
            producto.setImg(relativePath);
        }
        return productoRepository.save(producto);
    }

    public Productos obtenerProducto(Integer id) {
        return productoRepository.findById(id).orElse(null);
    }
    // Método para insertar un nuevo producto
    public Productos insertarProducto(Productos producto) throws IOException {
        // Aquí puedes realizar cualquier validación adicional antes de guardar el producto en la base de datos
        return productoRepository.save(producto);
    }

    // Método para actualizar un producto existente
    public Productos actualizarProducto(Integer id, Productos producto) throws IOException {
        Productos productoExistente = productoRepository.findById(id).orElse(null);
        if (productoExistente != null) {
            // Actualiza las propiedades del producto existente con las propiedades del producto recibido
            productoExistente.setNombre(producto.getNombre());
            productoExistente.setTipo_producto(producto.getTipo_producto());
            productoExistente.setCantidad_disponible(producto.getCantidad_disponible());
            productoExistente.setPrecio(producto.getPrecio());

            // Guarda el producto actualizado en la base de datos
            return productoRepository.save(productoExistente);
        } else {
            // Si no se encuentra el producto con el ID especificado, devuelve null o lanza una excepción según sea necesario
            return null;
        }
    }

    public void eliminarProducto(Integer id) {
        productoRepository.deleteById(id);
    }

    public boolean descontarInventario(int idProducto, int cantidad) {
        Productos producto = productoRepository.findById(idProducto).orElse(null);
        if (producto != null && producto.getCantidad_disponible() >= cantidad) {
            producto.setCantidad_disponible(producto.getCantidad_disponible() - cantidad);
            productoRepository.save(producto);
            return true;
        }
        return false;
    }
}
