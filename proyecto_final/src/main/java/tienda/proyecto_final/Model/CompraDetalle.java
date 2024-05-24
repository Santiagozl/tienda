package tienda.proyecto_final.Model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "compra_detalles")
public class CompraDetalle {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_detalle;

    @ManyToOne
    @JoinColumn(name = "compra_id_compra")
    private Compras id_compra;

    @ManyToOne
    @JoinColumn(name = "producto_id_producto")
    private Productos id_producto;

    private int cantidad;
    private BigDecimal precio;

    // Getters y Setters

    public int getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(int id_detalle) {
        this.id_detalle = id_detalle;
    }


    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Compras getId_compra() {
        return id_compra;
    }

    public void setId_compra(Compras id_compra) {
        this.id_compra = id_compra;
    }

    public Productos getId_producto() {
        return id_producto;
    }

    public void setId_producto(Productos id_producto) {
        this.id_producto = id_producto;
    }
}
