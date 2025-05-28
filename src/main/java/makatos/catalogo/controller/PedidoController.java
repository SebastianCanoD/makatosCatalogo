package makatos.catalogo.controller;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import makatos.catalogo.model.EstadoPedido;
import makatos.catalogo.model.MetodoPago;
import makatos.catalogo.model.Pedido;
import makatos.catalogo.model.PedidoDetalle;
import makatos.catalogo.model.PedidoPersonalizacion;
import makatos.catalogo.model.Producto;
import makatos.catalogo.model.StockPorTalla;
import makatos.catalogo.model.Usuario;
import makatos.catalogo.service.PedidoDetalleService;
import makatos.catalogo.service.PedidoPersonalizacionService;
import makatos.catalogo.service.PedidoService;
import makatos.catalogo.service.ProductoService;
import makatos.catalogo.service.StockPorTallaService;
import makatos.catalogo.service.UsuarioService;

@Controller
@RequestMapping("/compra")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private StockPorTallaService stockPorTallaService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PedidoDetalleService pedidoDetalleService;
	
	@Autowired
	private PedidoPersonalizacionService pedidoPersonalizacionService;
	
	// Este método registra un editor personalizado para convertir cadenas en
	// valores de enum,
	// haciendo la conversión insensible a mayúsculas.
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(MetodoPago.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if (text != null && !text.isEmpty()) {
					setValue(MetodoPago.valueOf(text.toUpperCase()));
				}
			}

			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
	}


	@GetMapping("/mis-pedidos")
	public String listarPedidos(Model model) {
		model.addAttribute("pedidos", pedidoService.buscarTodos());
		return "compra/lista"; // Vista: compra/lista.html
	}

	@GetMapping("/detalle/{id}")
	public String detallesPedido(@PathVariable Integer id, Model model) {
	    Pedido pedido = pedidoService.buscarPorId(id);
	    if (pedido == null) {
	        return "redirect:/compra/mis-pedidos";
	    }

	    List<PedidoDetalle> detalles = pedidoDetalleService.buscarPorPedidoId(id);
	    if (detalles.isEmpty()) {
	        return "redirect:/compra/mis-pedidos";
	    }

	    PedidoPersonalizacion personalizacion = pedidoPersonalizacionService.buscarPorPedidoDetalleId(detalles.get(0).getId());

	    model.addAttribute("pedido", pedido);
	    model.addAttribute("detalles", detalles); // ✅ Cambia "detalle" por "detalles"
	    model.addAttribute("personalizacion", personalizacion);

	    return "compra/detalle";
	}

	
		@PostMapping("/subirPersonalizacion")
		public String subirPersonalizacion(@RequestParam("pedidoDetalleId") Integer pedidoDetalleId,
		                                   @RequestParam("archivo") MultipartFile archivo,
		                                   @RequestParam("detalles") String detalles) {
	
		    PedidoDetalle detalle = pedidoDetalleService.buscarPorId(pedidoDetalleId);
		    if (detalle == null) {
		        return "redirect:/compra/mis-pedidos";
		    }
	
		    PedidoPersonalizacion personalizacion = pedidoPersonalizacionService.buscarPorPedidoDetalleId(detalle.getId());
		    if (personalizacion == null) {
		        personalizacion = new PedidoPersonalizacion();
		        personalizacion.setPedidoDetalle(detalle);
		    }
	
		    // Guardar archivo en la carpeta de personalización
		    if (archivo != null && !archivo.isEmpty()) {
		        try {
		            String carpeta = System.getProperty("user.home") + "/uploads/personalizacion";
		            File dir = new File(carpeta);
		            if (!dir.exists()) {
		                dir.mkdirs();
		            }
		            
		            String nombreArchivo = System.currentTimeMillis() + "_" + archivo.getOriginalFilename();
		            Path rutaArchivo = Paths.get(carpeta, nombreArchivo);
		            Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
		            
		            personalizacion.setArchivo(nombreArchivo);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
	
		    personalizacion.setDetalles(detalles);
		    pedidoPersonalizacionService.guardar(personalizacion);
	
		    return "redirect:/compra/detalle/" + pedidoDetalleId;
		}


	@GetMapping("/nueva")
	public String nuevaCompra(@RequestParam("productoId") Integer productoId, Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Usuario usuario = usuarioService.buscarPorUsuario(username);

		// Buscar el producto
		Producto producto = productoService.buscarPorId(productoId);
		if (producto == null) {
			model.addAttribute("error", "Producto no encontrado");
			return "redirect:/producto/lista";
		}
		// Buscar stock asociado para el producto
		Optional<StockPorTalla> optStock = stockPorTallaService.buscarPorProductoId(productoId);
		StockPorTalla stock = optStock.orElse(new StockPorTalla());

		// Crear un pedido nuevo (sin asignar el producto, ya que se relaciona en los
		// detalles)
		Pedido pedido = new Pedido();

		model.addAttribute("direccionRegistrada", usuario.getDireccion());

		// Agregar datos al modelo para la vista
		model.addAttribute("pedido", pedido);
		model.addAttribute("producto", producto);
		model.addAttribute("stock", stock);
		// Número CLABE fijo para transferencia (puedes obtenerlo de otra fuente)
		model.addAttribute("clabe", "012345678901234567");

		return "compra/form"; // Vista: compra/form.html
	}
	@PostMapping("/guardar")
	public String guardarPedido(@ModelAttribute Pedido pedido,
	                            @RequestParam("productoId") Integer productoId,
	                            @RequestParam(value="xs", required=false, defaultValue="0") Integer xs,
	                            @RequestParam(value="s", required=false, defaultValue="0") Integer s,
	                            @RequestParam(value="m", required=false, defaultValue="0") Integer m,
	                            @RequestParam(value="l", required=false, defaultValue="0") Integer l,
	                            @RequestParam(value="xl", required=false, defaultValue="0") Integer xl,
	                            @RequestParam(value="xxl", required=false, defaultValue="0") Integer xxl,
	                            @RequestParam("metodoPago") String metodoPago,
	                            @RequestParam(value="imagenComprobante", required=false) MultipartFile comprobante,
	                            @RequestParam("direccionEnvio") String direccionEnvio,
	                            @RequestParam(value="otraDireccion", required=false) String otraDireccion,
	                            Model model) {

	    // Recupera el Usuario autenticado
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String username = auth.getName();
	    Usuario usuario = usuarioService.buscarPorUsuario(username);
	    pedido.setUsuario(usuario);

	    // Recupera el producto
	    Producto producto = productoService.buscarPorId(productoId);
	    BigDecimal precioUnitario = producto.getPrecio();

	    // Obtener el stock actual del producto
	    Optional<StockPorTalla> optStock = stockPorTallaService.buscarPorProductoId(productoId);
	    if (!optStock.isPresent()) {
	        model.addAttribute("mensaje", "No hay stock disponible para este producto.");
	        return "compra/error";
	    }
	    StockPorTalla stock = optStock.get();

	    // Verificar stock disponible
	    if (xs > stock.getXs() || s > stock.getS() || m > stock.getM() || l > stock.getL() || xl > stock.getXl() || xxl > stock.getXxl()) {
	        model.addAttribute("mensaje", "Stock insuficiente para la talla seleccionada.");
	        return "compra/error";
	    }

	    // Asignar dirección de envío
	    pedido.setDireccionEnvio("otra".equalsIgnoreCase(direccionEnvio) ? otraDireccion : direccionEnvio);

	    // Calcular el total
	    BigDecimal total = precioUnitario.multiply(new BigDecimal(xs + s + m + l + xl + xxl));
	    pedido.setTotal(total);

	    // Procesar comprobante si el método de pago es transferencia
	    if ("transferencia".equalsIgnoreCase(metodoPago) && comprobante != null && !comprobante.isEmpty()) {
	        try {
	            String nombreArchivo = System.currentTimeMillis() + "_" + comprobante.getOriginalFilename();
	            Path rutaArchivo = Paths.get(System.getProperty("user.home") + "/uploads/comprobantes", nombreArchivo);
	            Files.copy(comprobante.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
	            pedido.setImagenTransferencia(nombreArchivo);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    // Asignar el estado inicial
	    pedido.setEstado(EstadoPedido.SIN_ESTAMPAR);

	    // Guardar el pedido primero para obtener su ID
	    pedidoService.guardarPedido(pedido);

	    // Ahora el pedido tiene un ID, se puede asociar correctamente al detalle
	    PedidoDetalle detalle = new PedidoDetalle();
	    detalle.setPedido(pedido);
	    detalle.setProducto(producto);
	    detalle.setPrecioUnitario(precioUnitario);
	    detalle.setXs(xs);
	    detalle.setS(s);
	    detalle.setM(m);
	    detalle.setL(l);
	    detalle.setXl(xl);
	    detalle.setXxl(xxl);

	    // Guardar el detalle del pedido
	    
	    System.out.println("XS: " + xs);
	    System.out.println("S: " + s);
	    System.out.println("M: " + m);
	    System.out.println("L: " + l);
	    System.out.println("XL: " + xl);
	    System.out.println("XXL: " + xxl);

	    pedidoDetalleService.guardarPedidoDetalle(detalle);

	    // Restar stock
	    stock.setXs(stock.getXs() - xs);
	    stock.setS(stock.getS() - s);
	    stock.setM(stock.getM() - m);
	    stock.setL(stock.getL() - l);
	    stock.setXl(stock.getXl() - xl);
	    stock.setXxl(stock.getXxl() - xxl);

	    // Actualizar el stock
	    stockPorTallaService.actualizarStock(stock);

	    return "redirect:/compra/confirmacion";
	}

	@GetMapping("/confirmacion")
	public String confirmacionPedido(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();

		Pedido pedido = pedidoService.obtenerUltimoPedidoPorUsuario(username);

		if (pedido == null) {
			return "redirect:/"; // Si no hay pedido, redirigir a home
		}

		model.addAttribute("pedido", pedido);
		return "compra/confirmacion";
	}

}
