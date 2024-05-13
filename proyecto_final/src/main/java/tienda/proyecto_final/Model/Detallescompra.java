package tienda.proyecto_final.Model;

import javax.persistence.*;
import java.math.BigDecimal;



@Entity
@Table(name = "detallescompra")
public class Detallescompra {

    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_compra")
    private Compras id_compra;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Productos id_producto;

    private int cantidad;

    private BigDecimal subtotal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

}
