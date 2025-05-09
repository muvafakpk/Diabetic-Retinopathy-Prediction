package com.example.diabeticretinopathyprediction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class custom_view_prescription extends BaseAdapter {
    String[] id,doc_name;
    private Context context;
    public  custom_view_prescription(Context context, String[] id, String[] doc_name){
        this.context = context;
        this.id=id;
        this.doc_name=doc_name;


    }



    @Override
    public int getCount() {
        return doc_name.length;
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
            gridView=inflator.inflate(R.layout.activity_custom_view_prescription,null);

        }
        else
        {
            gridView=(View)view;

        }
//        TextView tv1=(TextView)gridView.findViewById(R.id.textView18);
//        Button tv2=(Button)gridView.findViewById(R.id.button9);




//        ImageView im=(ImageView) gridView.findViewById(R.id.imageView10);

//        tv6.setTextColor(Color.BLACK);


//        tv1.setText(doc_name[i]);
//        tv2.setTag(i);
//        tv2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


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


