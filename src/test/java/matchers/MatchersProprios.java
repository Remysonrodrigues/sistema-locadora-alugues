package matchers;

import java.util.Calendar;

public class MatchersProprios {

    public static DiaSemanaMatcher cairEm(Integer diaSemana) {
        return new DiaSemanaMatcher(diaSemana);
    }

    public static DiaSemanaMatcher cairNumaSegunda() {
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }

    public static DataDiferencaDiasMatchers ehHojeComDiferencaDias(Integer qtdDias) {
        return new DataDiferencaDiasMatchers(qtdDias);
    }

    public static DataDiferencaDiasMatchers ehHoje() {
        return new DataDiferencaDiasMatchers(0);
    }
}
