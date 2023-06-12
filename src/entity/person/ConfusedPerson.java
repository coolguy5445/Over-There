package entity.person;

import game.ZoneTools;

public class ConfusedPerson extends Person {
    public ConfusedPerson(int x, int y) {
        super(x, y, Person.CONFUSED_COLOUR);
    }

    @Override
    public boolean look(int looking) {
        looking += 4;
        if (looking > 7) {
            looking -= 8;
        }
        return super.look(looking);
    }

    @Override
    public boolean spotted(int looking) {
        int zone = ZoneTools.getZone(x, y);
        return zone == looking;
    }
}
