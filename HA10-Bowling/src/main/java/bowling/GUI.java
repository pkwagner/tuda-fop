package bowling;

import acm.graphics.GLabel;
import acm.graphics.GOval;
import acm.program.GraphicsProgram;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JOptionPane;

public class GUI extends acm.program.GraphicsProgram implements KeyListener {
	IGame game;
	Player[] player;
	GLabel[] playerLabel;
	GOval[] pins;
	GLabel roundAndThrowLabel;
	Random random;
	int nrPinsCleared = 0, maxPins = 10;

	private void initGame() {
		random = new Random();

		String[] args = getArgumentArray();

		if (args.length >= 1 && args[0].equals("Tannenbaum"))
			game = new TannenbaumKegeln(args.length - 1);
		else
			game = new Bowling(args.length - 1);

		pins = new GOval[game.getPinCount()];

		// position pins
		double xCenter = getSize().getWidth() / 2;
		double yCenter = getSize().getHeight() / 2;

		double size = 40;
		double offset = size / 2;

		double rowCount = 4;
		int y = -2;
		double x = -rowCount / 2;

		for (int i = 0; i < game.getPinCount(); i++) {
			GOval pin = new GOval(size, size);
			pin.setFillColor(Color.BLACK);
			pin.setFilled(true);

			pin.setLocation(xCenter + x * (pin.getWidth() * 2) + offset, yCenter + y * (pin.getHeight() * 2) + offset);

			add(pin);
			pins[i] = pin;

			x++;
			if (x >= rowCount / 2) {
				rowCount--;
				x = -rowCount / 2;
				y++;
			}
		}
	}

	private void initPlayer() {
		String[] args = getArgumentArray();

		player = new Player[args.length - 1];
		playerLabel = new GLabel[args.length - 1];

		for (int i = 1; i < args.length; i++) {
			player[i - 1] = game.addPlayer(args[i]);

			GLabel label = new GLabel("");
			label.setLocation(10, (i + 1) * 20);
			add(label);

			playerLabel[i - 1] = label;
		}

		game.startGame();
	}

	private void initGUI() {
		setSize(800, 600);
		getGCanvas().addKeyListener(this);

		roundAndThrowLabel = new GLabel("");
		roundAndThrowLabel.setLocation(10, 20);
		add(roundAndThrowLabel);
	}

	@Override
	public void run() {
  	    if (getArgumentArray().length < 3) {
	        System.err.println("Usage: java GUI <gameMode> <playerNameA> <playerNameB> <playerC...>");
	        System.exit(0);
	    }
	    else {
	 		initGUI();
			initGame();
			initPlayer();
		}
	}

	private void updateLabels() {
		for (Player player : player) {
			GLabel label = playerLabel[player.getID()];
			StringBuilder str = new StringBuilder();
			str.append(player.getName());
			str.append(": ");

			for (int i : game.getScore(player)) {
				str.append(i);
				str.append(" ");
			}

			label.setLabel(str.toString());
		}
	}

	private void updateGameLabel() {
		if (game.hasFinished())
			roundAndThrowLabel.setLabel("Game complete!");
		else
			roundAndThrowLabel.setLabel("Round: " + game.getRound() + " Throw: " + game.getThrow() + " Player: "
					+ game.getActivePlayer().getName());
	}

	private void throwBall() {
		if (game.getThrow() == 1 || game.getPinsLeft() == game.getPinCount()) {
			for (GOval pin : pins)
				pin.setFillColor(Color.BLACK);
		}

		int count = 0;

		for (GOval pin : pins) {
			if (pin.getFillColor() == Color.BLACK && random.nextBoolean()) {
				pin.setFillColor(Color.CYAN);
				count++;
			}
		}
		game.throwBall(count);
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (game.hasStarted() && !game.hasFinished() && e.getKeyChar() == 10) {
			updateGameLabel();
			updateLabels();
			throwBall();
		}

		if (game.hasFinished()) {
			JOptionPane.showMessageDialog(this, "Player " + game.getWinner().getName() + " has won!");
		}
	}

	public static void main(String[] args) {
		new GUI().start(args);
	}
}
