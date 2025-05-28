package makatos.catalogo.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import makatos.catalogo.model.PedidoDetalle;

public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, Integer> {
    Optional<PedidoDetalle> findById(Integer id); // ✅ Correcto
    List<PedidoDetalle> findByPedidoId(Integer pedidoId); // ✅ Agregar este método
}


