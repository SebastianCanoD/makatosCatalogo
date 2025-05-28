package makatos.catalogo.service;

import java.util.List;

import makatos.catalogo.model.MetodoPago;
import makatos.catalogo.model.Pedido;

public interface PedidoService {
    List<Pedido> buscarTodos();
    Pedido buscarPorId(Integer id);
    Pedido guardarPedido(Pedido pedido);
    void eliminarPorId(Integer id);
    List<Pedido> buscarPorUsuarioId(Integer usuarioId);
	List<Pedido> buscarPorMetodoPago(MetodoPago metodoPago);
	Pedido obtenerUltimoPedidoPorUsuario(String username);
}
