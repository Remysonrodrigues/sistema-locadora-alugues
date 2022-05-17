package services;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.daos.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import builders.FilmeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

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
        LocacaoDAO dao = Mockito.mock(LocacaoDAO.class);
        service.setLocacaoDAO(dao);
    }

    @Parameterized.Parameters(name = "{2}")
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][] {
            {
                Arrays.asList(
                    FilmeBuilder.umFilme().agora(),
                    FilmeBuilder.umFilme().agora(),
                    FilmeBuilder.umFilme().agora()
                ),
                11.0,
                "3 Filmes: 25%"
            },
            {
                Arrays.asList(
                    FilmeBuilder.umFilme().agora(),
                    FilmeBuilder.umFilme().agora(),
                    FilmeBuilder.umFilme().agora(),
                    FilmeBuilder.umFilme().agora()
                ),
                13.0,
                "4 Filmes: 50%"
            },
            {
                Arrays.asList(
                    FilmeBuilder.umFilme().agora(),
                    FilmeBuilder.umFilme().agora(),
                    FilmeBuilder.umFilme().agora(),
                    FilmeBuilder.umFilme().agora(),
                    FilmeBuilder.umFilme().agora()
                ),
                14.0,
                "5 Filmes: 75%"
            },
            {
                Arrays.asList(
                    FilmeBuilder.umFilme().agora(),
                    FilmeBuilder.umFilme().agora(),
                    FilmeBuilder.umFilme().agora(),
                    FilmeBuilder.umFilme().agora(),
                    FilmeBuilder.umFilme().agora(),
                    FilmeBuilder.umFilme().agora()
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
