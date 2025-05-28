package makatos.catalogo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import makatos.catalogo.model.Rol;
import makatos.catalogo.model.Usuario;
import makatos.catalogo.service.UsuarioService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PasswordEncoder passwordEncoder; // Inyectamos el bean de encriptación

	@GetMapping("/registro")
	public String registroUsuario(Model model) {
		model.addAttribute("nuevoUsuario", new Usuario());
		return "cliente/form";
	}

    @PostMapping("/registro")
    public String guardarRegistro(@ModelAttribute("nuevoUsuario") Usuario usuario, Model model) {
        // Verificar duplicados
        if (usuarioService.buscarPorUsuario(usuario.getUsuario()) != null) {
            model.addAttribute("error", "El nombre de usuario ya existe, por favor elige otro.");
            return "cliente/form";
        }
        usuario.setRol(Rol.Cliente);
        // Encriptar la contraseña antes de guardar
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuarioService.guardarUsuario(usuario);
        return "redirect:/login";
    }

	@GetMapping("/perfil")
	public String editarPerfil(Model model, Principal principal) {
		Usuario u = usuarioService.buscarPorUsuario(principal.getName());
		model.addAttribute("usuarioForm", u);
		return "cliente/perfil";
	}

    @PostMapping("/perfil")
    public String guardarPerfil(@ModelAttribute("usuarioForm") Usuario usuarioForm, Principal principal) {
        Usuario actual = usuarioService.buscarPorUsuario(principal.getName());
        usuarioForm.setId(actual.getId());
        usuarioForm.setUsuario(actual.getUsuario());
        usuarioForm.setRol(Rol.Cliente);
        // Si en el formulario de perfil la contraseña queda vacía, se conserva la existente;
        // de lo contrario, se encripta la nueva
        if (usuarioForm.getContrasena() == null || usuarioForm.getContrasena().trim().isEmpty()) {
            usuarioForm.setContrasena(actual.getContrasena());
        } else {
            usuarioForm.setContrasena(passwordEncoder.encode(usuarioForm.getContrasena()));
        }
        usuarioService.guardarUsuario(usuarioForm);
        return "redirect:/home";
    }
}
