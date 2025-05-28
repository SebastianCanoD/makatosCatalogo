package makatos.catalogo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import makatos.catalogo.service.ProductoService;

@Controller
public class HomeController {

    @Autowired
    private ProductoService productoService;
    
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        // Se agregan las listas requeridas para el home.
        model.addAttribute("mayoreoProductos", productoService.buscarProductosMayoreoEnOferta());
        model.addAttribute("menudeoProductos", productoService.buscarProductosMenudeoEnOferta());
        return "home"; // La vista home.html es la que contiene el carrusel
    }
}
