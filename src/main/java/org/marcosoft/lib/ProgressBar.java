package org.marcosoft.lib;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

public class ProgressBar extends javax.swing.JFrame implements ActionListener, Progress {
	private static final int SIZE_ANIMATION_DELAY = 15;
	private static final Dimension MIN_SIZE = new Dimension(751, 90);

	private static final long serialVersionUID = -6989750848125302233L;

	private JLabel lblTitle;

	private JLabel lblMessage;

	private JPanel panMain;

	private JPanel panTitle;

	private final Timer timerAnimateSize;
	private double stepX;
	private double stepY;
	private int stepsCountDown;
	private JProgressBar progressBar;
	private static final Color BLUE_BACK_GROUND = new Color(58, 71, 106);

	public ProgressBar() {
		this.timerAnimateSize = new Timer(SIZE_ANIMATION_DELAY, this);
		initComponents();
	}

	@Override
	public void setTitle(String title) {
		this.lblTitle.setText(title);
	}

	private void initComponents() {
		final GridBagLayout thisLayout1 = new GridBagLayout();
		thisLayout1.columnWidths = new int[] { 7 };
		thisLayout1.rowHeights = new int[] { 7 };
		thisLayout1.columnWeights = new double[] { 0.1D };
		thisLayout1.rowWeights = new double[] { 0.1D };
		getContentPane().setLayout(thisLayout1);

		this.panMain = new JPanel();

		this.panMain.setBorder(new LineBorder(new Color(230, 230, 250), 3, true));
		this.panMain.setBackground(BLUE_BACK_GROUND);

		final GridBagLayout mainLayout = new GridBagLayout();
		mainLayout.columnWeights = new double[] { 1.0D };
		mainLayout.rowWeights = new double[] { 0.1D, 0.1D };
		mainLayout.rowHeights = new int[] { 7, 7 };
		mainLayout.columnWidths = new int[] { 1 };
		this.panMain.setLayout(mainLayout);

		getContentPane().add(this.panMain,
				new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(3, 0, 3, 0), 0, 0));

		this.panTitle = criarPanelTitulo();
		this.panMain.add(this.panTitle,
				new GridBagConstraints(0, 0, 2, 1, 0.0D, 0.0D, 11, 2, new Insets(5, 10, 15, 10), 0, 0));

		this.panMain.setOpaque(true);

		final MoveMouseListener mml = new MoveMouseListener(this.panMain);
		this.panMain.addMouseListener(mml);
		this.panMain.addMouseMotionListener(mml);
		this.panMain.setCursor(new Cursor(13));

		setUndecorated(true);
		pack();
		setSize(MIN_SIZE);
		setDefaultCloseOperation(2);

		centerMe();

		AWTUtilitiesWrapper.setOpacity(this);
		setAlwaysOnTop(true);
		setVisible(true);
	}

	private JPanel criarPanelTitulo() {
		final JPanel panTitle = new JPanel();
		final GridBagLayout panTitleLayout = new GridBagLayout();
		panTitleLayout.rowWeights = new double[] { 0.0D, 0.1D, 0.1D };
		panTitleLayout.rowHeights = new int[] { 0, 1, 1 };
		panTitleLayout.columnWeights = new double[] { 0.1D };
		panTitleLayout.columnWidths = new int[] { 7 };
		panTitle.setLayout(panTitleLayout);
		panTitle.setBackground(BLUE_BACK_GROUND);

		this.lblTitle = new JLabel();
		panTitle.add(this.lblTitle,
				new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(3, 0, 3, 0), 0, 0));
		this.lblTitle.setBackground(new Color(249, 249, 249));
		this.lblTitle.setHorizontalAlignment(0);
		this.lblTitle.setText("");
		this.lblTitle.setBorder(BorderFactory.createBevelBorder(0));
		this.lblTitle.setOpaque(true);
		this.lblTitle.setSize(679, 25);
		this.lblTitle.setPreferredSize(new Dimension(0, 25));

		this.lblMessage = new JLabel();
		panTitle.add(this.lblMessage,
				new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(2, 0, 0, 0), 0, 0));
		this.lblMessage.setHorizontalAlignment(0);
		this.lblMessage.setVerticalAlignment(0);
		this.lblMessage.setOpaque(false);
		this.lblMessage.setVisible(false);
		this.lblMessage.setForeground(new Color(255, 255, 255));
		this.lblMessage.setFont(new Font("Arial", 3, 18));

		this.progressBar = new JProgressBar(0, 100);
		panTitle.add(this.progressBar,
				new GridBagConstraints(0, 2, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(2, 0, 0, 0), 0, 0));
		this.progressBar.setBackground(java.awt.SystemColor.text);
		this.progressBar.setOpaque(true);
		this.progressBar.setStringPainted(true);
		this.progressBar.setFont(new Font("Dialog", 1, 12));

		return panTitle;
	}

	private void centerMe() {
		final Toolkit tk = Toolkit.getDefaultToolkit();
		final Dimension screenSize = tk.getScreenSize();
		final int screenWidth = screenSize.width;

		setLocation(screenWidth / 2 - getWidth() / 2, 30);
	}

	public static void main(String[] args) {
		final ProgressBar progressInfo = new ProgressBar();
		progressInfo.setTitle("Atualizando ...");
		progressInfo.changeSize(MIN_SIZE);
	}

	public void changeSize(Dimension targetSize) {
		while (this.timerAnimateSize.isRunning())
			SystemUtil.sleep(200L);
		animateChangeSize(new Dimension(getWidth(), 10));

		while (this.timerAnimateSize.isRunning())
			SystemUtil.sleep(200L);
		animateChangeSize(targetSize);
	}

	private void animateChangeSize(Dimension targetSize) {
		this.stepsCountDown = 20;
		this.timerAnimateSize.restart();
		this.stepX = ((targetSize.width - getWidth()) / this.stepsCountDown);
		this.stepY = ((targetSize.height - getHeight()) / this.stepsCountDown);
	}

	public void actionPerformed(ActionEvent e) {
		if (this.stepsCountDown-- == 0) {
			this.timerAnimateSize.stop();
		}
		setSize((int) Math.round(getWidth() + this.stepX), (int) Math.round(getHeight() + this.stepY));
	}

	public void setProgress(String template, Object... objects) {
		final String msg = String.format(template, objects);
		System.out.println(msg);
		this.lblMessage.setVisible(true);
		this.lblMessage.setText(msg);
	}

	public void setProgress(int value) {
		System.out.println(value + "%");
		this.progressBar.setVisible(true);
		this.progressBar.setValue(value);
	}

	public void finished() {
		this.progressBar.setVisible(false);
		this.lblMessage.setVisible(false);
	}
}

/*
 * Location:
 * /home/54706424372/bin/java/alarm-0.11.jar!/org/marcosoft/lib/ProgressBar.
 * class Java compiler version: 5 (49.0) JD-Core Version: 0.7.1
 */