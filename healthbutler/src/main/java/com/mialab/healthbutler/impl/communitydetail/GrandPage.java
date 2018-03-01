package com.mialab.healthbutler.impl.communitydetail;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.mialab.healthbutler.activity.ImageDetailActivity;
import com.mialab.healthbutler.base.CommunityBasePager;
import com.mialab.healthbutler.domain.GrandData;
import com.mialab.healthbutler.domain.GrandData.Content;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.utils.CacheUtils;
import com.mialab.healthbutler.utils.MyTimeUtils;
import com.mialab.healthbutler.view.GrandMenuDialog;
import com.mialab.healthbutler.view.GrandMenuDialog.ClickListenerInterface;
import com.mialab.healthbutler.view.LoadMoreListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * 用户动态
 *
 * @author Wesly
 */
public class GrandPage extends CommunityBasePager {

    LoadMoreListView mlvGrand;
    PtrFrameLayout ptrFrame;
    GrandListViewAdapter adapter;
    GrandData mGrandData;
    ArrayList<Content> mContentList;

    public GrandPage(Activity aty) {
        super(aty);

    }

    @Override
    public void initView() {

        mRootView = View.inflate(mainActivity, R.layout.pager_grand, null);
        mlvGrand = (LoadMoreListView) mRootView.findViewById(R.id.lv_grand);
        ptrFrame = (PtrFrameLayout) mRootView.findViewById(R.id.grand_ptr_frame);


    }

    @Override
    public void initData() {
        String result = CacheUtils.getCache(mainActivity, GlobalContants.GRAND_URL, "");
        if (!TextUtils.isEmpty(result)) {
            parseData(result, false);
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

                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mlvGrand, header);
            }
        });
        mlvGrand.setOnLoadListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadingMore() {
                getDataFromServer(true);
            }
        });
    }

    private void getDataFromServer(final boolean getMore) {
        HttpUtils utils = new HttpUtils();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time;
        if (!getMore) {
            //获取系统当前时间
            time = dateFormat.format(new Date());
        } else {
            //获取ArrayList最后一个时间
            time = mContentList.get(mContentList.size() - 1).getmTime();

        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("time", time);
        utils.send(HttpMethod.POST, GlobalContants.GRAND_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (!getMore) {
                    ptrFrame.refreshComplete();
                }
                String result = responseInfo.result;

                //404异常
                if (result.contains("<title>404</title>")) {
                    Toast.makeText(mainActivity, "404:grand", Toast.LENGTH_SHORT).show();
                } else {
                    CacheUtils.setCache(mainActivity, GlobalContants.GRAND_URL, result);
                    parseData(result, getMore);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (!getMore) {
                    ptrFrame.refreshComplete();
                } else {
                    mlvGrand.completeLoadMore(false);
                }

                Toast.makeText(mainActivity, "连接失败", Toast.LENGTH_SHORT).show();

            }

        });

    }

    protected void parseData(String result, boolean getMore) {
        Gson gson = new Gson();
        mGrandData = gson.fromJson(result, GrandData.class);
        if (!getMore) {
            mContentList = mGrandData.getmContentList();
            if (mContentList != null) {
                adapter = new GrandListViewAdapter();
                mlvGrand.setAdapter(adapter);
            }
        } else {
            ArrayList<Content> moreContentList = mGrandData.getmContentList();
            if (moreContentList.size() != 0) {
                mlvGrand.completeLoadMore(true);
                mContentList.addAll(moreContentList);
                adapter.notifyDataSetChanged();
            } else {
                mlvGrand.completeLoadMore(false);
                Toast.makeText(mainActivity, "没有更多了", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void postAttitude2Server(int id, String attitude) {
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", id + "");
        params.addBodyParameter("attitude", attitude);
        utils.send(HttpMethod.POST, GlobalContants.ATTITUDE_2_CONTENT, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });

    }

    class GrandListViewAdapter extends BaseAdapter {

        BitmapUtils headutils;
        BitmapUtils utils;

        public GrandListViewAdapter() {
            headutils = new BitmapUtils(mainActivity);
            headutils.configDefaultLoadingImage(R.drawable.default_head);
            utils = new BitmapUtils(mainActivity);
            utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mContentList.size();
        }

        @Override
        public Content getItem(int position) {
            return mContentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                convertView = View.inflate(mainActivity, R.layout.list_grand_item, null);
                holder.ivHead = (CircleImageView) convertView.findViewById(R.id.iv_head);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvPublishTime = (TextView) convertView.findViewById(R.id.tv_publish_time);
                holder.ivMenu = (ImageView) convertView.findViewById(R.id.iv_menu);
                holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                holder.ivContent = (ImageView) convertView.findViewById(R.id.iv_content);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Content content = getItem(position);

            //设置menu
            holder.ivMenu.setTag(content.getId());
            holder.ivMenu.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(final View v) {
                    GrandMenuDialog dialog = new GrandMenuDialog(mainActivity, "点赞", "举报", new ClickListenerInterface() {

                        @Override
                        public void like() {
                            postAttitude2Server((Integer) v.getTag(), "positive");
                        }

                        @Override
                        public void report() {

                            postAttitude2Server((Integer) v.getTag(), "negative");
                        }

                    });

                    dialog.show();

                }
            });

            //设置头像
            if (!TextUtils.isEmpty(content.getmHeadURL())) {
                headutils.display(holder.ivHead, content.getmHeadURL());
            } else {
                holder.ivHead.setImageResource(R.drawable.default_head);
            }
            if (!TextUtils.isEmpty(content.getmImageURL())) {
                holder.ivContent.setVisibility(View.VISIBLE);
                utils.display(holder.ivContent, content.getmImageURL());
            } else {
                holder.ivContent.setVisibility(View.GONE);
            }

            //设置name
            holder.tvName.setText(content.getmName());

            //设置publishTime
            holder.tvPublishTime.setText(MyTimeUtils.time2Now(content.getmTime()));

            //设置content
            holder.tvContent.setText(content.getmContent());

            //设置image点击事件
            holder.ivContent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mainActivity, ImageDetailActivity.class);
                    intent.putExtra("imageurl", getItem(position).getmImageURL());
                    mainActivity.startActivity(intent);
                    mainActivity.overridePendingTransition(R.anim.photo_scale_in, R.anim.grand_scale_out);// 进入动画和退出动画
                }
            });

            return convertView;
        }

    }

    class ViewHolder {
        public CircleImageView ivHead;
        public TextView tvName;
        public TextView tvPublishTime;
        public ImageView ivMenu;
        public TextView tvContent;
        public ImageView ivContent;
    }

}
