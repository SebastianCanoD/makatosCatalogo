package makatos.catalogo.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pedido_personalizacion")
public class PedidoPersonalizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    // Relación 1:1 con PedidoDetalle
    @OneToOne
    @JoinColumn(name = "pedidoDetalleId", nullable = false, unique = true)
    private PedidoDetalle pedidoDetalle;
    
    private String archivo; // Ruta o URL opcional del archivo generado
    
    @Column(columnDefinition = "TEXT")
    private String detalles; // Detalles en texto cuando no se use archivo
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion = new Date();
    
    // Getters y Setters

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public PedidoDetalle getPedidoDetalle() {
        return pedidoDetalle;
    }
    public void setPedidoDetalle(PedidoDetalle pedidoDetalle) {
        this.pedidoDetalle = pedidoDetalle;
    }
    public String getArchivo() {
        return archivo;
    }
    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }
    public String getDetalles() {
        return detalles;
    }
    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
