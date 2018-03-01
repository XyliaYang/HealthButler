package com.mialab.healthbutler.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.mialab.healthbutler.R;

public class GrandMenuDialog extends Dialog {
    Context mContext;
    String mLikeText;
    String mreportText;
    private ClickListenerInterface mClickListenerInterface;

    public GrandMenuDialog(Context contex, String likeText, String reportText,
                           ClickListenerInterface clickListenerInterface) {
        super(contex, R.style.Dialog);
        mContext = contex;
        mLikeText = likeText;
        mreportText = reportText;
        mClickListenerInterface = clickListenerInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        View view = View.inflate(mContext, R.layout.dialog_grand, null);
        setContentView(view);
        Button btnLike = (Button) view.findViewById(R.id.btn_like);
        Button btnReport = (Button) view.findViewById(R.id.btn_report);
        btnLike.setText(mLikeText);
        btnReport.setText(mreportText);
        btnLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mClickListenerInterface.like();
                dismiss();
            }
        });
        btnReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mClickListenerInterface.report();
                dismiss();

            }
        });
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.5);
        dialogWindow.setAttributes(lp);

    }

    public interface ClickListenerInterface {

        public void like();

        public void report();
    }

}
