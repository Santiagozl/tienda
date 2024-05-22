package tienda.proyecto_final.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tienda.proyecto_final.Model.Compras;
import tienda.proyecto_final.Service.CompraService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/compras")
public class ComprasController {
    @Autowired
    private CompraService comprasService;

    @PostMapping()
    public Compras realizarCompra(@RequestBody CompraRequest compraRequest) {
        return comprasService.realizarCompra(compraRequest.getIdCliente(), compraRequest.getIdProducto(), compraRequest.getTotal(), compraRequest.getCantidad());
    }

    public static class CompraRequest {
        private int idCliente;
        private int idProducto;
        private BigDecimal total;
        private int cantidad;

        // Getters y Setters
        public int getIdCliente() {
            return idCliente;
        }

        public void setIdCliente(int idCliente) {
            this.idCliente = idCliente;
        }

        public int getIdProducto() {
            return idProducto;
        }

        public void setIdProducto(int idProducto) {
            this.idProducto = idProducto;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }
    }
}


