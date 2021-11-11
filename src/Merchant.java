public class Merchant implements Seller {

    @Override
    public String sell(Goods goods) {
        String result = "";
        if (goods == Goods.HP_POTION) {
            result = "health_potion";
        }
        return result;
    }

    //Энам для товаров
    public enum Goods {
        HP_POTION
    }
}
