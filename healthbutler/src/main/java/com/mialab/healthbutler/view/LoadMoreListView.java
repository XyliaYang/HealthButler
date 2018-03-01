package com.mialab.healthbutler.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.mialab.healthbutler.R;

import java.util.Date;

/**
 * Created by Wesly186 on 2016/3/25.
 */
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    private static final int UPDATE_FOOTER = 1;
    private View footerView;
    private int footerViewHeight;
    private boolean isLoadingMore = false;//当前是否正在处于加载更多
    private long loadMoreBeginTime;
    private long loadMoreEndTime;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //重置footerView状态

            footerView.setPadding(0, -footerViewHeight, 0, 0);
        }
    };

    public LoadMoreListView(Context context) {
        super(context);
        initFooterView();
        setOnScrollListener(this);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFooterView();
        setOnScrollListener(this);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFooterView();
        setOnScrollListener(this);
    }

    private void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.layout_footer, null);
        footerView.measure(0, 0);//主动通知系统去测量该view;
        footerViewHeight = footerView.getMeasuredHeight();
        addFooterView(footerView);
    }

    public void completeLoadMore(boolean hasNew) {
        if (isLoadingMore) {
            //设置加载中最短时间为1000ms
            loadMoreEndTime = new Date().getTime();
            if (!hasNew) {
                if (loadMoreEndTime - loadMoreBeginTime < 1000) {
                    handler.sendEmptyMessageDelayed(UPDATE_FOOTER, 500 - (loadMoreEndTime - loadMoreBeginTime));

                } else {
                    handler.sendEmptyMessage(UPDATE_FOOTER);
                }
            }

            isLoadingMore = false;
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && getLastVisiblePosition() == (getCount() - 1) && !isLoadingMore) {

            loadMoreBeginTime = new Date().getTime();

            isLoadingMore = true;

            footerView.setPadding(0, 0, 0, 0);//显示出footerView
            setSelection(getCount());//让listview最后一条显示出来

            if (listener != null) {
                listener.onLoadingMore();
            }
        } else {
            footerView.setPadding(0, 0, 0, 0);//显示出footerView
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    private OnLoadMoreListener listener;

    public void setOnLoadListener(OnLoadMoreListener listener) {
        this.listener = listener;
    }

    public interface OnLoadMoreListener {
        void onLoadingMore();
    }
}
