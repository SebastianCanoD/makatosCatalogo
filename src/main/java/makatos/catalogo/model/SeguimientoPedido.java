package makatos.catalogo.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "seguimiento_pedidos")
public class SeguimientoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    // Relación: Cada seguimiento se asocia a un pedido
    @ManyToOne
    @JoinColumn(name = "pedidoId", nullable = false)
    private Pedido pedido;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;
    
    // Permite valor nulo para manejar ON DELETE SET NULL
    @ManyToOne
    @JoinColumn(name = "actualizadoPor")
    private Usuario actualizadoPor;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion = new Date();
    
    private String comentario;
    
    // Getters y Setters

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Pedido getPedido() {
        return pedido;
    }
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    public EstadoPedido getEstado() {
        return estado;
    }
    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }
    public Usuario getActualizadoPor() {
        return actualizadoPor;
    }
    public void setActualizadoPor(Usuario actualizadoPor) {
        this.actualizadoPor = actualizadoPor;
    }
    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }
    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    public String getComentario() {
        return comentario;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
