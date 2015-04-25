package ch.epfl.imhof.painting;

import static ch.epfl.imhof.painting.LineStyle.LineCap;
import static ch.epfl.imhof.painting.LineStyle.LineJoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.Set;

import ch.epfl.imhof.Attributed;

public final class RoadPainterGenerator {
    private RoadPainterGenerator() {
    };

    public static Painter<?> painterForRoads(RoadSpec... specifications) {
      return (map, canvas) -> {
          Painter<?> intBridge;
          Painter<?> coBridge;
          Painter<?> intNorm;
          Painter<?> coNorm;
          Painter<?> tunnel;
          for (RoadSpec spec : specifications){
             tunnel = Painter.line(style) ;
          }
      };
    }

    public static final class RoadSpec {
        private final float wI;
        private final float wC;
        private final Color cI;
        private final Color cC;
        private final Predicate<Attributed<?>> filter;

        public RoadSpec(Predicate<Attributed<?>> filter, float wI, Color cI,
                float wC, Color cC) {
            this.wI = wI;
            this.wC = wC;
            this.cI = cI;
            this.cC = cC;
            this.filter = filter;
        }

        /**
         * @return the wI
         */
        public float getwI() {
            return wI;
        }

        /**
         * @return the wC
         */
        public float getwC() {
            return wC;
        }

        /**
         * @return the cI
         */
        public Color getcI() {
            return cI;
        }

        /**
         * @return the cC
         */
        public Color getcC() {
            return cC;
        }

        /**
         * @return the filter
         */
        public Predicate<Attributed<?>> getFilter() {
            return filter;
        }
    }
}
