package makatos.catalogo.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import makatos.catalogo.model.StockPorTalla;
import makatos.catalogo.repository.StockPorTallaRepository;

@Service
public class StockPorTallaServiceImpl implements StockPorTallaService {

    @Autowired
    private StockPorTallaRepository stockRepository;
    
    @Override
    public Optional<StockPorTalla> buscarPorProductoId(Integer productoId) {
        return stockRepository.findByProducto_Id(productoId);
    }
    
    @Override
    public StockPorTalla guardarStock(StockPorTalla stock) {
        return stockRepository.save(stock);
    }
    
    public void actualizarStock(StockPorTalla stock) {
        stockRepository.save(stock);
    }
}
