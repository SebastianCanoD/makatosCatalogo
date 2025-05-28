package makatos.catalogo.service;

import java.util.List;

import makatos.catalogo.model.PedidoDetalle;

public interface PedidoDetalleService {
    void guardarPedidoDetalle(PedidoDetalle pedidoDetalle);
    List<PedidoDetalle> buscarPorPedidoId(Integer pedidoId);
    public PedidoDetalle buscarPorId(Integer id);
}
