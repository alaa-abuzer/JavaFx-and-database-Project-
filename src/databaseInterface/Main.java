package databaseInterface;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.sql.*;



public class Main extends Application {

	public static void main(String[] args) {
		launch(args);

	}
	public static BorderPane borderPane = new BorderPane();
	static VBox vpane = new VBox();

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) throws ClassNotFoundException,SQLException {
		// Create a pane and set its properties
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/lab8", "root", "");
			Statement stmt = (Statement) con.createStatement();
			VBox vbox = new VBox();
			HBox box = new HBox();
			HBox box2 = new HBox();
			borderPane = new BorderPane();


			borderPane.setCenter(vpane);
			Text text1 = new Text("Welcome !");
			text1.setStyle("-fx-font: 24 arial;");
			vpane.getChildren().add(text1);
			vpane.setAlignment(Pos.CENTER);

			Button student = new Button("student");
			Button department = new Button("department");
			Button course = new Button("course");
			Button advisor = new Button("advisor");
			Button classroom = new Button("classroom");
			Button instructor = new Button("instructor");
			Button section = new Button("section");
			Button takes = new Button("takes");
			Button teaches = new Button("teaches");
			Button time_slot = new Button("time_slot");
			Button prereq = new Button("prereq");
			vbox.setLayoutY(20);
			box.setPadding(new Insets(70, 15, 15, 15));
			box2.setPadding(new Insets(15, 15, 15, 15));
			box.setAlignment(Pos.CENTER);
			box2.setAlignment(Pos.CENTER);
			Text text = new Text("Developed by Alaa Abuzer - alaaabuzer2@gmail.com ");
			text.setStyle("-fx-font: 24 arial;");
			box2.getChildren().add(text);
			box.getChildren().add(student);
			box.getChildren().addAll(department);
			box.getChildren().addAll(course);
			box.getChildren().addAll(advisor);
			box.getChildren().addAll(classroom);
			box.getChildren().addAll(instructor);
			box.getChildren().addAll(section);
			box.getChildren().addAll(teaches);
			box.getChildren().addAll(takes);
			box.getChildren().addAll(time_slot);
			box.getChildren().addAll(prereq);
			vbox.getChildren().add(box);
			vbox.getChildren().add(box2);

			borderPane.setTop(vbox);
			student.setOnAction(e -> {
				GridPane pane = null;
				try {
					Student s = new Student();
					pane = s.studentPane(borderPane, stmt);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				borderPane.setCenter(pane);
			});
			department.setOnAction(e -> {
				GridPane pane = null;
				try {
					pane = Department.debartmentPane(borderPane, stmt);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				borderPane.setCenter(pane);
			});
			course.setOnAction(e -> {
				GridPane pane = null;
				try {
					pane = Course.coursePane(borderPane, stmt);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				borderPane.setCenter(pane);
			});
			instructor.setOnAction(e -> {
				GridPane pane = null;
				try {
					pane = Instructor.instructorPane(borderPane, stmt);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				borderPane.setCenter(pane);
			});
			classroom.setOnAction(e -> {
				GridPane pane = null;
				try {
					pane = ClassRoom.ClassRoomPane(borderPane, stmt);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				borderPane.setCenter(pane);
			});
			advisor.setOnAction(e -> {
				GridPane pane = null;
				try {
					pane = Advisor.advisorPane(borderPane, stmt);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				borderPane.setCenter(pane);
			});
			prereq.setOnAction(e -> {
				GridPane pane = null;
				try {
					pane = PreReq.prereqPane(borderPane, stmt);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				borderPane.setCenter(pane);
			});
			teaches.setOnAction(e -> {
				GridPane pane = null;
				try {
					pane = Teaches.teachesPane(borderPane, stmt);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				borderPane.setCenter(pane);
			});
			section.setOnAction(e -> {
				GridPane pane = null;
				try {
					pane = Section.sectionPane(borderPane, stmt);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				borderPane.setCenter(pane);
			});
			takes.setOnAction(e -> {
				GridPane pane = null;
				try {
					pane = Takes.takesPane(borderPane, stmt);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				borderPane.setCenter(pane);
			});

			time_slot.setOnAction(e -> {
				GridPane pane = null;
				try {
					pane = TimeSlote.timeSlotePane(borderPane, stmt);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				borderPane.setCenter(pane);
			});

		} catch (SQLException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(borderPane);

		primaryStage.setTitle("University DataBase"); // Set the stage title
		primaryStage.setWidth(1000);
		primaryStage.setHeight(800);

		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}

}