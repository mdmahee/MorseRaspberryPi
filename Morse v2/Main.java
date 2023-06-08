import java.awt.BorderLayout;

import java.awt.Color;

import java.awt.FlowLayout;

import java.awt.Font;

import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;

import java.io.File;

import java.io.IOException;

import java.io.InputStream;

import java.io.PipedReader;

import java.io.PipedWriter;
import java.sql.Time;
import java.time.Duration;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.HashMap;

import java.util.concurrent.Executors;

import java.util.concurrent.ScheduledExecutorService;

import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFileFormat;

import javax.sound.sampled.AudioFormat;

import javax.sound.sampled.AudioInputStream;

import javax.sound.sampled.AudioSystem;

import javax.sound.sampled.Clip;

import javax.sound.sampled.DataLine;

import javax.sound.sampled.LineUnavailableException;

import javax.sound.sampled.SourceDataLine;

import javax.sound.sampled.UnsupportedAudioFileException;

import javax.swing.BorderFactory;

import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.JTextField;

import com.pi4j.io.gpio.GpioController;

import com.pi4j.io.gpio.GpioFactory;

import com.pi4j.io.gpio.GpioPinDigitalInput;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

import com.pi4j.io.gpio.PinPullResistance;

import com.pi4j.io.gpio.PinState;

import com.pi4j.io.gpio.RaspiPin;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;

