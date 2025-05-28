package makatos.catalogo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import makatos.catalogo.model.StockPorTalla;

public interface StockPorTallaRepository extends JpaRepository<StockPorTalla, Integer> {
    Optional<StockPorTalla> findByProducto_Id(Integer productoId);
    Optional<StockPorTalla> findByProductoId(Integer productoId);
    
}
