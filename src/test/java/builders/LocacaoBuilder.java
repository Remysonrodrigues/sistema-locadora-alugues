package builders;

import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

import java.util.Arrays;
import java.util.Date;

public class LocacaoBuilder {

    private Locacao elemento;

    private LocacaoBuilder() {}

    public static LocacaoBuilder umLocacao() {
        LocacaoBuilder builder = new LocacaoBuilder();
        inicializarDadosPadroes(builder);
        return builder;
    }

    private static void inicializarDadosPadroes(LocacaoBuilder builder) {
        builder.elemento = new Locacao();
        Locacao elemento = builder.elemento;

        elemento.setUsuario(UsuarioBuilder.umUsuario().agora());
        elemento.setFilmes(Arrays.asList(FilmeBuilder.umFilme().agora()));
        elemento.setDataLocacao(new Date());
        elemento.setDataRetorno(DataUtils.obterDataComDiferencaDias(1));
        elemento.setValor(4.0);
    }

    public Locacao agora() {
        return elemento;
    }

    public LocacaoBuilder comUsuario(Usuario usuario) {
        elemento.setUsuario(usuario);
        return this;
    }

    public LocacaoBuilder atrasado() {
        elemento.setDataLocacao(DataUtils.obterDataComDiferencaDias(-4));
        elemento.setDataRetorno(DataUtils.obterDataComDiferencaDias(-2));
        return this;
    }

}
