package com.example.diabeticretinopathyprediction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class custom_view_reply extends BaseAdapter {

    String[] id, admin_reply, complaint;
    private Context context;
    public  custom_view_reply(Context context, String[] id, String[] admin_reply,String[] complaint){
        this.context = context;
        this.id=id;
        this.complaint=complaint;
        this.admin_reply=admin_reply;

    }



    @Override
    public int getCount() {
        return admin_reply.length;
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
            gridView=inflator.inflate(R.layout.activity_custom_view_reply,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView tv1=(TextView)gridView.findViewById(R.id.textView22);
        TextView tv2=(TextView)gridView.findViewById(R.id.textView31) ;






//        ImageView im=(ImageView) gridView.findViewById(R.id.imageView10);

//        tv6.setTextColor(Color.BLACK);


        tv2.setText(admin_reply[i]);
        tv1.setText(complaint[i]);



//        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
//        String ip=sh.getString("ip","");
//
//        String url="http://" + ip + ":5000/static/game/"+gamecode[i]+".jpg";
//
//
//        Picasso.with(context).load(url).transform(new CircleTransform()). into(im);

        return gridView;
    }
}