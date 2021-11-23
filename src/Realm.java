import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Realm {
    //Класс для чтения введенных строк из консоли
    private static BufferedReader br;
    //Игрок должен храниться на протяжении всей игры
    private static Hero player = null;
    //Класс для битвы можно не создавать каждый раз, а переиспользовать
    private static BattleScene battleScene = null;
    // мерчант
    private static final Merchant merchant = new Merchant();

    public static void main(String[] args) {
        //Инициализируем BufferedReader
        br = new BufferedReader(new InputStreamReader(System.in));
        //Инициализируем класс для боя
        battleScene = new BattleScene();
        //заполняем мап айтемов
        Merchant.itemsMap.put("Health potion", new Item("Health potion", 75, true));
        Merchant.itemsMap.put("Power rune", new Item("Power rune", 1500, true));
        //Первое, что нужно сделать при запуске игры, это создать персонажа, поэтому мы предлагаем ввести его имя
        System.out.println("Введите имя персонажа:");
        //Далее ждем ввод от пользователя
        try {
            command(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Взаимодействия с мерчантом
    public static void merchantAction() {
        System.out.printf("Приветствую %s! Какой товар желаешь приобрести? ('назад', если больше ничего не желаешь)%n", player.getName());
    }

    public static void merchantGoodsScrolling() throws IOException {
        while (true) {
            System.out.println("Выбирай!\n" + Arrays.toString(Merchant.Goods.values()));
            String answer = br.readLine();
            if (answer.equals("назад")) {
                System.out.printf("Приятно иметь с тобой дело, %s%n", player.getName());
                printNavigation();
                command(br.readLine());
                break;
            }
            for (Merchant.Goods good : Merchant.Goods.values()) {
                if (good.name().equals(answer)) {
                    Item itemToSell = merchant.sell(good);
                    int price = itemToSell.getPrice();
                    if (player.getGold() >= price) {
                        player.setGold(Math.max(player.getGold() - price, 0));
                        player.take(itemToSell);
                        System.out.println("Держи " + itemToSell.getName());
                    } else System.out.println("У тебя не хватает монет на это добро...");
                }
            }
        }
    }

    private static void command(String string) throws IOException {
        //Если это первый запуск, то мы должны создать игрока, именем будет служить первая введенная строка из консоли
        if (player == null) {
            player = new Hero(
                    string,
                    100,
                    20,
                    20,
                    0,
                    0,
                    1
            );
            System.out.printf("Спасти наш мир от драконов вызвался %s! Да будет его броня крепка и бицепс кругл!%n", player.getName());
            //Метод для вывода меню
            printNavigation();
        }
        //Варианты для команд
        switch (string) {
            case "1": {
                merchantAction();
                merchantGoodsScrolling();
            }
            break;
            case "2": {
                commitFight();
            }
            break;
            case "3": {
                commitDragonFight();
            }
            break;
            case "4": {
                backpackAction();
            }
            break;
            case "да": {
                command("2");
            }
            break;
            case "нет": {
                printNavigation();
                command(br.readLine());
            }
            break;
            case "99": {
                getStats();
            }
            break;
            case "404": {
                System.exit(1);
            }
            break;
        }
        //Снова ждем команды от пользователя
        command(br.readLine());
    }

    public static void backpackAction() throws IOException {
        System.out.println("Что бы такого использовать... ('назад' - чтобы вернуться)");
        System.out.println(player.backpack);
        String answer = br.readLine();
        switch (answer) {
            case "назад": {
            }
            printNavigation();
            break;
            case "Health potion": {
                player.useItem(Merchant.itemsMap.get("Health potion"));
                backpackAction();
            }
            break;
            case "Power rune": {
                player.useItem(Merchant.itemsMap.get("Power rune"));
                backpackAction();
            }
            break;
            default: {
                System.out.println("Что-то не получилось...");
                backpackAction();
            }
            break;
        }
    }

    public static void getStats() {
        System.out.printf("Текущие характеристики героя:%nHP %d (max %d)%nStrength %d%nDexterity %d%nGold %d%nExp %d%n",
                player.getHealthPoints(), player.getMaxHP(), player.getStrength(), player.getDexterity(), player.getGold(), player.getXp());
    }

    private static void printNavigation() {
        System.out.println("Куда вы хотите пойти?");
        System.out.println("1. К Торговцу");
        System.out.println("2. В темный лес");
        System.out.println("3. Воевать с драконом");
        System.out.println("4. Инвентарь");
        System.out.println("99. Статы");
        System.out.println("404. Выход");
    }

    private static void commitFight() {
        battleScene.fight(player, createMonster(), new FightCallback() {
            @Override
            public void fightWin() {
                System.out.printf("%s победил! Теперь у вас %d (MAX %d) опыта и %d золота, а также осталось %d единиц здоровья.%n",
                        player.getName(), player.getXp(), player.getMaxXP(), player.getGold(), player.getHealthPoints());
                System.out.println("Желаете продолжить поход? (да/нет)");
            }

            @Override
            public void fightLost() {
                player.setGold(player.getGold() / 2);
                player.setXp(player.getXp() / 2);
                player.setHealthPoints(player.getMaxHP() / 2);
                System.out.printf("%s пал в бою. Вы теряете половину всего заработанного опыта и золота, и восстаете из пепла с 50 процентами здоровья%n",
                        player.getName());
                printNavigation();
            }
        });
    }

    private static void commitDragonFight() {
        battleScene.fight(player, createDragon(), new FightCallback() {
            @Override
            public void fightWin() {
                System.out.printf("%s победил! Теперь у вас %d (MAX %d) опыта и %d золота, а также осталось %d единиц здоровья.%n",
                        player.getName(), player.getXp(), player.getMaxXP(), player.getGold(), player.getHealthPoints());
                printNavigation();
            }

            @Override
            public void fightLost() {
                player.setGold(player.getGold() / 2);
                player.setXp(player.getXp() / 2);
                player.setHealthPoints(player.getMaxHP() / 2);
                System.out.printf("%s пал в бою. Вы теряете половину всего заработанного опыта и золота, и восстаете из пепла с 50 процентами здоровья%n",
                        player.getName());
                printNavigation();
            }
        });
    }

    private static FantasyCharacter createDragon() {
        return new Dragon("Дракон", 200, 45, 15, 500, 400, 10);
    }

    private static FantasyCharacter createMonster() {
        //Рандомайзер
        int random = (int) (Math.random() * 10);
        //С вероятностью 50% создается или скелет, или гоблин
        if (random % 2 == 0) return new Goblin(
                "Гоблин",
                50,
                10,
                10,
                110,
                20,
                4
        );
        else return new Skeleton(
                "Скелет",
                25,
                20,
                20,
                100,
                10,
                1
        );
    }

    interface FightCallback {

        void fightWin();

        void fightLost();
    }
}
