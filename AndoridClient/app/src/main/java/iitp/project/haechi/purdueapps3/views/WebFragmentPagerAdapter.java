package iitp.project.haechi.purdueapps3.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import iitp.project.haechi.purdueapps3.fragments.ChildAbstract;
import iitp.project.haechi.purdueapps3.fragments.ChildFragment1;
import iitp.project.haechi.purdueapps3.fragments.ChildFragment2;
import iitp.project.haechi.purdueapps3.fragments.ChildFragment3;

/**
 * Created by dnay2 on 2016-12-07.
 */

public class WebFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private static final String URL = "URL";

    private static final int NORMAL = 0;
    private static final int THERMO = 1;
    private static final int IRADIATE = 2;

    private ChildAbstract[] items;

    public WebFragmentPagerAdapter(FragmentManager fm, ChildAbstract[] items) {
        super(fm);
        this.items = items;
        items[NORMAL] = new ChildFragment1();
        items[THERMO] = new ChildFragment2();
        items[IRADIATE] = new ChildFragment3();
    }

    public ChildAbstract getAFragment(int position) {
        return items[position];
    }

    @Override
    public Fragment getItem(int position) {
        return getFragment(position);
    }

    @Override
    public int getCount() {
        if (items != null) return items.length;
        else return 0;
    }

    private ChildAbstract getFragment(int position) {

        switch (position) {
            case NORMAL:
                if (items[NORMAL] == null)
                    items[NORMAL] = new ChildFragment1();
                break;
            case THERMO:
                if (items[THERMO] == null)
                    items[THERMO] = new ChildFragment2();
                break;
            case IRADIATE:
                if (items[IRADIATE] == null)
                    items[IRADIATE] = new ChildFragment3();
                break;
        }
        return items[position];
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }
}
