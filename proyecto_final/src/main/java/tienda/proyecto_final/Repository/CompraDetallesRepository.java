package tienda.proyecto_final.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto_final.Model.CompraDetalle;

public interface CompraDetallesRepository extends JpaRepository<CompraDetalle, Integer> {
}
