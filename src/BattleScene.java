public class BattleScene {
    //Метод, который вызывается при начале боя, сюда мы передаем ссылки на нашего героя и монстра, который встал у него на пути
    public void fight(FantasyCharacter hero, FantasyCharacter monster, Realm.FightCallback fightCallback) {
        //Ходы будут идти в отдельном потоке
        Runnable runnable = () -> {
            //Сюда будем записывать, какой сейчас ход по счету
            int turn = 1;
            //Когда бой будет закончен мы
            boolean isFightEnded = false;
            System.out.printf("На вашем пути появляется %s с %d HP, %d золота и %d опыта...%n",
                    monster.getName(),monster.getMaxHP(), monster.getGold(), monster.getXp());
            while (!isFightEnded) {
                System.out.println("----Ход: " + turn + "----");
                //Воины бьют по очереди, поэтому здесь мы описываем логику смены сторон
                if (turn++ % 2 != 0) {
                    isFightEnded = makeHit(monster, hero, fightCallback);
                } else {
                    isFightEnded = makeHit(hero, monster, fightCallback);
                }
                try {
                    //Чтобы бой не проходил за секунду, сделаем имитацию работы, как если бы
                    //у нас была анимация
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //Запускаем новый поток
        Thread thread = new Thread(runnable);
        thread.start();
    }

    //Метод для совершения удара
    private Boolean makeHit(FantasyCharacter defender, FantasyCharacter attacker, Realm.FightCallback fightCallback) {
        //Получаем силу удара
        int hit = attacker.attack();
        //Отнимаем количество урона из здоровья защищающегося
        int defenderHealth = defender.getHealthPoints() - hit;
        //Если атака прошла, выводим в консоль сообщение об этом
        if (hit != 0) {
            System.out.printf("%s Нанес удар в %d единиц!%n", attacker.getName(), hit);
            if (defenderHealth <= 0) {
                System.out.printf("%s прощается с жизнью...%n", defender.getName());
            } else
            System.out.printf("У %s осталось %d единиц здоровья%n", defender.getName(), defenderHealth);
        } else {
            //Если атакующий промахнулся (то есть урон не 0), выводим это сообщение
            System.out.printf("%s промахнулся!%n", attacker.getName());
        }
        if (defenderHealth <= 0 && defender instanceof Hero) {
            //Если здоровье меньше 0 и если защищающейся был героем, то игра заканчивается
            System.out.println("Извините, вы пали в бою...");
            //Вызываем коллбэк, что мы проиграли
            fightCallback.fightLost();
            return true;
        } else if (defenderHealth <= 0) {
            //Если здоровья больше нет и защищающийся – это монстр, то мы забираем от монстра его опыт и золото
            System.out.printf("Враг повержен! Вы получаете %d ед. опыта и %d золота%n", defender.getXp(), defender.getGold());
            attacker.setXp(attacker.getXp() + defender.getXp());
            attacker.setGold(attacker.getGold() + defender.getGold());
            if (attacker.getXp() >= attacker.getMaxXP()) {
                attacker.setLvl(attacker.getLvl() + 1);
                attacker.setXp(attacker.getXp() - attacker.getMaxXP());
                attacker.setMaxXP(attacker.getMaxXP() + attacker.getMaxXP()/2);
                attacker.setMaxHP(attacker.getMaxHP() + 15);
                attacker.setHealthPoints(attacker.getMaxHP());
                attacker.setStrength(attacker.getStrength() + 3);
                attacker.setDexterity(attacker.getDexterity() + 3);
                System.out.printf("%s достиг %d уровня!%n", attacker.getName(), attacker.getLvl());
                Realm.getStats();
            }
            //вызываем коллбэк, что мы победили
            fightCallback.fightWin();
            return true;
        } else {
            //если защищающийся не повержен, то мы устанавливаем ему новый уровень здоровья
            defender.setHealthPoints(defenderHealth);
            return false;
        }
    }
}