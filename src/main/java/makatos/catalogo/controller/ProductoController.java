package makatos.catalogo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import makatos.catalogo.model.Producto;
import makatos.catalogo.model.StockPorTalla;
import makatos.catalogo.service.ProductoService;
import makatos.catalogo.service.StockPorTallaService;

@Controller
@RequestMapping("/producto")
public class ProductoController {

	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private StockPorTallaService stockPorTallaService;

	@GetMapping("/lista")
	public String listarProductos(@RequestParam(name = "tipo", required = false) String tipo,
			@RequestParam(name = "precioMax", required = false) Double precioMax,
			@RequestParam(name = "nombre", required = false) String nombre, Model model) {

		List<Producto> productos;
		// Si se ha enviado algún parámetro se aplica el filtro.
		if ((tipo != null && !tipo.isEmpty()) || precioMax != null || (nombre != null && !nombre.isEmpty())) {
			productos = productoService.buscarPorFiltro(tipo, precioMax, nombre);
		} else {
			productos = productoService.buscarTodos();
		}
		model.addAttribute("productos", productos);
		return "producto/lista";
	}

	// Endpoint para ver detalle de un producto por ID
	@GetMapping("/ver/{id}")
	public String verProducto(@PathVariable("id") Integer id, Model model) {
	    Producto producto = productoService.buscarPorId(id);
	    if (producto == null) {
	        model.addAttribute("error", "Producto no encontrado");
	        return "redirect:/producto/lista";
	    }

	    // Buscar el stock del producto
	    StockPorTalla stock = stockPorTallaService.buscarPorProductoId(id).orElse(null);
	    
	    model.addAttribute("producto", producto);
	    model.addAttribute("stock", stock); // Agregar stock al modelo
	    
	    return "producto/detalle";
	}

	// Mostrar formulario para crear un nuevo producto
	@GetMapping("/create")
	public String crearProducto(Model model) {
		model.addAttribute("producto", new Producto());
		return "producto/form"; // Vista con formulario, por ejemplo, form.html
	}

