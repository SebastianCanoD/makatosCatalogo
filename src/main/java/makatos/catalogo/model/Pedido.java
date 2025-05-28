package makatos.catalogo.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "pedidos")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // Este es el único campo mapeado a la columna "id"

    // Resto de propiedades y relaciones...
    // Por ejemplo:
    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false)
    private Usuario usuario;

    //@ManyToOne
    //@JoinColumn(name = "productoId", nullable = false)
    //private Producto producto;

    @Enumerated(EnumType.STRING)
    @Column(name="tipo", nullable = false)
    private MetodoPago metodoPago;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha = new Date();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    private String direccionEnvio;
    
    // Relación: Un pedido tiene muchos detalles
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<PedidoDetalle> pedidoDetalles;
    
    // Relación: Historial de seguimiento del pedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<SeguimientoPedido> seguimientos;

    @Column(name = "imagen_transferencia")
    private String imagenTransferencia;
    // Getters y Setters

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }
    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
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
    public EstadoPedido getEstado() {
        return estado;
    }
    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }
    
    public String getDireccionEnvio() {
        return direccionEnvio;
    }
    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }
    public List<PedidoDetalle> getPedidoDetalles() {
        return pedidoDetalles;
    }
    public void setPedidoDetalles(List<PedidoDetalle> pedidoDetalles) {
        this.pedidoDetalles = pedidoDetalles;
    }
    public List<SeguimientoPedido> getSeguimientos() {
        return seguimientos;
    }
    public void setSeguimientos(List<SeguimientoPedido> seguimientos) {
        this.seguimientos = seguimientos;
    }
    
    public String getImagenTransferencia() {
        return imagenTransferencia;
    }
    public void setImagenTransferencia(String imagenTransferencia) {
        this.imagenTransferencia = imagenTransferencia;
    }
}
