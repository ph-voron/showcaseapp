package app.voron.ph.showcaseapp.Adapters;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
