package reminderApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

public class NoteEditUtils {
	public void addNote(EventListModel eventList) throws IOException {
		File f = new File("/notice.txt");
		boolean flag=false;
		if (!f.exists()) {
			f.createNewFile();
			flag=true;
		}
		FileWriter fileWritter = new FileWriter(f.getName(), true);
		String content = getContent(eventList.getEventType(), eventList.getEventTime(), eventList.getEventContent());
		fileWritter.write(content);
		if(!flag) {
			fileWritter.write("\r\n");			
		}
		fileWritter.close();
	}
	private void addNote(String content) throws IOException {
		File f = new File("/notice.txt");
		boolean flag=false;
		if (!f.exists()) {
			f.createNewFile();
			flag=true;
		}
		FileWriter fileWritter = new FileWriter(f.getName(), true);
		fileWritter.write(content);
		if(!flag) {
			fileWritter.write("\r\n");			
		}
		fileWritter.close();
	}

	public void updateNote(String oldItem,String newItem) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("notice.txt"));
		StringBuffer sb = new StringBuffer(4096);
		String temp = null;
		while ((temp = br.readLine()) != null) {
			if (oldItem.equals(StringUtils.trim(temp))) {
				sb.append(newItem).append("\r\n");				
			}else if(temp.equals("")){
				continue;
			}else {
				sb.append(temp).append("\r\n");				
			}
		}
		br.close();
		clearFileInfo(new File("notice.txt"));
		String sb1=sb.toString();
		if(sb1.length()>0) {
			this.addNote(sb1.substring(0, sb1.length()-1));			
		}
	}
	public String readNote() throws IOException {
		BufferedReader br=new BufferedReader(new FileReader("notice.txt"));
		StringBuffer sb = new StringBuffer(4096);
		String temp = null;
		while ((temp = br.readLine()) != null) {
			sb.append(temp).append("\r\n");
		}
		br.close();
		String sb1=sb.toString();
		if(sb1.length()>0) {
			return sb1.substring(0, sb1.length()-1);
		}else {
			return "";
		}
	}
	public void delNote(EventListModel eventList) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("notice.txt"));
		StringBuffer sb = new StringBuffer(4096);
		String content = getContent(eventList.getEventType(), eventList.getEventTime(), eventList.getEventContent());
		String temp = null;
		while ((temp = br.readLine()) != null) {
			if (content.equals(StringUtils.trim(temp))) {
				continue;
			}else if(temp.equals("")) {
				continue;
			}
			sb.append(temp).append("\r\n");
		}
		br.close();
		clearFileInfo(new File("notice.txt"));
		
		String sb1=sb.toString();
		if(sb1.length()>0) {
			this.addNote(sb1.substring(0, sb1.length()-1));
		}else {
			this.addNote("");
		}
	}
	
	private void clearFileInfo (File file) {
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	private String getContent(String type, String datetime, String content) {
		if (StringUtils.isEmpty(type) || StringUtils.isEmpty(datetime) || StringUtils.isEmpty(content))
			return "";
		return type.concat("\t").concat(datetime).concat("\t").concat(content);
	}
}
