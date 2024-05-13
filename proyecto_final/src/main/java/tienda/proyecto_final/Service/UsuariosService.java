package tienda.proyecto_final.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tienda.proyecto_final.Model.ActualizarClienteDTO;
import tienda.proyecto_final.Model.Usuarios;
import tienda.proyecto_final.Repository.UsuariosRepository;
import tienda.proyecto_final.Util.GenerarContraseña;

import java.util.Optional;

@Service
public class UsuariosService {
    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private EmailService emailService;

    public boolean esAdministrador(String usuario, String contraseña) {
        Optional<Usuarios> admin = usuariosRepository.findByUsuarioAndContraseñaAndRol(usuario, contraseña, "admin");
        return admin.isPresent();
    }

    public boolean esCliente(String usuario, String contraseña) {
        Optional<Usuarios> cliente = usuariosRepository.findByUsuarioAndContraseñaAndRol(usuario, contraseña, "cliente");
        return cliente.isPresent();
    }

    public java.util.List<Usuarios> obtenerTodosClientes() {
        return usuariosRepository.findAll();
    }

    public java.util.Optional<Usuarios> obtenerClientePorId(Integer id) {
        return usuariosRepository.findById(id);
    }

    public Usuarios agregarCliente(Usuarios cliente) {
        Optional<Usuarios> usuarioExistente = usuariosRepository.findByEmail(cliente.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new RuntimeException("El correo electrónico ya está en uso.");
        } else {
            cliente.setRol("cliente");
            generarUsuarioYContraseña(cliente);
            Usuarios nuevoCliente = usuariosRepository.save(cliente);
            return nuevoCliente;
        }
    }

    public Usuarios actualizarCliente(Integer id, ActualizarClienteDTO clienteDTO) {
        Optional<Usuarios> clienteExistente = usuariosRepository.findById(id);
        if (clienteExistente.isPresent()) {
            Usuarios clienteActual = clienteExistente.get();
            clienteActual.setCedula(clienteDTO.getCedula());
            clienteActual.setNombre(clienteDTO.getNombre());
            clienteActual.setApellidos(clienteDTO.getApellidos());
            clienteActual.setEmail(clienteDTO.getEmail());
            clienteActual.setNumero_celular(clienteDTO.getNumero_celular());
            clienteActual.setDireccion(clienteDTO.getDireccion());
            return usuariosRepository.save(clienteActual);
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }

    public void eliminarCliente(Integer id) {
        usuariosRepository.deleteById(id);
    }


    public void eliminarCliente(Integer id, String usuario, String contraseña) {
        if (!esAdministrador(usuario, contraseña)) {
            throw new RuntimeException("Acceso denegado. Se requieren permisos de administrador.");
        }

        usuariosRepository.deleteById(id);
    }

    private void generarUsuarioYContraseña(Usuarios cliente) {
        String usuario = generarUsuario(cliente.getNombre(), cliente.getApellidos());
        String contraseña = GenerarContraseña.generarContraseña();
        cliente.setUsuario(usuario);
        cliente.setContraseña(contraseña);
        enviarCorreoNuevoCliente(cliente.getEmail(), usuario, contraseña);
    }

    private String generarUsuario(String nombre, String apellidos) {
        String[] nombres = nombre.split(" ");
        String[] apellidosArr = apellidos.split(" ");
        String usuario = nombres[0].substring(0, 1) + apellidosArr[0];
        return usuario.toLowerCase();
    }

    private void enviarCorreoNuevoCliente(String email, String usuario, String contraseña) {
        String mensaje = "¡Bienvenido a nuestra tienda!\n\n"
                + "Tu usuario: " + usuario + "\n"
                + "Tu contraseña: " + contraseña + "\n\n"
                + "Por favor, guarda esta información para poder acceder a nuestra tienda.";
        emailService.enviarCorreo(email, "Bienvenido a la tienda", mensaje);
    }
}
