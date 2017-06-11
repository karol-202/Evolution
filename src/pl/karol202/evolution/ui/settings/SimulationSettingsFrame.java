package pl.karol202.evolution.ui.settings;

import pl.karol202.evolution.simulation.Simulation;
import pl.karol202.evolution.world.World;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class SimulationSettingsFrame extends JFrame implements DocumentListener
{
	private static final Integer[] FREQUENCY_VALUES = { 256, 512, 1024, 2048, 4096, 8192, 16384, 32768 };
	private static final int MIN_TIMESTEP = 1;
	private static final int MAX_TIMESTEP = 30;
	
	private World world;
	private Simulation simulation;
	
	private JLabel labelSize;
	private JLabel labelX;
	private JTextField fieldX;
	private JLabel labelY;
	private JTextField fieldY;
	
	private JLabel labelTemperatureFrequency;
	private JComboBox<Integer> comboBoxTemperatureFrequency;
	
	private JLabel labelHumidityFrequency;
	private JComboBox<Integer> comboBoxHumidityFrequency;
	
	private JLabel labelTemperatureRange;
	private JTextField fieldMinTemperature;
	private JLabel labelTemperatureDash;
	private JTextField fieldMaxTemperature;
	
	private JLabel labelHumidityRange;
	private JTextField fieldMinHumidity;
	private JLabel labelHumidityDash;
	private JTextField fieldMaxHumidity;
	
	private JLabel labelTimeStep;
	private JSlider sliderTimeStep;
	
	private JButton buttonCancel;
	private JButton buttonOK;
	
	public SimulationSettingsFrame(World world, Simulation simulation)
	{
		super("Ustawienia symulacji");
		this.world = world;
		this.simulation = simulation;
		
		setFrameParams();
		initSizeSection();
		initTemperatureFrequencySection();
		initHumidityFrequencySection();
		initTemperatureRangeSection();
		initHumidityRangeSection();
		initTimeSection();
		initFooter();
		pack();
	}
	
	private void setFrameParams()
	{
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setLayout(new GridBagLayout());
		setVisible(true);
	}
	
	private void initSizeSection()
	{
		initSizeLabel();
		initXLabel();
		initXField();
		initYLabel();
		initYField();
	}
	
	private void initSizeLabel()
	{
		labelSize = new JLabel("Rozmiar");
		add(labelSize, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(7, 8, 0, 10),
				0, 0));
	}
	
	private void initXLabel()
	{
		labelX = new JLabel("X");
		add(labelX, new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(7, 0, 0, 0),
				0, 0));
	}
	
	private void initXField()
	{
		fieldX = new JTextField(Integer.toString(world.getWidth()), 10);
		fieldX.getDocument().addDocumentListener(this);
		add(fieldX, new GridBagConstraints(2, 0, 1, 1, 1, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(7, 0, 0, 7),
				0, 0));
	}
	
	private void initYLabel()
	{
		labelY = new JLabel("Y");
		add(labelY, new GridBagConstraints(3, 0, 1, 1, 0, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(7, 0, 0, 0),
				0, 0));
	}
	
	private void initYField()
	{
		fieldY = new JTextField(Integer.toString(world.getHeight()), 10);
		fieldY.getDocument().addDocumentListener(this);
		add(fieldY, new GridBagConstraints(4, 0, 2, 1, 1, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(7, 0, 0, 5),
				0, 0));
	}
	
	private void initTemperatureFrequencySection()
	{
		initTemperatureFrequencyLabel();
		initTemperatureFrequencyComboBox();
	}
	
	private void initTemperatureFrequencyLabel()
	{
		labelTemperatureFrequency = new JLabel("Częstotliwość generatora temperatury");
		add(labelTemperatureFrequency, new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(7, 8, 0, 5),
				0, 0));
	}
	
	private void initTemperatureFrequencyComboBox()
	{
		comboBoxTemperatureFrequency = new JComboBox<>(FREQUENCY_VALUES);
		comboBoxTemperatureFrequency.setSelectedIndex(getIndexOfFrequencyValue(world.getTemperatureFrequency()));
		add(comboBoxTemperatureFrequency, new GridBagConstraints(2, 1, 4, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(7, 0, 0, 5),
				0, 0));
	}
	
	private void initHumidityFrequencySection()
	{
		initHumidityFrequencyLabel();
		initHumidityFrequencyComboBox();
	}
	
	private void initHumidityFrequencyLabel()
	{
		labelHumidityFrequency = new JLabel("Częstotliwość generatora wilgotności");
		add(labelHumidityFrequency, new GridBagConstraints(0, 2, 1, 1, 0, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(7, 8, 0, 5),
				0, 0));
	}
	
	private void initHumidityFrequencyComboBox()
	{
		comboBoxHumidityFrequency = new JComboBox<>(FREQUENCY_VALUES);
		comboBoxHumidityFrequency.setSelectedIndex(getIndexOfFrequencyValue(world.getHumidityFrequency()));
		add(comboBoxHumidityFrequency, new GridBagConstraints(2, 2, 4, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(7, 0, 0, 5),
				0, 0));
	}
	
	private int getIndexOfFrequencyValue(int frequency)
	{
		for(int i = 0; i < FREQUENCY_VALUES.length; i++)
			if(FREQUENCY_VALUES[i] == frequency) return i;
		return -1;
	}
	
	private void initTemperatureRangeSection()
	{
		initTemperatureRangeLabel();
		initTemperatureRangeMinField();
		initTemperatureRangeDashLabel();
		initTemperatureRangeMaxField();
	}
	
	private void initTemperatureRangeLabel()
	{
		labelTemperatureRange = new JLabel("Zakres temperatury");
		add(labelTemperatureRange, new GridBagConstraints(0, 3, 1, 1, 0, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(7, 8, 0, 5),
				0, 0));
	}
	
	private void initTemperatureRangeMinField()
	{
		fieldMinTemperature = new JTextField(Float.toString(world.getMinTemperature()), 6);
		fieldMinTemperature.getDocument().addDocumentListener(this);
		add(fieldMinTemperature, new GridBagConstraints(2, 3, 1, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(7, 0, 0, 0),
				0, 0));
	}
	
	private void initTemperatureRangeDashLabel()
	{
		labelTemperatureDash = new JLabel("-");
		add(labelTemperatureDash, new GridBagConstraints(3, 3, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(7, 4, 0, 4),
				0, 0));
	}
	
	private void initTemperatureRangeMaxField()
	{
		fieldMaxTemperature = new JTextField(Float.toString(world.getMaxTemperature()), 6);
		fieldMaxTemperature.getDocument().addDocumentListener(this);
		add(fieldMaxTemperature, new GridBagConstraints(4, 3, 2, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(7, 0, 0, 5),
				0, 0));
	}
	
	private void initHumidityRangeSection()
	{
		initHumidityRangeLabel();
		initHumidityRangeMinField();
		initHumidityRangeDashLabel();
		initHumidityRangeMaxField();
	}
	
	private void initHumidityRangeLabel()
	{
		labelHumidityRange = new JLabel("Zakres wilgotności");
		add(labelHumidityRange, new GridBagConstraints(0, 4, 1, 1, 0, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(7, 8, 0, 5),
				0, 0));
	}
	
	private void initHumidityRangeMinField()
	{
		fieldMinHumidity = new JTextField(Float.toString(world.getMinHumidity()), 6);
		fieldMinHumidity.getDocument().addDocumentListener(this);
		add(fieldMinHumidity, new GridBagConstraints(2, 4, 1, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(7, 0, 0, 0),
				0, 0));
	}
	
	private void initHumidityRangeDashLabel()
	{
		labelHumidityDash = new JLabel("-");
		add(labelHumidityDash, new GridBagConstraints(3, 4, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(7, 4, 0, 4),
				0, 0));
	}
	
	private void initHumidityRangeMaxField()
	{
		fieldMaxHumidity = new JTextField(Float.toString(world.getMaxHumidity()), 6);
		fieldMaxHumidity.getDocument().addDocumentListener(this);
		add(fieldMaxHumidity, new GridBagConstraints(4, 4, 2, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(7, 0, 0, 5),
				0, 0));
	}
	
	private void initTimeSection()
	{
		initTimeStepLabel();
		initTimeStepSlider();
	}
	
	private void initTimeStepLabel()
	{
		labelTimeStep = new JLabel("Jednostka czasu");
		add(labelTimeStep, new GridBagConstraints(0, 5, 1, 1, 0, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(-10, 8, 0, 10),
				0, 0));
	}
	
	private void initTimeStepSlider()
	{
		sliderTimeStep = new JSlider(MIN_TIMESTEP, MAX_TIMESTEP, simulation.getTimeStep());
		sliderTimeStep.setMajorTickSpacing(29);
		sliderTimeStep.setMinorTickSpacing(5);
		sliderTimeStep.setPaintLabels(true);
		sliderTimeStep.setPaintTicks(true);
		sliderTimeStep.setFocusable(false);
		add(sliderTimeStep, new GridBagConstraints(1, 5, 5, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 0, 5),
				0, 0));
	}
	
	private void initFooter()
	{
		initCancelButton();
		initOKButton();
	}
	
	private void initCancelButton()
	{
		buttonCancel = new JButton("Anuluj");
		buttonCancel.addActionListener(e -> closeFrame());
		add(buttonCancel, new GridBagConstraints(2, 6, 3, 1, 1, 0,
				GridBagConstraints.LINE_END, GridBagConstraints.VERTICAL, new Insets(3, 0, 5, 2),
				0, 0));
	}
	
	private void initOKButton()
	{
		buttonOK = new JButton("OK");
		buttonOK.addActionListener(e -> applySettings());
		add(buttonOK, new GridBagConstraints(5, 6, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 2, 5, 5),
				0, 0));
	}
	
	private void closeFrame()
	{
		setVisible(false);
		dispose();
	}
	
	private void applySettings()
	{
		applyFrequencies();
		applyRanges();
		applyTimestep();
		applySize();
		closeFrame();
	}
	
	private void applySize()
	{
		int width = Integer.parseInt(fieldX.getText());
		int height = Integer.parseInt(fieldY.getText());
		world.generateWorld(width, height);
	}
	
	private void applyFrequencies()
	{
		world.setTemperatureFrequency((int) comboBoxTemperatureFrequency.getSelectedItem());
		world.setHumidityFrequency((int) comboBoxHumidityFrequency.getSelectedItem());
	}
	
	private void applyRanges()
	{
		float minTemperature = Float.parseFloat(fieldMinTemperature.getText());
		world.setMinTemperature(minTemperature);
		
		float maxTemperature = Float.parseFloat(fieldMaxTemperature.getText());
		world.setMaxTemperature(maxTemperature);
		
		float minHumidity = Float.parseFloat(fieldMinHumidity.getText());
		world.setMinHumidity(minHumidity);
		
		float maxHumidity = Float.parseFloat(fieldMaxHumidity.getText());
		world.setMaxHumidity(maxHumidity);
	}
	
	private void applyTimestep()
	{
		int timeStep = sliderTimeStep.getValue();
		if(!simulation.isRunning()) simulation.setTimeStep(timeStep);
	}
	
	@Override
	public void insertUpdate(DocumentEvent e)
	{
		updateOKButton();
	}
	
	@Override
	public void removeUpdate(DocumentEvent e)
	{
		updateOKButton();
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) { }
	
	private void updateOKButton()
	{
		boolean dataCorrect = isTextValidSize(fieldX) && isTextValidSize(fieldY) &&
							  isTextValidFloat(fieldMinTemperature) && isTextValidFloat(fieldMaxTemperature) &&
							  isTextValidFloat(fieldMinHumidity) && isTextValidFloat(fieldMaxHumidity);
		buttonOK.setEnabled(dataCorrect);
	}
	
	private boolean isTextValidSize(JTextField field)
	{
		try
		{
			int i = Integer.parseInt(field.getText());
			return i > 0 && i <= World.MAX_SIZE;
		}
		catch(NumberFormatException ex)
		{
			return false;
		}
	}
	
	private boolean isTextValidFloat(JTextField field)
	{
		try
		{
			Float.parseFloat(field.getText());
			return true;
		}
		catch(NumberFormatException ex)
		{
			return false;
		}
	}
}