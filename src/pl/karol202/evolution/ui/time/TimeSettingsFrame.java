package pl.karol202.evolution.ui.time;

import pl.karol202.evolution.simulation.Simulation;

import javax.swing.*;
import java.awt.*;

public class TimeSettingsFrame extends JFrame
{
	private static final int MIN_TIMESTEP = 1;
	private static final int MAX_TIMESTEP = 100;
	
	private Simulation simulation;
	
	private JLabel labelTimestep;
	private JSlider sliderTimeStep;
	
	private JButton buttonCancel;
	private JButton buttonOK;
	
	public TimeSettingsFrame(Simulation simulation)
	{
		super("Ustawienia czasu");
		this.simulation = simulation;
		
		setFrameParams();
		initTimeStepLabel();
		initTimeStepSlider();
		initCancelButton();
		initOKButton();
		pack();
	}
	
	private void setFrameParams()
	{
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setLayout(new GridBagLayout());
		setVisible(true);
	}
	
	private void initTimeStepLabel()
	{
		labelTimestep = new JLabel("Jednostka czasu");
		add(labelTimestep, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.PAGE_START, GridBagConstraints.NONE, new Insets(7, 8, 0, 0),
				0, 0));
	}
	
	private void initTimeStepSlider()
	{
		sliderTimeStep = new JSlider(MIN_TIMESTEP, MAX_TIMESTEP, simulation.getTimeStep());
		sliderTimeStep.setMajorTickSpacing(99);
		sliderTimeStep.setMinorTickSpacing(10);
		sliderTimeStep.setPaintLabels(true);
		sliderTimeStep.setPaintTicks(true);
		sliderTimeStep.setFocusable(false);
		add(sliderTimeStep, new GridBagConstraints(1, 0, 2, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 0, 5),
				0, 0));
	}
	
	private void initCancelButton()
	{
		buttonCancel = new JButton("Anuluj");
		buttonCancel.addActionListener(e -> closeFrame());
		add(buttonCancel, new GridBagConstraints(1, 1, 1, 1, 1, 0,
				GridBagConstraints.LINE_END, GridBagConstraints.VERTICAL, new Insets(3, 0, 5, 2),
				0, 0));
	}
	
	private void initOKButton()
	{
		buttonOK = new JButton("OK");
		buttonOK.addActionListener(e -> applySettings());
		add(buttonOK, new GridBagConstraints(2, 1, 1, 1, 0, 0,
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
		int timeStep = sliderTimeStep.getValue();
		if(!simulation.isRunning()) simulation.setTimeStep(timeStep);
		closeFrame();
	}
}