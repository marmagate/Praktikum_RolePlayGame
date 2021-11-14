public abstract class FantasyCharacter implements Fighter {
    //Имя персонажа
    private String name;
    //Статы персонажа
    private int maxHP;
    private int maxXP;
    private int lvl;
    private int healthPoints;
    private int strength;
    private int dexterity;
    //Опыт и золото
    private int xp;
    private int gold;

    //Конструктор
    public FantasyCharacter(String name, int healthPoints, int strength, int dexterity, int xp, int gold, int lvl) {
        this.name = name;
        this.healthPoints = healthPoints;
        this.maxHP = healthPoints;
        this.strength = strength;
        this.dexterity = dexterity;
        this.xp = xp;
        this.maxXP = 150;
        this.gold = gold;
        this.lvl = lvl;
    }

    //Метод для ведения боя
    @Override
    public int attack() {
        if (dexterity * 3 > getRandomValue() * 4) {
            return strength * 2;
        } else
            if (dexterity * 3 > getRandomValue()) return strength;
        else return 0;
    }

    //Геттеры и сеттеры
    public int getMaxXP() {
        return maxXP;
    }

    public void setMaxXP(int maxXP) {
        this.maxXP = maxXP;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public String getName() {
        return name;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    private int getRandomValue() {
        return (int) (Math.random() * 100);
    }

    //Переопределяем вывод в консоль, чтобы выводилось имя и очки здоровья
    @Override
    public String toString() {
        return String.format("%s with %d HP", name, healthPoints);
    }
}