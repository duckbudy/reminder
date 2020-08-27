package reminderApp;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class RemindController implements Initializable {
	@FXML
	private TextField textArea1 = new TextField();
	@FXML
	private TableView<EventListModel> table1 = new TableView<EventListModel>();
	@FXML
	private TableColumn<EventListModel, Date> eventTime = new TableColumn<EventListModel, Date>();
	@FXML
	private TableColumn<EventListModel, String> eventType = new TableColumn<EventListModel, String>();
	@FXML
	private TableColumn<EventListModel, String> eventContent = new TableColumn<EventListModel, String>();
	@FXML
	private TextField timeArea = new TextField();
	@FXML
	private TextField contentArea = new TextField();
	@FXML
	private Button btn1;
	@FXML
	private Button btn2;
	@FXML
	private Button btn3;
	@FXML
	private ComboBox<String> cbox;

	private static ObservableList<EventListModel> eventList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cbox.getItems().setAll("每天", "指定时间");
		readNoticeFile();
	}
	private void readNoticeFile() {
//		NoteEditUtils n=new NoteEditUtils();
//		try {
//			String sb=n.readNote();
//			if(sb.length()>0) {
//				eventType.setCellValueFactory(new PropertyValueFactory<>("eventType"));
//				eventTime.setCellValueFactory(new PropertyValueFactory<>("eventTime"));
//				eventContent.setCellValueFactory(new PropertyValueFactory<>("eventContent"));
//				eventList.clear();
//				String[] ss=sb.split("\r\n");
//				for(int i=0;i<ss.length;i++) {
//					String[] item=ss[i].split("\t");
//					if(item.length==3) {
//						eventList.add(new EventListModel(item[0], item[1], item[2]));						
//					}
//				}
//				table1.setItems(eventList);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		eventType.setCellValueFactory(new PropertyValueFactory<>("eventType"));
		eventTime.setCellValueFactory(new PropertyValueFactory<>("eventTime"));
		eventContent.setCellValueFactory(new PropertyValueFactory<>("eventContent"));
		eventList.clear();
		MongoDBJDBC m=new MongoDBJDBC();
		List<Map<String,String>> list=m.queryDocument();
		list.forEach(l->{
			eventList.add(new EventListModel(l.get("eventType"), l.get("eventTime"), l.get("eventContent")));
			table1.setItems(eventList);
		});
	}

	public void showText(String text) {
		textArea1.setText(text);
	}

	public void addItem(ActionEvent event) {
		// table1.setEditable(true);
		eventType.setCellValueFactory(new PropertyValueFactory<>("eventType"));
		eventTime.setCellValueFactory(new PropertyValueFactory<>("eventTime"));
		eventContent.setCellValueFactory(new PropertyValueFactory<>("eventContent"));

		String content = contentArea.getText();
		String time = timeArea.getText();
		String contentType = cbox.getValue();
		if (contentType == null || contentType.equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("警告");
			alert.setHeaderText("警告信息");
			alert.setContentText("请选择类型！");
			alert.showAndWait();
			return;
		}
		if (content != null && time != null) {
			eventList.add(new EventListModel(contentType, time.toString(), content));
		}
		table1.setItems(eventList);
		NoteEditUtils n=new NoteEditUtils();
		try {
			n.addNote(eventList.get(eventList.size()-1));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//插入mongodb
		MongoDBJDBC m=new MongoDBJDBC();
		m.addDocument(eventList.get(eventList.size()-1));
		
	}

	public void deleteItem(ActionEvent event) {
		TableViewSelectionModel<EventListModel> t = table1.getSelectionModel();
		int index = t.getSelectedIndex();
		if (index < 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("错误");
			alert.setHeaderText("错误信息");
			alert.setContentText("请选择一条记录！");
			alert.showAndWait();
			return;
		}
		NoteEditUtils n=new NoteEditUtils();
		try {
			n.delNote(eventList.get(index));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		MongoDBJDBC m=new MongoDBJDBC();
		m.deleteDocument("eventContent", t.getSelectedItem().getEventContent());
		
		eventList.remove(index);
		
		eventList.clear();
		readNoticeFile();
		
	}

	public void updateItem(ActionEvent event) {
		TableViewSelectionModel<EventListModel> t = table1.getSelectionModel();
		int index = t.getSelectedIndex();
		if (index < 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("错误");
			alert.setHeaderText("错误信息");
			alert.setContentText("请选择一条记录！");
			alert.showAndWait();
			return;
		}
		String oldItem=t.getSelectedItem().getEventType().concat("\t").concat(t.getSelectedItem().getEventTime()).concat("\t").concat(t.getSelectedItem().getEventContent());
		String content = contentArea.getText();
		String datetime = timeArea.getText();
		String type = cbox.getValue();
//从txt读取		
		String newItem=type.concat("\t").concat(datetime).concat("\t").concat(content);
		NoteEditUtils n=new NoteEditUtils();
		try {
			n.updateNote(oldItem, newItem);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//从mongodb读取		
		MongoDBJDBC m=new MongoDBJDBC();
		if(!content.equals(t.getSelectedItem().getEventContent())) {
			m.updateDocument("eventContent", t.getSelectedItem().getEventContent(), content);
		}else if(!datetime.equals(t.getSelectedItem().getEventTime())){
			m.updateDocument("eventTime", t.getSelectedItem().getEventTime(), datetime);
		}else if(!type.equals(t.getSelectedItem().getEventType())) {
			m.updateDocument("eventType", t.getSelectedItem().getEventType(), type);
		}
		
		eventList.clear();
		readNoticeFile();
	}

	public void getItem(MouseEvent event) {
		TableViewSelectionModel<EventListModel> t = table1.getSelectionModel();
		int index = t.getSelectedIndex();
		if(index<0) return;
		cbox.setValue(eventList.get(index).getEventType());
		timeArea.setText(eventList.get(index).getEventTime());
		contentArea.setText(eventList.get(index).getEventContent());
	}

	
	public void timeStart() {
    	TimerTask timerTask = new TimeSet();
	    Timer timer = new Timer();
	    timer.schedule(timerTask, 2000, 1000);
    }

}
