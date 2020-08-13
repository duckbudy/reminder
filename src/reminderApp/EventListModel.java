package reminderApp;

public class EventListModel {
	private String eventContent;
	private String eventTime;
	private String eventType;
	public EventListModel(String eventTime,String eventContent) {
		this.setEventTime(eventTime);
		this.setEventContent(eventContent);
	}
	public EventListModel(String eventType,String eventTime,String eventContent) {
		this.setEventType(eventType);
		this.setEventTime(eventTime);
		this.setEventContent(eventContent);
	}
	public String getEventContent() {
		return eventContent;
	}
	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}
	public String getEventTime() {
		return eventTime;
	}
	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
}
