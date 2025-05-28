package makatos.catalogo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import makatos.catalogo.model.PedidoPersonalizacion;
import makatos.catalogo.repository.PedidoPersonalizacionRepository;

@Service
public class PedidoPersonalizacionService {

    @Autowired
    private PedidoPersonalizacionRepository repository;

    public void guardar(PedidoPersonalizacion personalizacion) {
        repository.save(personalizacion);
    }

    public PedidoPersonalizacion buscarPorPedidoDetalleId(Integer pedidoDetalleId) {
        return repository.findByPedidoDetalleId(pedidoDetalleId).orElse(null);
    }
}