import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class Main {

	public static long startTime = 0, endTime = 0, lastTime = 0, lastTimeForThread = 0;

	public static int i = 0, letterGap = 0, wordGap = 0, jack = 0;

	public static String temps, s;

	public static String finalString;

	public static ArrayList<String> code = new ArrayList<String>();

	public static TextToMorse textToMorse;

	public static JButton[] buttons = new JButton[42];

	public static JLabel dotLengthForEncodingSoundLabel;

	public static JTextArea dotLengthForEncodingSoundTextArea;

	public static JLabel frequencyLengthForEncodingSoundLabel;

	public static JTextArea frequencyLengthForEncodingSoundTextArea;

	private static javax.swing.Timer timerSwing;

	private static long lastTickTime;

	private static JLabel timerLabel;

	public static Thread beepSoundRunnerThread;

	public static TextToMorse textToMorseForMakingHoldingBeep;

	public static Clip clip;

	public static GpioPinDigitalOutput pin3out, pin4out;

	public static void main(String[] args) {

		// TODO Auto-generated method stub

		String filename = "cpu2x-jtfe7.wav";

		JFrame jframe = new JFrame("Morse Code");

		jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);

		// jframe.setSize(800, 800);

		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/*
		 * 
		 * JFrame jframeDecoder = new JFrame(); jframeDecoder.setSize(600, 600);
		 * 
		 */

		JScrollPane scrollPane = new JScrollPane();

		JPanel parentPanel = new JPanel();

		parentPanel.setLayout(new BorderLayout());

		JPanel decoderMainPanel = new JPanel();

		decoderMainPanel.setSize(600, 600);

		decoderMainPanel.setLayout(new FlowLayout());

		JPanel alphabetButtonPanel = new JPanel();

		alphabetButtonPanel.setLayout(new GridLayout(2, 18));

		alphabetButtonPanel.setSize(600, 100);

		alphabetButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

		JPanel encodeMainPanel = new JPanel();

		encodeMainPanel.setLayout(new GridLayout(3, 1));

		// encodeMainPanel.setSize(600, 300);

		JPanel textInputPanel = new JPanel();

		GridLayout gridLayoutForTextInputPanel = new GridLayout();

		gridLayoutForTextInputPanel.setColumns(1);

		gridLayoutForTextInputPanel.setRows(5);

		textInputPanel.setLayout(gridLayoutForTextInputPanel);

		JPanel textOnlyInputPanel = new JPanel();

		GridLayout gridLayoutForTextOnlyInputPanel = new GridLayout();

		gridLayoutForTextOnlyInputPanel.setColumns(1);

		gridLayoutForTextOnlyInputPanel.setRows(2);

		textOnlyInputPanel.setLayout(gridLayoutForTextOnlyInputPanel);

		// jframeDecoder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// JTextArea decodedTextLabel = new JTextArea(100,100);
		JTextArea decodedTextLabel = new JTextArea(3, 3);

		// JLabel decodedTextLabel = new JLabel();
		// decodedTextLabel.setSize(1200, 800);
		decodedTextLabel.setFont(new Font("Arial Black", Font.BOLD, 30));
		decodedTextLabel.setEditable(false); 
		decodedTextLabel.setLineWrap(true);
		decodedTextLabel.setWrapStyleWord(true);
		
		
		// decodedTextLabel.setEditable(false);
		// unhide chilo
		// decodedTextLabel.setText("Investigation");

		scrollPane.setViewportView(decodedTextLabel);

		JLabel inputtedCodesLabel = new JLabel();

		inputtedCodesLabel.setFont(new Font("Arial Black", Font.BOLD, 20));

		// inputtedCodesLabel.setText("---.-");

		JLabel wrongCodesLabel = new JLabel();

		wrongCodesLabel.setFont(new Font("Arial Black", Font.BOLD, 28));

		JLabel dotLowLimitLabel = new JLabel("Dot Lower Limit");

		JLabel dotUpperLimitLabel = new JLabel("Dot Upper Limit");

		JLabel dashLowLimitLabel = new JLabel("Dash  Lower Limit");

		JLabel dashUpperLimitLabel = new JLabel("Dash Upper Limit");

		JLabel letterLowLimitLabel = new JLabel("Letter Low Limit");

		JLabel letterUpperLimitLabel = new JLabel("Letter Upper Limit");

		JLabel codeLowLimitLabel = new JLabel("Code Low Limit");

		JLabel codeUpperLimitLabel = new JLabel("Code Upper Limit");

		JLabel wordLowLimitLabel = new JLabel("Word Low Limit");

		JLabel wordUpperLimitLabel = new JLabel("Word Upper Limit");

		JLabel time = new JLabel("Time : ");

		timerLabel = new JLabel();

		timerLabel = new JLabel(String.format("%02d.%03d", 0, 0, 0, 0));

		JTextArea dotLowLimit = new JTextArea("1");

		JTextArea dotUpperLimit = new JTextArea("200");

		JTextArea dashLowLimit = new JTextArea("250");

		JTextArea dashUpperLimit = new JTextArea("1000");

		JTextArea codeLowLimit = new JTextArea("10");

		JTextArea codeUpperLimit = new JTextArea("400");

		JTextArea letterLowLimit = new JTextArea("401");

		JTextArea letterUpperLimit = new JTextArea("800");

		JTextArea wordLowLimit = new JTextArea("801");

		JTextArea wordUpperLimit = new JTextArea("3000");

		JTextField timeField = new JTextField();

		timeField.setEditable(false);

		// dotLowLimit.setSize(100,10);

		timerSwing = new javax.swing.Timer(100, new ActionListener() {

			@Override

			public void actionPerformed(ActionEvent e) {

				long runningTime = System.currentTimeMillis() - lastTickTime;

				Duration duration = Duration.ofMillis(runningTime);

				long hours = duration.toHours();

				duration = duration.minusHours(hours);

				long minutes = duration.toMinutes();

				duration = duration.minusMinutes(minutes);

				long millis = duration.toMillis();

				long seconds = millis / 1000;

				millis -= (seconds * 1000);

				timerLabel.setText(String.format("%02d.%03d", seconds, millis));

			}

		});

		buttons[0] = new JButton("A");

		buttons[1] = new JButton("B");

		buttons[2] = new JButton("C");

		buttons[3] = new JButton("D");

		buttons[4] = new JButton("E");

		buttons[5] = new JButton("F");

		buttons[6] = new JButton("G");

		buttons[7] = new JButton("H");

		buttons[8] = new JButton("I");

		buttons[9] = new JButton("J");

		buttons[10] = new JButton("K");

		buttons[11] = new JButton("L");

		buttons[12] = new JButton("M");

		buttons[13] = new JButton("N");

		buttons[14] = new JButton("O");

		buttons[15] = new JButton("P");

		buttons[16] = new JButton("Q");

		buttons[17] = new JButton("R");

		buttons[18] = new JButton("S");

		buttons[19] = new JButton("T");

		buttons[20] = new JButton("U");

		buttons[21] = new JButton("V");

		buttons[22] = new JButton("W");

		buttons[23] = new JButton("X");

		buttons[24] = new JButton("Y");

		buttons[25] = new JButton("Z");

		buttons[26] = new JButton("0");

		buttons[27] = new JButton("1");

		buttons[28] = new JButton("2");

		buttons[29] = new JButton("3");

		buttons[30] = new JButton("4");

		buttons[31] = new JButton("5");

		buttons[32] = new JButton("6");

		buttons[33] = new JButton("7");

		buttons[34] = new JButton("8");

		buttons[35] = new JButton("9");

		buttons[36] = new JButton(".");
		buttons[37] = new JButton(",");
		buttons[38] = new JButton(":");
		buttons[39] = new JButton("-");
		buttons[40] = new JButton("@");
		buttons[41] = new JButton("=");

		System.out.println(buttons[29].getText());

		for (int i = 0; i < buttons.length; ++i) {

			alphabetButtonPanel.add(buttons[i]);

			buttons[i].addActionListener(new ButtonActionListener(i));

		}

		JLabel textToBeEncodedLabel = new JLabel("Enter Text to Encode as Morse Code:");

		JTextArea textToBeEncodedTextArea = new JTextArea(2, 5);

		textToBeEncodedTextArea.setFont(new Font("Arial Black", Font.BOLD, 20));

		JButton encodeTextButton = new JButton("Encode");

		encodeTextButton.addActionListener(new ActionListener() {

			@Override

			public void actionPerformed(ActionEvent arg0) {

				TextToMorse textToMorse = new TextToMorse(textToBeEncodedTextArea.getText());

			}

		});

		dotLengthForEncodingSoundLabel = new JLabel("Unit length (ms)");

		dotLengthForEncodingSoundTextArea = new JTextArea("100");
		// JLabel copyright = new JLabel("Made by Cyborg Technologies Bangladesh");

		/*
		 * //JButton resetSequenceButton = new JButton("Reset Sequence");
		 * 
		 * //resetSequenceButton.addActionListener(new ActionListener() {
		 * dotLengthForEncodingSoundTextArea.addActionListener(new ActionListener() {
		 * 
		 * @Override
		 * 
		 * public void actionPerformed(ActionEvent arg0) {
		 * 
		 * // s = "";
		 * 
		 * // inputtedCodesLabel.setText(s);
		 * dotLengthForEncodingSoundTextArea=JTextArea();
		 * 
		 * }
		 * 
		 * });
		 * 
		 */

		// frequencyLengthForEncodingSoundLabel = new JLabel("Frequency (hz)");

		// frequencyLengthForEncodingSoundTextArea = new JTextArea("10000");

		textOnlyInputPanel.add(textToBeEncodedLabel);

		textOnlyInputPanel.add(textToBeEncodedTextArea);

		textInputPanel.add(encodeTextButton);

		textInputPanel.add(dotLengthForEncodingSoundLabel);

		textInputPanel.add(dotLengthForEncodingSoundTextArea);

		// textInputPanel.add(frequencyLengthForEncodingSoundLabel);

		// textInputPanel.add(frequencyLengthForEncodingSoundTextArea);

		encodeMainPanel.add(textOnlyInputPanel);

		encodeMainPanel.add(textInputPanel);

		encodeMainPanel.add(alphabetButtonPanel);

		JPanel mainPanel = new JPanel();

		mainPanel.setSize(900, 900);

		JPanel subPanel = new JPanel();

		subPanel.setSize(1000, 1000);

		JPanel setupPanel = new JPanel();

		setupPanel.setSize(1200, 1200);

		setupPanel.setLayout(new GridLayout(6, 4, 3, 3));

		setupPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		BorderLayout borderLayout2 = new BorderLayout();

		mainPanel.setLayout(borderLayout2);

		BorderLayout borderLayout = new BorderLayout();

		subPanel.setLayout(borderLayout);

		// mainPanel.setLayout(new FlowLayout());

		JButton resetSequenceButton = new JButton("Reset Sequence");

		resetSequenceButton.addActionListener(new ActionListener() {

			@Override

			public void actionPerformed(ActionEvent arg0) {

				s = "";

				inputtedCodesLabel.setText(s);

			}

		});

		JButton resetMessageButton = new JButton("Reset Message");

		resetMessageButton.addActionListener(new ActionListener() {

			@Override

			public void actionPerformed(ActionEvent arg0) {

				finalString = "";

				decodedTextLabel.setText("");

			}

		});

		subPanel.add(resetMessageButton, BorderLayout.EAST);

		subPanel.add(resetSequenceButton, BorderLayout.WEST);

		subPanel.add(scrollPane, BorderLayout.NORTH);

		subPanel.add(wrongCodesLabel, BorderLayout.CENTER);

		subPanel.add(inputtedCodesLabel, BorderLayout.SOUTH);

		mainPanel.add(subPanel, BorderLayout.NORTH);

		setupPanel.add(dotLowLimitLabel);

		setupPanel.add(dotLowLimit);

		setupPanel.add(dotUpperLimitLabel);

		setupPanel.add(dotUpperLimit);

		setupPanel.add(dashLowLimitLabel);

		setupPanel.add(dashLowLimit);

		setupPanel.add(dashUpperLimitLabel);

		setupPanel.add(dashUpperLimit);

		setupPanel.add(codeLowLimitLabel);

		setupPanel.add(codeLowLimit);

		setupPanel.add(codeUpperLimitLabel);

		setupPanel.add(codeUpperLimit);

		setupPanel.add(letterLowLimitLabel);

		setupPanel.add(letterLowLimit);

		setupPanel.add(letterUpperLimitLabel);

		setupPanel.add(letterUpperLimit);

		setupPanel.add(wordLowLimitLabel);

		setupPanel.add(wordLowLimit);

		setupPanel.add(wordUpperLimitLabel);

		setupPanel.add(wordUpperLimit);

		setupPanel.add(time);

		setupPanel.add(timeField);

		JLabel timerActualLabel = new JLabel("Time Elapsed From last Button Pressed");

		setupPanel.add(timerActualLabel);

		setupPanel.add(timerLabel);

		mainPanel.add(setupPanel, BorderLayout.SOUTH);

		decoderMainPanel.add(mainPanel);

		// decodeMainPanel.setVisible(true);

		parentPanel.add(decoderMainPanel, BorderLayout.NORTH);

		parentPanel.add(encodeMainPanel, BorderLayout.SOUTH);

		jframe.add(parentPanel);

		jframe.setVisible(true); // parent frame

		startTime = System.currentTimeMillis();

		s = "";

		finalString = "";

		final GpioController gpio = GpioFactory.getInstance();

		final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,

				PinPullResistance.PULL_DOWN);

		pin3out = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, // PIN NUMBER

				"6_PINOUT", // PIN FRIENDLY NAME (optional)

				PinState.LOW); // PIN STARTUP STATE (optional)

		pin4out = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, // PIN NUMBER

				"4_PINOUT", // PIN FRIENDLY NAME (optional)

				PinState.LOW); // PIN STARTUP STATE (optional)

		myButton.setShutdownOptions(true);

		Runnable deocderThread = new Runnable() {

			public void run() {

				System.out.println("Last Time for Thread" + lastTimeForThread);

				long timeDifference = System.currentTimeMillis() - lastTimeForThread;

				System.out.println("Time Diffrence " + timeDifference);

				if (letterGap == 1 || wordGap == 1

						|| ((letterGap == 0 || wordGap == 0)

								&& timeDifference > Integer.parseInt(letterUpperLimit.getText()))

								&& lastTimeForThread != 0) {

					System.out.println("Letter Gap " + letterGap);

					System.out.println("Word Gap " + wordGap);

					if (timeDifference > Integer.parseInt(letterUpperLimit.getText())) {

						if (!temps.isEmpty()) {

							System.out.println("Temps Received " + s);

							s += temps;

							temps = "";

							System.out.println("After Adding temps " + s);

						}

					}

					if (s.equals(".-")) {

						System.out.println("A");

						finalString += "A";

						decodedTextLabel.setText(finalString);

						System.out.println(finalString);

						s = "";

					} else if (s.equals("-...")) {

						finalString += "B";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("-.-.")) {

						finalString += "C";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("-..")) {

						finalString += "D";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals(".")) {

						finalString += "E";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("..-.")) {

						finalString += "F";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("--.")) {

						finalString += "G";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("....")) {

						finalString += "H";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("..")) {

						finalString += "I";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals(".---")) {

						finalString += "J";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("-.-")) {

						finalString += "K";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals(".-..")) {

						finalString += "L";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("--")) {

						finalString += "M";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("-.")) {

						finalString += "N";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("---")) {

						finalString += "O";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals(".--.")) {

						finalString += "P";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("--.-")) {

						finalString += "Q";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals(".-.")) {

						finalString += "R";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("...")) {

						finalString += "S";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("-")) {

						finalString += "T";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("..-")) {

						finalString += "U";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("...-")) {

						finalString += "V";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals(".--")) {

						finalString += "W";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("-..-")) {

						finalString += "X";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("-.--")) {

						finalString += "Y";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("--..")) {

						finalString += "Z";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("-----")) {

						finalString += "0";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals(".----")) {

						finalString += "1";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("..---")) {

						finalString += "2";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("...--")) {

						finalString += "3";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("....-")) {

						finalString += "4";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals(".....")) {

						finalString += "5";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("-....")) {

						finalString += "6";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("--...")) {

						finalString += "7";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("---..")) {

						finalString += "8";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("----.")) {

						finalString += "9";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals(".-.-.-")) {

						finalString += ".";

						s = "";

						decodedTextLabel.setText(finalString);

					}

					else if (s.equals("--..--")) {

						finalString += ",";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("---...")) {

						finalString += ":";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("..--..")) {

						finalString += "?";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("-....-")) {

						finalString += "-";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals(".--.-.")) {

						finalString += "@";

						s = "";

						decodedTextLabel.setText(finalString);

					} else if (s.equals("-...-")) {

						finalString += "=";

						s = "";

						decodedTextLabel.setText(finalString);

					}

					/*
					 * 
					 * .,:? dictionary.put('-', "-....-");
					 * 
					 * dictionary.put('@', ".--.-.");
					 * 
					 * dictionary.put('=', "-...-");
					 */

					else {

						s = "";

						temps = "";

						letterGap = 0;

						wordGap = 0;

						lastTimeForThread = 0;

						inputtedCodesLabel.setText("");

						wrongCodesLabel.setText("");

					}

					lastTimeForThread = 0;

					inputtedCodesLabel.setText("");

					wrongCodesLabel.setText("");

				}

			}

		};

		myButton.addListener(new GpioPinListenerDigital() {

			@Override

			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

				if (event.getState() == PinState.HIGH) {

					System.out.println("High");

					pin4out.setState(PinState.HIGH);

					pin3out.setState(PinState.HIGH);

					startTime = System.currentTimeMillis();

					timerSwing.stop();

					if (startTime - lastTime > Integer.parseInt(codeLowLimit.getText())

							&& startTime - lastTime < Integer.parseInt(codeUpperLimit.getText())) {

						s += temps;

						temps = "";

						inputtedCodesLabel.setText(s);

						wrongCodesLabel.setText("");

					} else if (startTime - lastTime > Integer.parseInt(letterLowLimit.getText())

							&& startTime - lastTime < Integer.parseInt(letterUpperLimit.getText())) {

						System.out.println("Letter ");

						finalString += "";

						letterGap = 1;

						wrongCodesLabel.setText("Letter Gap");

						deocderThread.run(); // look for current code stream if its matches any letter code

						inputtedCodesLabel.setText(s);

						timeField.setText(String.valueOf(startTime - lastTime));

						// lastTime = endTime;

					} else if (startTime - lastTime > Integer.parseInt(wordLowLimit.getText())

							&& startTime - lastTime >= Integer.parseInt(wordUpperLimit.getText())) {

						System.out.println("New Word");

						finalString += " "; // adding white space if the button is pressed between the word limit

						decodedTextLabel.setText(finalString); // setting to the Label

						wordGap = 1;

						wrongCodesLabel.setText("Word Gap");

						deocderThread.run();

						inputtedCodesLabel.setText(s);

						timeField.setText(String.valueOf(startTime - lastTime));

						// lastTime = endTime;

					} else if (startTime - lastTime > Integer.parseInt(wordUpperLimit.getText())) {

						System.out.println("WRONG >>" + (startTime - lastTime));

						wrongCodesLabel.setText("Wrong. Time : " + (startTime - lastTime));

						wrongCodesLabel.setForeground(Color.RED);

						// lastTime = endTime;

						timeField.setText("Wrong Timing: " + String.valueOf(lastTime));

					} else if (startTime - lastTime < Integer.parseInt(letterLowLimit.getText())) {

						System.out.println("WRONG >>" + (startTime - lastTime));

						wrongCodesLabel.setText("Wrong. Time : " + (startTime - lastTime));

						wrongCodesLabel.setForeground(Color.RED);

						// lastTime = endTime;

						timeField.setText("Wrong Timing: " + String.valueOf(startTime - lastTime));

					}

				}

				else if (event.getState() == PinState.LOW) { // System.out.println("End");

					pin4out.setState(PinState.LOW);

					pin3out.setState(PinState.LOW);

					endTime = System.currentTimeMillis();

					lastTime = System.currentTimeMillis();

					wrongCodesLabel.setText("");

					// starting the timer

					if (!timerSwing.isRunning()) {

						lastTickTime = System.currentTimeMillis();

						timerSwing.start();

					}

					// stopping beep sound thread

					jack = 1;

					if (endTime - startTime > Integer.parseInt(dotLowLimit.getText())

							&& endTime - startTime < Integer.parseInt(dotUpperLimit.getText())) {

						System.out.println(".");

						temps = ".";

						// inputtedCodesLabel.setText(s);

						wrongCodesLabel.setText(temps);

						wrongCodesLabel.setForeground(Color.BLUE);

						lastTime = endTime;

						lastTimeForThread = endTime;

						timeField.setText("Dot : " + String.valueOf(endTime - startTime));

						System.out.println("last time after dot >>" + lastTime);

					} else if (endTime - startTime > Integer.parseInt(dashLowLimit.getText())

							&& endTime - startTime < Integer.parseInt(dashUpperLimit.getText())) {

						System.out.println("-");

						temps = "-";

						wrongCodesLabel.setText(temps);

						wrongCodesLabel.setForeground(Color.BLUE);

						// inputtedCodesLabel.setText(s);

						lastTime = endTime;

						lastTimeForThread = endTime;

						timeField.setText("Dash : " + String.valueOf(endTime - startTime));

						System.out.println("last time after dash >>" + lastTime);

					}

					// }

					System.out.println(s);

					inputtedCodesLabel.setText(s);

				}

			}

		});

		// thread should be after all codes

		for (;;) {

			try {

				ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

				// System.out.println("Time before thread call "+System.currentTimeMillis());

				executor.schedule(deocderThread, Integer.parseInt(letterUpperLimit.getText()), TimeUnit.MILLISECONDS);

				Thread.sleep(Integer.parseInt(letterUpperLimit.getText()));

				// System.out.println("Time after sleep "+System.currentTimeMillis());

			} catch (InterruptedException e) {

				e.printStackTrace();

			}

		}

	}

}

