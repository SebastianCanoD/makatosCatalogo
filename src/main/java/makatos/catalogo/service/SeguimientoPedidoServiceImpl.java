package makatos.catalogo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import makatos.catalogo.model.SeguimientoPedido;
import makatos.catalogo.repository.SeguimientoPedidoRepository;

@Service
public class SeguimientoPedidoServiceImpl implements SeguimientoPedidoService {

    @Autowired
    private SeguimientoPedidoRepository seguimientoRepository;
    
    @Override
    public List<SeguimientoPedido> buscarPorPedidoId(Integer pedidoId) {
        return seguimientoRepository.findByPedido_Id(pedidoId);
    }
    
    @Override
    public SeguimientoPedido guardarSeguimiento(SeguimientoPedido seguimiento) {
        return seguimientoRepository.save(seguimiento);
    }
}
