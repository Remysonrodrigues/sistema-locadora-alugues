package services;

import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.servicos.Calculadora;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class CalculadoraMockTest {

    @Test
    public void test() {
        Calculadora calc = Mockito.mock(Calculadora.class);
        // Se utilizar um match (Mockito.anyInt()) todos os outros params devem ser match
        ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class); // Captura o argumento não visivel
        Mockito.when(calc.somar(argCapt.capture(), Mockito.anyInt())).thenReturn(5);
        calc.somar(1, 8);
        System.out.println(argCapt.getAllValues()); // Retorna todos os valores capturados
    }

}
