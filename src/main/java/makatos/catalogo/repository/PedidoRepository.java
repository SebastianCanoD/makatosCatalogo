package makatos.catalogo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import makatos.catalogo.model.Pedido;
import makatos.catalogo.model.MetodoPago;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByUsuario_Id(Integer usuarioId);
    List<Pedido> findByMetodoPago(MetodoPago metodoPago);
    Optional<Pedido> findFirstByUsuario_UsuarioOrderByFechaDesc(String username);
}

