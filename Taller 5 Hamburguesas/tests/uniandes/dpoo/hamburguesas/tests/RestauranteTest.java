package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class RestauranteTest
{
    private Restaurante restaurante;

    @BeforeEach
    void setUp()
    {
        restaurante = new Restaurante();
    }

    @Test
    void testGetPedidoEnCursoInicial()
    {
        assertNull(restaurante.getPedidoEnCurso());
    }

    @Test
    void testGetPedidos()
    {
        assertNotNull(restaurante.getPedidos());
        assertTrue(restaurante.getPedidos().isEmpty());
    }

    @Test
    void testGetMenuBase()
    {
        assertNotNull(restaurante.getMenuBase());
        assertTrue(restaurante.getMenuBase().isEmpty());
    }

    @Test
    void testGetMenuCombos()
    {
        assertNotNull(restaurante.getMenuCombos());
        assertTrue(restaurante.getMenuCombos().isEmpty());
    }

    @Test
    void testGetIngredientes()
    {
        assertNotNull(restaurante.getIngredientes());
        assertTrue(restaurante.getIngredientes().isEmpty());
    }
    @Test
    void testIniciarPedido() throws Exception
    {
        restaurante.iniciarPedido("Ana", "Calle 123");

        assertNotNull(restaurante.getPedidoEnCurso());
        assertEquals("Ana", restaurante.getPedidoEnCurso().getNombreCliente());
    }

    @Test
    void testIniciarPedidoCuandoYaExisteUno()
    {
        assertDoesNotThrow(() ->
                restaurante.iniciarPedido("Ana", "Calle 123"));

        assertThrows(YaHayUnPedidoEnCursoException.class,
                () -> restaurante.iniciarPedido("Luis", "Calle 456"));
    }

    @Test
    void testCerrarPedidoSinPedidoEnCurso()
    {
        assertThrows(NoHayPedidoEnCursoException.class,
                () -> restaurante.cerrarYGuardarPedido());
    }

    @Test
    void testCerrarYGuardarPedido() throws Exception
    {
        restaurante.iniciarPedido("Ana", "Calle 123");

        File carpeta = new File("./facturas");
        carpeta.mkdirs();

        File factura = new File("./facturas/factura_"
                + restaurante.getPedidoEnCurso().getIdPedido() + ".txt");

        try
        {
            restaurante.cerrarYGuardarPedido();

            assertNull(restaurante.getPedidoEnCurso());
            assertTrue(factura.exists());

            String contenido = java.nio.file.Files.readString(factura.toPath());
            assertTrue(contenido.contains("Cliente: Ana"));
            assertTrue(contenido.contains("Dirección: Calle 123"));
        }
        finally
        {
            factura.delete();
        }
    }
    private File crearArchivoTemporal(String contenido) throws IOException
    {
        File archivo = File.createTempFile("restaurante_test_", ".txt");

        try (FileWriter escritor = new FileWriter(archivo))
        {
            escritor.write(contenido);
        }

        return archivo;
    }

    @Test
    void testCargarInformacionCorrectamente() throws Exception
    {
        File ingredientes = crearArchivoTemporal("Tomate;1000\nQueso;2000\n");
        File menu = crearArchivoTemporal("Hamburguesa;12000\nPapas;5000\n");
        File combos = crearArchivoTemporal("Combo sencillo;10%;Hamburguesa;Papas\n");

        try
        {
            restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);

            assertEquals(2, restaurante.getIngredientes().size());
            assertEquals(2, restaurante.getMenuBase().size());
            assertEquals(1, restaurante.getMenuCombos().size());
        }
        finally
        {
            ingredientes.delete();
            menu.delete();
            combos.delete();
        }
    }

    @Test
    void testIngredienteRepetido() throws Exception
    {
        File ingredientes = crearArchivoTemporal("Tomate;1000\nTomate;1500\n");
        File menu = crearArchivoTemporal("");
        File combos = crearArchivoTemporal("");

        try
        {
            assertThrows(IngredienteRepetidoException.class,
                    () -> restaurante.cargarInformacionRestaurante(
                            ingredientes, menu, combos));
        }
        finally
        {
            ingredientes.delete();
            menu.delete();
            combos.delete();
        }
    }

    @Test
    void testProductoRepetido() throws Exception
    {
        File ingredientes = crearArchivoTemporal("");
        File menu = crearArchivoTemporal("Hamburguesa;12000\nHamburguesa;15000\n");
        File combos = crearArchivoTemporal("");

        try
        {
            assertThrows(ProductoRepetidoException.class,
                    () -> restaurante.cargarInformacionRestaurante(
                            ingredientes, menu, combos));
        }
        finally
        {
            ingredientes.delete();
            menu.delete();
            combos.delete();
        }
    }

    @Test
    void testProductoFaltanteEnCombo() throws Exception
    {
        File ingredientes = crearArchivoTemporal("");
        File menu = crearArchivoTemporal("Hamburguesa;12000\n");
        File combos = crearArchivoTemporal("Combo sencillo;10%;Producto inexistente\n");

        try
        {
            assertThrows(ProductoFaltanteException.class,
                    () -> restaurante.cargarInformacionRestaurante(
                            ingredientes, menu, combos));
        }
        finally
        {
            ingredientes.delete();
            menu.delete();
            combos.delete();
        }
    }
    @Test
    void testPedidoCerradoSeGuardaEnHistorial() throws Exception
    {
        restaurante.iniciarPedido("Ana", "Calle 123");

        int id = restaurante.getPedidoEnCurso().getIdPedido();

        new File("./facturas").mkdirs();

        restaurante.cerrarYGuardarPedido();

        assertEquals(1, restaurante.getPedidos().size());
        assertNotNull(restaurante.buscarPedido(id));
        assertEquals("Ana", restaurante.buscarPedido(id).getNombreCliente());

        new File("./facturas/factura_" + id + ".txt").delete();
    }

    @Test
    void testBuscarPedidoInexistente()
    {
        assertNull(restaurante.buscarPedido(-1));
    }
}
