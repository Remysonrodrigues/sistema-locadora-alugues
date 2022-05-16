package suites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.*;
import services.CalculadoraTest;
import services.CalculoValorLocacaoTest;
import services.LocacaoServiceTest;

@RunWith(Suite.class)
@SuiteClasses({
    CalculadoraTest.class,
    CalculoValorLocacaoTest.class,
    LocacaoServiceTest.class
})
public class SuiteExecucao { // Executa uma suite de testes

    @BeforeClass
    public static void before() {}

    @AfterClass
    public static void after() {}

}
