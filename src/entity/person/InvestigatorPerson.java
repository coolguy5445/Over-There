package entity.person;

import game.ZoneTools;

public class InvestigatorPerson extends Person {
    protected static final int MOVE_SPEED = TURN_SPEED * 2;
    protected int looking;

    public InvestigatorPerson(int x, int y, int looking) {
        super(x, y, Person.INVESTIGATOR_COLOUR, looking * 45 + 1);
        this.looking = looking;
    }

    @Override
    public boolean look(int looking) {
        int bounds[] = ZoneTools.getZoneBounds(looking);
        bounds[0] = (bounds[0] + bounds[1]) / 2;
        bounds[2] = (bounds[2] + bounds[3]) / 2;

        if (Math.abs(bounds[0] - this.x) < InvestigatorPerson.MOVE_SPEED) {
            this.x = bounds[0];
        }
        else {
            if (bounds[0] - this.x < 0) {
                this.x -= InvestigatorPerson.MOVE_SPEED;
            }
            else {
                this.x += InvestigatorPerson.MOVE_SPEED;
            }
        }

        if (Math.abs(bounds[2] - this.y) < InvestigatorPerson.MOVE_SPEED) {
            this.y = bounds[2];
        }
        else {
            if (bounds[2] - this.y < 0) {
                this.y -= InvestigatorPerson.MOVE_SPEED;
            }
            else {
                this.y += InvestigatorPerson.MOVE_SPEED;
            }
        }

        return this.x == bounds[0] && this.y == bounds[2];
    }

    @Override
    public boolean spotted(int looking) {
        int zone = ZoneTools.getZone(x, y) + 4;  
        if (zone > 7) zone -= 8;
        return zone == this.looking; 
    }
}
