package sz.heytomato.justweatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/17.
 */
public class WeatherDailyAdapter extends BaseAdapter {

    private Context context=null;

    public WeatherDaily[] datas= new WeatherDaily[]{
            new WeatherDaily("Day1","- -","- -","- -",R.drawable.w99),
            new WeatherDaily("Day2","- -","- -","- -",R.drawable.w99),
            new WeatherDaily("Day3","- -","- -","- -",R.drawable.w99),
            new WeatherDaily("Day4","- -","- -","- -",R.drawable.w99),
            new WeatherDaily("Day5","- -","- -","- -",R.drawable.w99)};

    public WeatherDailyAdapter(Context context){
        this.context=context;
    }

    public Context getContext(){
        return context;
    }





    @Override
    public int getCount() {
        return datas.length;
    }

    @Override
    public WeatherDaily getItem(int i) {
        return datas[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout linearLayout=null;
        if (view!=null){
            linearLayout= (LinearLayout) view;
        }else {
            linearLayout= (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.day_weather_cell,null);
        }
        WeatherDaily data=getItem(i);

        TextView day= (TextView) linearLayout.findViewById(R.id.daily_date);
        TextView text= (TextView) linearLayout.findViewById(R.id.daily_text);
        TextView tempL= (TextView) linearLayout.findViewById(R.id.daily_low_temp);
        TextView tempH= (TextView) linearLayout.findViewById(R.id.daily_high_temp);
        ImageView pic= (ImageView) linearLayout.findViewById(R.id.daily_picture);
        day.setText(data.getwDay());
        text.setText(data.getwText());
        tempL.setText(data.getwTempL());
        tempH.setText(data.getwTempH());
        pic.setImageResource(data.getwPidId());
        return linearLayout;


    }







}
