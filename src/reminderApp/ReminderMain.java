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
		// 执行stage.close()方法,窗口不直接退出
		Platform.setImplicitExit(false);

		// 菜单项(打开)中文乱码的问题是编译器的锅,如果使用IDEA,需要在Run-Edit Configuration在LoginApplication中的VM
		// Options中添加-Dfile.encoding=GBK
		// 如果使用Eclipse,需要右键Run as-选择Run Configuration,在第二栏Arguments选项中的VM
		// Options中添加-Dfile.encoding=GBK
		showItem = new MenuItem("打开");
		// 菜单项(退出)
		exitItem = new MenuItem("退出");

		URL url = ReminderMain.class.getResource("timg.png");
		Image image = Toolkit.getDefaultToolkit().getImage(url);

		trayIcon = new TrayIcon(image);
		// 初始化监听事件(空)
		showListener = e -> Platform.runLater(() -> {
		});
		exitListener = e -> {
		};
		mouseListener = new MouseAdapter() {
		};
	}

	private void initReminder() {
		if (!SystemTray.isSupported()) {
			// 系统托盘不支持
			log.info(Thread.currentThread().getStackTrace()[1].getClassName() + ":系统托盘不支持");
			return;
		}
		trayIcon.setImageAutoSize(true);
		// 系统托盘
		SystemTray tray = SystemTray.getSystemTray();
		final PopupMenu popup = new PopupMenu();
		popup.add(showItem);
		popup.add(exitItem);
		trayIcon.setPopupMenu(popup);
		trayIcon.setToolTip("提示");
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
        //行为事件: 点击"打开"按钮,显示窗口
        showListener = e -> Platform.runLater(() -> showStage(stage));
        //行为事件: 点击"退出"按钮, 就退出系统
        exitListener = e -> {
            System.exit(0);
        };
       //鼠标行为事件: 单机显示stage
        mouseListener = new MouseAdapter() {
        	@Override
            public void mouseClicked(MouseEvent e) {
                //鼠标左键
                if (e.getButton() == MouseEvent.BUTTON1) {
                    showStage(stage);
                }
            }
        };
        
        //给菜单项添加事件
        showItem.addActionListener(showListener);
        exitItem.addActionListener(exitListener);
        //给系统托盘添加鼠标响应事件
        trayIcon.addMouseListener(mouseListener);
	}

	/**
	 * 关闭窗口
	 */
	public void hide(Stage stage) {
		Platform.runLater(() -> {
			// 如果支持系统托盘,就隐藏到托盘,不支持就直接退出
			if (SystemTray.isSupported()) {
				// stage.hide()与stage.close()等价
				stage.hide();
			} else {
				System.exit(0);
			}
		});
	}

	/**
	 * 点击系统托盘,显示界面(并且显示在最前面,将最小化的状态设为false)
	 */
	private void showStage(Stage stage) {
		// 点击系统托盘,
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
		//初始化
		initReminder();
		//设置监听
		listen(primaryStage);
		
		Parent root = FXMLLoader.load(getClass().getResource("/reminderApp/ReminderScene.fxml"));
		primaryStage.setTitle("提醒工具");
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
