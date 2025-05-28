package makatos.catalogo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import makatos.catalogo.model.EstadoPedido;
import makatos.catalogo.model.Pedido;

import makatos.catalogo.service.PedidoService;

@Controller
@RequestMapping("/reporte")
public class ReporteController {

    @Autowired
    private PedidoService pedidoService;
    

    
    // Reporte de ventas: lista de todos los pedidos
    @GetMapping("/ventas")
    public String reporteVentas(Model model) {
        List<Pedido> pedidos = pedidoService.buscarTodos();
        model.addAttribute("pedidos", pedidos);
        return "reporte/ventas";  // Vista: reporte/ventas.html
    }
    
    // Ver el detalle de una venta (compra)
    @GetMapping("/ventas/detalle/{id}")
    public String detalleVenta(@PathVariable("id") Integer id, Model model) {
        Pedido pedido = pedidoService.buscarPorId(id);
        model.addAttribute("pedido", pedido);
        return "reporte/detalle";  // Vista: reporte/detalle.html
    }
    
    // Actualizar el estado del pedido
    @PostMapping("/ventas/detalle/{id}")
    public String actualizarEstadoVenta(@PathVariable("id") Integer id,
            @RequestParam("estado") String nuevoEstado) {
        Pedido pedido = pedidoService.buscarPorId(id);
        // Convertir el String a enum; el valor debe coincidir exactamente con las constantes definidas
        pedido.setEstado(EstadoPedido.valueOf(nuevoEstado));
        pedidoService.guardarPedido(pedido);
        return "redirect:/reporte/ventas";
    }

}
