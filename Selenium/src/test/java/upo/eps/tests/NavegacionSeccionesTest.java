package upo.eps.tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.Actions; // Se mantiene solo por las interacciones originales
import static org.testng.Assert.assertTrue; // Usamos la aserción de TestNG
import java.time.Duration;

/**
 * Clase de prueba que simula un recorrido secuencial a través de varias
 * secciones del menú principal y subsecciones de la Escuela Politécnica Superior (EPS) de la UPO.
 * Cumple con la nomenclatura PascalCase y utiliza TestNG para la gestión del ciclo de vida.
 */
public class NavegacionSeccionesTest {
    
    // WebDriver declarado como static para usar en @BeforeClass y @AfterClass (eficiencia)
    private static WebDriver driver;
    // Se elimina la variable 'vars' (Map) y 'js' (JavascriptExecutor) por ser innecesarias.

    /**
     * Configuración inicial antes de que se ejecute *cualquier* prueba en esta clase (@BeforeClass).
     * Inicializa el WebDriver (Firefox).
     */
    @BeforeClass
    static void setUp() { // Método estático
        driver = new FirefoxDriver();
        // Se establece una espera implícita estándar como buena práctica
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    /**
     * Limpieza después de que *todos* los métodos de prueba de esta clase han finalizado (@AfterClass).
     * Cierra la instancia del navegador (WebDriver).
     */
    @AfterClass
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Verifica el recorrido secuencial de clics a través de las diferentes secciones del menú de la EPS.
     */
    @Test
    void testRecorridoCompletoMenu() {
        
        // 1. Navegación inicial y configuración
        driver.get("https://www.upo.es/escuela-politecnica-superior/es/");
        driver.manage().window().setSize(new Dimension(1936, 1048));
        
        // 2. Secuencia de navegación
        
        // El Centro -> Mensaje de la Dirección
        driver.findElement(By.cssSelector(".mainnav-lvl1:nth-child(1) > .mainnav-lvl1 > .section-text")).click();
        driver.findElement(By.linkText("Mensaje de la Dirección de la EPS")).click();
        
        // Estudios -> Acceso
        driver.findElement(By.cssSelector(".mainnav-lvl1:nth-child(2) > .mainnav-lvl1 .fa")).click();
        driver.findElement(By.linkText("Acceso")).click();
        
        // Estudiantes
        driver.findElement(By.linkText("Estudiantes")).click();
        
        // Investigación
        driver.findElement(By.cssSelector(".mainnav-lvl1:nth-child(4) > .mainnav-lvl1 .fa")).click();
        
        // Empleo
        driver.findElement(By.linkText("Empleo")).click();
        
        // Calidad -> Informes de Seguimiento
        driver.findElement(By.cssSelector(".mainnav-lvl1:nth-child(6) .section-text")).click();
        driver.findElement(By.linkText("Informes de Seguimiento y Planes de Mejora del GIISI")).click();
        
        // Igualdad
        driver.findElement(By.linkText("Igualdad")).click();
        
        // Noticias -> Eventos
        driver.findElement(By.linkText("Noticias")).click();
        driver.findElement(By.cssSelector(".active:nth-child(1) > a")).click();
        driver.findElement(By.linkText("Eventos")).click();
        
        // Agenda
        // Se elimina el 'moveToElement' innecesario previo al clic
        driver.findElement(By.cssSelector(".mainnav-lvl1:nth-child(9) > .mainnav-lvl1")).click();
        
        // Contacto -> Secretaría -> Información
        driver.findElement(By.cssSelector("#menuleft-heading-3 a")).click();
        driver.findElement(By.cssSelector("#menuleft-heading-4 a")).click();
        
        // Volver a la portada usando el logo
        driver.findElement(By.cssSelector(".navbar-brand > .img-responsive")).click();

        // Se eliminan los 'moveToElement' finales ya que son comandos residuales innecesarios
        // y no añaden valor al test.
        
        // 3. Aserción clara para validar el resultado
        // Se verifica que, tras el recorrido y el clic en el logo, la página haya vuelto al inicio.
        assertTrue(driver.getCurrentUrl().endsWith("/es/"), 
                   "ERROR: La URL final no corresponde a la página principal de la EPS.");
    }
}