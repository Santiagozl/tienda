package tienda.proyecto_final.Model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "compras")
public class Compras {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_compra;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Usuarios cliente;

    private Date fecha;

    // Se añade inicialización para evitar nullPointerException
    @OneToMany(mappedBy = "id_compra", cascade = CascadeType.ALL)
    private List<CompraDetalle> detalles = new ArrayList<>();

    private BigDecimal total;

    // Constructores, Getters y Setters

    public Compras() {
        this.detalles = new ArrayList<>();
    }


    public Compras(Usuarios cliente, Date fecha, BigDecimal total) {
        this.cliente = cliente;
        this.fecha = fecha;
        this.total = total;
    }

    // Otros constructores si es necesario

    // Getters y Setters

    public int getId_compra() {
        return id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    public Usuarios getCliente() {
        return cliente;
    }

    public void setCliente(Usuarios cliente) {
        this.cliente = cliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<CompraDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<CompraDetalle> detalles) {
        this.detalles = detalles;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}