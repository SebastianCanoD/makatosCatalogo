package makatos.catalogo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import makatos.catalogo.model.PedidoDetalle;
import makatos.catalogo.repository.PedidoDetalleRepository;

@Service
public class PedidoDetalleServiceImpl implements PedidoDetalleService {

    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;

    @Override
    public void guardarPedidoDetalle(PedidoDetalle pedidoDetalle) {
        pedidoDetalleRepository.save(pedidoDetalle);
    }

    @Override
    public PedidoDetalle buscarPorId(Integer id) {
        return pedidoDetalleRepository.findById(id).orElse(null);
    }

    @Override
    public List<PedidoDetalle> buscarPorPedidoId(Integer pedidoId) {
        return pedidoDetalleRepository.findByPedidoId(pedidoId); // ✅ Ahora es correcto
    }
    
}
