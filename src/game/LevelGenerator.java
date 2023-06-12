package game;

import java.util.Random;

import display.Screen;
import entity.person.ConfusedPerson;
import entity.person.DistractedPerson;
import entity.person.InvestigatorPerson;
import entity.person.Person;

public class LevelGenerator {
    private static Random random = new Random();
    private static int level;

    public static void resetLevel() {
        level = 0;
    }

    public static void levelUp() {
        level++;
    }
    
    public static void generateNextLevel() {
        int counts[] = new int[4];
        for (int i = 0; i < counts.length; ++i) counts[i] = 0;

        counts[0] = 1 + (int) (level / 2);
        counts[1] = (int) (level / 3);
        counts[2] = (int) (level / 5);
        counts[3] = (int) (level / 8);

        int safeZone = random.nextInt(0, 8);

            // Add victim
        DistractedPerson person = new DistractedPerson(Screen.WIDTH / 2 - Person.PERSON_SIZE / 2, (int) (Screen.HEIGHT / 2 - Person.PERSON_SIZE - (Screen.HEIGHT + (Screen.HEIGHT % 16)) * 2), 0);
        Game.people.add(person);
        
        LevelGenerator.generateBasicPeople(safeZone, counts[0]);
        LevelGenerator.generateDistractedPeople(counts[1]);
        LevelGenerator.generateConfusedPeople(safeZone, counts[2]);
        LevelGenerator.generateInvestigatorPeople(safeZone, counts[3]);
    }

    private static int[] generatePosition(int[] bounds) {
        int[] pos = new int[2];
        pos[0] = random.nextInt(bounds[0], bounds[1]);
        pos[1] = random.nextInt(bounds[2], bounds[3]) - (Screen.HEIGHT + (Screen.HEIGHT % level)) * 2;
        return pos;
    }

    private static void generateBasicPeople(int safeZone, int count) {
            // Get zone opposite safe zone, no people can spawn here
        safeZone = safeZone + 4;
        if (safeZone >= 8) safeZone -= 8;

            // Get people's zones
        for (int i = 0; i < count; ++i) {
            int zone = random.nextInt(0, 7);
            if (zone >= safeZone) zone += 1;

            int[] bounds = ZoneTools.getZoneBounds(zone);
            int[] pos = LevelGenerator.generatePosition(bounds);

            Person person = new Person(pos[0], pos[1]);
            Game.people.add(person);
        }
    }

    private static void generateDistractedPeople(int count) {
            // Get people's zones
        for (int i = 0; i < count; ++i) {
            int zone = random.nextInt(0, 8);
            
            int[] bounds = ZoneTools.getZoneBounds(zone);
            int[] pos = LevelGenerator.generatePosition(bounds);

            DistractedPerson person = new DistractedPerson(pos[0], pos[1]);
            Game.people.add(person);
        }
    }

    private static void generateConfusedPeople(int safeZone, int count) {
            // Get people's zones
        for (int i = 0; i < count; ++i) {
            int zone = random.nextInt(0, 7);
            if (zone >= safeZone) zone += 1;

            int[] bounds = ZoneTools.getZoneBounds(zone);
            int[] pos = LevelGenerator.generatePosition(bounds);

            ConfusedPerson person = new ConfusedPerson(pos[0], pos[1]);
            Game.people.add(person);
        }
    }

    private static void generateInvestigatorPeople(int safeZone, int count) {
            // Get people's zones
        for (int i = 0; i < count; ++i) {
            int zone = random.nextInt(0, 8);

            int[] bounds = ZoneTools.getZoneBounds(zone);
            int[] pos = LevelGenerator.generatePosition(bounds);

            zone = random.nextInt(0, 7);
            if (zone >= safeZone) {
                zone++;
            }

            InvestigatorPerson person = new InvestigatorPerson(pos[0], pos[1], zone);
            Game.people.add(person);
        }
    }
}
