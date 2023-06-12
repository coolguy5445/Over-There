package display;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Image {
    protected int[] pixels;
    protected int w, h;

        // New empty image
    public Image(int w, int h) {
        this.w = w;
        this.h = h;
        pixels = new int[w * h];
        for (int i = 0; i < pixels.length; ++i) {
            pixels[i] = 0xFFFF00FF;
        }
    }

        // Load image from file
    public Image(String path) {
        load(path);
    }

        // Select an image from a spritesheet
    public Image(int x, int y, int w, int h, Image sheet) {
        this.w = w;
        this.h = h;
        this.pixels = new int[w * h];
		for (int r = 0; r < h; ++r) {
			for (int c = 0; c < w; ++c) {
				pixels[c + r * w] = sheet.pixels[(c + x) + (r + y) * sheet.w];
			}
		}
    }

    private void load(String path) {
		try {
			BufferedImage image = ImageIO.read(Image.class.getResource(path));
			
            int w = image.getWidth();
			int h = image.getHeight();
			int[] pixels = new int[w * h];
			
			image.getRGB(0, 0, w, h, pixels, 0, w);

            this.w = w;
            this.h = h;
            this.setImageData(pixels);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public int[] getImageData() {
        return pixels;
    }

    public void setImageData(int[] data) {
        this.pixels = new int[data.length];
        for (int i = 0; i < this.pixels.length; ++i) {
            this.pixels[i] = data[i];
        }
    }
}
