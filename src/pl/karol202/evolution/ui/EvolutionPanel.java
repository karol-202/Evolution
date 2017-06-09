package pl.karol202.evolution.ui;

import pl.karol202.evolution.entity.Entities;
import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.utils.Gradient;
import pl.karol202.evolution.utils.Utils;
import pl.karol202.evolution.world.OnWorldUpdateListener;
import pl.karol202.evolution.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class EvolutionPanel extends JPanel implements OnWorldUpdateListener, MouseWheelListener, MouseMotionListener, MouseListener
{
	public interface OnViewParametersChangeListener
	{
		void onViewParametersChanged();
		
		void onEntitySelectionChanged();
	}
	
	private static final double SQRT2 = Math.sqrt(2);
	private static final double MIN_ZOOM = 0.03125f;
	private static final double MAX_ZOOM = 16;
	
	private World world;
	private Entities entities;
	private OnViewParametersChangeListener viewListener;
	
	private Gradient temperatureGradient;
	private BufferedImage temperatureImage;
	private Gradient humidityGradient;
	private BufferedImage humidityImage;
	
	private ViewMode viewMode;
	private double scale;
	private int xPosition;
	private int yPosition;
	
	private int draggingStartX;
	private int draggingStartY;
	private int xPosAtDraggingStart;
	private int yPosAtDraggingStart;
	
	private int mouseX;
	private int mouseY;
	private Entity hoveredEntity;
	
	public EvolutionPanel(World world, OnViewParametersChangeListener listener)
	{
		this.world = world;
		this.entities = world.getEntities();
		this.viewListener = listener;
		this.viewMode = ViewMode.STANDARD;
		this.scale = 1;
		this.xPosition = 0;
		this.yPosition = 0;
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		world.addListener(this);
		initTemperatureGradient();
		initHumidityGradient();
		createTemperatureImage();
		createHumidityImage();
	}
	
	private void initTemperatureGradient()
	{
		temperatureGradient = new Gradient();
		temperatureGradient.addColor(new Color(0.96f, 0.04f, 0f), 45);
		temperatureGradient.addColor(new Color(1f, 0.27f, 0.02f), 31);
		temperatureGradient.addColor(new Color(0.8f, 0.87f, 0.02f), 18);
		temperatureGradient.addColor(new Color(0.42f, 0.91f, 0f), 8);
		temperatureGradient.addColor(new Color(0f, 0.85f, 0.6f), -4);
		temperatureGradient.addColor(new Color(0.1f, 0.28f, 1f), -20);
	}
	
	private void initHumidityGradient()
	{
		humidityGradient = new Gradient();
		humidityGradient.addColor(Color.WHITE, 0);
		humidityGradient.addColor(new Color(0.1f, 0.28f, 1f), 100);
	}
	
	private void createTemperatureImage()
	{
		temperatureImage = new BufferedImage(world.getWidth(), world.getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < temperatureImage.getWidth(); x++)
		{
			for(int y = 0; y < temperatureImage.getHeight(); y++)
			{
				float temperature = world.getTemperature()[x][y];
				int color = temperatureGradient.getColorAtPosition(temperature).getRGB();
				temperatureImage.setRGB(x, y, color);
			}
		}
	}
	
	private void createHumidityImage()
	{
		humidityImage = new BufferedImage(world.getWidth(), world.getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < humidityImage.getWidth(); x++)
		{
			for(int y = 0; y < humidityImage.getHeight(); y++)
			{
				float humidity = world.getHumidity()[x][y];
				int color = humidityGradient.getColorAtPosition(humidity).getRGB();
				humidityImage.setRGB(x, y, color);
			}
		}
	}
	
	@Override
	public void onWorldUpdated()
	{
		createTemperatureImage();
		createHumidityImage();
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		setGraphicsParams(g);
		drawBackground(g);
		drawBorder(g);
		setClipping(g);
		drawEntities(g);
	}
	
	private void setGraphicsParams(Graphics2D g)
	{
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	private void drawBackground(Graphics2D g)
	{
		if(viewMode == ViewMode.STANDARD) drawStandardBackground(g);
		else if(viewMode == ViewMode.TEMPERATURE) drawTemperature(g);
		else if(viewMode == ViewMode.HUMIDITY) drawHumidity(g);
	}
	
	private void drawStandardBackground(Graphics2D g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(xPosition, yPosition, getScaledWorldWidth(), getScaledWorldHeight());
	}
	
	private void drawTemperature(Graphics2D g)
	{
		g.drawImage(temperatureImage, xPosition, yPosition, getScaledWorldWidth(), getScaledWorldHeight(), null);
	}
	
	private void drawHumidity(Graphics2D g)
	{
		g.drawImage(humidityImage, xPosition, yPosition, getScaledWorldWidth(), getScaledWorldHeight(), null);
	}
	
	private void drawBorder(Graphics2D g)
	{
		g.setColor(Color.DARK_GRAY);
		g.setStroke(new BasicStroke(2));
		g.drawRect(xPosition - 1, yPosition - 1, getScaledWorldWidth() + 1, getScaledWorldHeight() + 1);
	}
	
	private void setClipping(Graphics2D g)
	{
		g.clipRect(xPosition, yPosition, getScaledWorldWidth(), getScaledWorldHeight());
	}
	
	private void drawEntities(Graphics2D g)
	{
		hoveredEntity = null;
		for(Entity entity : entities.getEntities()) drawEntity(g, entity);
	}
	
	private void drawEntity(Graphics2D g, Entity entity)
	{
		Rectangle bounds = getEntityBounds(entity);
		boolean hovered = isHovered(bounds);
		if(hovered) hoveredEntity = entity;
		g.setColor(hovered ? new Color(216, 216, 216) : Color.WHITE);
		g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
		g.setColor(Color.DARK_GRAY);
		g.setStroke(new BasicStroke(1));
		g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
	private Rectangle getEntityBounds(Entity entity)
	{
		int size = (int) (20 * scale);
		int x = (int) (entity.getX() * scale) + xPosition - (size / 2);
		int y = (int) (entity.getY() * scale) + yPosition - (size / 2);
		return new Rectangle(x, y, size, size);
	}
	
	private boolean isHovered(Rectangle rectangle)
	{
		double x = mouseX - rectangle.getCenterX();
		double y = mouseY - rectangle.getCenterY();
		double distance = Math.sqrt((x * x) + (y * y));
		return distance < rectangle.width / 2;
	}
	
	private int getScaledWorldWidth()
	{
		return (int) Math.round(world.getWidth() * scale);
	}
	
	private int getScaledWorldHeight()
	{
		return (int) Math.round(world.getHeight() * scale);
	}
	
	public void setViewMode(ViewMode viewMode)
	{
		this.viewMode = viewMode;
		repaint();
	}
	
	public double getScale()
	{
		return scale;
	}
	
	public void scaleDown()
	{
		double oldScale = scale;
		scaleDownInternal();
		applyScalingToPosition(oldScale);
	}
	
	private void scaleDownInternal()
	{
		scale = getLowerZoom();
		if(scale < MIN_ZOOM) scale = MIN_ZOOM;
		
		repaint();
		if(viewListener != null) viewListener.onViewParametersChanged();
	}
	
	public void scaleUp()
	{
		double oldScale = scale;
		scaleUpInternal();
		applyScalingToPosition(oldScale);
	}
	
	private void scaleUpInternal()
	{
		scale = getGreaterZoom();
		if(scale > MAX_ZOOM) scale = MAX_ZOOM;
		
		repaint();
		if(viewListener != null) viewListener.onViewParametersChanged();
	}
	
	private double getLowerZoom()
	{
		int position = 0;
		if(scale > 1)
		{
			double lower = 1;
			while(true)
			{
				double next = calculateZoomRatio(++position);
				if(next >= scale) return lower;
				lower = next;
			}
		}
		else
		{
			while(true)
			{
				double next = calculateZoomRatio(--position);
				if(next < scale) return next;
			}
		}
	}
	
	private double getGreaterZoom()
	{
		int position = 0;
		if(scale < 1)
		{
			double greater = 1;
			while(true)
			{
				double next = calculateZoomRatio(--position);
				if(next <= scale) return greater;
				greater = next;
			}
		}
		else
		{
			while(true)
			{
				double next = calculateZoomRatio(++position);
				if(next > scale) return next;
			}
		}
	}
	
	private double calculateZoomRatio(int position)
	{
		int posAbs = Math.abs(position);
		double fract = Math.pow(SQRT2, posAbs);
		double round = Math.round(fract * 2) / 2f;
		if(position >= 0) return round;
		else return 1 / round;
	}
	
	private void applyScalingToPosition(double oldScale)
	{
		applyScalingToPosition(oldScale, getWidth() / 2, getHeight() / 2);
	}
	
	private void applyScalingToPosition(double oldScale, int focusX, int focusY)
	{
		int scaledWorldSizeX = getScaledWorldWidth();
		int scaledWorldSizeY = getScaledWorldHeight();
		int oldScaledWorldSizeX = (int) Math.round(world.getWidth() * oldScale);
		int oldScaledWorldSizeY = (int) Math.round(world.getHeight() * oldScale);
		float xMultiplier = Utils.map(focusX, xPosition, xPosition + oldScaledWorldSizeX, 0, 1);
		float yMultiplier = Utils.map(focusY, yPosition, yPosition + oldScaledWorldSizeY, 0, 1);
		xPosition -= Math.round((scaledWorldSizeX - oldScaledWorldSizeX) * xMultiplier);
		yPosition -= Math.round((scaledWorldSizeY - oldScaledWorldSizeY) * yMultiplier);
	}
	
	public void centerView()
	{
		xPosition = (getWidth() / 2) - (getScaledWorldWidth() / 2);
		yPosition = (getHeight() / 2) - (getScaledWorldHeight() / 2);
		repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(hoveredEntity != null)
		{
			entities.selectEntity(hoveredEntity);
			viewListener.onEntitySelectionChanged();
		}
		else
		{
			entities.selectNothing();
			viewListener.onEntitySelectionChanged();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		draggingStartX = e.getX();
		draggingStartY = e.getY();
		xPosAtDraggingStart = xPosition;
		yPosAtDraggingStart = yPosition;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) { }
	
	@Override
	public void mouseEntered(MouseEvent e) { }
	
	@Override
	public void mouseExited(MouseEvent e) { }
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		xPosition = xPosAtDraggingStart + (e.getX() - draggingStartX);
		yPosition = yPosAtDraggingStart + (e.getY() - draggingStartY);
		if(xPosition < -getScaledWorldWidth()) xPosition = -getScaledWorldWidth();
		if(yPosition < -getScaledWorldHeight()) yPosition = -getScaledWorldHeight();
		if(xPosition > getWidth()) xPosition = getWidth();
		if(yPosition > getHeight()) yPosition = getHeight();
		
		mouseX = e.getX();
		mouseY = e.getY();
		repaint();
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
		repaint();
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		if(e.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL) return;
		int rotation = e.getWheelRotation();
		
		double oldScale = scale;
		if(rotation > 0) scaleDownInternal();
		else scaleUpInternal();
		applyScalingToPosition(oldScale, e.getX(), e.getY());
	}
}