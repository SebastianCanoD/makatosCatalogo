package makatos.catalogo.model;

import jakarta.persistence.*;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 100)
	private String nombre;

	@Column(columnDefinition = "TEXT")
	private String descripcion;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoProducto tipo;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal precio;

	private boolean oferta;

	private String imagen;

	@Column(name = "fechaCreacion") // Indica que se use exactamente "fechaCreacion"
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCreacion = new Date();

	@OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
	private List<StockPorTalla> stockPorTallas;
	// Getters y Setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public TipoProducto getTipo() {
		return tipo;
	}

	public void setTipo(TipoProducto tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public boolean isOferta() {
		return oferta;
	}

	public void setOferta(boolean oferta) {
		this.oferta = oferta;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public List<StockPorTalla> getStockPorTallas() {
		return stockPorTallas;
	}

	public void setStockPorTallas(List<StockPorTalla> stockPorTallas) {
		this.stockPorTallas = stockPorTallas;
	}
}
