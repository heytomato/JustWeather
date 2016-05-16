package sz.heytomato.justweatherforecast;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkpage.lib.api.TPCity;
import com.thinkpage.lib.api.TPListeners;
import com.thinkpage.lib.api.TPWeatherManager;
import com.thinkpage.lib.api.TPWeatherNow;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<View> views;
    private ImageView[] points;
    private int[] ids={R.id.point_t,R.id.point_s};


    private LinearLayout weatherInfoLayout;
    /**
     * 用于显示城市名
     * */
    private TextView cityNameText;
    /**
     * 用于显示发布时间
     */
    private TextView publishText;
    /**
     * 用于显示天气描述信息
     */
    private TextView weatherDespText;

    /**
     * 用于显示当前日期
     */
    private TextView currentDateText;
    /**
     * 切换城市按钮
     */
    private Button switchCity;
    /**
     * 更新天气按钮
     */
    private Button refreshWeather;

    private ImageView weatherPicture;

    private TextView temp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initPoint();
        showWeather();

    }

    private void initViews(){
        LayoutInflater layoutInflater=LayoutInflater.from(this);

        views=new ArrayList<View>();
        views.add(layoutInflater.inflate(R.layout.today,null));
        views.add(layoutInflater.inflate(R.layout.seven_day,null));


        viewPagerAdapter=new ViewPagerAdapter(views,this);
        viewPager=(ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPagerAdapter);



        weatherInfoLayout = (LinearLayout)views.get(0).findViewById(R.id.weather_info_layout);
        cityNameText = (TextView)views.get(0). findViewById(R.id.city_name);
        publishText = (TextView)views.get(0). findViewById(R.id.publish_text);
        weatherDespText = (TextView)views.get(0). findViewById(R.id.weather_desp);
        weatherPicture=(ImageView)views.get(0).findViewById(R.id.weather_picture);

        temp= (TextView) views.get(0).findViewById(R.id.temp);
//        switchCity = (Button)views.get(0). findViewById(R.id.switch_city);
//        refreshWeather = (Button)views.get(0). findViewById(R.id.refresh_weather);








        viewPager.setOnPageChangeListener(this);



    }

    public void showWeather(){

        final Resources resources=getResources();


        final TPWeatherManager weatherManager = TPWeatherManager.sharedWeatherManager();
        weatherManager.initWithKeyAndUserId("kjhcscay7iilh3qf","U5AF3E16E8");


        weatherManager.getWeatherNow(new TPCity("广州"), TPWeatherManager.TPWeatherReportLanguage.kSimplifiedChinese
                , TPWeatherManager.TPTemperatureUnit.kCelsius
                , new TPListeners.TPWeatherNowListener() {
                    @Override
                    public void onTPWeatherNowAvailable(TPWeatherNow tpWeatherNow, String s) {
                        if (tpWeatherNow != null) {

                            weatherPicture.setImageResource(resources.getIdentifier("w"+tpWeatherNow.code,"drawable",getPackageName()));
//                            cityNameText.setText();
                            weatherDespText.setText(tpWeatherNow.text);
                            temp.setText(String.valueOf(tpWeatherNow.temperature)+"℃");
                            String d=String .valueOf(tpWeatherNow.lastUpdateDate);
                            String[] m=d.split(" ");
                            String[] t=m[3].split(":");
                            publishText.setText("发布于"+m[2]+"日"+t[0]+":"+t[1]);


                            Log.d("x","发布于"+m[2]+"日"+t[0]+":"+t[1]);
                            Log.d("x", String.valueOf(tpWeatherNow.lastUpdateDate));
                            Log.d("x",tpWeatherNow.text);
                            Log.d("x",tpWeatherNow.code);
                            Log.d("x",tpWeatherNow.windDirection);
                            Log.d("x", String.valueOf(tpWeatherNow.temperature)+"℃");

                            Log.d("x", String.valueOf(tpWeatherNow.dewPoint));


//                            publishText.setText(tpWeatherNow.lastUpdateDate.toString());
//                            weatherDespText.setText(tpWeatherNow.text);
//                            cityNameText.setText("城市");
////                            temp.setText(tpWeatherNow.temperature);
////                            weatherPicture.setImageResource(R.drawable.);
                        }else {
                            Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
                        }
                    }


                });

    }

    private void initPoint(){
        points=new ImageView[views.size()];
        for (int i=0;i<views.size();i++){
            points[i]= (ImageView) findViewById(ids[i]);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i=0; i <ids.length;i++){
            if (position==i){
                points[i].setImageResource(R.drawable.point_c);
            }else {
                points[i].setImageResource(R.drawable.point_n);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
