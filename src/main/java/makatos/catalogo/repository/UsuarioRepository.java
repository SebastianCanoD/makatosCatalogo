package makatos.catalogo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import makatos.catalogo.model.Rol;
import makatos.catalogo.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByUsuario(String usuario);
    List<Usuario> findByRol(Rol rol);
    // Puedes agregar otros métodos de búsqueda, por ejemplo por correo
}
