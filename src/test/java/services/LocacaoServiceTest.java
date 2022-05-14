package services;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static matchers.MatchersProprios.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private LocacaoService service;

    @Before
    public void setup() { // Executa antes de cada teste
        service = new LocacaoService();
    }

    @After
    public void tearDown() { // Executa apos cada teste
    }

    @BeforeClass
    public static void setupClass() { // Executa antes da classe ser instanciada
    }

    @AfterClass
    public static void tearDownClass() { // Executa apos a classe ser destruida
    }


    @Test
    public void deveAlugarFilme() throws Exception {
        // Assuma que este esta não sera executado em um sabado
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificao
        errorCollector.checkThat(locacao.getValor(), is(equalTo(5.0)));
        errorCollector.checkThat(locacao.getDataLocacao(), ehHoje());
        errorCollector.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
    }

    @Test(expected = FilmeSemEstoqueException.class) // Forma elegante
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

        //acao
        service.alugarFilme(usuario, filmes);

    }

    @Test //Forma robusta
    public void naoDeveAlugarFilmeSemUsuario() throws Exception {

        //cenario
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

        //acao
        try {
            service.alugarFilme(null, filmes);
            fail("Deveria gerar uma LocadoraException");
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario vazio"));
        }
    }

    @Test //Forma nova
    public void naoDeveAlugarFilmeSemFilme() throws Exception {

        //cenario
        Usuario usuario = new Usuario("Usuario 1");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //acao
        service.alugarFilme(usuario, null);

    }

    @Test
    public void deveDevolverFilmeNaSegundaAoAlugarNoSabado() throws Exception {
        // Assuma que esse teste sera executado em um sabado
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SUNDAY));
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));
        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);
        //verificacao
        assertThat(resultado.getDataRetorno(), cairNumaSegunda());
    }

}
