package makatos.catalogo.model;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "pedido_detalle")
public class PedidoDetalle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "pedidoId", nullable = false)
	private Pedido pedido;

	@ManyToOne
	@JoinColumn(name = "productoId", nullable = false)
	private Producto producto;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal precioUnitario;

	// Nueva relación 1:1 inversa
	@OneToOne(mappedBy = "pedidoDetalle")
	private PedidoPersonalizacion pedidoPersonalizacion;

	// Cantidades por talla en columnas separadas
	@Column(nullable = false, columnDefinition = "int default 0")
	private Integer xs;

	@Column(nullable = false, columnDefinition = "int default 0")
	private Integer s;

	@Column(nullable = false, columnDefinition = "int default 0")
	private Integer m;

	@Column(nullable = false, columnDefinition = "int default 0")
	private Integer l;

	@Column(nullable = false, columnDefinition = "int default 0")
	private Integer xl;

	@Column(nullable = false, columnDefinition = "int default 0")
	private Integer xxl;

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

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public Integer getXs() {
		return xs;
	}

	public void setXs(Integer xs) {
		this.xs = xs;
	}

	public Integer getS() {
		return s;
	}

	public void setS(Integer s) {
		this.s = s;
	}

	public Integer getM() {
		return m;
	}

	public void setM(Integer m) {
		this.m = m;
	}

	public Integer getL() {
		return l;
	}

	public void setL(Integer l) {
		this.l = l;
	}

	public Integer getXl() {
		return xl;
	}

	public void setXl(Integer xl) {
		this.xl = xl;
	}

	public Integer getXxl() {
		return xxl;
	}

	public void setXxl(Integer xxl) {
		this.xxl = xxl;
	}

	public PedidoPersonalizacion getPedidoPersonalizacion() {
		return pedidoPersonalizacion;
	}

	public void setPedidoPersonalizacion(PedidoPersonalizacion pedidoPersonalizacion) {
		this.pedidoPersonalizacion = pedidoPersonalizacion;
	}
}
