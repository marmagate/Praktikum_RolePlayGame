public class Item {

    private final String name;
    private final int price;
    private final boolean usable;

    public Item(String name, int price, boolean usable) {
        this.name = name;
        this.price = price;
        this.usable = usable;
    }

    public boolean isUsable() {
        return usable;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
