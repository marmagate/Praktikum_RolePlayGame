import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Hero extends FantasyCharacter {
    // реализуем рюкзак
    static class Backpack {

        Queue<Item> staff = new ConcurrentLinkedQueue<>();

        void put(Item item) {
            staff.add(item);
        }

        public String toString() {
            if (staff.isEmpty()) {
                return "В инвентаре пусто";
            }
            StringBuilder itemNames = new StringBuilder();
            for (Item item : staff) {
                itemNames.append(item.getName()).append(", ");
            }
            return itemNames + "не густо...";
        }
    }

    Backpack backpack;

    public Hero(String name, int healthPoints, int strength, int dexterity, int xp, int gold, int lvl) {
        super(name, healthPoints, strength, dexterity, xp, gold, lvl);
        backpack = new Backpack();
    }

    public void take(Item item) {
        backpack.put(item);
    }

    public void useItem(Item item) {
        for (Item anotherItem : backpack.staff) {
            if (anotherItem.isUsable() && item.getName().equals(anotherItem.getName())) {
                // switch for future items
                switch (anotherItem.getName()) {
                    case "Health potion": {
                        setHealthPoints(Math.min(getHealthPoints() + 50, getMaxHP()));
                        System.out.println("Health potion использован");
                    }
                    break;
                    case "Power rune": {
                        setStrength(getStrength() + 35);
                        System.out.println("МОООООООООООООЩЩЩЩЩЩЩЩЩЩЩЩЩЩь!");
                    }
                    break;
                }
                backpack.staff.remove(anotherItem);
                break;
            } else System.out.println("Не получается это использовать...");
        }
    }
}