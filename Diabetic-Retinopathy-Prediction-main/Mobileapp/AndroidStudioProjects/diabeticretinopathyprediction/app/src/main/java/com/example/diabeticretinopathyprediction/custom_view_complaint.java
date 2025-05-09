package com.example.diabeticretinopathyprediction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class custom_view_complaint extends BaseAdapter {
    String[] id,complaint, reply;
    private Context context;
    public  custom_view_complaint(Context context, String[] id, String[] doc_name, String[] doc_phone, String[] doc_email, String[] doc_status, String[] token,String[] doc_from_time, String[] doc_to_time){
        this.context = context;
        this.id=id;
        this.complaint=complaint;
        this.reply=reply;



    }



    @Override
    public int getCount() {
        return id.length;
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
            gridView=inflator.inflate(R.layout.activity_custom_view_complaint,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView tv1=(TextView)gridView.findViewById(R.id.textView4);
        TextView tv2=(TextView)gridView.findViewById(R.id.textView6);
        TextView tv3=(TextView)gridView.findViewById(R.id.textView11);
        TextView tv4=(TextView)gridView.findViewById(R.id.textView17);
        TextView tv5=(TextView)gridView.findViewById(R.id.textView15);
        TextView tv6=(TextView)gridView.findViewById(R.id.textView12);
        TextView tv7=(TextView)gridView.findViewById(R.id.textView14);
        Button tv8=(Button)gridView.findViewById(R.id.button6);
        Button tv9=(Button)gridView.findViewById(R.id.button7);




//        ImageView im=(ImageView) gridView.findViewById(R.id.imageView10);

//        tv6.setTextColor(Color.BLACK);


//        tv1.setText(doc_name[i]);
//        tv2.setText(doc_phone[i]);
//        tv3.setText(doc_email[i]);
//        tv4.setText(doc_status[i]);
//        tv5.setText(token[i]);
//        tv6.setText(doc_from_time[i]);
//        tv7.setText(doc_to_time[i]);
        tv8.setTag(i);
        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tv9.setTag(i);
        tv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




//        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
//        String ip=sh.getString("ip","");
//
//        String url="http://" + ip + ":8000"+img[i]+".jpg";
//
//
//        Picasso.with(context).load(url).transform(new CircleTransform()). into(img);

        return gridView;
    }
}