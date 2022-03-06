
// Name: Jason Younan
// Description: Implement the JavaFX GUI “Sketchy” to draw lines, circles, and rectangles with user-defined stroke width, stroke color, and fill color.
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class SketchPane extends BorderPane {
//Task 1: Declare all instance variables listed in UML diagram
	private ArrayList<Shape> shapeList;
	private ArrayList<Shape> tempList;
	private Button undoButton;
	private Button eraseButton;
	private Label fillColorLabel;
	private Label strokeWidthLabel;
	private Label strokeColorLabel;
	private ComboBox<String> fillColorCombo;
	private ComboBox<String> strokeWidthCombo;
	private ComboBox<String> strokeColorCombo;
	private RadioButton radioButtonLine;
	private RadioButton radioButtonRectangle;
	private RadioButton radioButtonCircle;
	private Pane sketchCanvas;
	private Color[] colors;
	private String[] strokeWidth;
	private String[] colorLabels;
	private Color currentStrokeColor;
	private Color currentFillColor;
	private int currentStrokeWidth;
	private Line line;
	private Circle circle;
	private Rectangle rectangle;
	private double x1;
	private double y1;

//Task 2: Implement the constructor
	public SketchPane() {
		undoButton = new Button("Undo");
		eraseButton = new Button("Erase");

		undoButton.setOnAction(new ButtonHandler());
		eraseButton.setOnAction(new ButtonHandler());

		fillColorCombo = new ComboBox<String>();
		strokeWidthCombo = new ComboBox<String>();
		strokeColorCombo = new ComboBox<String>();

		shapeList = new ArrayList<Shape>();
		tempList = new ArrayList<Shape>();

		ToggleGroup group = new ToggleGroup();

		radioButtonLine = new RadioButton("Line");
		radioButtonRectangle = new RadioButton("Rectangle");
		radioButtonCircle = new RadioButton("Circle");

		radioButtonLine.setToggleGroup(group);
		radioButtonRectangle.setToggleGroup(group);
		radioButtonCircle.setToggleGroup(group);

// Colors, labels, and stroke widths that are available to the user
		colors = new Color[] { Color.BLACK, Color.GREY, Color.YELLOW, Color.GOLD, Color.ORANGE, Color.DARKRED,
				Color.PURPLE, Color.HOTPINK, Color.TEAL, Color.DEEPSKYBLUE, Color.LIME };
		colorLabels = new String[] { "black", "grey", "yellow", "gold", "orange", "dark red", "purple", "hot pink",
				"teal", "deep sky blue", "lime" };
		fillColorLabel = new Label("Fill Color:");
		strokeColorLabel = new Label("Stroke Color:");
		strokeWidthLabel = new Label("Stroke Width:");
		strokeWidth = new String[] { "1", "3", "5", "7", "9", "11", "13" };

		fillColorCombo.getItems().addAll(colorLabels);
		strokeColorCombo.getItems().addAll(colorLabels);
		strokeWidthCombo.getItems().addAll(strokeWidth);

		fillColorCombo.getSelectionModel().select(colorLabels[0]);
		strokeColorCombo.getSelectionModel().select(colorLabels[0]);
		strokeWidthCombo.getSelectionModel().select(strokeWidth[0]);

		currentStrokeColor = colors[0];
		currentFillColor = colors[0];
		currentStrokeWidth = Integer.parseInt(strokeWidth[0]);

		fillColorCombo.setOnAction(new ColorHandler());
		strokeColorCombo.setOnAction(new ColorHandler());
		strokeWidthCombo.setOnAction(new WidthHandler());

		sketchCanvas = new Pane();
		sketchCanvas.setStyle("-fx-Background-color: white");

		HBox hBoxTop = new HBox(20);
		hBoxTop.setMinSize(20, 40);
		hBoxTop.setAlignment(Pos.CENTER);
		hBoxTop.setStyle("-fx-Background-color: lightgrey");

		hBoxTop.getChildren().add(fillColorLabel);
		hBoxTop.getChildren().add(fillColorCombo);
		hBoxTop.getChildren().add(strokeWidthLabel);
		hBoxTop.getChildren().add(strokeWidthCombo);
		hBoxTop.getChildren().add(strokeColorLabel);
		hBoxTop.getChildren().add(strokeColorCombo);

		HBox hBoxBottom = new HBox(20);
		hBoxBottom.setMinSize(20, 40);
		hBoxBottom.setAlignment(Pos.CENTER);
		hBoxBottom.setStyle("-fx-Background-color: lightgrey");

		hBoxBottom.getChildren().add(radioButtonLine);
		hBoxBottom.getChildren().add(radioButtonRectangle);
		hBoxBottom.getChildren().add(radioButtonCircle);
		hBoxBottom.getChildren().add(undoButton);
		hBoxBottom.getChildren().add(eraseButton);

		this.setCenter(sketchCanvas);
		this.setTop(hBoxTop);
		this.setBottom(hBoxBottom);

		sketchCanvas.setOnMousePressed(new MouseHandler());
		sketchCanvas.setOnMouseDragged(new MouseHandler());
		sketchCanvas.setOnMouseReleased(new MouseHandler());

		x1 = 0;
		y1 = 0;
	}

	private class MouseHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			// TASK 3: Implement the mouse handler for Circle and Line
			// Rectangle Example given!
			if (radioButtonRectangle.isSelected()) {
				// Mouse is pressed
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
					x1 = event.getX();
					y1 = event.getY();
					rectangle = new Rectangle();
					rectangle.setX(x1);
					rectangle.setY(y1);
					shapeList.add(rectangle);
					rectangle.setFill(Color.WHITE);
					rectangle.setStroke(Color.BLACK);
					sketchCanvas.getChildren().add(rectangle);
				}
				// Mouse is dragged
				else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					if (event.getX() - x1 < 0)
						rectangle.setX(event.getX());
					if (event.getY() - y1 < 0)
						rectangle.setY(event.getY());
					rectangle.setWidth(Math.abs(event.getX() - x1));
					rectangle.setHeight(Math.abs(event.getY() - y1));

				}
				// Mouse is released
				else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
					rectangle.setFill(currentFillColor);
					rectangle.setStroke(currentStrokeColor);
					rectangle.setStrokeWidth(currentStrokeWidth);
				}
			} else if (radioButtonCircle.isSelected()) {
				// Mouse is pressed
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
					x1 = event.getX();
					y1 = event.getY();
					circle = new Circle();
					circle.setCenterX(x1);
					circle.setCenterY(y1);
					shapeList.add(circle);
					circle.setFill(Color.WHITE);
					circle.setStroke(Color.BLACK);
					sketchCanvas.getChildren().add(circle);
				}
				// Mouse is dragged
				else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					if (event.getX() - x1 < 0)
						circle.setCenterX(event.getX());
					if (event.getY() - y1 < 0)
						circle.setCenterY(event.getY());
					circle.setRadius(getDistance(x1, y1, event.getX(), event.getY()));
				}
				// Mouse is released
				else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
					circle.setFill(currentFillColor);
					circle.setStroke(currentStrokeColor);
					circle.setStrokeWidth(currentStrokeWidth);
				}
			} else if (radioButtonLine.isSelected()) {
				// Mouse is pressed
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
					x1 = event.getX();
					y1 = event.getY();
					line = new Line();
					line.setStartX(x1);
					line.setStartY(y1);
					line.setEndX(x1);
					line.setEndY(y1);
					shapeList.add(line);
					line.setFill(Color.WHITE);
					line.setStroke(Color.BLACK);
					sketchCanvas.getChildren().add(line);
				}
				// Mouse is dragged
				else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					line.setEndX(Math.abs(event.getX()));
					line.setEndY(Math.abs(event.getY()));
				}
				// Mouse is released
				else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
					line.setFill(currentFillColor);
					line.setStroke(currentStrokeColor);
					line.setStrokeWidth(currentStrokeWidth);
				}
			}
		}
	}

	private class ButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// TASK 4: Implement the button handler
			if (event.getSource().equals(undoButton)) {
				if (!shapeList.isEmpty()) {
					shapeList.remove(shapeList.size() - 1);
					sketchCanvas.getChildren().remove(sketchCanvas.getChildren().size() - 1);
				} else {
					for (int i = 0; i < tempList.size(); i++) {
						shapeList.add(tempList.get(i));
					}
					tempList.clear();
					for (int i = 0; i < shapeList.size(); i++) {
						sketchCanvas.getChildren().add(shapeList.get(i));
					}
				}
			} else if (event.getSource().equals(eraseButton))
				if (!shapeList.isEmpty()) {
					tempList.clear();
					for (int i = 0; i < shapeList.size(); i++) {
						tempList.add(shapeList.get(i));
					}
					shapeList.clear();
					sketchCanvas.getChildren().clear();
				}
		}
	}

	private class ColorHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
// TASK 5: Implement the color handler
			if (event.getSource().equals(strokeColorCombo))
				currentStrokeColor = colors[strokeColorCombo.getSelectionModel().getSelectedIndex()];
			else if (event.getSource().equals(fillColorCombo))
				currentFillColor = colors[fillColorCombo.getSelectionModel().getSelectedIndex()];
		}
	}

	private class WidthHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
// TASK 6: Implement the stroke width handler
			if (event.getSource().equals(strokeWidthCombo))
				currentStrokeWidth = Integer
						.parseInt(strokeWidth[strokeWidthCombo.getSelectionModel().getSelectedIndex()]);
		}
	}

// Get the Euclidean distance between (x1,y1) and (x2,y2)
	private double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
}