package com.mialab.healthbutler.impl.communitydetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.activity.NewsBodyActivity;
import com.mialab.healthbutler.base.CommunityBasePager;
import com.mialab.healthbutler.domain.NewsData;
import com.mialab.healthbutler.domain.NewsData.SimpleNewsData;
import com.mialab.healthbutler.domain.NewsData.TopNewsData;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.utils.CacheUtils;
import com.mialab.healthbutler.utils.PrefUtils;
import com.mialab.healthbutler.view.LoadMoreListView;
import com.viewpagerindicator.PageIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * 新闻页面
 *
 * @author Wesly
 */
public class NewsPager extends CommunityBasePager {

    public View topNewsHeadView;
    public TextView mTopTitle;
    public ViewPager vpTopNews;
    public PageIndicator indicator;
    public LoadMoreListView mLvnews;
    public PtrFrameLayout ptrFrame;

    public NewsData mNewsData;
    ArrayList<TopNewsData> mTopNewsList;
    ArrayList<SimpleNewsData> mSimpleNewsList;
    LvnewsAdapter listNewsAdapter;

    Handler mHandler;

    public NewsPager(Activity aty) {
        super(aty);

    }

    @Override
    public void initView() {
        mRootView = View.inflate(mainActivity, R.layout.pager_news, null);
        topNewsHeadView = View.inflate(mainActivity, R.layout.top_news_headview, null);

        mTopTitle = (TextView) topNewsHeadView.findViewById(R.id.tv_title);
        vpTopNews = (ViewPager) topNewsHeadView.findViewById(R.id.vp_topnews);
        indicator = (PageIndicator) topNewsHeadView.findViewById(R.id.indicator);
        mLvnews = (LoadMoreListView) mRootView.findViewById(R.id.lv_news);
        ptrFrame = (PtrFrameLayout) mRootView.findViewById(R.id.news_ptr_frame);
        mLvnews.addHeaderView(topNewsHeadView);
    }

