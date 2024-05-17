package tienda.proyecto_final.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tienda.proyecto_final.Model.Productos;

@Repository
public interface ProductosRepository extends JpaRepository <Productos, Integer> {
}
