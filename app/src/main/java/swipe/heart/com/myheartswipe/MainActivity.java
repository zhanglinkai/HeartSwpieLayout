package swipe.heart.com.myheartswipe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private HeartSwipeLayout lay;
    private TextView test;
    private TextView test2;
    private TextView test3;
    private TextView left,top,right,bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lay=(HeartSwipeLayout)findViewById(R.id.lay);
        test=(TextView)findViewById(R.id.test);
        test2=(TextView)findViewById(R.id.test2);
        test3=(TextView)findViewById(R.id.test3);
        left=(TextView)findViewById(R.id.left);
        top=(TextView)findViewById(R.id.top);
        right=(TextView)findViewById(R.id.right);
        bottom=(TextView)findViewById(R.id.bottom);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test3.setVisibility(View.VISIBLE);
            }
        });

        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test3.setVisibility(View.GONE);
            }
        });
        left.setOnClickListener(this);
        top.setOnClickListener(this);
        right.setOnClickListener(this);
        bottom.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.left:
                lay.setSwipeMode(HeartSwipeLayout.Mode.LEFT);
                break;
            case R.id.top:
                lay.setSwipeMode(HeartSwipeLayout.Mode.TOP);
                break;
            case R.id.right:
                lay.setSwipeMode(HeartSwipeLayout.Mode.RIGHT);
                break;
            case R.id.bottom:
                lay.setSwipeMode(HeartSwipeLayout.Mode.BOTTOM);
                break;
        }
    }
}
