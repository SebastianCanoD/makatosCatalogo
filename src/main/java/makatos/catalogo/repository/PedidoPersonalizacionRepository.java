package makatos.catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import makatos.catalogo.model.PedidoPersonalizacion;

public interface PedidoPersonalizacionRepository extends JpaRepository<PedidoPersonalizacion, Integer> {
    Optional<PedidoPersonalizacion> findByPedidoDetalleId(Integer pedidoDetalleId);
}
