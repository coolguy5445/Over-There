package entity.person;

import java.util.Random;

import display.Image;
import display.Screen;
import entity.Entity;
import game.MathCore;
import game.ZoneTools;

public class Person extends Entity {
    private static Random random = new Random();
    
    public static final byte LOOK_NEUTRAL = -1;
    public static final byte NORTH = 0;
    public static final byte NORTH_EAST = 1;
    public static final byte EAST = 2;
    public static final byte SOUTH_EAST = 3;
    public static final byte SOUTH = 4;
    public static final byte SOUTH_WEST = 5;
    public static final byte WEST = 6;
    public static final byte NORTH_WEST = 7;

    public static final int BASIC_COLOUR = 0xFF999999;
    public static final int DISTRACTED_COLOUR = 0xFF3333DD;
    public static final int CONFUSED_COLOUR = 0xFFFFFF22;
    public static final int INVESTIGATOR_COLOUR = 0xFFFF8888;

    public static final int PERSON_SIZE = 45;
    private static final int IMAGE_SCALE = 1;
    private static final int IMAGE_SIZE = PERSON_SIZE / IMAGE_SCALE;
    protected static final int TURN_SPEED = 10;
    protected static final double POINTING_SPEED = 0.1;

    protected int angle;
    protected double pointage;
    protected int colour;
    protected Image image;
    
    public Person(int x, int y) {
        super(x, y, PERSON_SIZE, PERSON_SIZE);
        this.colour = BASIC_COLOUR;
        this.angle = getRandomAngle();
        this.image = new Image(IMAGE_SIZE, IMAGE_SIZE);
        this.updateImage();
    }

    protected Person(int x, int y, int colour) {
        super(x, y, PERSON_SIZE, PERSON_SIZE);
        this.colour = colour;
        this.angle = getRandomAngle();
        this.image = new Image(IMAGE_SIZE, IMAGE_SIZE);
        this.updateImage();
    }

    protected Person(int x, int y, int colour, int angle) {
        super(x, y, PERSON_SIZE, PERSON_SIZE);
        this.colour = colour;
        this.angle = angle;
        this.image = new Image(IMAGE_SIZE, IMAGE_SIZE);
        this.updateImage();
    }

    private int getRandomAngle() {
        int rand = random.nextInt(0, 324);
        for (int i = 5; i < 360; i += 10) {
            if (rand >= i) rand++;
            else break;
        }
        return rand;
    }

    private void updateImage() {
        int[] imageData = new int[IMAGE_SIZE * IMAGE_SIZE];
        for (int r = 0; r < IMAGE_SIZE; ++r) {
            for (int c = 0; c < IMAGE_SIZE; ++c) {
                double[] centerPos = {
                    c - IMAGE_SIZE / 2,
                    r - IMAGE_SIZE / 2
                };

                double mainCircleEquation = Math.pow(centerPos[0], 2) + Math.pow(centerPos[1], 2);
                double mainTestedValue = Math.pow((IMAGE_SIZE - 10) / 2, 2); 

                final int EYE_SIZE = IMAGE_SIZE / 5;
                final int EYE_DISTANCE = IMAGE_SIZE / 4;
                final int EYE_SPACING = 45;

                int[] eyePos = MathCore.getPoint(centerPos, angle - EYE_SPACING, EYE_DISTANCE);
                double eyeEquation1 = Math.pow(eyePos[0], 2) + Math.pow(eyePos[1], 2);

                eyePos = MathCore.getPoint(centerPos, angle + EYE_SPACING, EYE_DISTANCE);
                double eyeEquation2 = Math.pow(eyePos[0], 2) + Math.pow(eyePos[1], 2);

                double eyeTestedValue = Math.pow(EYE_SIZE / 2, 2);

                if (eyeEquation1 <= eyeTestedValue || eyeEquation2 <= eyeTestedValue) {
                    imageData[c + r * IMAGE_SIZE] = 0xFF000000;
                }
                else if (mainCircleEquation <= mainTestedValue && mainTestedValue - mainCircleEquation  < IMAGE_SIZE) {
                    imageData[c + r * IMAGE_SIZE] = 0xFF000000;
                }
                else if (mainCircleEquation < mainTestedValue) {
                    imageData[c + r * IMAGE_SIZE] = colour;
                }
                else {
                    imageData[c + r * IMAGE_SIZE] = 0xFFFF00FF;
                }
            }
        }
        image.setImageData(imageData);
    }

    private void setPointage(double pointage) {
        this.pointage = pointage;
        if (pointage > 1) pointage = 1;
        if (pointage < 0) pointage = 0;
        this.updateImage();
    }

    public boolean point() {
        this.setPointage(pointage + POINTING_SPEED);
        return pointage == 1;
    }

    public boolean unPoint() {
        this.setPointage(pointage - POINTING_SPEED);
        return pointage == 0;
    }

    public boolean look(int looking) {
        int goal = looking * 45 + 1;
        if (goal == angle) return true;

        double effortClockwise = 0;
        double effortCounterClockwise = 0;
        if (goal < angle) {
            effortCounterClockwise = angle - goal;
            effortClockwise = 360 - effortCounterClockwise;
        }
        else {
            effortClockwise = goal - angle;
            effortCounterClockwise = 360 - effortClockwise;
        }

        if (effortClockwise <= TURN_SPEED || effortCounterClockwise <= TURN_SPEED) {
            this.setAngle(goal);
            return true;
        }
        else if (effortClockwise <= effortCounterClockwise) {
            this.setAngle(angle + TURN_SPEED);
        }
        else {
            this.setAngle(angle - TURN_SPEED);
        }

        return angle == goal;
    }

    public void setAngle(int angle) {
        if (angle < 0) angle += 360;
        if (angle > 360) angle -= 360;
        this.angle = angle;
        this.updateImage();
    }

    public void render(Screen screen) {
        screen.render(x, y, IMAGE_SCALE, image);
    }

    public int getAngle() {
        return angle;
    }

    public void scroll(int distance) {
        this.y += distance;
    }

    public boolean spotted(int looking) {
        int zone = ZoneTools.getZone(x, y) + 4;  
        if (zone > 7) zone -= 8;
        return zone == looking; 
    }
}
