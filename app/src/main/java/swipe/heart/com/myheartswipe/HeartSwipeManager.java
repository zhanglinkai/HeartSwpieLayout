package swipe.heart.com.myheartswipe;

/**
 * Created by Administrator on 2018/8/29.
 */

public class HeartSwipeManager {

    private static HeartSwipeManager manager;
    private HeartSwipeLayout layout;
    public static HeartSwipeManager newInstance() {
        if (manager==null){
            manager=new HeartSwipeManager();
        }
        return manager;
    }
    public void setLayout(HeartSwipeLayout layout) {
        this.layout = layout;
    }
    public void closeLayout(){
        if (layout!=null){
            layout.close();
        }
    }

}
