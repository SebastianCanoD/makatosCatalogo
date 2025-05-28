package makatos.catalogo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import makatos.catalogo.model.Producto;
import makatos.catalogo.model.TipoProducto;
import makatos.catalogo.repository.ProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private ProductoRepository productoRepository;

	@Override
	public List<Producto> buscarTodos() {
		return productoRepository.findAll();
	}

	@Override
	public Producto buscarPorId(Integer id) {
		Optional<Producto> optProducto = productoRepository.findById(id);
		return optProducto.orElse(null);
	}

	@Override
	public Producto guardarProducto(Producto producto) {
		return productoRepository.save(producto);
	}

	@Override
	public void eliminarPorId(Integer id) {
		productoRepository.deleteById(id);
	}

	@Override
	public List<Producto> buscarPorNombre(String nombre) {
		return productoRepository.findByNombreContaining(nombre);
	}

	@Override
	public List<Producto> buscarPorTipo(TipoProducto tipo) {
		return productoRepository.findByTipo(tipo);
	}

	@Override
	public List<Producto> buscarProductosMayoreoEnOferta() {
		// Se asume que en la base de datos, 'oferta' tiene valor 1 cuando el producto
		// está en oferta.
		return productoRepository.findByTipoAndOferta(TipoProducto.mayoreo, true);
	}

	@Override
	public List<Producto> buscarProductosMenudeoEnOferta() {
		return productoRepository.findByTipoAndOferta(TipoProducto.menudeo, true);
	}

	@Override
	public List<Producto> buscarPorFiltro(String tipo, Double precioMax, String nombre) {
		// Traer todos los productos (o podrías crear una consulta personalizada si la
		// tabla es grande)
		List<Producto> todos = productoRepository.findAll();
		return todos.stream().filter(p -> {
			boolean matches = true;
			// Filtrar por tipo, si se especifica
			if (tipo != null && !tipo.isEmpty()) {
				// Se compara ignorando mayúsculas a partir del valor (suponiendo que el enum
				// TipoProducto se define en minúsculas: 'menudeo' y 'mayoreo')
				matches = matches && p.getTipo().toString().equalsIgnoreCase(tipo);
			}
			// Filtrar por precio máximo, si se especifica
			if (precioMax != null) {
				matches = matches && p.getPrecio().doubleValue() <= precioMax;
			}
			// Filtrar por nombre (parcial), si se especifica
			if (nombre != null && !nombre.isEmpty()) {
				matches = matches && p.getNombre().toLowerCase().contains(nombre.toLowerCase());
			}
			return matches;
		}).collect(Collectors.toList());
	}
}
