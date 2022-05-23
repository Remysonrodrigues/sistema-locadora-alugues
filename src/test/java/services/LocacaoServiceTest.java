package services;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.servicos.EmailService;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.servicos.SPCService;
import br.ce.wcaquino.utils.DataUtils;
import builders.FilmeBuilder;
import builders.LocacaoBuilder;
import builders.UsuarioBuilder;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static matchers.MatchersProprios.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    @Spy
    private LocacaoService service;

    @Mock
    private LocacaoDAO dao;

    @Mock
    private SPCService spc;

    @Mock
    private EmailService email;

    @Before
    public void setup() { // Executa antes de cada teste
        MockitoAnnotations.initMocks(this); // Injeta os mocks
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
        //Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().comValor(5.0).agora());

        Mockito.doReturn(DataUtils.obterData(13, 5, 2022)).when(service).obterData();

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificao
        errorCollector.checkThat(locacao.getValor(), is(equalTo(5.0)));
//        errorCollector.checkThat(locacao.getDataLocacao(), ehHoje());
//        errorCollector.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
    }

    @Test(expected = FilmeSemEstoqueException.class) // Forma elegante
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {

        //cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilmeSemEstoque().agora());

        //acao
        service.alugarFilme(usuario, filmes);

    }

    @Test //Forma robusta
    public void naoDeveAlugarFilmeSemUsuario() throws Exception {

        //cenario
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

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
        Usuario usuario = UsuarioBuilder.umUsuario().agora();

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //acao
        service.alugarFilme(usuario, null);

    }

    @Test
    public void deveDevolverFilmeNaSegundaAoAlugarNoSabado() throws Exception {
        // Assuma que esse teste sera executado em um sabado
        //Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SUNDAY));
        //cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

        Mockito.doReturn(DataUtils.obterData(14, 5, 2022)).when(service).obterData();

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);
        //verificacao
        Assert.assertThat(resultado.getDataRetorno(), cairNumaSegunda());
    }

    @Test
    public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
        //cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

        Mockito.when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

        //acao
        try {
            service.alugarFilme(usuario, filmes);
        //verificacao
            Assert.fail();
        } catch (Exception e) {
            Assert.assertThat(e.getMessage(), is("Usuário Negativado"));
        }

        Mockito.verify(spc).possuiNegativacao(usuario);
    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasadas() throws Exception {
        //cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario em dia").agora();
        Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Outro atrasado").agora();
        List<Locacao> locacaos = Arrays.asList(
            LocacaoBuilder.umLocacao().comUsuario(usuario).atrasado().agora(),
            LocacaoBuilder.umLocacao().comUsuario(usuario2).agora(),
            LocacaoBuilder.umLocacao().comUsuario(usuario3).atrasado().agora(),
            LocacaoBuilder.umLocacao().comUsuario(usuario3).atrasado().agora()
        );

        Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacaos);

        //acao
        service.notificarAtrasos();
        //verificacao
        Mockito.verify(email, Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));
        Mockito.verify(email).notificarAtraso(usuario);
        Mockito.verify(email, Mockito.times(2)).notificarAtraso(usuario3); // O metodo ocorreu 2 vezes para o usuario3
        Mockito.verify(email, Mockito.never()).notificarAtraso(usuario2); // O metodo nunca aconteceu para usuario2
        Mockito.verifyNoMoreInteractions(email); // Não aconteceu mais chamadas para o metodo
    }

    @Test
    public void deveTratarErroNoSPC() throws Exception {
        //cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

        Mockito.when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastrófica"));

        //verificacao
        exception.expect(LocadoraException.class);
        exception.expectMessage("Problemas com SPC, tente novamente");

        //acao
        service.alugarFilme(usuario, filmes);

    }

    @Test
    public void deveProrrogarUmaLocacao() {
        //cenario
        Locacao locacao = LocacaoBuilder.umLocacao().agora();
        //acao
        service.prorrogarLocacao(locacao, 3);
        //verificacao
        ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class); // Captura o argumento não visivel
        Mockito.verify(dao).salvar(argCapt.capture());
        Locacao locacaoRetornada = argCapt.getValue();

        errorCollector.checkThat(locacaoRetornada.getValor(), is(12.0));
        errorCollector.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
        errorCollector.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(3));
    }

    @Test
    public void deveCalcularValorLocacao() throws Exception {
        //cenario
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
        //acao
        // Testando uma classe privada com Java reflecton
        Class<LocacaoService> clazz = LocacaoService.class;
        Method metodo = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
        metodo.setAccessible(true);
        Double valor = (Double) metodo.invoke(service, filmes);
        //verificacao
        Assert.assertThat(valor, is(4.0));
    }

}
