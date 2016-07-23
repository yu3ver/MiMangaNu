package ar.rulosoft.mimanganu.componentes.readers.paged;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

import java.util.Collections;
import java.util.List;

/**
 * Created by Raul on 26/06/2016.
 */
public class L2RPagedReader extends HorizontalPagedReader {

    public L2RPagedReader(Context context) {
        super(context);
    }


    @Override
    public void addOnPageChangeListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (readerListener != null) {
                    readerListener.onPageChanged(paths.size() - position - 1);
                }
                currentPage = position;
                mPageAdapter.setCurrentPage(position);
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setPaths(List<String> paths) {
        Collections.reverse(paths);
        this.paths = paths;
        setPagerAdapter(new PageAdapter());
    }

    @Override
    public void seekPage(int aPage) {
        if (paths != null) {
            int page = paths.size() - aPage;
            mViewPager.setCurrentItem(page);
            if (readerListener != null) {
                readerListener.onPageChanged(transformPage(page));
            }
            currentPage = page;
        }
    }

    @Override
    public void goToPage(int aPage) {
        seekPage(aPage);
    }

    @Override
    public void reloadImage(int idx) {
        if (paths != null) {
            int intIdx = paths.size() - idx;
            if (mPageAdapter.pages[intIdx] != null) {
                mPageAdapter.pages[intIdx].setImage();
            }
        }
    }

    @Override
    public String getPath(int idx) {
        return paths.get(paths.size() - idx);
    }

    @Override
    public void freePage(int idx) {
        if (mPageAdapter != null && mPageAdapter.pages[paths.size() - idx] != null) {
            mPageAdapter.pages[paths.size() - idx].unloadImage();
        }
    }


    @Override
    public boolean isLastPageVisible() {
        return mViewPager.getCurrentItem() == 0;
    }

    @Override
    public int getCurrentPage() {
        return transformPage(currentPage);
    }

    @Override
    protected int transformPage(int page) {
        return paths.size() - page;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (readerListener != null)
            if (e.getX() < getWidth() / 4) {
                if (currentPage == 0) {
                    if (readerListener != null) {
                        readerListener.onEndOver();
                    }
                } else {
                    mViewPager.setCurrentItem(currentPage - 1);
                }
            } else if (e.getX() > getWidth() / 4 * 3) {
                if (currentPage == paths.size() - 1) {
                    if (readerListener != null) {
                        readerListener.onStartOver();
                    }
                } else {
                    mViewPager.setCurrentItem(currentPage + 1);
                }
            } else {
                readerListener.onMenuRequired();
            }
        return false;
    }

    @Override
    public void onStartOver() {
        if (readerListener != null) {
            readerListener.onEndOver();
        }
    }

    @Override
    public void onEndOver() {
        if (readerListener != null) {
            readerListener.onStartOver();
        }
    }

}
