package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest
{
    private ProductoMenu producto1;

    @BeforeEach
    void setUp()
    {
        producto1 = new ProductoMenu("Hamburguesa sencilla", 12000);
    }

    @Test
    void testGetNombre()
    {
        assertEquals("Hamburguesa sencilla", producto1.getNombre(),
                "El nombre del producto no es el esperado.");
    }

    @Test
    void testGetPrecio()
    {
        assertEquals(12000, producto1.getPrecio(),
                "El precio del producto no es el esperado.");
    }

    @Test
    void testGenerarTextoFactura()
    {
        String esperado = "Hamburguesa sencilla\n"
                + "            12000\n";

        assertEquals(esperado, producto1.generarTextoFactura(),
                "El texto de la factura no es el esperado.");
    }
}