class ButtonActionListener implements ActionListener {

	public int i;

	public ButtonActionListener(int i) {

		// TODO Auto-generated constructor stub

		this.i = i;

	}

	@Override

	public void actionPerformed(ActionEvent e) {

		TextToMorse textToMorse = new TextToMorse(Main.buttons[this.i].getText());

	}

}

class TextToMorse {

	private static String textAreaText;

	final Thread fileReaderFilter;

	final Thread morseFilter;

	public TextToMorse(String textAreaText) {

		super();

		this.textAreaText = textAreaText;

		final Timer timer = new Timer();

		timer.start();

		final PipedWriter pipe01 = new PipedWriter();

		final PipedWriter pipe02 = new PipedWriter();

		final PipedWriter pipe03 = new PipedWriter();

		fileReaderFilter = new FileReaderFilter(textAreaText, pipe01);

		morseFilter = new MorseFilter(pipe01, pipe02, pipe03);

		fileReaderFilter.start();

		morseFilter.start();

	}

	public static void main(String[] argc) {

	}

	@SuppressWarnings("deprecation")

	public void stopTextToMorse() {

		System.out.println("Stopping Morse Converted thread");

		fileReaderFilter.stop();

		morseFilter.stop();

	}

}

class Timer {

	private long startTime;

	private long endTime;

