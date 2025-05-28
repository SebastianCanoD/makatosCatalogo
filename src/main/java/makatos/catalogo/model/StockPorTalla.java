package makatos.catalogo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "stock_por_talla")
public class StockPorTalla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    // Relación: Cada registro se asocia a un producto
    @ManyToOne
    @JoinColumn(name = "productoId", nullable = false)
    private Producto producto;
    
    // Cada columna representa el stock para una talla
    @Column(nullable = false)
    private int xs = 0;
    
    @Column(nullable = false)
    private int s = 0;
    
    @Column(nullable = false)
    private int m = 0;
    
    @Column(nullable = false)
    private int l = 0;
    
    @Column(nullable = false)
    private int xl = 0;
    
    @Column(nullable = false)
    private int xxl = 0;

    // Getters y Setters
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public int getXs() {
        return xs;
    }
    
    public void setXs(int xs) {
        this.xs = xs;
    }
    
    public int getS() {
        return s;
    }
    
    public void setS(int s) {
        this.s = s;
    }
    
    public int getM() {
        return m;
    }
    
    public void setM(int m) {
        this.m = m;
    }
    
    public int getL() {
        return l;
    }
    
    public void setL(int l) {
        this.l = l;
    }
    
    public int getXl() {
        return xl;
    }
    
    public void setXl(int xl) {
        this.xl = xl;
    }
    
    public int getXxl() {
        return xxl;
    }
    
    public void setXxl(int xxl) {
        this.xxl = xxl;
    }
}
