import java.util.HashMap;
import java.util.Map;

public class Merchant implements Seller {

    public static Map<String, Item> itemsMap = new HashMap<>();

    @Override
    public Item sell(Goods goods) {
        // TODO switch case
        if (goods == Goods.HP_POTION) {
            return itemsMap.get("Health potion");
        } else if (goods == Goods.POWER_RUNE) {
            return itemsMap.get("Power rune");
        } else System.out.println("Кажется у меня такого нет...");
        return null;
    }

    //Энам для товаров
    public enum Goods {
        HP_POTION,
        POWER_RUNE
    }
}
