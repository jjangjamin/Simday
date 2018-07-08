package com.example.q.simday;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eftimoff.viewpagertransformers.BaseTransformer;
import com.eftimoff.viewpagertransformers.CubeOutTransformer;

public class MainActivity extends AppCompatActivity implements ContactFragment.OnFragmentInteractionListener {

    private PageAdapter mAdapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter2 = new PageAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.container);
        pager.setAdapter(mAdapter2);
        pager.setPageTransformer(true, new CubeOutTransformer(){

        });
    }
    private static long back_pressed;
    @Override
    public void onBackPressed()
    {
        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
        else Toast.makeText(getBaseContext(), "앱을 종료하시려면 뒤로가기 한번 더!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class CubeOutTransformer extends BaseTransformer {

        @Override
        protected void onTransform(View view, float position) {
            view.setPivotX(position < 0f ? view.getWidth() : 0f);
            view.setPivotY(view.getHeight() * 0.5f);
            view.setRotationY(40f * position);
        }

        @Override
        public boolean isPagingEnabled() {
            return true;
        }

    }

    public static final class PageAdapter extends FragmentStatePagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    ContactFragment tab1 = new ContactFragment();
                    return tab1;
                case 1:
                    GalleryFragment tab2 = new GalleryFragment();
                    return tab2;
                case 2:
                    SimsimFragment tab3 = new SimsimFragment();
                    return tab3;
                default:return null;
            }
        }



        @Override
        public int getCount() {
            return 3;
        }
    }

}
