package makatos.catalogo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import makatos.catalogo.model.Producto;
import makatos.catalogo.model.TipoProducto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Búsqueda por nombre (contiene una cadena)
    List<Producto> findByNombreContaining(String nombre);
    
    // Búsqueda por tipo (MENUDEO o MAYOREO)
    List<Producto> findByTipo(TipoProducto tipo);
    
    // Nuevo método para filtrar los productos en oferta según su tipo
    List<Producto> findByTipoAndOferta(TipoProducto tipo, boolean oferta);
}