	public void start() {

		startTime = System.nanoTime();

	}

	public void stop() {

		endTime = System.nanoTime();

		final long executionTime = endTime - startTime;

		System.out.println(String.format("\n\n The execution took %s ms", (executionTime / 1000000f)));

	}

}

class FileReaderFilter extends Thread {

	private PipedWriter outputPipe;

	private String inputTextP;

	// public FileReaderFilter (final String fileName, final PipedWriter outputPipe,

	// final String inputText) {

	public FileReaderFilter(final String inputText, final PipedWriter outputPipe) {

		// if (!fileName.isEmpty()) {

		this.inputTextP = inputText;

		this.outputPipe = outputPipe;

		System.out.print("FileReaderFilter");

		System.out.print(inputText);

	}

	@Override

	public void run() {

		// FileInputStream fileInputStream;

		Integer readByte;

		try {

			for (int i = 0; i < inputTextP.length(); i++) {

				System.out.println("Inside Loop");

				System.out.println(inputTextP.charAt(i));

				outputPipe.write((byte) inputTextP.charAt(i));

			}

			outputPipe.flush();

			outputPipe.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}

class MorseFilter extends Thread {

	private HashMap<Character, String> dictionary;

	private PipedReader inputPipe;

	private PipedWriter outputPipe1;

	private PipedWriter outputPipe2;

	private BuzzerSound buzzerSound;

	public MorseFilter(final PipedWriter inputPipe, final PipedWriter outputPipe1, final PipedWriter outputPipe2) {

		try {

			this.inputPipe = new PipedReader();

			this.inputPipe.connect(inputPipe);

			this.outputPipe1 = outputPipe1;

			this.outputPipe2 = outputPipe2;

			this.dictionary = buildDictionary();

		} catch (IOException e) {

			System.out.println("Error while instantiating MorseFilter");

		}

	}

	private HashMap<Character, String> buildDictionary() {

		final HashMap<Character, String> dictionary = new HashMap<>();

		dictionary.put('[', "]");

		dictionary.put('A', ".-");

		dictionary.put('B', "-...");

		dictionary.put('C', "-.-.");

		dictionary.put('D', "-..");

		dictionary.put('E', ".");

		dictionary.put('F', "..-.");

		dictionary.put('G', "--.");

		dictionary.put('H', "....");

		dictionary.put('I', "..");

		dictionary.put('J', ".---");

		dictionary.put('K', "-.-");

		dictionary.put('L', ".-..");

		dictionary.put('M', "--");

		dictionary.put('N', "-.");

		dictionary.put('O', "---");

		dictionary.put('P', ".--.");

		dictionary.put('Q', "--.-");

		dictionary.put('R', ".-.");

		dictionary.put('S', "...");

		dictionary.put('T', "-");

		dictionary.put('U', "..-");

		dictionary.put('V', "...-");

		dictionary.put('W', ".--");

		dictionary.put('X', "-..-");

		dictionary.put('Y', "-.--");

		dictionary.put('Z', "--..");

		dictionary.put('0', "-----");

		dictionary.put('1', ".----");

		dictionary.put('2', "..---");

		dictionary.put('3', "...--");

		dictionary.put('4', "....-");

		dictionary.put('5', ".....");

		dictionary.put('6', "-....");

		dictionary.put('7', "--...");

		dictionary.put('8', "---..");

		dictionary.put('9', "----.");

		dictionary.put(' ', "/");

		dictionary.put('.', ".-.-.-");

		dictionary.put(',', "--..--");

		dictionary.put(':', "---...");

		dictionary.put('?', "..--..");

		dictionary.put('\'', ".----.");

		dictionary.put('-', "-....-");

		dictionary.put('/', "-..-.");

		dictionary.put(')', "-.--.-");

		dictionary.put('(', "-.--.-");

		dictionary.put('\"', ".-..-.");

		dictionary.put('@', ".--.-.");

		dictionary.put('=', "-...-");

		return dictionary;

	}

	@Override

	public void run() {

		Boolean done;

		int currentByte;

		done = false;

		try {

			while (!done) {

				System.out.println("piped reader");

				System.out.println(inputPipe.toString());

				currentByte = inputPipe.read();

				if (currentByte == -1) {

					done = true;

				} else {

					char currentChar = Character.toUpperCase((char) currentByte);

					String morseLetter = dictionary.get(currentChar);

					// making the buzz sound here

					if (currentChar == ' ') {

						buzzerSound.wordGap();

					} else {

						buzzerSound = new BuzzerSound(morseLetter);

					}

					// outputPipe1.write(morseLetter);

					// outputPipe2.write(morseLetter);

					// outputPipe1.write(' ');

					// outputPipe2.write(' ');

					// outputPipe1.flush();

					// outputPipe2.flush();

				}

			}

			outputPipe1.close();

			outputPipe2.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

}

class BuzzerSound {

	private final static Integer DOT_LENGTH = Integer.parseInt(Main.dotLengthForEncodingSoundTextArea.getText());

	private final static Integer BAR_LENGTH = 3 * DOT_LENGTH;
	// before *3

	private final static Integer TONALITY_SPACE_LENGTH = DOT_LENGTH;
	// before only dot length

	private final static Integer LETTER_SPACE_LENGTH = 2 * DOT_LENGTH;
	// before *3

	private final static Integer WORD_SPACE_LENGTH = 5 * DOT_LENGTH;
	// before *5

	public BuzzerSound(String textToMakeBuzz)

	{

		for (int i = 0; i < textToMakeBuzz.length(); i++) {

			System.out.println("textToMakeBuzz.charAt(i)" + textToMakeBuzz.charAt(i));

			if (textToMakeBuzz.charAt(i) == '.') {

				makingDotBuzz();

			} else if (textToMakeBuzz.charAt(i) == '-') {

				makingDashBuzz();

			}

		}
		letterGap();

	}

	public void makingDotBuzz() {

		Main.pin4out.setState(PinState.HIGH);

		Main.pin3out.setState(PinState.HIGH);

		try {

			Thread.sleep(DOT_LENGTH);

		} catch (InterruptedException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		silenceBetweenTwoCodes();

	}

	public void makingDashBuzz() {

		Main.pin4out.setState(PinState.HIGH);

		Main.pin3out.setState(PinState.HIGH);

		try {

			Thread.sleep(BAR_LENGTH);

		} catch (InterruptedException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		silenceBetweenTwoCodes();

	}

	public void silenceBetweenTwoCodes() {

		Main.pin4out.setState(PinState.LOW);

		Main.pin3out.setState(PinState.LOW);

		try {

			Thread.sleep(TONALITY_SPACE_LENGTH);
			// LETTER_SPACE_LENGTH TONALITY_SPACE_LENGTH

		} catch (InterruptedException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}
		// letterGap();
	}

	public void letterGap() {

		Main.pin4out.setState(PinState.LOW);

		Main.pin3out.setState(PinState.LOW);

		try {

			Thread.sleep(LETTER_SPACE_LENGTH);
			// Thread.sleep(1000);

		} catch (InterruptedException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

	}

	public void wordGap() {

		Main.pin4out.setState(PinState.LOW);

		Main.pin3out.setState(PinState.LOW);

		try {

			Thread.sleep(WORD_SPACE_LENGTH);

		} catch (InterruptedException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

	}

}
