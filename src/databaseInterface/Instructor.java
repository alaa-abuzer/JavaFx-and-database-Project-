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

public class Instructor {


    public static ObservableList<ObservableList> data;

    public static TableView tableview;

	public static GridPane instructorPane(BorderPane borderPane,Statement stmt) throws ClassNotFoundException {
		GridPane pane =new GridPane();
	        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
	        pane.setHgap(5.5);
	        pane.setVgap(5.5);
	        borderPane.setBottom(null);
	        tableview = new TableView();
	        ComboBox dept_name = new ComboBox();
			try{   
				String query1="SELECT dept_name from department";
				ResultSet rs = stmt.executeQuery(query1);
				
				while(rs.next()) {
					dept_name.getItems().addAll(rs.getString(1));
				}
				
				pane.add(new Label("Instructor ID:"), 0, 0);
				TextField id =new TextField();
				TextField name =new TextField();
				TextField salary =new TextField();
				pane.add(id, 1, 0);
				pane.add(new Label("Name:"), 0, 1);
				pane.add(name, 1, 1);
				pane.add(new Label("Department name:"), 0, 2);
				pane.add(dept_name, 1, 2);
				pane.add(new Label("Salary:"), 0, 3);
				pane.add(salary, 1, 3);
				Button btAdd = new Button("Add");
				Button btsrh = new Button("Search");
				pane.add(btAdd, 1, 4);
				pane.add(btsrh, 0, 4);

				btAdd.setOnAction(e->{
					try {
						String query2="INSERT INTO `Instructor`(`ID`, `name`, `dept_name`, `salary`)"
								+ " VALUES ("+id.getText()+",'"+name.getText()+"','"+dept_name.getSelectionModel().getSelectedItem()+"'"
								+ ","+salary.getText()+")";
						
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
		        	StringBuilder sql = new StringBuilder("SELECT * from `instructor` ");
		        	if(!id.getText().isEmpty()||!name.getText().isEmpty()||!dept_name.getSelectionModel().isEmpty()||!salary.getText().isEmpty()) {
		        		sql.append("where ");
				        if(!id.getText().isEmpty()) {
				        	sql.append("ID="+id.getText()+" AND");
				        }
				        if(!name.getText().isEmpty()) {
			        	sql.append(" `name`= '"+name.getText()+"' AND");
			            }
				        if(!dept_name.getSelectionModel().isEmpty()) {
				        	sql.append(" `dept_name` = '"+dept_name.getSelectionModel().getSelectedItem()+"' AND");
				        }
				        if(!salary.getText().isEmpty()) {
			        	sql.append(" `salary`= "+salary.getText()+" AND");
			            }
				        if(sql.substring(sql.length()-3, sql.length()).equals("AND")) {
				        	sql.delete(sql.length()-3,sql.length());
				        }
				        
		        	}
		        	borderPane.setBottom(buildData(sql.toString()));
					
				});
				
				}catch(SQLException e){ 
					e.printStackTrace();
				} 
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
