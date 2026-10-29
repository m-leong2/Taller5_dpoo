package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoAjustadoTest
{
    private ProductoMenu productoBase;
    private ProductoAjustado productoAjustado;
    private Ingrediente tomate;
    private Ingrediente queso;

    @BeforeEach
    void setUp()
    {
        productoBase = new ProductoMenu("Hamburguesa sencilla", 12000);
        productoAjustado = new ProductoAjustado(productoBase);

        tomate = new Ingrediente("Tomate", 1000);
        queso = new Ingrediente("Queso", 2000);
    }

    @Test
    void testGetNombre()
    {
        assertEquals("Hamburguesa sencilla", productoAjustado.getNombre(),
                "El nombre del producto ajustado no es el esperado.");
    }

    @Test
    void testGetPrecio()
    {
        assertEquals(12000, productoAjustado.getPrecio(),
                "El precio inicial debe ser igual al precio base.");
    }

	@Test
	void testPrecioConIngredienteAgregado()
	{
	    productoAjustado.agregarIngrediente(tomate);
	
	    assertEquals(13000, productoAjustado.getPrecio(),
	            "El precio debe incluir el costo del ingrediente agregado.");
	}


    @Test
    void testGenerarTextoFactura()
    {
        String esperado = "Hamburguesa sencilla\n"
                + "            12000\n";

        assertEquals(esperado, productoAjustado.generarTextoFactura(),
                "El texto de la factura no es el esperado.");
    }
    
    @Test
    void testPrecioConVariosIngredientesAgregados()
    {
        productoAjustado.agregarIngrediente(tomate);
        productoAjustado.agregarIngrediente(queso);

        assertEquals(15000, productoAjustado.getPrecio());
    }


    @Test
    void testEliminarIngredienteNoReducePrecio()
    {
        productoAjustado.agregarIngrediente(tomate);
        assertEquals(13000, productoAjustado.getPrecio());

        productoAjustado.eliminarIngrediente(tomate);

        assertEquals(13000, productoAjustado.getPrecio());
        assertTrue(productoAjustado.generarTextoFactura().contains("-Tomate"));
    }
    
    @Test
    void testFacturaConIngredientesAgregados()
    {
        productoAjustado.agregarIngrediente(tomate);
        productoAjustado.agregarIngrediente(queso);

        String factura = productoAjustado.generarTextoFactura();

        assertTrue(factura.contains("+Tomate"));
        assertTrue(factura.contains("1000"));
        assertTrue(factura.contains("+Queso"));
        assertTrue(factura.contains("2000"));
        assertTrue(factura.contains("15000"));
    }
}
