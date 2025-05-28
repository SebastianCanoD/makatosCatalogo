package makatos.catalogo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import makatos.catalogo.model.SeguimientoPedido;

public interface SeguimientoPedidoRepository extends JpaRepository<SeguimientoPedido, Integer> {
    List<SeguimientoPedido> findByPedido_Id(Integer pedidoId);
}
