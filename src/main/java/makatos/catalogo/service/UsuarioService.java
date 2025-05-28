package makatos.catalogo.service;

import java.util.List;

import makatos.catalogo.model.Rol;
import makatos.catalogo.model.Usuario;

public interface UsuarioService {
    List<Usuario> buscarTodos();
    Usuario buscarPorId(Integer id);
    Usuario guardarUsuario(Usuario usuario);
    void eliminarPorId(Integer id);
    Usuario buscarPorUsuario(String usuario);
    List<Usuario> buscarPorRol(Rol rol);
}
