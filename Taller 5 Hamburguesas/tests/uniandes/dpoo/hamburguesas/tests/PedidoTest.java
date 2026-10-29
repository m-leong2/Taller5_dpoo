package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class PedidoTest
{
    private Pedido pedido;
    private ProductoMenu hamburguesa;
    private ProductoMenu papas;

    @BeforeEach
    void setUp()
    {
        pedido = new Pedido("Ana", "Calle 123");
        hamburguesa = new ProductoMenu("Hamburguesa", 12000);
        papas = new ProductoMenu("Papas", 5000);
    }

    @Test
    void testGetIdPedido()
    {
        assertTrue(pedido.getIdPedido() >= 0);
    }

    @Test
    void testGetNombreCliente()
    {
        assertEquals("Ana", pedido.getNombreCliente());
    }

    @Test
    void testAgregarProducto()
    {
        pedido.agregarProducto(hamburguesa);

        assertTrue(pedido.generarTextoFactura().contains("Hamburguesa"));
        assertEquals(14280, pedido.getPrecioTotalPedido());
    }

    @Test
    void testGetPrecioTotalPedido()
    {
        pedido.agregarProducto(hamburguesa);
        pedido.agregarProducto(papas);

        assertEquals(20230, pedido.getPrecioTotalPedido());
    }

    @Test
    void testPedidoVacio()
    {
        assertEquals(0, pedido.getPrecioTotalPedido());
    }

    @Test
    void testGenerarTextoFactura()
    {
        pedido.agregarProducto(hamburguesa);

        String esperado = "Cliente: Ana\n"
                + "Dirección: Calle 123\n"
                + "----------------\n"
                + "Hamburguesa\n"
                + "            12000\n"
                + "----------------\n"
                + "Precio Neto:  12000\n"
                + "IVA:          2280\n"
                + "Precio Total: 14280\n";

        assertEquals(esperado, pedido.generarTextoFactura());
    }
    @Test
    void testGuardarFactura() throws Exception
    {
        File archivo = File.createTempFile("factura_test_", ".txt");

        try
        {
            pedido.agregarProducto(hamburguesa);
            pedido.guardarFactura(archivo);

            String contenido = Files.readString(archivo.toPath());

            assertEquals(pedido.generarTextoFactura(), contenido);
        }
        finally
        {
            archivo.delete();
        }
    }

    @Test
    void testGuardarFacturaArchivoInvalido()
    {
        File archivo = new File("carpeta_que_no_existe/factura.txt");

        assertThrows(FileNotFoundException.class,
                () -> pedido.guardarFactura(archivo));
    }
}