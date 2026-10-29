package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ComboTest
{
    private Combo combo;
    private ProductoMenu hamburguesa;
    private ProductoMenu papas;
    private ProductoMenu bebida;

    @BeforeEach
    void setUp()
    {
        hamburguesa = new ProductoMenu("Hamburguesa", 12000);
        papas = new ProductoMenu("Papas", 5000);
        bebida = new ProductoMenu("Bebida", 3000);

        ArrayList<ProductoMenu> productos = new ArrayList<>();
        productos.add(hamburguesa);
        productos.add(papas);
        productos.add(bebida);

        combo = new Combo("Combo sencillo", 0.07, productos);
    }

    @Test
    void testGetNombre()
    {
        assertEquals("Combo sencillo", combo.getNombre());
    }

    @Test
    void testGetPrecio()
    {
        assertEquals(18600, combo.getPrecio());
    }

    @Test
    void testGenerarTextoFactura()
    {
        String esperado = "Combo Combo sencillo\n"
                + " Descuento: 0.07\n"
                + "            18600\n";

        assertEquals(esperado, combo.generarTextoFactura());
    }

    @Test
    void testSinDescuento()
    {
    	ArrayList<ProductoMenu> productos = new ArrayList<>();
        productos.add(hamburguesa);
        productos.add(papas);
        productos.add(bebida);

        Combo comboSinDescuento = new Combo("Combo sin descuento", 0.0, productos);

        assertEquals(20000, comboSinDescuento.getPrecio());
    }

    @Test
    void testDescuentoCompleto()
    {
    	ArrayList<ProductoMenu> productos = new ArrayList<>();
        productos.add(hamburguesa);
        productos.add(papas);
        productos.add(bebida);

        Combo comboGratis = new Combo("Combo gratis", 1.0, productos);

        assertEquals(0, comboGratis.getPrecio());
    }
}