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

public class ClassRoom {


    public static ObservableList<ObservableList> data;

    public static TableView tableview;

	public static GridPane ClassRoomPane(BorderPane borderPane,Statement stmt) throws ClassNotFoundException, SQLException {
		GridPane pane =new GridPane();
	        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
	        pane.setHgap(5.5);
	        pane.setVgap(5.5);
	        borderPane.setBottom(null);

	        tableview = new TableView();
			pane.add(new Label("building:"), 0, 0);
			TextField building =new TextField();
			TextField room_number =new TextField();
			TextField capacity =new TextField();
			Button btAdd = new Button("Add");
			Button btsrh = new Button("Search");
			pane.add(building, 1, 0);
			pane.add(new Label("room_number:"), 0, 1);
			pane.add(room_number, 1, 1);
			pane.add(new Label("capacity:"), 0, 2);
			pane.add(capacity, 1, 2);
			pane.add(btsrh, 0, 3);
			pane.add(btAdd, 1, 3);


			btAdd.setOnAction(e->{
				try {
					String query2="INSERT INTO `classroom`(`building`, `room_number`, `capacity`)"
							+ " VALUES ('"+building.getText()+"',"+room_number.getText()+"',"+capacity.getText()+")";
					
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
				StringBuilder sql = new StringBuilder("SELECT * from `classroom` ");
				if(!building.getText().isEmpty()||!building.getText().isEmpty()||!capacity.getText().isEmpty()) {
					sql.append("where ");
			        if(!building.getText().isEmpty()) {
			        	sql.append("building='"+building.getText()+"' AND");
			        }
			        if(!room_number.getText().isEmpty()) {
			    	sql.append(" `room_number`= '"+room_number.getText()+"' AND");
			        }
			        if(!capacity.getText().isEmpty()) {
			    	sql.append(" `capacity`= "+capacity.getText()+" AND");
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
