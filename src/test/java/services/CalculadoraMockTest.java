package services;

import br.ce.wcaquino.servicos.Calculadora;
import br.ce.wcaquino.servicos.EmailService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class CalculadoraMockTest {

    @Mock
    private Calculadora calcMock;

    @Spy // Spy funciona apenas com classes concretas
    private Calculadora calcSpy;

//    @Spy // Spy funciona apenas com classes concretas
//    private EmailService email;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void devoMostrarDiferencaEntreMockSpy() {
        // Comportamento de um Mock
        Mockito.when(calcMock.somar(1, 2)).thenReturn(8);
        //System.out.println(calcMock.somar(1, 3)); // = 0 - Valor padrão retornado por mock
        // Comportamento de um Spy
        Mockito.when(calcSpy.somar(1, 2)).thenReturn(8);
        //Mockito.doReturn(8).when(calcSpy).somar(1, 2); // Não ira executar somar
        //System.out.println(calcSpy.somar(1, 3)); // = 4 - Retorna a execução real do metodo como padrão
        // Fazendo um Mock chamar a implementação real do metodo
        Mockito.when(calcMock.somar(1, 2)).thenCallRealMethod();
        //System.out.println(calcMock.somar(1, 3)); // = 4 - Retorna a execução real do metodo como padrão
        // Comportamento Mock e Spy para metodo void
        //calcMock.imprime(); // Não faz nada
        //calcSpy.imprime(); // Executa o comportamento padrão imprime
        // Para um Spy não executar o metodo
        Mockito.doNothing().when(calcSpy).imprime();
        //calcSpy.imprime(); // Não faz nada

    }

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
