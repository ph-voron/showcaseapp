package app.voron.ph.showcaseapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

import app.voron.ph.showcaseapp.Fragments.ShowcaseCategoryFragment;

/**
 * Created by TJack on 11.11.2017.
 */

public class ShowcaseFragmentPagerAdapter extends FragmentPagerAdapter {

    //
    private List<Pair<ShowcaseCategoryFragment, String>> mFragments = new ArrayList<>();
    //
    public void addFragment(ShowcaseCategoryFragment categoryFragment, String title){
        mFragments.add(new Pair<ShowcaseCategoryFragment, String>(categoryFragment, title));
    }

    public ShowcaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position < 0 && position >= mFragments.size()) return null;
        return mFragments.get(position).first;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).second;
    }
}
