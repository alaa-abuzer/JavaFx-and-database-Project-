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

public class Advisor {


    public static ObservableList<ObservableList> data;

    public static TableView tableview;

	public static GridPane advisorPane(BorderPane borderPane,Statement stmt) throws ClassNotFoundException {
		GridPane pane =new GridPane();
	        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
	        pane.setHgap(5.5);
	        pane.setVgap(5.5);
	        borderPane.setBottom(null);

	        tableview = new TableView();
	        ComboBox st_id = new ComboBox();
	        ComboBox inst_id = new ComboBox();
			try{   
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/lab8", "root", "");
				Statement stmt2 = (Statement) con.createStatement();
				String query1="SELECT ID  from `student`";
				String query="SELECT ID  from `instructor`";
				ResultSet rs1 = stmt.executeQuery(query1);
				ResultSet rs = stmt2.executeQuery(query);
				while(rs1.next()) {
					st_id.getItems().addAll(rs1.getString(1));
				}
				
				while(rs.next()) {
					inst_id.getItems().addAll(rs.getString(1));
				}
				
				pane.add(new Label("S_id:"), 0, 0);
				pane.add(st_id, 1, 0);
				pane.add(new Label("I_id:"), 0, 1);
				pane.add(inst_id, 1, 1);

				Button btAdd = new Button("Add");
				Button btsrh = new Button("Search");
				pane.add(btAdd, 1, 4);
				pane.add(btsrh, 0, 4);

				btAdd.setOnAction(e->{
					try {
						String query2="INSERT INTO `advisor`(`s_ID`, `i_ID`)"
								+ " VALUES ('"+st_id.getSelectionModel().getSelectedItem()+"','"+inst_id.getSelectionModel().getSelectedItem()+"')";
						
						stmt.executeUpdate(query2);
						
					}catch(SQLException e2){ 

							Alert err = new Alert(AlertType.ERROR);
							err.setHeaderText("ERROR OCCUERS");
							err.show();

						e2.printStackTrace();
					}
				});
				
				
				btsrh.setOnAction(e->{
		        	StringBuilder sql = new StringBuilder("SELECT * from `advisor` ");
		        	if(!st_id.getSelectionModel().isEmpty()||!inst_id.getSelectionModel().isEmpty()) {
		        		sql.append("where ");
				        if(!st_id.getSelectionModel().isEmpty()) {
				        	sql.append("s_ID='"+st_id.getSelectionModel().getSelectedItem()+"' AND");
				        }
				        if(!inst_id.getSelectionModel().isEmpty()) {
			        	sql.append(" `i_ID`= '"+inst_id.getSelectionModel().getSelectedItem()+"' AND");
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
