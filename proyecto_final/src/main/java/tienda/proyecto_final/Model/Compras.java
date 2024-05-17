package tienda.proyecto_final.Model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;


@Entity
@Table(name = "compras")
public class Compras {

    @Id
    private int id_compra;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Usuarios id_cliente;
    @ManyToOne
    @JoinColumn(name = "productos_id_producto")
    private Productos productos_id_producto;

    private Date fecha;

    private BigDecimal total;

    public Productos getProductos_id_producto() {
        return productos_id_producto;
    }

    public void setProductos_id_producto(Productos productos_id_producto) {
        this.productos_id_producto = productos_id_producto;
    }

    public int getId_compra() {
        return id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    public Usuarios getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Usuarios id_cliente) {
        this.id_cliente = id_cliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
