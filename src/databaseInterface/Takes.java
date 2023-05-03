package databaseInterface;

import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.stage.Stage;
import javafx.util.Callback;

public class Takes {


    public static ObservableList<ObservableList> data;

    public static TableView tableview;

	public static GridPane takesPane(BorderPane borderPane,Statement stmt) throws ClassNotFoundException, SQLException {
		GridPane pane =new GridPane();
	        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
	        pane.setHgap(5.5);
	        pane.setVgap(5.5);
	        borderPane.setBottom(null);

	        tableview = new TableView();
			TextField id =new TextField();
			TextField course_id =new TextField();
			TextField sec_id =new TextField();
			TextField semester =new TextField();
			TextField year =new TextField();
			TextField grade =new TextField();

			pane.add(new Label(" ID:"), 0, 0);
			pane.add(id, 1, 0);
			pane.add(new Label("course id:"), 0, 1);
			pane.add(course_id, 1, 1);
			pane.add(new Label("section id:"), 0, 2);
			pane.add(sec_id, 1, 2);
			pane.add(new Label("semester:"), 0, 3);
			pane.add(semester, 1, 3);
			pane.add(new Label("year:"), 0, 4);
			pane.add(year, 1, 4);
			pane.add(new Label("grades:"), 0, 5);
			pane.add(grade, 1, 5);
			Button btAdd = new Button("Add");
			Button btsrh = new Button("Search");
			pane.add(btAdd, 1, 6);
			pane.add(btsrh, 0, 6);

			btAdd.setOnAction(e->{
				try {
					String query2="INSERT INTO `takes`(`ID`, `course_id`, `sec_id`, `semester`, `year`, `grade` )"
							+ " VALUES ('"+id.getText()+"','"+course_id.getText()+"','"+sec_id.getText()+"'"+semester.getText()
							+ "',"+year.getText()+",'"+grade.getText()+"')";
					
					System.out.println(query2);
					stmt.executeUpdate(query2);
					
				}catch(SQLException e2){ 

					Alert err = new Alert(AlertType.ERROR);
					err.setHeaderText("ERROR OCCUERS");
					err.show();

				e2.printStackTrace();
				} 
				
			});
			
			
			btsrh.setOnAction(e->{
				StringBuilder sql = new StringBuilder("SELECT * from `takes` ");
				if(!id.getText().isEmpty()||!grade.getText().isEmpty()||!semester.getText().isEmpty()||!year.getText().isEmpty()||!course_id.getText().isEmpty()||!sec_id.getText().isEmpty()) {
					sql.append("where ");
			        if(!id.getText().isEmpty()) {
			        	sql.append("ID='"+id.getText()+"' AND");
			        }
			        if(!course_id.getText().isEmpty()) {
			    	sql.append(" `course_id`= '"+course_id.getText()+"' AND");
			        }
			        if(!sec_id.getText().isEmpty()) {
			    	sql.append(" `sec_id`= '"+sec_id.getText()+"' AND");
			        }
			        if(!semester.getText().isEmpty()) {
			    	sql.append(" `semester`= '"+semester.getText()+"' AND");
			        }
			        if(!year.getText().isEmpty()) {
			    	sql.append(" `year`= "+year.getText()+" AND");
			        }
			        if(!grade.getText().isEmpty()) {
				    	sql.append(" `grade`= '"+grade.getText()+"' AND");
				        }
			        if(sql.substring(sql.length()-3, sql.length()).equals("AND")) {
			        	sql.delete(sql.length()-3,sql.length());
			        }
			        
				}
				borderPane.setBottom(buildData(sql.toString()));
				
			}); 
			return pane;
		
	}
    
    public static TableView buildData(String SQL) {

        data = FXCollections.observableArrayList();
        try {

            //SQL FOR SELECTING ALL OF CUSTOMER
            
            Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/lab8","root",""); 
			Statement stmt= (Statement)con.createStatement();  

			
			//ResultSet
			ResultSet rs = con.createStatement().executeQuery(SQL);
			tableview.getColumns().clear();
	        tableview.getItems().clear();

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableview.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList *
             *******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            tableview.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        return tableview;
    }




}
