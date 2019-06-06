package home.accounting.view;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

import home.accounting.DA.FlatDA;
import home.accounting.DA.WaterDA;
import home.accounting.controller.CurrentUser;
import home.accounting.model.Flat;
import home.accounting.model.Water;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class WaterListPageController {
	
	@FXML
	private AnchorPane page;
	
	@FXML
	private TableView<Flat> waterTable;
	
	@FXML
	private TableColumn<Flat, Integer> flatNumberColumn;
	
	@FXML
	private TableColumn<Flat, Flat> ownerColumn;
	
	@FXML
	private TableColumn<Flat, Water> valueColumn;
	
	@FXML
	private TableColumn<Flat, Water> oldValueColumn;
	
	
	@FXML
	private HBox columnContainerHBox;
	
	@FXML
	private VBox firstColumn;
	
	private static final StringConverter<Integer> VALUE_CONVERTER = new StringConverter<Integer>() {

	    @Override
	    public String toString(Integer object) {
	    	//System.out.println("TO STRING CONVERTER: "+object);
	        return object == null ? "" : home.accounting.controller.converter.Water.toString(object);
	    }

	    @Override
	    public Integer fromString(String string) {
	    	System.out.println("TO INTEGER CONVERTER:"+string+";"+string.isEmpty());
	        return string.isEmpty() ? null : home.accounting.controller.converter.Water.toInt(string);
	    }

	};
	
	private static final UnaryOperator<TextFormatter.Change> VALUE_FILTER = change -> {
	    int maxLength = 10;

	    if (change.isAdded()) {
	        if(change.getControlNewText().length() <= maxLength){
	            if (change.getText().contains(",")) {
	                change.setText(change.getText().replaceAll(",", "."));
	            }
	            change = change.getControlNewText().matches("^\\d*(\\.\\d{0,1})?$") ? change : null;
	        } else {
	            if (change.getText().length() == 1){
	                change = null;
	            } else {
	                int allowedLength = maxLength - change.getControlText().length();
	                change.setText(change.getText().substring(0, allowedLength));
	            }
	        }
	    }
	    return change;
	};

	@FXML
	private void initialize(){
		
		List<Flat> allFlats = FlatDA.getAll(CurrentUser.getHouse());
		
		flatNumberColumn.setCellValueFactory(flat -> new ReadOnlyObjectWrapper<>(flat.getValue().getId()));
		
		ownerColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
		ownerColumn.setCellFactory(new Callback<TableColumn<Flat,Flat>, TableCell<Flat,Flat>>() {
			
			@Override
			public TableCell<Flat, Flat> call(TableColumn<Flat, Flat> param) {
				return new TableCell<Flat, Flat>(){
					@Override
					protected void updateItem(Flat item, boolean empty){
						super.updateItem(item, empty);
						if(item==null || empty){
							setText(null);
						}else{
							setText(item.getOwnerName()+" "+item.getOwnerLastName());
						}
					}
				};
			}
		});
		
		valueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Flat,Water>, ObservableValue<Water>>() {

			@Override
			public ObservableValue<Water> call(CellDataFeatures<Flat, Water> param) {
				Water water = WaterDA.get(param.getValue(), java.sql.Date.valueOf(LocalDate.now()));
				if(water == null){
					water = new Water(param.getValue(), java.sql.Date.valueOf(LocalDate.now()));
					WaterDA.save(water);
				}
				return new SimpleObjectProperty<>(water);
			}
		});
		valueColumn.setCellFactory(new Callback<TableColumn<Flat,Water>, TableCell<Flat,Water>>() {
			
			@Override
			public TableCell<Flat, Water> call(TableColumn<Flat, Water> param) {
				return new TableCell<Flat, Water>(){
					
					private final TextFormatter<Integer> formatter;
	                private final TextField textField;
	                {
	                    textField = new TextField();
	                    //formatter = new TextFormatter<>(VALUE_CONVERTER, null, null);
	                    formatter = new TextFormatter<>(VALUE_FILTER);
	                    textField.setTextFormatter(formatter);
	                    /*formatter.valueProperty().addListener((o, oldValue, newValue) -> {
	                    	Water water = getTableColumn().getCellData((Flat) getTableRow().getItem());
	                        if (!Objects.equals(water.getAmount(), newValue)) {
	                             // update item and db, if value was modified
	                             //water.setAmount(newValue);
	                            //WaterDA.update(water);
	                        	System.out.println("Should get updated! "+water.getFlat().getNumber());
	                        }
	                    });*/
	                   /* textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
	                    	private String initialValue = null;
	                    	
							@Override
							public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
									Boolean focus) {
								if(focus){
									initialValue = textField.getText();
								}else{
									System.out.println("FOCUS CHANGE LISTENER");
									if(!initialValue.equals(textField.getText())){
										System.out.println("ini: "+initialValue+" new: "+textField.getText());
										Water water = getTableColumn().getCellData((Flat) getTableRow().getItem());
										water.setAmount(home.accounting.controller.converter.Water.toInt(textField.getText()));
										WaterDA.update(water);
										System.out.println(water.getFlat().getNumber()+" updated!"+ water.getAmount());
									}
								}
							}
						});*/
	                }
	                @Override
	                protected void updateItem(Water value, boolean empty){
	                    super.updateItem(value, empty);
	                    if (empty){
	                        setGraphic(null);
	                    } else {
	                        setGraphic(textField);
	                        formatter.setValue(value.getAmount());
	                    }
	                }
	            };   
			}
		});
		
		oldValueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Flat,Water>, ObservableValue<Water>>() {

			@Override
			public ObservableValue<Water> call(CellDataFeatures<Flat, Water> param) {
				Water water = WaterDA.get(param.getValue(), java.sql.Date.valueOf(LocalDate.now().minus(Period.ofMonths(1))));
				if(water == null){
					System.out.println("Creating water for flat "+param.getValue().getNumber());
					water = new Water(param.getValue(), java.sql.Date.valueOf(LocalDate.now().minus(Period.ofMonths(1))));
					WaterDA.save(water);
				}
				return new SimpleObjectProperty<>(water);
			}
		});
		oldValueColumn.setCellFactory(new Callback<TableColumn<Flat,Water>, TableCell<Flat,Water>>() {
			
			@Override
			public TableCell<Flat, Water> call(TableColumn<Flat, Water> param) {
				return new TableCell<Flat, Water>(){
					
					private final TextFormatter<Integer> formatter;
	                private final TextField textField;
	                {
	                    textField = new TextField();
	                    formatter = new TextFormatter<>(VALUE_CONVERTER, null, VALUE_FILTER);
	                    textField.setTextFormatter(formatter);
	                    /*formatter.valueProperty().addListener((o, oldValue, newValue) -> {
	                    	Water water = getTableColumn().getCellData((Flat) getTableRow().getItem());
	                        if (!Objects.equals(water.getAmount(), newValue)) {
	                             // update item and db, if value was modified
	                             //water.setAmount(newValue);
	                            //WaterDA.update(water);
	                        	System.out.println("Should get updated! "+water.getFlat().getNumber());
	                        }
	                    });*/
	                    textField.focusedProperty().addListener((o, lostFocus, newValue) -> {
	                    	if(lostFocus){
	                    		Water water = getTableColumn().getCellData((Flat) getTableRow().getItem());
		                        if (!Objects.equals(water.getAmount(), newValue)) {
		                             // update item and db, if value was modified
		                             //water.setAmount(newValue);
		                            //WaterDA.update(water);
		                        	System.out.println("Should get updated! "+water.getFlat().getNumber());
		                        }
	                    	}
	                    });
	                }
	                @Override
	                protected void updateItem(Water value, boolean empty){
	                    super.updateItem(value, empty);
	                    if (empty){
	                        setGraphic(null);
	                    } else {
	                        setGraphic(textField);
	                        formatter.setValue(value.getAmount());
	                    	//setText(Integer.toString(value.getAmount()));
	                    }
	                }
	            };   
			}
		});
		
		waterTable.setItems(FXCollections.observableArrayList(allFlats));
	}
	
	private void createRows(List<Water> waterList){
		boolean odd = true;
		for(Water water : waterList){
			HBox row = new HBox();
			row.setPrefHeight(40);
			row.setMaxWidth(Double.MAX_VALUE);
			row.setMaxHeight(Double.MAX_VALUE);
			row.setMinHeight(row.getPrefHeight());
			row.getStyleClass().add(odd ? "odd":"even");
			
			Label flatNumber = new Label();
			flatNumber.setText(Integer.toString(water.getFlat().getNumber()));
			flatNumber.setPrefWidth(40);
			flatNumber.setMaxHeight(Double.MAX_VALUE);
			flatNumber.setAlignment(Pos.CENTER);
			
			Label owner = new Label();
			owner.setText(water.getFlat().getOwnerName()+" "+water.getFlat().getOwnerLastName());
			owner.setMaxWidth(200);
			owner.setMaxHeight(Double.MAX_VALUE);
			owner.getStyleClass().add("name");
			
			TextField value = new TextField();
			value.getStyleClass().add("current_value");
			value.setMaxHeight(Double.MAX_VALUE);
			value.setMaxWidth(200);
			if(water.getAmount() == null){
				value.setText("");
			}else{
				value.setText(home.accounting.controller.converter.Water.toString(water.getAmount()));
			}
			
			TextField oldValue = new TextField();
			oldValue.setMaxWidth(100);
			oldValue.setMaxHeight(Double.MAX_VALUE);
			
			HBox.setHgrow(owner, Priority.ALWAYS);
			HBox.setHgrow(value, Priority.ALWAYS);
			HBox.setHgrow(oldValue, Priority.ALWAYS);
			
			row.getChildren().addAll(flatNumber, owner, value, oldValue);
			
			firstColumn.getChildren().add(row);
			
			odd = !odd;
		}
		
	}
	
	private void createTable(){
		
	}
	
	private void columnCountHandler(){
		int columnMargin = 50;
		int minimalColumnWidth = (int) firstColumn.getMinWidth();
		List<VBox> columnStorage = new ArrayList<>();
		
		page.widthProperty().addListener((obs, aldVal, newVal) -> {
			double result = columnContainerHBox.getWidth()/minimalColumnWidth;
			int columnCount = (int) result;
			int numberOfMargins = columnCount - 1;
			double finalCount = columnContainerHBox.getWidth()/minimalColumnWidth*columnCount+columnMargin*numberOfMargins;
			if(finalCount<1){
				columnCount--;
			}
			
			if(columnCount!=columnContainerHBox.getChildren().size()){
				int diff = columnCount - columnContainerHBox.getChildren().size();
				if(diff>0){
					addColumns(diff, columnStorage);
					System.out.println("diff: "+diff);
					System.out.println("Number of columns: "+columnStorage.size());
				}else{
					removeColumns(Math.abs(diff));
				}
			}
		});
	}
	
	private void removeColumns(int number){
		
	}
	
	private void addColumns(int number, List<VBox> storage){
		for(int i=0; i<number; i++){
			if(storage.isEmpty()){
				storage.add(createColumn());
			}
		}
	}
	
	private VBox createColumn(){
		VBox column = new VBox();
		column.getStyleClass().add("table");
		column.setMinWidth(firstColumn.getMinWidth());
		column.setMaxWidth(firstColumn.getMaxWidth());
		
		return column;
	}
}
