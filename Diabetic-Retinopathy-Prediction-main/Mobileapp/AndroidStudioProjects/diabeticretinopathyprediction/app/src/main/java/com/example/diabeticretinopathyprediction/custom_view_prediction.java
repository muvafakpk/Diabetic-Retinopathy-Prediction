package com.example.diabeticretinopathyprediction;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class custom_view_prediction extends BaseAdapter {
   String[] id, date,  prediction, img;
    private Context context;
    SharedPreferences sh;
    public custom_view_prediction(Context context, String[] id, String[] date, String[] prediction, String[] img){
        this.context = context;
        this.id=id;
        this.date=date;
        this.prediction=prediction;
        this.img=img;
        sh= PreferenceManager.getDefaultSharedPreferences(context);



    }



    @Override
    public int getCount() {
        return date.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(view==null)
        {
            gridView=new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView=inflator.inflate(R.layout.activity_custom_view_prediction,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView tv1=(TextView)gridView.findViewById(R.id.textView5);
        TextView tv2=(TextView)gridView.findViewById(R.id.textView7);
        ImageView im = (ImageView)gridView.findViewById(R.id.imageView);

        tv1.setText(date[i]);
        tv2.setText(prediction[i]);
        String url=sh.getString("url", "")+img[i];
        Picasso.with(context).load(url).transform(new CircleTransform()). into(im);

        return gridView;
    }
}