    @Override
    public void initData() {

        String cache = CacheUtils.getCache(mainActivity, GlobalContants.NEWS_URL, null);
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache, false);
        }

        final MaterialHeader header = new MaterialHeader(mainActivity);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPtrFrameLayout(ptrFrame);
        ptrFrame.setLoadingMinTime(500);
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);
        ptrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrame.autoRefresh(false);
            }
        }, 150);
        ptrFrame.setPinContent(true);
        ptrFrame.disableWhenHorizontalMove(true);
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getDataFromServer(false);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mLvnews, header);
            }
        });

        mLvnews.setOnLoadListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadingMore() {
                getDataFromServer(true);
            }
        });

        //topnews滑动监听
        indicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                mTopTitle.setText(mTopNewsList.get(arg0).title);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        //listView点击监听
        mLvnews.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SimpleNewsData newsData = mSimpleNewsList.get(position - 1);

                // 在本地保存已经阅读过的新闻id
                int newsid = newsData.id;
                String ids = PrefUtils.getString(mainActivity, GlobalContants.NEWS_READED, "");
                if (!ids.contains(newsid + "")) {
                    ids = ids + newsid;
                }
                PrefUtils.setString(mainActivity, GlobalContants.NEWS_READED, ids);
                // 切换颜色
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(mainActivity.getResources().getColor(R.color.read_news_Color));
                TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
                tvTime.setTextColor(mainActivity.getResources().getColor(R.color.read_news_Color));

                // 跳转NewsBodyActivity
                Bundle bundle = new Bundle();
                bundle.putString("BodyURL", newsData.BodyURL);
                bundle.putString("newsTitle", newsData.title);
                Intent intent = new Intent();
                intent.setClass(mainActivity, NewsBodyActivity.class);
                intent.putExtra("newsInf", bundle);
                mainActivity.startActivity(intent);

            }
        });
    }

    /**
     * 获得服务器数据
     */
    private void getDataFromServer(final boolean getMore) {
        HttpUtils hUtils = new HttpUtils();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time;
        if (!getMore) {
            //获取系统当前时间
            time = dateFormat.format(new Date());
        } else {
            //获取ArrayList最后一个时间
            time = mSimpleNewsList.get(mSimpleNewsList.size() - 1).pubTime;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("time", time);
        hUtils.send(HttpMethod.POST, GlobalContants.NEWS_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (!getMore) {
                    ptrFrame.refreshComplete();
                }
                String result = responseInfo.result;


                //404异常
                if (result.contains("<title>404</title>")) {
                    Toast.makeText(mainActivity, "404:news", Toast.LENGTH_SHORT).show();
                } else {
                    CacheUtils.setCache(mainActivity, GlobalContants.NEWS_URL, result);
                    parseData(result, getMore);
                }


            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (!getMore) {
                    ptrFrame.refreshComplete();
                } else {
                    mLvnews.completeLoadMore(false);
                }
            }
        });

    }

    /**
     * 解析网络数据
     *
     * @param result
     */
    private void parseData(String result, boolean getMore) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(result, NewsData.class);

        if (!getMore) {
            mTopNewsList = mNewsData.topNews;
            mSimpleNewsList = mNewsData.simpleNews;
            if (mNewsData != null && mTopNewsList.size() != 0 && mSimpleNewsList.size() != 0) {
                vpTopNews.setAdapter(new vpTopNewsAdapter());
                indicator.setViewPager(vpTopNews);
                indicator.setCurrentItem(0);
                mTopTitle.setText(mTopNewsList.get(0).title);

                // TopNews轮播显示
                if (mHandler == null) {
                    mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            int currentItem = vpTopNews.getCurrentItem();
                            if (currentItem < mTopNewsList.size() - 1) {
                                currentItem++;
                            } else {
                                currentItem = 0;
                            }
                            indicator.setCurrentItem(currentItem);
                            mTopTitle.setText(mTopNewsList.get(currentItem).title);
                            mHandler.sendEmptyMessageDelayed(0, 5000);
                        }
                    };
                    mHandler.sendEmptyMessageDelayed(0, 3000);
                }
                listNewsAdapter = new LvnewsAdapter();
                mLvnews.setAdapter(listNewsAdapter);
            }
        } else {
            ArrayList<SimpleNewsData> NewsList = mNewsData.simpleNews;
            if (NewsList.size() != 0) {
                mLvnews.completeLoadMore(true);
                mSimpleNewsList.addAll(NewsList);
                listNewsAdapter.notifyDataSetChanged();
            } else {
                mLvnews.completeLoadMore(false);
                Toast.makeText(mainActivity, "没有更多了", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * TopNews适配器
     *
     * @author Wesly
     */
    class vpTopNewsAdapter extends PagerAdapter {

        private BitmapUtils utils;

        public vpTopNewsAdapter() {
            utils = new BitmapUtils(mainActivity);
            utils.configDefaultLoadingImage(R.drawable.topnews_item_default);
        }

        @Override
        public int getCount() {
            return mTopNewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            TopNewsData topNewsData = mTopNewsList.get(position);
            String imageURL = topNewsData.imageURL;

            ImageView ivTopNews = new ImageView(mainActivity);
            ivTopNews.setScaleType(ScaleType.CENTER_CROP);

            utils.display(ivTopNews, imageURL);

            ivTopNews.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int id = vpTopNews.getCurrentItem();
                    // 跳转NewsBodyActivity
                    Bundle bundle = new Bundle();
                    bundle.putString("BodyURL", mTopNewsList.get(id).BodyURL);
                    bundle.putString("newsTitle", mTopNewsList.get(id).title);
                    Intent intent = new Intent();
                    intent.setClass(mainActivity, NewsBodyActivity.class);
                    intent.putExtra("newsInf", bundle);
                    mainActivity.startActivity(intent);

                }
            });

            container.addView(ivTopNews);
            return ivTopNews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    /**
     * ListView适配器
     *
     * @author Wesly
     */
    class LvnewsAdapter extends BaseAdapter {

        private BitmapUtils utils;

        public LvnewsAdapter() {
            super();
            utils = new BitmapUtils(mainActivity);
            utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mSimpleNewsList.size();
        }

        @Override
        public SimpleNewsData getItem(int position) {

            return mSimpleNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = new ViewHolder();

            if (convertView == null) {
                convertView = View.inflate(mainActivity, R.layout.list_news_item, null);

                holder.newspic = (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.newsTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.newsTime = (TextView) convertView.findViewById(R.id.tv_time);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            SimpleNewsData newsData = getItem(position);
            String imageURL = newsData.imageURL;

            utils.display(holder.newspic, imageURL);
            holder.newsTitle.setText(newsData.title);
            holder.newsTime.setText(newsData.pubTime);

            String ids = PrefUtils.getString(mainActivity, GlobalContants.NEWS_READED, "");
            if (ids.contains(getItem(position).id + "")) {
                holder.newsTitle.setTextColor(mainActivity.getResources().getColor(R.color.read_news_Color));
                holder.newsTime.setTextColor(mainActivity.getResources().getColor(R.color.read_news_Color));
            } else {
                holder.newsTitle.setTextColor(mainActivity.getResources().getColor(R.color.news_Color));
                holder.newsTime.setTextColor(mainActivity.getResources().getColor(R.color.news_Color));
            }

            return convertView;
        }

    }

    class ViewHolder {
        public ImageView newspic;
        public TextView newsTitle;
        public TextView newsTime;
    }

}
