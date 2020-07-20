package engine.gfx.engine3d.orthographic;

public enum Face {

    FLOOR {
        @Override
        public String toString() {
            return "0";
        }
    },
    NORTH {
        @Override
        public String toString() {
            return "1";
        }
    },
    EAST {
        @Override
        public String toString() {
            return "2";
        }
    },
    SOUTH {
        @Override
        public String toString() {
            return "3";
        }
    },
    WEST {
        @Override
        public String toString() {
            return "4";
        }
    },
    TOP {
        @Override
        public String toString() {
            return "5";
        }
    }

}
