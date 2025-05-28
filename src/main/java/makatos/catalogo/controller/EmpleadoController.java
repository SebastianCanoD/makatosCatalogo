package makatos.catalogo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import makatos.catalogo.model.Rol;
import makatos.catalogo.model.Usuario;
import makatos.catalogo.service.UsuarioService;

@Controller
@RequestMapping("/empleado")
public class EmpleadoController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	// Listar solo empleados (filtrar por rol)
	@GetMapping("/lista")
	public String listarEmpleados(Model model) {
		// Asumiendo que en el servicio tienes un método que retorna solo empleados.
		List<Usuario> empleados = usuarioService.buscarPorRol(Rol.Empleado);
		model.addAttribute("empleados", empleados);
		return "empleado/lista"; // Vista: empleado/lista.html
	}

	// Ver detalle de un empleado
	@GetMapping("/ver/{id}")
	public String verEmpleado(@PathVariable("id") Integer id, Model model) {
		Usuario empleado = usuarioService.buscarPorId(id);
		model.addAttribute("empleado", empleado);
		return "empleado/detalle"; // Puedes crear la vista empleado/detalle.html para mostrar más información
	}

	// Crear un nuevo empleado
	@GetMapping("/crear")
	public String crearEmpleado(Model model) {
		Usuario empleado = new Usuario();
		// Asigna el rol EMPLEADO automáticamente
		empleado.setRol(Rol.Empleado);
		model.addAttribute("empleado", empleado);
		return "empleado/form"; // Vista: empleado/form.html
	}

	@PostMapping("/guardar")
	public String guardarEmpleado(@ModelAttribute("empleado") Usuario empleado) {
		if (empleado.getId() != null) {
			Usuario existente = usuarioService.buscarPorId(empleado.getId());
			if (empleado.getContrasena() == null || empleado.getContrasena().trim().isEmpty()) {
				empleado.setContrasena(existente.getContrasena());
			} else {
				empleado.setContrasena(passwordEncoder.encode(empleado.getContrasena()));
			}
			if (empleado.getRol() == null) {
				empleado.setRol(existente.getRol());
			}
		} else {
			empleado.setRol(Rol.Empleado);
			// Encriptar la contraseña nueva
			empleado.setContrasena(passwordEncoder.encode(empleado.getContrasena()));
		}
		usuarioService.guardarUsuario(empleado);
		return "redirect:/empleado/lista";
	}

	// Editar empleado
	@GetMapping("/editar/{id}")
	public String editarEmpleado(@PathVariable("id") Integer id, Model model) {
		Usuario empleado = usuarioService.buscarPorId(id);
		model.addAttribute("empleado", empleado);
		return "empleado/form"; // Reutilizamos la misma vista de formulario para editar
	}

	// (Opcional) Eliminar empleado
	@GetMapping("/eliminar/{id}")
	public String eliminarEmpleado(@PathVariable("id") Integer id) {
		usuarioService.eliminarPorId(id); // Asegúrate de tener este método implementado en el service
		return "redirect:/empleado/lista";
	}
}
