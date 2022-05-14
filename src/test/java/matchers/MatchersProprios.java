package matchers;

import java.util.Calendar;

public class MatchersProprios {

    public static DiaSemanaMatcher cairEm(Integer diaSemana) {
        return new DiaSemanaMatcher(diaSemana);
    }

    public static DiaSemanaMatcher cairNumaSegunda() {
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }

}
