package makatos.catalogo.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import makatos.catalogo.model.MetodoPago;
import makatos.catalogo.model.Pedido;
import makatos.catalogo.repository.PedidoRepository;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Override
    public List<Pedido> buscarTodos() {
        return pedidoRepository.findAll();
    }
    
    @Override
    public Pedido buscarPorId(Integer id) {
        Optional<Pedido> optPedido = pedidoRepository.findById(id);
        return optPedido.orElse(null);
    }
    
    @Override
    public Pedido guardarPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }
    
    @Override
    public void eliminarPorId(Integer id) {
        pedidoRepository.deleteById(id);
    }
    
    @Override
    public List<Pedido> buscarPorUsuarioId(Integer usuarioId) {
        return pedidoRepository.findByUsuario_Id(usuarioId);
    }
    
    
    @Override
    public List<Pedido> buscarPorMetodoPago(MetodoPago metodoPago) {
        return pedidoRepository.findByMetodoPago(metodoPago);
    }

    public Pedido obtenerUltimoPedidoPorUsuario(String username) {
        return pedidoRepository.findFirstByUsuario_UsuarioOrderByFechaDesc(username)
                .orElse(null);
    }
    
}
