package com.example.mobileprogramming_termproject.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.mobileprogramming_termproject.R;

public class ImageSliderAdapter extends PagerAdapter {
   private int[] images={R.drawable.ad_cu,R.drawable.ad_seven,R.drawable.ad_gs,R.drawable.ad_emart,R.drawable.ad_coffee,R.drawable.ad_diet};
   private LayoutInflater inflater;
   private Context context;

   public ImageSliderAdapter(Context context){
      this.context=context;
   }
   @Override
   public int getCount(){
      return images.length;
   }
   @Override
   public boolean isViewFromObject(View view, Object object){
      return view ==((View)object);
   }
   @Override
   public Object instantiateItem(ViewGroup container, int position){
      inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View view=inflater.inflate(R.layout.item_slider,container,false);
      ImageView imageView=view.findViewById(R.id.imageSlider);
      imageView.setImageResource(images[position]);

      view.setOnClickListener(new View.OnClickListener(){
         public void onClick(View v){
            //this will log the page number that was click
            Log.i("TAG", "This page was clicked: " + position);
            if(position==0){

               Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("http://cu.bgfretail.com/index.do"));
               context.startActivity(intent);
            }
            else if(position==1){

               Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.7-eleven.co.kr/"));
               context.startActivity(intent);
            }
            else if(position==2){

               Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("http://gs25.gsretail.com/gscvs/ko/main"));
               context.startActivity(intent);
            }
            else if(position==4){

               Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://emart24.co.kr/"));
               context.startActivity(intent);
            }
         }
      });

      container.addView(view);
      return view;
   }
   @Override
   public void destroyItem(ViewGroup container,int position,Object object){
      container.invalidate();
   }
}