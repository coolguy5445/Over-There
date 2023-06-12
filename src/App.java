import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import inputs.Keyboard;
import inputs.Mouse;
import display.Screen;
import game.Game;
import game.GameController;

public class App extends Canvas implements Runnable{

    private static final long serialVersionUID = 1L;
	public static App app;
	private JFrame frame;
	private Screen screen;
	private Thread thread;
	private boolean running = false;
	private BufferedImage image = new BufferedImage(Screen.WIDTH, Screen.HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

    public App() {		
		frame  = new JFrame();
		screen = new Screen();
        
		Dimension size = new Dimension(Screen.WIDTH, Screen.HEIGHT);
		this.setSize(size);
		
        this.addMouseListener(Mouse.mouse);
		this.addKeyListener(Keyboard.keyboard);

        Game.initialize();
	}
		
	public synchronized void start() {
	    running = true;
	    thread = new Thread(this, "Game");
	    thread.start();
	}
    
	public synchronized void stop() {
	    running = false;
	    try {
	    	thread.join();
	    }
	    catch (InterruptedException e) {
	    	e.printStackTrace();
	    }
	}
	
	public void run() {
		long lastTime = System.nanoTime();
	    long timer = System.currentTimeMillis();
	    final double ns = 1000000000.0 / 30.0; // Conversion from nanoseconds to milliseconds
	    double delta = 0;
	    this.requestFocus();
	    
	    while (running) {
	    	long now = System.nanoTime();
	    	delta += (now - lastTime) / ns;
	    	lastTime = now;
	        	// Update 30 times a second
	    	while (delta >= 1) {
	    		update();
	    		--delta;
	    	}
	    	
	    	render();
	    	if (System.currentTimeMillis() - timer > 1000) {
	    		timer += 1000;
	    	}
	    }
	}
	
	public void update() {
		Mouse.mouse.update(this.getLocationOnScreen().x, this.getLocationOnScreen().y);
        GameController.update();
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
        screen.clear();
        
        Game.render(screen);
        
        int[] screenData = screen.getImageData();
		for (int i = 0; i < pixels.length; ++i) {
			pixels[i] = screenData[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, Screen.WIDTH, Screen.HEIGHT, null);
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		app = new App();
		app.frame.setResizable(false);
	    app.frame.add(app);
	    app.frame.pack();
	    app.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    app.frame.setLocationRelativeTo(null);
        app.frame.setTitle("Over There!");
	    app.frame.setVisible(true);
	    app.start();
	}
}
