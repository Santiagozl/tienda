package tienda.proyecto_final.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto_final.Model.Compras;

public interface ComprasRepository extends JpaRepository<Compras, Integer> {
}
