package game;

import display.Screen;
import entity.person.Person;

public class ZoneTools {

    public static int[] getZoneBounds(int zone) {
        int[] bounds = new int[4];

        if (zone == Person.LOOK_NEUTRAL) {
            for (int i = 0; i < 4; ++i) bounds[i] = -Screen.WIDTH;
            return bounds;
        }

            // Get X values
        if (zone == Person.NORTH_WEST || zone == Person.WEST || zone == Person.SOUTH_WEST) {
            bounds[0] = Person.PERSON_SIZE;
            bounds[1] = Screen.WIDTH / 3 - Person.PERSON_SIZE;
        }
        else if (zone == Person.NORTH || zone == Person.SOUTH) {
            bounds[0] = Screen.WIDTH / 3 + Person.PERSON_SIZE;
            bounds[1] = Screen.WIDTH / 3 * 2 - Person.PERSON_SIZE;
        }
        else {
            bounds[0] = Screen.WIDTH / 3 * 2 + Person.PERSON_SIZE;
            bounds[1] = Screen.WIDTH - Person.PERSON_SIZE;
        }

            // Get Y values
        if (zone == Person.NORTH_WEST || zone == Person.NORTH || zone == Person.NORTH_EAST) {
            bounds[2] = Person.PERSON_SIZE;
            bounds[3] = Screen.HEIGHT / 3 - Person.PERSON_SIZE;
        }
        else if (zone == Person.EAST || zone == Person.WEST) {
            bounds[2] = Screen.HEIGHT / 3 + Person.PERSON_SIZE;
            bounds[3] = Screen.HEIGHT / 3 * 2 - Person.PERSON_SIZE;
        }
        else {
            bounds[2] = Screen.HEIGHT / 3 * 2 + Person.PERSON_SIZE;
            bounds[3] = Screen.HEIGHT - Person.PERSON_SIZE;
        }

        return bounds; 
    }

    public static int getZone(int x, int y) {
        if (x < Screen.WIDTH / 3) {
            if (y < Screen.HEIGHT / 3) {
                return Person.NORTH_WEST;
            }
            else if (y < Screen.HEIGHT / 3 * 2) {
                return Person.WEST;
            }
            else {
                return Person.SOUTH_WEST;
            }
        }
        else if (x < Screen.WIDTH / 3 * 2) {
            if (y < Screen.HEIGHT / 3) {
                return Person.NORTH;
            }
            else if (y < Screen.HEIGHT / 3 * 2) {
                return -1;
            }
            else {
                return Person.SOUTH;
            }
        }
        else {
            if (y < Screen.HEIGHT / 3) {
                return Person.NORTH_EAST;
            }
            else if (y < Screen.HEIGHT / 3 * 2) {
                return Person.EAST;
            }
            else {
                return Person.SOUTH_EAST;
            }
        }
    }
}
