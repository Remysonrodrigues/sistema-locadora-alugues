package services;

import br.ce.wcaquino.servicos.Calculadora;
import org.junit.Test;
import org.mockito.Mockito;

public class CalculadoraMockTest {

    @Test
    public void test() {
        Calculadora calc = Mockito.mock(Calculadora.class);
        // Se utilizar um match (Mockito.anyInt()) todos os outros params devem ser match
        Mockito.when(calc.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);
        calc.somar(1, 8);
    }

}
