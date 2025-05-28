package makatos.catalogo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import makatos.catalogo.model.Rol;
import makatos.catalogo.model.Usuario;
import makatos.catalogo.service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/lista")
	public String listarUsuarios(Model model) {
		// Se filtran solo los usuarios con rol Cliente
		List<Usuario> clientes = usuarioService.buscarPorRol(Rol.Cliente);
		model.addAttribute("usuarios", clientes);
		return "usuario/lista"; // Vista: src/main/resources/templates/usuario/lista.html
	}

	// Mostrar detalle de una cuenta de usuario
	@GetMapping("/ver/{id}")
	public String verUsuario(@PathVariable("id") Integer id, Model model) {
		Usuario usuario = usuarioService.buscarPorId(id);
		model.addAttribute("usuario", usuario);
		return "usuario/detalle"; // Vista: src/main/resources/templates/usuario/detalle.html
	}
}
