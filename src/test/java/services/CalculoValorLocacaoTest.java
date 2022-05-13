package services;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    @Parameterized.Parameter
    public List<Filme> filmes;

    @Parameterized.Parameter(value = 1)
    public Double valorLocacao;

    @Parameterized.Parameter(value = 2)
    public String cenario;

    private LocacaoService service;

    @Before
    public void setup() { // Executa antes de cada teste
        service = new LocacaoService();
    }

    @Parameterized.Parameters(name = "{2}")
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][] {
            {
                Arrays.asList(
                    new Filme("Filme 1", 2, 4.0),
                    new Filme("Filme 2", 2, 4.0),
                    new Filme("Filme 3", 2, 4.0)
                ),
                11.0,
                "3 Filmes: 25%"
            },
            {
                Arrays.asList(
                    new Filme("Filme 1", 2, 4.0),
                    new Filme("Filme 2", 2, 4.0),
                    new Filme("Filme 3", 2, 4.0),
                    new Filme("Filme 4", 2, 4.0)
                ),
                13.0,
                "4 Filmes: 50%"
            },
            {
                Arrays.asList(
                    new Filme("Filme 1", 2, 4.0),
                    new Filme("Filme 2", 2, 4.0),
                    new Filme("Filme 3", 2, 4.0),
                    new Filme("Filme 4", 2, 4.0),
                    new Filme("Filme 5", 2, 4.0)
                ),
                14.0,
                "5 Filmes: 75%"
            },
            {
                Arrays.asList(
                    new Filme("Filme 1", 2, 4.0),
                    new Filme("Filme 2", 2, 4.0),
                    new Filme("Filme 3", 2, 4.0),
                    new Filme("Filme 4", 2, 4.0),
                    new Filme("Filme 5", 2, 4.0),
                    new Filme("Filme 6", 2, 4.0)
                ),
                14.0,
                "6 Filmes: 100%"
            }
        });
    }

    @Test
    public void deveCalcularValorLocacaoConsiderandoDescontos() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);
        //verificacao
        assertThat(resultado.getValor(), is(valorLocacao));
    }

}