	@PostMapping("/guardar")
	public String guardarProducto(
	        @ModelAttribute Producto producto,
	        BindingResult result,
	        @RequestParam("archivoImagen") MultipartFile imagen,  // Usamos 'archivoImagen'
	        @RequestParam(value = "stockXs", required = false) Integer stockXs,
	        @RequestParam(value = "stockS", required = false) Integer stockS,
	        @RequestParam(value = "stockM", required = false) Integer stockM,
	        @RequestParam(value = "stockL", required = false) Integer stockL,
	        @RequestParam(value = "stockXl", required = false) Integer stockXl,
	        @RequestParam(value = "stockXxl", required = false) Integer stockXxl,
	        Model model) {

	    System.out.println("Entrando en guardarProducto...");
	    if (result.hasErrors()) {
	        System.out.println("Errores de validación: " + result.getAllErrors());
	        return "producto/form";
	    }

	    // Definir la ruta de subida de imágenes
	    String carpetaImagenes = System.getProperty("user.home") + "/uploads/images";
	    System.out.println("Carpeta de imágenes: " + carpetaImagenes);
	    File directorio = new File(carpetaImagenes);
	    if (!directorio.exists()) {
	        System.out.println("El directorio no existe. Creándolo...");
	        directorio.mkdirs();
	    }

	    // Procesar la imagen solo si se envía una nueva
	    if (!imagen.isEmpty()) {
	        try {
	            String nombreArchivo = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
	            Path rutaArchivo = Paths.get(carpetaImagenes, nombreArchivo);
	            System.out.println("Copiando imagen a: " + rutaArchivo.toString());
	            Files.copy(imagen.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
	            producto.setImagen(nombreArchivo);
	            System.out.println("Imagen asignada: " + nombreArchivo);
	        } catch (IOException e) {
	            e.printStackTrace();
	            result.rejectValue("imagen", "error.imagen", "No se pudo guardar la imagen.");
	            return "producto/form";
	        }
	    } else {
	        // Si no se envía imagen nueva
	        if (producto.getId() != null) {  
	            // Se trata de una edición, así que se conserva la imagen existente.
	            Producto productoExistente = productoService.buscarPorId(producto.getId());
	            if (productoExistente != null && productoExistente.getImagen() != null && !productoExistente.getImagen().isEmpty()) {
	                producto.setImagen(productoExistente.getImagen());
	                System.out.println("Sin nueva imagen: se conserva la imagen existente: " + producto.getImagen());
	            } else {
	                producto.setImagen("no-imagen.jpg");
	                System.out.println("Producto en edición sin imagen previa; se asigna imagen por defecto.");
	            }
	        } else {
	            // Si se trata de un producto nuevo y no se envió imagen
	            producto.setImagen("no-imagen.jpg");
	            System.out.println("No se subió imagen. Se asigna imagen por defecto.");
	        }
	    }

	    // Guardamos el producto (insert o update según el id)
	    Producto productoGuardado = productoService.guardarProducto(producto);
	    if (productoGuardado == null || productoGuardado.getId() == null) {
	        System.out.println("ERROR: El producto no se guardó correctamente.");
	        model.addAttribute("error", "No se pudo guardar el producto.");
	        return "producto/form";
	    }
	    System.out.println("Producto guardado con ID: " + productoGuardado.getId());

	    // Manejo del stock (actualización / creación)
	    StockPorTalla stock;
	    Optional<StockPorTalla> optStock = stockPorTallaService.buscarPorProductoId(productoGuardado.getId());
	    if (optStock.isPresent()) {
	        stock = optStock.get();
	        System.out.println("Se encontró registro de stock existente. Actualizando...");
	    } else {
	        stock = new StockPorTalla();
	        stock.setProducto(productoGuardado);
	        System.out.println("No existe stock previo. Creando registro nuevo...");
	    }

	    stock.setXs(stockXs != null ? stockXs : 0);
	    stock.setS(stockS != null ? stockS : 0);
	    stock.setM(stockM != null ? stockM : 0);
	    stock.setL(stockL != null ? stockL : 0);
	    stock.setXl(stockXl != null ? stockXl : 0);
	    stock.setXxl(stockXxl != null ? stockXxl : 0);

	    // Guardamos el registro de stock
	    StockPorTalla stockGuardado = stockPorTallaService.guardarStock(stock);
	    System.out.println("Registro de stock guardado para producto ID: " + productoGuardado.getId());

	    return "redirect:/producto/lista";
	}



	@GetMapping("/editar/{id}")
	public String editarProducto(@PathVariable("id") Integer id, Model model) {
	    Producto producto = productoService.buscarPorId(id);
	    if (producto == null) {
	        model.addAttribute("error", "Producto no existe");
	        return "redirect:/producto/lista";
	    }
	    model.addAttribute("producto", producto);
	    
	    // Cargar el stock asociado y agregar sus valores al modelo.
	    Optional<StockPorTalla> optStock = stockPorTallaService.buscarPorProductoId(producto.getId());
	    StockPorTalla stock = optStock.orElse(new StockPorTalla());
	    
	    model.addAttribute("stockXs", stock.getXs());
	    model.addAttribute("stockS", stock.getS());
	    model.addAttribute("stockM", stock.getM());
	    model.addAttribute("stockL", stock.getL());
	    model.addAttribute("stockXl", stock.getXl());
	    model.addAttribute("stockXxl", stock.getXxl());
	    
	    return "producto/form";
	}

	// Endpoint para eliminar un producto (ten en cuenta la seguridad para que solo
	// el administrador lo realice)
	@GetMapping("/eliminar/{id}")
	public String eliminarProducto(@PathVariable("id") Integer id, Model model) {
		try {
			productoService.eliminarPorId(id);
		} catch (Exception e) {
			model.addAttribute("error", "Error al eliminar el producto");
		}
		return "redirect:/producto/lista";
	}

	// Conversión de fechas para la vinculación de formularios
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
