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
     * 用户单击计数
     */
    //private AtomicInteger atomicInteger = new AtomicInteger(0);
 
    /**
     * 同样实现run方法
     */
    @Override
    public void run() {
//        /**
//         * 单数次与偶数次分别单击桌面两个不同的位置
//         */
//        if (atomicInteger.addAndGet(1) % 2 == 0) {
//            clickScreenByXY(100, 300);
//        } else {
//            clickScreenByXY(1200, 300);
//        }
//        System.out.println(new Date() + " click " + atomicInteger.get() + " time ,Thread name is :" + Thread.currentThread().getName());
//        Object[] options = {"继续工作","下班啦"};
       // JOptionPane.showOptionDialog(new JFrame(), "休息一下吧~","",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    	try {
			String remind=showRemind();
			if (remind!=null) {
		        Object[] options = {"知道啦"};
		        JFrame j=new JFrame();
		        j.setAlwaysOnTop(true);	
			    JOptionPane.showOptionDialog(j, remind,"提醒",JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    private String  showRemind() throws IOException {
    	//从txt读取
//    	NoteEditUtils n=new NoteEditUtils();
//    	String items=n.readNote();
//    	String[] item=items.split("\r\n");
//    	for(int i=0;i<item.length;i++) {
//    		String[] s=item[i].split("\t");
//    		if(s.length==3) {
//    			String currentDate="";
//    			if(s[0].equals("每天")) {
//    			  currentDate=getCurrentDate("HH:mm");
//    			}else if(s[0].contentEquals("指定时间")){
//    				currentDate=getCurrentDate("yyyy-MM-dd HH:mm");
//    			}
//    			System.out.println("Type is "+s[0]+",the current time is "+currentDate+",s1 is "+s[1]+",the result is "+currentDate.equals(s[1]));
//    			if(currentDate.equals(s[1])) {
//					 return s[2];
//				}
//    		}
//    	}
    	//从mogodb读取    	
    	MongoDBJDBC m=new MongoDBJDBC();
		List<Map<String,String>> list=m.queryDocument();
		for(int i=0;i<list.size();i++) {
			Map<String,String> l=list.get(i);
			String currentDate="";
			if(l.get("eventType").equals("每天")) {
			  currentDate=getCurrentDate("HH:mm");
			}else if(l.get("eventType").contentEquals("指定时间")){
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
     * 模拟用户单击屏幕指定区域,默认单击屏幕最中央
     *
     * @param x：x坐标
     * @param y：y坐标
     */
    public static final void clickScreenByXY(Integer x, Integer y) {
        try {
            /**创建工具包对象*/
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            /**创建自动化对象*/
            Robot robot = new Robot();
            /**利用工具包对象获取屏幕分辨率*/
            if (x == null) {
                x = toolkit.getScreenSize().width / 2;
            }
            if (y == null) {
                y = toolkit.getScreenSize().height / 2;
            }
            /**
             * 移动鼠标到指定位置
             * 然后按下鼠标左键，再松开，模拟单击操作
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
//        // 创建定时任务
//        TimerTask timerTask = new TimeSet();
//        //创建定时器对象
//        Timer timer = new Timer();
//        /**相当于新开一个线程去执行定时器任务
//         * 定时器执行定时器任务，3 秒后开始执行任务
//         * 任务执行完成后，间隔 5 秒再次执行，循环往复
//         */
//        timer.schedule(timerTask, 3000, 5000);
//    }
}
