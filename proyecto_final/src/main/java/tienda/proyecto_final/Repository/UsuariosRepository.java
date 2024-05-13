package tienda.proyecto_final.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tienda.proyecto_final.Model.Usuarios;

import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Integer> {
    Optional<Usuarios> findByUsuarioAndContraseñaAndRol(String usuario, String contraseña, String rol);
    Optional<Usuarios> findByEmail(String email);

}
