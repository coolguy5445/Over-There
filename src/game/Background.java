package game;

import display.Image;
import display.Screen;

public class Background {
    private Image backgroundImage;

    public Background(String path) {
        this.backgroundImage = new Image(path);
    }

    private int[] getOffsetData(int distance) {
        int imageWidth = this.backgroundImage.getWidth();
        int imageHeight = this.backgroundImage.getHeight();
        int[] baseData = this.backgroundImage.getImageData();
        int[] result = new int[imageWidth * imageHeight];
        for (int r = 0; r < imageHeight; ++r) {
            int scrolledRow = r - distance;
            while (scrolledRow < 0) {
                scrolledRow += imageHeight;
            }
            for (int c = 0; c < imageWidth; ++c) {
                result[c + r * imageWidth] = baseData[c + scrolledRow * imageWidth];
            }
        }
        return result;
    }

    public void scroll(int distance) {
        this.backgroundImage.setImageData(this.getOffsetData(distance));
    }

    public void render(Screen screen) {
        for (int y = 0; y < Screen.HEIGHT; y += this.backgroundImage.getHeight() * 4) {
            screen.render(0, y, 4, this.backgroundImage);
        }
    }
}
