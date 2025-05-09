//package com.example.breastcancerprecition;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RatingBar;
//import android.widget.TextView;
//
//import com.squareup.picasso.Picasso;
//
//public class custom_send_review extends AppCompatActivity {
//    EditText id,review;
//    RatingBar r;
//    Button b;
//    private Context context;
//    public  custom_view_doc(Context context, String[] id, String[] review){
//        this.context = context;
//        this.id=id;
//        this.review=review;
//
//    }
//
//
//
//    @Override
//    public int getCount() {
//        return doc_q.length;
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View gridView;
//        if(view==null)
//        {
//            gridView=new View(context);
//            //gridView=inflator.inflate(R.layout.customview, null);
//            gridView=inflator.inflate(R.layout.activity_custom_send_review,null);
//
//        }
//        else
//        {
//            gridView=(View)view;
//
//        }
//        TextView tv1=(TextView)gridView.findViewById(R.id.editTextTextPersonName5);
//        RatingBar rb1=(RatingBar)gridView.findViewById(R.id.ratingBar);
//        Button tv2=(Button)gridView.findViewById(R.id.button8);
//
//
//
//
////        ImageView im=(ImageView) gridView.findViewById(R.id.imageView10);
//
////        tv6.setTextColor(Color.BLACK);
//
//
//        tv1.setText(review[i]);
//        tv2.setText(doc_email[i]);
//        tv5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
////
////        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
////        String ip=sh.getString("ip","");
////
////        String url="http://" + ip + ":8000"+img[i]+".jpg";
////
////
////        Picasso.with(context).load(url).transform(new CircleTransform()). into(img);
//
//        return gridView;
//    }
//}