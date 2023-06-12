package display;

import game.ZoneTools;

public class Screen extends Image {
    public static int WIDTH = 600;
    public static int HEIGHT = 600;

    private static final double GRID_OPACTIY = 0.2;

    public Screen() {
        super(Screen.WIDTH, Screen.HEIGHT);
    }

    public void clear() {
        for (int i = 0; i < pixels.length; ++i) {
            pixels[i] = 0xFF000000;
        }
    }

    public void fillRect(int x, int y, int w, int h, int colour) {
        for (int r = y; r < y + h; ++r) {
            for (int c = x; c < x + w; ++c) {
                pixels[c + r * Screen.WIDTH] = colour;
            }
        }
    }

    public void render(int x, int y, Image image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int[] imageData = image.getImageData();
        for (int r = y; r < y + imageHeight; ++r) {
            if (r < 0) {
                r = -1;
                continue;
            } 
            if (r >= Screen.HEIGHT) {
                break;
            }
            for (int c = x; c < x + imageWidth; ++c) {
                if (c < 0) {
                    c = -1;
                    continue;
                }
                if (c >= Screen.WIDTH) {
                    break;
                }
                int pixelColour = imageData[(c - x) + (r - y) * imageWidth];
                if (pixelColour != 0xFFFF00FF) {
                    this.pixels[c + r * Screen.WIDTH] = pixelColour;
                }
            }
        }
    }

    public void render(int x, int y, int scale, Image image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int[] imageData = image.getImageData();
        for (int r = 0; r < imageHeight; ++r) {
            int screenRow = y + r * scale;
            if (screenRow < 0) {
                continue;
            } 
            if (screenRow + scale - 1 >= Screen.HEIGHT) {
                break;
            }
            for (int c = 0; c < imageWidth; ++c) {
                int screenColumn = x + c * scale;
                if (screenColumn < 0) {
                    c = - x - 1;
                    continue;
                }
                if (screenColumn + scale - 1 >= Screen.WIDTH) {
                    break;
                }
                int pixelColour = imageData[c + r * imageWidth];
                if (pixelColour != 0xFFFF00FF) {
                    this.fillRect(screenColumn, screenRow, scale, scale, pixelColour);
                }
            }
        }
    }

    private int[] getRGB(int colour) {
        int[] rgb = new int[3];
        rgb[0] = (colour / 0x10000) % 0x100;
        rgb[1] = (colour / 0x100) % 0x100;
        rgb[2] = (colour / 0x1) % 0x100;
        return rgb;
    }

    private int getColour(int[] rgb) {
        int colour = 0xFF000000;
        colour += rgb[0] * 0x10000;
        colour += rgb[1] * 0x100;
        colour += rgb[2] * 0x1;
        return colour;
    }

    private void drawHorizontalGridLine(int y) {
        for (int x = 0; x < Screen.WIDTH; ++x) {
            int[] currentPixel = getRGB(this.pixels[x + y * Screen.WIDTH]);
            currentPixel[0] = (int) (currentPixel[0] * (1 - GRID_OPACTIY));
            currentPixel[1] = (int) (currentPixel[1] * (1 - GRID_OPACTIY));
            currentPixel[2] = (int) (currentPixel[2] * (1 - GRID_OPACTIY));
            int colour = getColour(currentPixel);
            this.pixels[x + y * Screen.WIDTH] = colour;
        }
    }

    private void drawVerticalGridLine(int x) {
        for (int y = 0; y < Screen.HEIGHT; ++y) {
            int[] currentPixel = getRGB(this.pixels[x + y * Screen.WIDTH]);
            currentPixel[0] = (int) (currentPixel[0] * (1 - GRID_OPACTIY));
            currentPixel[1] = (int) (currentPixel[1] * (1 - GRID_OPACTIY));
            currentPixel[2] = (int) (currentPixel[2] * (1 - GRID_OPACTIY));
            int colour = getColour(currentPixel);
            this.pixels[x + y * Screen.WIDTH] = colour;
        }
    }

    public void renderGrid() {
        drawHorizontalGridLine(Screen.HEIGHT / 3);
        drawHorizontalGridLine(Screen.HEIGHT / 3 * 2);
        drawVerticalGridLine(Screen.WIDTH / 3);
        drawVerticalGridLine(Screen.WIDTH / 3 * 2);
    }

    public void renderCellHighlight(int zone) {
        int[] pos = ZoneTools.getZoneBounds(zone);
        for (int y = pos[2]; y < pos[3]; ++y) {
            for (int x = pos[0]; x < pos[1]; ++x) {
                int[] currentPixel = getRGB(this.pixels[x + y * Screen.WIDTH]);
                currentPixel[0] = (int) (currentPixel[0] + (0xFFFFFFFF - currentPixel[0]) * GRID_OPACTIY);
                currentPixel[1] = (int) (currentPixel[1] + (0xFFFFFFFF - currentPixel[1]) * GRID_OPACTIY);
                currentPixel[2] = (int) (currentPixel[2] + (0xFFFFFFFF - currentPixel[2]) * GRID_OPACTIY);
                int colour = getColour(currentPixel);
                this.pixels[x + y * Screen.WIDTH] = colour;
            }
        }
    }
}
