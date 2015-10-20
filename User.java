import java.util.Timer;
import java.util.ArrayList;
import java.util.Date;

public class User {

	public enum Mode{
		UNINITIALIZED,
		IDLE,
		DEAD,
		ACTIVE
	}

	public String alias;
	public String ip;
	public String unikey;
	public String lastStatus;
	public Mode mode;
	java.util.Timer timer;
	java.util.Timer timer2;
	ArrayList<User> container;

	public User(String alias, String ip, String unikey, ArrayList<User> container){
		this.alias = alias;
		this.ip = ip;
		this.unikey = unikey;
		this.lastStatus = "";
		this.mode = Mode.UNINITIALIZED;
		this.timer = new java.util.Timer();
		this.timer2 = new java.util.Timer();
		this.container = container;
		refreshTimers();
	}

	public void receiveStatus(String status){
		mode = Mode.ACTIVE;
		this.lastStatus = status;
	}

	public void refresh(){
		mode = Mode.ACTIVE;
		refreshTimers();
	}
	
	public void refreshTimers(){
		final User _this = this;
		timer.cancel();
		timer2.cancel();
		timer = new java.util.Timer();
		timer.schedule(new java.util.TimerTask(){
			public void run(){
				_this.mode = Mode.IDLE;
				timer2.cancel();
				timer2 = new java.util.Timer();
				timer2.schedule(new java.util.TimerTask(){
					public void run(){
						_this.mode = Mode.DEAD;
					}
				}, 10000);
			}
		}, 10000);
	}

}