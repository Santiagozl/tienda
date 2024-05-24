package tienda.proyecto_final.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tienda.proyecto_final.Model.ActualizarClienteDTO;
import tienda.proyecto_final.Model.LoginDTO;
import tienda.proyecto_final.Model.Usuarios;
import tienda.proyecto_final.Service.EmailService;
import tienda.proyecto_final.Service.UsuariosService;

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

    public static class LoginResponse {

        private Data data;
        private String message;
        private boolean status;

        public LoginResponse(String message, Data data, boolean status) {
            this.message = message;
            this.data = data;
            this.status = status;
        }

        // Getters y setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public boolean getStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public static class Data {

            private int id;
            private String rol;

            public Data(int id, String rol) {
                this.id = id;
                this.rol = rol;
            }

            // Getters y setters
            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getRol() {
                return rol;
            }

            public void setRol(String rol) {
                this.rol = rol;
            }
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDTO loginDTO) {
        String usuario = loginDTO.getUsuario();
        String contraseña = loginDTO.getContraseña();

        Optional<Usuarios> adminOpt = usuariosService.obtenerPorUsuarioYContraseñaYRol(usuario, contraseña, "admin");
        Optional<Usuarios> clienteOpt = usuariosService.obtenerPorUsuarioYContraseñaYRol(usuario, contraseña, "cliente");

        if (adminOpt.isPresent()) {
            Usuarios admin = adminOpt.get();
            LoginResponse.Data data = new LoginResponse.Data(admin.getId_cliente(), "admin");
            return new ResponseEntity<>(new LoginResponse("Bienvenido, administrador", data, true), HttpStatus.OK);
        } else if (clienteOpt.isPresent()) {
            Usuarios cliente = clienteOpt.get();
            LoginResponse.Data data = new LoginResponse.Data(cliente.getId_cliente(), "cliente");
            return new ResponseEntity<>(new LoginResponse("Bienvenido, cliente", data, true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new LoginResponse("Credenciales invalidas", new LoginResponse.Data(-1, "none"), false), HttpStatus.OK);
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
