package reminderApp;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class TimeSet extends TimerTask {
	/**
     * �û���������
     */
    //private AtomicInteger atomicInteger = new AtomicInteger(0);
 
    /**
     * ͬ��ʵ��run����
     */
    @Override
    public void run() {
//        /**
//         * ��������ż���ηֱ𵥻�����������ͬ��λ��
//         */
//        if (atomicInteger.addAndGet(1) % 2 == 0) {
//            clickScreenByXY(100, 300);
//        } else {
//            clickScreenByXY(1200, 300);
//        }
//        System.out.println(new Date() + " click " + atomicInteger.get() + " time ,Thread name is :" + Thread.currentThread().getName());
//        Object[] options = {"��������","�°���"};
       // JOptionPane.showOptionDialog(new JFrame(), "��Ϣһ�°�~","",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    	try {
			String remind=showRemind();
			if (remind!=null) {
		        Object[] options = {"֪����"};
		        JFrame j=new JFrame();
		        j.setAlwaysOnTop(true);	
			    JOptionPane.showOptionDialog(j, remind,"����",JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    private String  showRemind() throws IOException {
    	//��txt��ȡ
//    	NoteEditUtils n=new NoteEditUtils();
//    	String items=n.readNote();
//    	String[] item=items.split("\r\n");
//    	for(int i=0;i<item.length;i++) {
//    		String[] s=item[i].split("\t");
//    		if(s.length==3) {
//    			String currentDate="";
//    			if(s[0].equals("ÿ��")) {
//    			  currentDate=getCurrentDate("HH:mm");
//    			}else if(s[0].contentEquals("ָ��ʱ��")){
//    				currentDate=getCurrentDate("yyyy-MM-dd HH:mm");
//    			}
//    			System.out.println("Type is "+s[0]+",the current time is "+currentDate+",s1 is "+s[1]+",the result is "+currentDate.equals(s[1]));
//    			if(currentDate.equals(s[1])) {
//					 return s[2];
//				}
//    		}
//    	}
    	//��mogodb��ȡ    	
    	MongoDBJDBC m=new MongoDBJDBC();
		List<Map<String,String>> list=m.queryDocument();
		for(int i=0;i<list.size();i++) {
			Map<String,String> l=list.get(i);
			String currentDate="";
			if(l.get("eventType").equals("ÿ��")) {
			  currentDate=getCurrentDate("HH:mm");
			}else if(l.get("eventType").contentEquals("ָ��ʱ��")){
				currentDate=getCurrentDate("yyyy-MM-dd HH:mm");
			}
			System.out.println("Type is "+l.get("eventType")+",the current time is "+currentDate+",s1 is "+l.get("eventTime").toString()+",the result is "+currentDate.equals(l.get("eventTime").toString()));
			if(currentDate.equals(l.get("eventTime").toString())) {
				 return l.get("eventContent").toString();
			}
		}
    	return null;
    }
    private String getCurrentDate(String format) {
    	SimpleDateFormat bartDateFormat = new SimpleDateFormat(format); 
    	Date date = new Date(); 
    	String currentdate=bartDateFormat.format(date);
    	return currentdate;
    	
    }
 
    /**
     * ģ���û�������Ļָ������,Ĭ�ϵ�����Ļ������
     *
     * @param x��x����
     * @param y��y����
     */
    public static final void clickScreenByXY(Integer x, Integer y) {
        try {
            /**�������߰�����*/
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            /**�����Զ�������*/
            Robot robot = new Robot();
            /**���ù��߰������ȡ��Ļ�ֱ���*/
            if (x == null) {
                x = toolkit.getScreenSize().width / 2;
            }
            if (y == null) {
                y = toolkit.getScreenSize().height / 2;
            }
            /**
             * �ƶ���굽ָ��λ��
             * Ȼ���������������ɿ���ģ�ⵥ������
             */
            robot.mouseMove(x, y);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(100);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
 
//    public static void main(String[] args) {
//        // ������ʱ����
//        TimerTask timerTask = new TimeSet();
//        //������ʱ������
//        Timer timer = new Timer();
//        /**�൱���¿�һ���߳�ȥִ�ж�ʱ������
//         * ��ʱ��ִ�ж�ʱ������3 ���ʼִ������
//         * ����ִ����ɺ󣬼�� 5 ���ٴ�ִ�У�ѭ������
//         */
//        timer.schedule(timerTask, 3000, 5000);
//    }
}
