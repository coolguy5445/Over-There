package display;

public class ImageTools {

    	// Returns an image that is a scaled version of the original
	public static Image scale(Image image, int w, int h) {
			// Get image pixel values
		int[] imageData = image.getImageData();
		
			// Create an array of new pixel values
		int[] scaledData = new int[w * h];
		
			// Get the modification value of height and width
			// (How many times smaller the original is to the scale)
		double widthMod = image.getWidth() / (double) (w);
		double heightMod = image.getHeight() / (double) (h);
		
			// Fill in the scaled data using colors from the original
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
					// Calculate old pixel positions
				int x = (int) (i * widthMod);
				int y = (int) (j * heightMod);
				
					// Check if out of bounds
				if (x < 0) x = 0;
				else if (x >= image.getWidth()) x = image.getWidth() - 1;
				if (y < 0) y = 0;
				else if (y >= image.getHeight()) y = image.getHeight() - 1;
				
					// Assign pixel value
				scaledData[i + j * w] = imageData[x + y * image.getWidth()];
			}
		}
		
			// Generate the scaled image
            image.setImageData(scaledData);
		
			// Return result
		return image;
	}
	
		// Returns an image that is a rotated version of the original
	public static Image rotate(Image image, double angle) {
			// Get image pixel values
		int[] imageData = image.getImageData();
		
			// Create an array of new pixel values
		int[] rotatedData = new int[image.getWidth() * image.getHeight()];
		for (int i = 0; i < rotatedData.length; ++i) rotatedData[i] = 0;
		
			// Fill in the rotated data using colors from the original
		for (int i = 0; i < image.getWidth(); ++i) {
			for (int j = 0; j < image.getHeight(); ++j) {
					// Get position from center of image
				int x = i - image.getWidth() / 2;
				int y = j - image.getHeight() / 2;
					// Calculate distance from center
				double h = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
				
					// Find position rotated by the angle
				double a = Math.atan2(y, x);
				a -= Math.toRadians(angle);
				if (a < -Math.PI) a = 2 * Math.PI - Math.abs(a);
				
					// Find pixel position
				double at = Math.abs(a);
				if (at > Math.PI / 2) at = Math.PI - at;
				int xt = (int) (h * Math.cos(at));
				int yt = (int) (h * Math.sin(at));
				//
				if (a < 0) y = -yt + image.getHeight() / 2;
				else y = yt + image.getHeight() / 2;
				if (Math.abs(a) > Math.PI / 2) x = -xt + image.getWidth() / 2;
				else x = xt + image.getWidth() / 2;
				
				if (x < 0) x = 0;
				else if (x >= image.getWidth()) x = image.getWidth() - 1;
				if (y < 0) y = 0;
				else if (y >= image.getHeight()) y = image.getHeight() - 1;
				
				rotatedData[i + j * image.getWidth()] = imageData[x + y * image.getWidth()];
			}
		}
        image.setImageData(rotatedData);
		return image;
	}
}
