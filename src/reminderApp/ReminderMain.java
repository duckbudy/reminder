package reminderApp;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ReminderMain extends Application {
	private static Logger log;
	private static TrayIcon trayIcon;
	private static MenuItem showItem;
	private static MenuItem exitItem;

	private static ActionListener showListener;
	private static ActionListener exitListener;
	private static MouseListener mouseListener;

	private static ReminderMain instance;
	static { 
		// ִ��stage.close()����,���ڲ�ֱ���˳�
		Platform.setImplicitExit(false);

		// �˵���(��)��������������Ǳ������Ĺ�,���ʹ��IDEA,��Ҫ��Run-Edit Configuration��LoginApplication�е�VM
		// Options�����-Dfile.encoding=GBK
		// ���ʹ��Eclipse,��Ҫ�Ҽ�Run as-ѡ��Run Configuration,�ڵڶ���Argumentsѡ���е�VM
		// Options�����-Dfile.encoding=GBK
		showItem = new MenuItem("��");
		// �˵���(�˳�)
		exitItem = new MenuItem("�˳�");

		URL url = ReminderMain.class.getResource("timg.png");
		Image image = Toolkit.getDefaultToolkit().getImage(url);

		trayIcon = new TrayIcon(image);
		// ��ʼ�������¼�(��)
		showListener = e -> Platform.runLater(() -> {
		});
		exitListener = e -> {
		};
		mouseListener = new MouseAdapter() {
		};
	}

	private void initReminder() {
		if (!SystemTray.isSupported()) {
			// ϵͳ���̲�֧��
			log.info(Thread.currentThread().getStackTrace()[1].getClassName() + ":ϵͳ���̲�֧��");
			return;
		}
		trayIcon.setImageAutoSize(true);
		// ϵͳ����
		SystemTray tray = SystemTray.getSystemTray();
		final PopupMenu popup = new PopupMenu();
		popup.add(showItem);
		popup.add(exitItem);
		trayIcon.setPopupMenu(popup);
		trayIcon.setToolTip("��ʾ");
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public static ReminderMain getInstance() {
		if (instance == null) {
			instance = new ReminderMain();
		}
		return instance;
	}

	public void listen(Stage stage) {
		if (showListener == null || exitListener == null || mouseListener == null || showItem == null
				|| exitItem == null || trayIcon == null) {
			return;
		}
		showItem.removeActionListener(showListener);
		exitItem.removeActionListener(exitListener);
        trayIcon.removeMouseListener(mouseListener);
        //��Ϊ�¼�: ���"��"��ť,��ʾ����
        showListener = e -> Platform.runLater(() -> showStage(stage));
        //��Ϊ�¼�: ���"�˳�"��ť, ���˳�ϵͳ
        exitListener = e -> {
            System.exit(0);
        };
       //�����Ϊ�¼�: ������ʾstage
        mouseListener = new MouseAdapter() {
        	@Override
            public void mouseClicked(MouseEvent e) {
                //������
                if (e.getButton() == MouseEvent.BUTTON1) {
                    showStage(stage);
                }
            }
        };
        
        //���˵�������¼�
        showItem.addActionListener(showListener);
        exitItem.addActionListener(exitListener);
        //��ϵͳ������������Ӧ�¼�
        trayIcon.addMouseListener(mouseListener);
	}

	/**
	 * �رմ���
	 */
	public void hide(Stage stage) {
		Platform.runLater(() -> {
			// ���֧��ϵͳ����,�����ص�����,��֧�־�ֱ���˳�
			if (SystemTray.isSupported()) {
				// stage.hide()��stage.close()�ȼ�
				stage.hide();
			} else {
				System.exit(0);
			}
		});
	}

	/**
	 * ���ϵͳ����,��ʾ����(������ʾ����ǰ��,����С����״̬��Ϊfalse)
	 */
	private void showStage(Stage stage) {
		// ���ϵͳ����,
		Platform.runLater(() -> {
			if (stage.isIconified()) {
				stage.setIconified(false);
			}
			if (!stage.isShowing()) {
				stage.show();
			}
			stage.toFront();
		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//��ʼ��
		initReminder();
		//���ü���
		listen(primaryStage);
		
		Parent root = FXMLLoader.load(getClass().getResource("/reminderApp/ReminderScene.fxml"));
		primaryStage.setTitle("���ѹ���");
		primaryStage.setResizable(false);
		// primaryStage.setIconified(true);
		// primaryStage.initStyle(StageStyle.UTILITY);
		// Platform.setImplicitExit(false);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		
		TimerTask timerTask = new TimeSet();
	    Timer timer = new Timer();
	    timer.schedule(timerTask, 2000, 60000);
	}

}
