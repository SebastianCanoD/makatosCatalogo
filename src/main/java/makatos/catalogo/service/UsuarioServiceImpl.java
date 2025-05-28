package makatos.catalogo.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import makatos.catalogo.model.Rol;
import makatos.catalogo.model.Usuario;
import makatos.catalogo.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public List<Usuario> buscarTodos() {
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario buscarPorId(Integer id) {
		Optional<Usuario> optUsuario = usuarioRepository.findById(id);
		return optUsuario.orElse(null);
	}

	@Override
	public Usuario guardarUsuario(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	@Override
	public void eliminarPorId(Integer id) {
		usuarioRepository.deleteById(id);
	}

	@Override
	public Usuario buscarPorUsuario(String usuario) {
		return usuarioRepository.findByUsuario(usuario);
	}

	@Override
	public List<Usuario> buscarPorRol(Rol rol) {
		return usuarioRepository.findByRol(rol);
	}

}
