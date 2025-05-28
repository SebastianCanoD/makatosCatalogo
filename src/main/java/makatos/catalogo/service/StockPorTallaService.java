package makatos.catalogo.service;

import java.util.Optional;
import makatos.catalogo.model.StockPorTalla;

public interface StockPorTallaService {
    Optional<StockPorTalla> buscarPorProductoId(Integer productoId);
    StockPorTalla guardarStock(StockPorTalla stock);
    void actualizarStock(StockPorTalla stock);
    
}
