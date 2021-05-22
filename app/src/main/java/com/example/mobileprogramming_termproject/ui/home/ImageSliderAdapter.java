package com.example.mobileprogramming_termproject.ui.home;

import android.content.Context;
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
   private int[] images={R.drawable.ad_coffee,R.drawable.ad_diet,R.drawable.ad_cu,R.drawable.ad_cu};
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
      container.addView(view);
      return view;
   }
   @Override
   public void destroyItem(ViewGroup container,int position,Object object){
      container.invalidate();
   }
}