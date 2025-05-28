package makatos.catalogo.service;

import java.util.List;
import makatos.catalogo.model.SeguimientoPedido;

public interface SeguimientoPedidoService {
    List<SeguimientoPedido> buscarPorPedidoId(Integer pedidoId);
    SeguimientoPedido guardarSeguimiento(SeguimientoPedido seguimiento);
}
