package sz.heytomato.justweatherforecast;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkpage.lib.api.TPCity;
import com.thinkpage.lib.api.TPListeners;
import com.thinkpage.lib.api.TPWeatherDaily;
import com.thinkpage.lib.api.TPWeatherManager;
import com.thinkpage.lib.api.TPWeatherNow;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<View> views;
    private ImageView[] points;
    private int[] ids={R.id.point_t,R.id.point_s};


    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    private TextView publishText;
    private TextView weatherDespText;
    private ImageView refreshWeather;
    private ImageView weatherPicture;
    private TextView temp;
    private ListView weatherDaily;
    private String getEditText;
    private TextView weatherFuture;
    private int mBackKeyPressedTimes;
    private Boolean isFirstIn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initViews();
        initPoint();
        readData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.change_city:
                final EditText editText=new EditText(this);
                editText.setHint("请输入城市名称 例：“深圳”");
                final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                dialog.setTitle("切换城市");
                dialog.setCancelable(true);
                dialog.setView(editText);

                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getEditText=editText.getText().toString();
                        showWeather();
                    }
                });

                dialog.setNegativeButton("取消", null);
                dialog.show();
                break;
            case R.id.info:
                AlertDialog.Builder dialogi=new AlertDialog.Builder(MainActivity.this);
                dialogi.setTitle("应用信息");
                dialogi.setMessage(R.string.info);
                dialogi.setCancelable(true);
                dialogi.setNeutralButton("关闭",null);
                dialogi.show();
                break;
        }
        return true;
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

        refreshWeather= (ImageView) findViewById(R.id.refresh_weather);
        refreshWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getEditText==null){
                    Toast.makeText(MainActivity.this,"请点击菜单选择城市",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    showWeather();
                }
            }
        });

        weatherFuture= (TextView) views.get(1).findViewById(R.id.weather_future);
        weatherDaily=(ListView) views.get(1).findViewById(R.id.weather_daily);
        weatherDaily.setAdapter(new WeatherDailyAdapter(this));
        viewPager.setOnPageChangeListener(this);
    }

    private void init(){
        SharedPreferences preferences=getSharedPreferences("one",MODE_PRIVATE);
        isFirstIn=preferences.getBoolean("isFirstIn",true);
        if (isFirstIn){
            final EditText editText=new EditText(this);
            editText.setHint("请输入城市名称 例：“深圳”");
            final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
            dialog.setTitle("选择城市");
            dialog.setCancelable(true);
            dialog.setView(editText);

            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getEditText=editText.getText().toString();
                    showWeather();
                }
            });
            dialog.setNegativeButton("取消", null);
            dialog.show();


        }else {
        }
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("isFirstIn",false);
        editor.commit();
    }





    public void showWeather(){

        final Resources resources=getResources();
        final TPWeatherManager weatherManager = TPWeatherManager.sharedWeatherManager();
        weatherManager.initWithKeyAndUserId("kjhcscay7iilh3qf","U5AF3E16E8");


        weatherManager.getWeatherNow(new TPCity(getEditText), TPWeatherManager.TPWeatherReportLanguage.kSimplifiedChinese
                , TPWeatherManager.TPTemperatureUnit.kCelsius
                , new TPListeners.TPWeatherNowListener() {
                    @Override
                    public void onTPWeatherNowAvailable(TPWeatherNow tpWeatherNow, String s) {
                        s="请输入正确的城市名称！";
                        if (tpWeatherNow != null) {
                            final ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
                            progressDialog.setMessage("正在更新天气...");
                            progressDialog.show();
                            new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(2000);
                                        progressDialog.dismiss();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();

                            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM月dd日HH:mm");
                            weatherPicture.setImageResource(resources.getIdentifier("w"+tpWeatherNow.code,"drawable",getPackageName()));
                            cityNameText.setText(getEditText);
                            weatherDespText.setText(tpWeatherNow.text);
                            temp.setText(String.valueOf(tpWeatherNow.temperature)+"℃");
                            publishText.setText(simpleDateFormat.format(tpWeatherNow.lastUpdateDate)+"更新");

                        }else {
                            Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.add(Calendar.DATE,1);
        Date nextDay=calendar.getTime();

        weatherManager.getWeatherDailyArray(new TPCity(getEditText),TPWeatherManager.TPWeatherReportLanguage.kSimplifiedChinese
                , TPWeatherManager.TPTemperatureUnit.kCelsius
                ,nextDay,5
                , new TPListeners.TPWeatherDailyListener() {
                @Override
                public void onTPWeatherDailyAvailable(TPWeatherDaily[] tpWeatherDailies, String s) {
                    if (tpWeatherDailies!=null) {

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd日");
                        for (int i =0;i < tpWeatherDailies.length;i++){
                            View w=new WeatherDailyAdapter(MainActivity.this).getView(i,weatherDaily.getChildAt(i), null);
                            TextView day= (TextView)w.findViewById(R.id.daily_date);
                            TextView text= (TextView) w.findViewById(R.id.daily_text);
                            TextView tempL= (TextView)w.findViewById(R.id.daily_low_temp);
                            TextView tempH= (TextView)w.findViewById(R.id.daily_high_temp);
                            ImageView pic= (ImageView)w.findViewById(R.id.daily_picture);
                            day.setText(simpleDateFormat.format(tpWeatherDailies[i].date));
                            text.setText(tpWeatherDailies[i].textDay);
                            tempL.setText(tpWeatherDailies[i].lowTemperature+"℃");
                            tempH.setText(tpWeatherDailies[i].highTemperature+"℃");
                            pic.setImageResource(resources.getIdentifier("w"+tpWeatherDailies[i].codeDay,"drawable",getPackageName()));
                            simpleDateFormat.format(tpWeatherDailies[i].date);
                        }
                        weatherFuture.setText(getEditText+"未来5天天气");
                    }else{
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

    @Override
    public void onBackPressed() {
        if (mBackKeyPressedTimes == 0) {
            Toast.makeText(this, "再按一次退出程序 ", Toast.LENGTH_SHORT).show();
            mBackKeyPressedTimes = 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        mBackKeyPressedTimes = 0;
                    }
                }
            }.start();
            return;
        }
            else{
               finish();
            }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    private void saveData(){
        try {
            OutputStream outputStream=openFileOutput("data", Context.MODE_PRIVATE);
            outputStream.write(getEditText.getBytes("utf-8"));
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readData(){
        try {
            InputStream inputStream=openFileInput("data");
            byte[] bytes=new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            getEditText=new String(bytes,"utf-8");
            showWeather();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
