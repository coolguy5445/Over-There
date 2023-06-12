package entity.person;

public class DistractedPerson extends Person {
    public DistractedPerson(int x, int y) {
        super(x, y, Person.DISTRACTED_COLOUR);
    }

    public DistractedPerson(int x, int y, int angle) {
        super(x, y, Person.DISTRACTED_COLOUR, angle);
    }

    @Override
    public boolean look(int looking) {
        return true;
    }

    @Override
    public boolean spotted(int looking) {
        return false;
    }
}
