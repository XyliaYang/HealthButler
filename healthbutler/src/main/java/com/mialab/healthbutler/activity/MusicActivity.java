package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.app.ActivityCollector;
import com.mialab.healthbutler.domain.MusicData;
import com.mialab.healthbutler.domain.MusicData.Music;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.utils.CacheUtils;
import com.mialab.healthbutler.utils.PrefUtils;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

import java.util.ArrayList;

/**
 * Created by Wesly186 on 2016/3/25.
 */
public class MusicActivity extends Activity implements View.OnClickListener {

    //音乐状态
    public static final int MUSIC_PLAY_URL = 1;
    public static final int MUSIC_PAUSE = 2;
    public static final int MUSIC_STOP = 3;
    public static final int MUSIC_PLAY = 4;
    //下一首的广播接收器
    public NextMusicReceiver nextMusicReceiver;

    LvMusicAdapter lvMusicAdapter;
    String mToken;
    private ListView mlvMusic;
    private MusicData mMusicData;
    private ArrayList<MusicData.Music> mMusicList;

    private ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_music);
        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.musicactivity));
        ActivityCollector.addActivity(this);

        initView();

        initData();
    }

    private void initView() {
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        mlvMusic = (ListView) findViewById(R.id.lv_music);
    }

    private void initData() {
        //注册NextMusicReceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.mialab.sleepangel.nextmusicreceiver");
        filter.setPriority(100);
        nextMusicReceiver = new NextMusicReceiver();
        registerReceiver(nextMusicReceiver, filter);

        mToken = PrefUtils.getString(MusicActivity.this, GlobalContants.TOKEN, "");

        //获取缓存
        String cache = CacheUtils.getCache(MusicActivity.this, GlobalContants.MUSIC_URL, "");
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache);
        }

        getDataFromServer();

        ibBack.setOnClickListener(this);

    }

    private void getDataFromServer() {

        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("UUID", mToken);
        utils.send(HttpMethod.POST, GlobalContants.MUSIC_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null) {
                    CacheUtils.setCache(MusicActivity.this, GlobalContants.MUSIC_URL, result);
                    parseData(result);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(MusicActivity.this, "请检查网络:music", Toast.LENGTH_SHORT).show();

            }

        });

    }

    protected void parseData(String result) {
        Log.i("musicdate", result);
        Gson gson = new Gson();
        mMusicData = gson.fromJson(result, MusicData.class);
        mMusicList = mMusicData.getmMusicList();

        if (mMusicList != null) {
            lvMusicAdapter = new LvMusicAdapter();
            mlvMusic.setAdapter(lvMusicAdapter);

            mlvMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Log.i("current-togleon", PrefUtils.getString(MusicActivity.this, "currentplaying", "-1") + PrefUtils.getString(MusicActivity.this, "togleOn", ""));

                    Intent intent = new Intent("com.mialab.sleepangel.musicreceiver");
                    if ((PrefUtils.getString(MusicActivity.this, "togleOn", "")).equals(position + "")) {
                        PrefUtils.setString(MusicActivity.this, "togleOn", "");
                        lvMusicAdapter.notifyDataSetChanged();
                        // 停止音乐
                        intent.putExtra("control", MUSIC_PAUSE);
                        intent.putExtra("position", position);
                        sendBroadcast(intent);
                    } else {
                        //设置当前哪一个togle为true
                        PrefUtils.setString(MusicActivity.this, "togleOn", position + "");
                        lvMusicAdapter.notifyDataSetChanged();

                        //上次播放的是哪一个
                        int currentPlaying = Integer.parseInt(PrefUtils.getString(MusicActivity.this, "currentplaying", "-1"));
                        if (currentPlaying == position) {
                            //播放音乐
                            intent.putExtra("control", MUSIC_PLAY);
                            intent.putExtra("musicURL", mMusicList.get(position).getmMusicURL());
                            intent.putExtra("position", position);
                            sendBroadcast(intent);
                        } else {
                            //播放音乐
                            intent.putExtra("control", MUSIC_PLAY_URL);
                            intent.putExtra("musicURL", mMusicList.get(position).getmMusicURL());
                            intent.putExtra("position", position);
                            sendBroadcast(intent);
                        }
                        //更新currentplaying
                        PrefUtils.setString(MusicActivity.this, "currentplaying", position + "");
                    }

                }

            });

        }

    }

    private void post2Server(final boolean like, int id, final int tag) {
        HttpUtils utils = new HttpUtils();

        RequestParams params = new RequestParams();
        params.addBodyParameter("UUID", PrefUtils.getString(MusicActivity.this, GlobalContants.TOKEN, ""));
        params.addBodyParameter("MusicId", id + "");
        if (like) {
            params.addBodyParameter("like", "true");
        } else {
            params.addBodyParameter("like", "false");
        }

        utils.send(HttpMethod.POST, GlobalContants.ATTITUDE_2_CONTENT, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                //更新userCenterPage信息
                if (!TextUtils.isEmpty(mToken)) {
                    updateUserCenterPage();
                }
                mMusicList.get(tag).setmLike(like);
                mMusicData.setmMusicList(mMusicList);
                String cache = new Gson().toJson(mMusicData);
                CacheUtils.setCache(MusicActivity.this, GlobalContants.MUSIC_URL, cache);
                lvMusicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(MusicActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();

            }

        });

    }

    //通过广播通知Activity更新UserCenterPage
    private void updateUserCenterPage() {
        Intent intent = new Intent("com.mialab.sleepangel.updateusercenterpage");
        sendOrderedBroadcast(intent, null);
    }

    class LvMusicAdapter extends BaseAdapter {

        BitmapUtils utils;
        private Music music;

        public LvMusicAdapter() {
            super();
            utils = new BitmapUtils(MusicActivity.this);
            utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mMusicList.size();
        }

        @Override
        public Music getItem(int position) {
            return mMusicList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                convertView = View.inflate(MusicActivity.this, R.layout.list_music_item, null);
                holder.ivMusicImage = (ImageView) convertView.findViewById(R.id.iv_music_image);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_music_name);
                holder.mibMenu = (ImageView) convertView.findViewById(R.id.iv_menu);
                holder.tbPlay = (ToggleButton) convertView.findViewById(R.id.tb_play);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            music = getItem(position);

            // 设置menu的position标识
            holder.mibMenu.setTag(position);
            final PopupMenu popupMenu = new PopupMenu(MusicActivity.this, holder.mibMenu);
            if (music.ismLike()) {
                getMenuInflater().inflate(R.menu.music_menu_dislike, popupMenu.getMenu());
            } else {
                getMenuInflater().inflate(R.menu.music_menu_like, popupMenu.getMenu());
            }

            //设置menu点击事件
            holder.mibMenu.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    final int tag = (int) v.getTag();


                    popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.like:
                                    if (TextUtils.isEmpty(mToken)) {
                                        startActivity(new Intent(MusicActivity.this, LoginActivity.class));
                                    } else {
                                        if (music.ismLike()) {
                                            post2Server(false, mMusicList.get(tag).getId(), tag);
                                        } else {
                                            post2Server(true, mMusicList.get(tag).getId(), tag);
                                        }
                                    }
                                    break;

                                case R.id.share:
                                    Toast.makeText(MusicActivity.this, "share", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.download:
                                    Toast.makeText(MusicActivity.this, "download", Toast.LENGTH_SHORT).show();
                                    break;

                                default:
                                    break;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });

            // 设置play状态
            if ((PrefUtils.getString(MusicActivity.this, "togleOn", "")).equals(position + "")) {
                holder.tbPlay.setChecked(true);
            } else {
                holder.tbPlay.setChecked(false);
            }

            utils.display(holder.ivMusicImage, music.getmImageURL());
            holder.tvName.setText(music.getmName());

            return convertView;
        }

    }

    class ViewHolder {
        public ImageView ivMusicImage;
        public TextView tvName;
        public ToggleButton tbPlay;
        public ImageView mibMenu;
    }

    public class NextMusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int position = intent.getIntExtra("position", -1);
            if (position < mMusicList.size() - 1) {
                position++;
            } else {
                position = 0;
            }
            Log.i("receiverposition+1:", position + "");
            //更改按钮状态
            PrefUtils.setString(MusicActivity.this, "togleOn", position + "");
            lvMusicAdapter.notifyDataSetChanged();
            //播放音乐
            Intent i = new Intent("com.mialab.sleepangel.musicreceiver");
            i.putExtra("control", MUSIC_PLAY_URL);
            i.putExtra("musicURL", mMusicList.get(position).getmMusicURL());
            i.putExtra("position", position);
            sendBroadcast(i);
            //更新currentplaying
            PrefUtils.setString(MusicActivity.this, "currentplaying", position + "");

            abortBroadcast();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nextMusicReceiver);
    }
}
