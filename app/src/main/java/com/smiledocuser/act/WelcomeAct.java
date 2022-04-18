package com.smiledocuser.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.smiledocuser.R;
import com.smiledocuser.databinding.ActivityWelBinding;

public class WelcomeAct extends AppCompatActivity {
    ActivityWelBinding binding;
    int[] screens;
    MyViewPagerAdapter myvpAdapter;
    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wel);


        screens = new int[]{
                R.layout.activity_welcome_page,
                R.layout.activity_welcome_page1,
                R.layout.activity_wel_come_page2,
                R.layout.activity_wel_come_page3
        };
        myvpAdapter = new MyViewPagerAdapter();
        binding.viewPager.setAdapter(myvpAdapter);


//        ColoredBars(0);

        dotscount = myvpAdapter.getCount();
        dots = new ImageView[dotscount];

      /*  for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(WelcomeAct.this, R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            binding.layoutBars.addView(dots[i], params);


            dots[0].setImageDrawable(ContextCompat.getDrawable(WelcomeAct.this, R.drawable.active_dot));

            binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    for (int i = 0; i < dotscount; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(WelcomeAct.this, R.drawable.non_active_dot));
                    }

                    dots[position].setImageDrawable(ContextCompat.getDrawable(WelcomeAct.this, R.drawable.active_dot));

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }*/

    }

        public void next (View v){
            int i = getItem(+1);
            if (i < screens.length) {
                binding.viewPager.setCurrentItem(i);
                Log.e("i=====",i+"");
                if(i==0) binding.nextBtn.setImageDrawable(getDrawable(R.drawable.ic_next));
                else if(i==1) binding.nextBtn.setImageDrawable(getDrawable(R.drawable.ic_next1));
                else if(i==2) binding.nextBtn.setImageDrawable(getDrawable(R.drawable.ic_next2));
                else if(i==3) binding.nextBtn.setImageDrawable(getDrawable(R.drawable.ic_next_finish));

            } else {
                startActivity(new Intent(WelcomeAct.this, LoginAct.class));
                finish();
            }
        }

        public void skip (View view){
//        launchMain();
        }

  /*  private void ColoredBars(int thisScreen) {
        int[] colorsInactive = getResources().getIntArray(R.array.dot_on_page_not_active);
        int[] colorsActive = getResources().getIntArray(R.array.dot_on_page_active);
        bottomBars = new TextView[screens.length];

        Layout_bars.removeAllViews();
        for (int i = 0; i < bottomBars.length; i++) {
            bottomBars[i] = new TextView(this);
            bottomBars[i].setTextSize(100);
            bottomBars[i].setText(Html.fromHtml("&#175"));
            Layout_bars.addView(bottomBars[i]);
            bottomBars[i].setTextColor(colorsInactive[thisScreen]);
        }
        if (bottomBars.length > 0)
            bottomBars[thisScreen].setTextColor(colorsActive[thisScreen]);
    }*/

        private int getItem ( int i){
            return binding.viewPager.getCurrentItem() + i;
        }



/*  ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

      @Override
      public void onPageSelected(int position) {
          ColoredBars(position);
          if (position == screens.length - 1) {
              Next.setText("start");
              Skip.setVisibility(View.GONE);
          } else {
              Next.setText(getString(R.string.next));
              Skip.setVisibility(View.VISIBLE);
          }
      }

      @Override
      public void onPageScrolled(int arg0, float arg1, int arg2) {

      }

      @Override
      public void onPageScrollStateChanged(int arg0) {

      }
  };
*/
        public class MyViewPagerAdapter extends PagerAdapter {
            private LayoutInflater inflater;


            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(screens[position], container, false);
                container.addView(view);
                return view;
            }

            @Override
            public int getCount() {
                return screens.length;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View v = (View) object;
                container.removeView(v);
            }

            @Override
            public boolean isViewFromObject(View v, Object object) {
                return v == object;
            }
        }
    }






