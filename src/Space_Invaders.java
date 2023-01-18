
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Space_Invaders extends JFrame {
	public static final JOptionPane OptionPane = null;
	private static final String Space_Invaders = null;
	private JLabel lbl[][] = new JLabel[20][20];
	private Container c;
	private MyPanel mainPanel = new MyPanel();
	private JPanel southPanel = new JPanel();
	private JButton actionBtn1 = new JButton("Play");
	private JButton actionBtn2 = new JButton("Reset");
	private JButton exit = new JButton("Exit");
	public JLabel Ω;

	public Space_Invaders () {

		super(Space_Invaders);

		southPanel.setLayout(new GridLayout(1, 3));

		c = getContentPane();

		c.setLayout(new BorderLayout());

		// create and add buttons

		actionBtn1.addActionListener(mainPanel);

		actionBtn1.addKeyListener(mainPanel);

		southPanel.add(actionBtn1);

		actionBtn2.addActionListener(mainPanel);

		actionBtn2.addKeyListener(mainPanel);

		southPanel.add(actionBtn2);

		exit.addActionListener(mainPanel);

		exit.addKeyListener(mainPanel);

		southPanel.add(exit);

		addKeyListener(mainPanel);

		c.add(southPanel, BorderLayout.SOUTH);

		c.add(mainPanel, BorderLayout.CENTER);

		setSize(620, 620); // size of the window, can be changed

		setVisible(true);

		setBackground(Color.blue);

	}

	public static void main(String args[]) {

		Space_Invaders app = new Space_Invaders ();
		app.addWindowListener(

				new WindowAdapter() {

					public void windowClosing(WindowEvent e)

					{
						System.exit(0);

					}

				}

				);

	}




	class MyPanel extends JPanel implements ActionListener, KeyListener {

		// variables - they are all global

		int index, loopCount = 0;
		private JLabel[] aliens = new JLabel[35];
		private int[] alienX = new int[35];
		private int[] alienY = new int[35];
		boolean alive[] = new boolean[35];
		ImageIcon pic = new ImageIcon("Space-Invaders.jpg");
		ImageIcon pic2 = new ImageIcon("alien.png");
		ImageIcon pic3 = new ImageIcon("welcome.jpg");
		private Timer myTimer = new Timer(60, this);
		int x = 0, y = 500, Y1 = 600, X1 = 0, f = 0, stopper = 0;
		int alienCount = 0;
		boolean right = false;
		boolean fire, shot = false;
		boolean bulletMove1 = false;
		Rectangle bullet;
		Rectangle alien;
		Rectangle ship;
		int alienWidth = 45;
		int alienHeight = 45;
		int alienSpacing = 25;
		int startX = 80;
		int startY = 40;







		public MyPanel() { // initial all the variables

			JOptionPane.showMessageDialog(null, "Welcome to Space Invaders",  "WELCOME!!", JOptionPane.INFORMATION_MESSAGE,
					pic3);
			JOptionPane.showMessageDialog(null,
					"To play the game, use the arrow keys to move and the spacebar to shoot. Avoid letting the red bar reach the bottom or you will lose the game.\n  "
							+ "The game will begin as soon as you click 'OK'.\n"
							+ "                                                                                                                  Good Luck!", "Instructions", JOptionPane.INFORMATION_MESSAGE);

			// Constructor: set background color to white set up listeners to respond to
			// mouse actions

			setBackground(Color.BLACK);
			myTimer.start();
			addKeyListener(this);
			setFocusable(true);
			setFocusTraversalKeysEnabled(false);


			// making aliens in the array

			for (int i = 0; i < 35; i++) {
				alive[i] = true;
				alienX[i] = startX + (i % 7) * (alienWidth + alienSpacing);
				alienY[i] = startY + (i / 7) * (alienHeight + alienSpacing);
			}
			// Coordinates for the aliens

		}

		// Plays a sound when the bullet hits the alien.
		public void BulletSound0() {
			try {
				File soundFile = new File("bulletSound.wav");
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile); 
				Clip clip = AudioSystem.getClip();
				clip.open(audioIn);
				clip.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		public void paintComponent(Graphics g) {

			super.paintComponent(g);
			for (int i = 0; i < 35; i++) {
				if (alive[i] == true)
					g.drawImage(pic2.getImage(), alienX[i], alienY[i], 50, 47, null);// alien
				g.setColor(Color.RED);// To know where the game will end.
				g.fillRect(0, alienY[34] + 50, getWidth(), 2);

			}
			g.drawImage(pic.getImage(), x, y, 50, 47, null);// Spaceship

			if (shot) {
				g.setColor(Color.blue);
				g.fillOval(bullet.x, bullet.y, bullet.width, bullet.height);// color of the bullet;

			}

		}



		public void actionPerformed(ActionEvent e) {




			if (e.getSource() == myTimer) {
				int delx = (int) (Math.random() * 3 + 2);
				for (int i = 0; i < 35; i++) {
					alienX[i] = alienX[i] + delx;
					if (alienX[i] >= 550) {
						alienX[i] = 0;
					}

					else if (alienX[i] <= 0) {
						alienX[i] = 550;
					}
				}

				int dely = (int) (Math.random() * 12 - 5);
				for (int j = 0; j < 35; j++) {
					alienY[j] = alienY[j] + dely;
					if (alienY[j] >= 340 && loopCount <= 35) {
						alienY[j] = 0;
						loopCount++;
					}

				}

				Y1 -= 50;

				for (int i = 0; i < 35; i++) {
					if (alive[i] == true) {
						bullet = new Rectangle(X1-25, Y1-25, 5, 20);
						alien = new Rectangle(alienX[i], alienY[i], 50, 47);
						ship = new Rectangle(x, y, 50, 47);
						shot = true;
						if (bullet.intersects(alien)) {
							BulletSound0();
							X1 = 10000;
							Y1 = 10000;
							alive[i] = false;
							bulletMove1 = true;
							alienCount++;
						}
						repaint();
					}
				}

				for (int i = 0; i < 35; i++) {
					if (alienY[i] > 480 && stopper == 0) {
						myTimer.stop();
						JOptionPane.showMessageDialog(null, "Game Over! You killed " + alienCount + " aliens", "Done",
								JOptionPane.WARNING_MESSAGE);
						stopper++;
						alienCount = 0;
						myTimer.stop();


						for (int j = 0; j < 35; j++) {
							alive[j] = true;
							alienX[j] = startX + (j % 7) * (alienWidth + alienSpacing);
							alienY[j] = startY + (j / 7) * (alienHeight + alienSpacing);
						}						

						loopCount = 0;

						repaint();






					} else if (alienCount == 35 && stopper == 0) {
						JOptionPane.showMessageDialog(null, "Good Job!! \nYou killed all the aliens", "Done",
								JOptionPane.WARNING_MESSAGE);
						stopper++;
						alienCount = 0;
						loopCount = 0;
						myTimer.stop();

						for (int j = 0; j < 35; j++) {
							alive[j] = true;
							alienX[j] = startX + (j % 7) * (alienWidth + alienSpacing);
							alienY[j] = startY + (j / 7) * (alienHeight + alienSpacing);
						}						



						repaint();
					}

				}
			}

			else {

				JButton b = (JButton) e.getSource();

				if (b.getText() == "Play") {

					myTimer.start();
					stopper = 0;

				}

				else if (b.getText() == "Reset") {

					myTimer.stop();


					for (int j = 0; j < 35; j++) {
						alive[j] = true;
						alienX[j] = startX + (j % 7) * (alienWidth + alienSpacing);
						alienY[j] = startY + (j / 7) * (alienHeight + alienSpacing);
					}						



					repaint();
					loopCount = 0;

					repaint();

				}

				else if (b.getText() == "Exit") {

					System.exit(0);

				}

			}


		}// end actionPerformed

		public void keyPressed(KeyEvent ev) {
			System.out.println(ev.getKeyCode());
			if (ev.getKeyCode() == 37) {// left arrow
				x += -15;
				if (x <= 0)
					x = 0;

			}

			if (ev.getKeyCode() == 39) { // right arrow
				x += 15;
				if (x >= 520) {
					x = 520;
				}
			}

			if (ev.getKeyCode() == 32) {
				if (bulletMove1 == false)
					fire = true;
				if (fire) {
					Y1 = 600;
					X1 = x + 47;
					shot = true;

				}

			}

			repaint();

		}

		public void keyReleased(KeyEvent e) {

			if (e.getKeyCode() == 65) {// shooting the bullet from the spaceship
				fire = false;

				if (bullet.y <= -5) {

					bullet = new Rectangle(0, 0, 0, 0);

					shot = false;

					fire = true;

				}

			}

		}
		public void shoot() {

			if (shot) {

				bullet.y--;

			}

		}

		public void keyTyped(KeyEvent e) {
		}





	}



}