package graficos;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Games extends Canvas implements Runnable {
	private Thread thread;
	public static JFrame frame;
	private boolean isRunning = true;
	private final int WIDTH = 160;
	private final int HEIGTH = 120;
	private final int SCALE = 3;
	
	private BufferedImage image;
	
	public Games()	{
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGTH * SCALE));
		initFrame();
		image = new BufferedImage(WIDTH, HEIGTH, BufferedImage.TYPE_INT_RGB);

	}
	
	public void initFrame( ) {
		frame = new JFrame("Game#1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start()	{
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Games game = new Games();
		game.start();
		
	}
	
	public void update() {
		
	}
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(250,0,0));
		g.fillRect(0, 0, WIDTH, HEIGTH);
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGTH * SCALE, null);
		bs.show();
	}
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		final double amoutOfTicks = 60.0;
		final double ns = 1000000000 / amoutOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();		
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				update();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
			
			stop();
			
		}
		
	}

}
