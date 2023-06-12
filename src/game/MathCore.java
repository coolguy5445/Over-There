package game;

public class MathCore {
    public static int[] getPoint(double[] start, double angle, double length) {
        while (angle < 0) {
            angle += 360;
        }

        while (angle > 360) {
            angle -= 360;
        }

        int result[] = new int[2];

        if (angle == 0) {
            result[0] = (int) Math.ceil(start[0]);
            result[1] = (int) Math.ceil(start[1] - length);
        }
        else if (angle == 90) {
            result[0] = (int) Math.ceil(start[0] + length);
            result[1] = (int) Math.ceil(start[1]);
        }
        else if (angle == 180) {
            result[0] = (int) Math.ceil(start[0]);
            result[1] = (int) Math.ceil(start[1] + length);
        }
        else if (angle == 270) {
            result[0] = (int) Math.ceil(start[0] - length);
            result[1] = (int) Math.ceil(start[1]);
        }
        else {
            double triangleAngle;
            int xMod = 1;
            int yMod = 1;

            if (angle < 90) {
                yMod = -1;
                triangleAngle = 90 - angle;
            }
            else if (angle < 180) {
                triangleAngle = angle - 90;
            }
            else if (angle < 270) {
                xMod = -1;
                triangleAngle = 270 - angle;
            }
            else {
                xMod = -1;
                yMod = -1;
                triangleAngle = angle - 270;
            }

            xMod = -xMod;
            yMod = -yMod;

            triangleAngle = Math.toRadians(triangleAngle);
            double xDiff = length * Math.cos(triangleAngle) * xMod;
            double yDiff = length * Math.sin(triangleAngle) * yMod;

            result[0] = (int) Math.ceil(start[0] + xDiff);
            result[1] = (int) Math.ceil(start[1] + yDiff);
        }

        return result;
    }
}
