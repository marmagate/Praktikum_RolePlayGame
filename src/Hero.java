public class Hero extends FantasyCharacter {
    // реализуем рюкзак
    static class Backpack {

        private String staff = "";

        void put(String thing) {
            staff += thing + ",";
        }

        public String toString() {
            if (staff.equals("")) {
                return "the backpack is empty";
            }
            return staff.substring(0, staff.length() - 1) + " in the backpack";
        }
    }

    Backpack backpack;

    public Hero(String name, int healthPoints, int strength, int dexterity, int xp, int gold, int lvl) {
        super(name, healthPoints, strength, dexterity, xp, gold, lvl);
        backpack = new Backpack();
    }

    public void take(String thing) {
        backpack.put(thing);
    }
}
