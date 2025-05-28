package makatos.catalogo.service;

import java.util.List;
import makatos.catalogo.model.Producto;
import makatos.catalogo.model.TipoProducto;

public interface ProductoService {
    List<Producto> buscarTodos();
    Producto buscarPorId(Integer id);
    Producto guardarProducto(Producto producto);
    void eliminarPorId(Integer id);
    List<Producto> buscarPorNombre(String nombre);
    List<Producto> buscarPorTipo(TipoProducto tipo);
    // Nuevos métodos para el home: obtener productos en oferta según tipo
    List<Producto> buscarProductosMayoreoEnOferta();
    List<Producto> buscarProductosMenudeoEnOferta();
    List<Producto> buscarPorFiltro(String tipo, Double precioMax, String nombre);
}
