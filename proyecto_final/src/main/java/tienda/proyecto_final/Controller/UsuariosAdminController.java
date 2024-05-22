package tienda.proyecto_final.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tienda.proyecto_final.Model.ActualizarClienteDTO;
import tienda.proyecto_final.Model.Usuarios;
import tienda.proyecto_final.Service.UsuariosService;
import tienda.proyecto_final.Service.EmailService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosAdminController {

    @Autowired
    private UsuariosService usuariosService;
    @Autowired
    private EmailService emailService;

    // Endpoint para obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<Usuarios>> obtenerTodosClientes() {
        List<Usuarios> clientes = usuariosService.obtenerTodosClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    // Endpoint para obtener un cliente por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuarios> obtenerClientePorId(@PathVariable("id") Integer id) {
        Usuarios cliente = usuariosService.obtenerClientePorId(id).orElse(null);
        if (cliente != null) {
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para agregar un nuevo cliente
    @PostMapping("/agregar")
    public ResponseEntity<Usuarios> agregarCliente(@RequestBody Usuarios cliente) {
        Usuarios nuevoCliente = usuariosService.agregarCliente(cliente);
        emailService.enviarCorreoNuevoCliente(nuevoCliente);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String usuario, @RequestParam String contraseña) {
        // Verificar si el usuario es administrador
        if (usuariosService.esAdministrador(usuario, contraseña)) {
            return new ResponseEntity<>("Bienvenido, administrador", HttpStatus.OK);
        }
        // Verificar si el usuario es cliente
        else if (usuariosService.esCliente(usuario, contraseña)) {
            return new ResponseEntity<>("Bienvenido, cliente", HttpStatus.OK);
        }
        // Si las credenciales son inválidas
        else {
            return new ResponseEntity<>("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
        }
    }

    // Endpoint para actualizar un cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<Usuarios> actualizarClciente(@PathVariable("id") Integer id, @RequestBody ActualizarClienteDTO clienteDTO) {
        Usuarios clienteActualizado = usuariosService.actualizarCliente(id, clienteDTO);
        if (clienteActualizado != null) {
            return new ResponseEntity<>(clienteActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para eliminar un cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable("id") Integer id) {
        usuariosService.eliminarCliente(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}