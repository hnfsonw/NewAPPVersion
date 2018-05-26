package com.hnf.guet.comhnfpatent.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.presenter.TalentPersionPresenter;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class TalentPersionActivity extends BaseActivity {
    private static final String TAG = "TalentPersionActivity";
    private String selectedAcountName;

    @BindView(R.id.iv_back_persion)
    LinearLayout IvBack;

    @BindView(R.id.head_img_persion)
    CircleImageView headImg;

    @BindView(R.id.nick_tv_person)
    TextView nickNameText;

    @BindView(R.id.persion_skills_text)
    TextView skillsText;

    @BindView(R.id.persion_information_text)
    TextView informationText;

    @BindView(R.id.touch_hime_btn)
    Button connectHimBtn;

    @BindView(R.id.colletion_btn)
    Button collectionBtn;

    private TalentPersionPresenter talentPersionPresenter;
    private Context mContext;
    private String nickName;
    private String tagAcount;//点击的专家的账号

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_talent_persion;
    }

    @Override
    protected void init() {
        talentPersionPresenter = new TalentPersionPresenter(this,this);
        final Intent intent = getIntent();
        LogUtils.e(TAG,"acountName:"+intent.getStringExtra("acountName"));
        selectedAcountName = intent.getStringExtra("acountName");
        nickName = intent.getStringExtra("nickName");
        tagAcount = intent.getStringExtra("acountName");
        initData();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap = getBitmap(intent.getStringExtra("imgUrl"));
                    headImg.post(new Runnable() {
                        @Override
                        public void run() {
                            headImg.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtils.e(TAG,"bitmap转换失败"+e);
                }
            }
        }).start();
        nickNameText.setText(intent.getStringExtra("nickName"));
        skillsText.setText(intent.getStringExtra("goodAt"));
        informationText.setText(intent.getStringExtra("information"));
        collectionBtn.setSelected(false);
    }

    private void initData() {
        talentPersionPresenter.queryCollections(MyApplication.sToken,selectedAcountName);
    }

    /**
     * 根据url地址加载网络头像
     * @param imgUrl
     * @return
     * @throws IOException
     */
    private Bitmap getBitmap(String imgUrl) throws IOException {
        LogUtils.e(TAG,"头像url:"+imgUrl);
        URL url = new URL(imgUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200){
            InputStream inputStream = null;
            try {
                inputStream = conn.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }

        return null;
    }


    @OnClick({R.id.touch_hime_btn,R.id.colletion_btn,R.id.iv_back_persion})
    public void OnclickListener(View view){
        switch (view.getId()){
            case R.id.touch_hime_btn:
                LogUtils.e(TAG,"联系他");
                //从这里进入与专家的聊天界面
                Intent chatIntent = new Intent(TalentPersionActivity.this,ChatActivity.class);
                chatIntent.putExtra(EaseConstant.EXTRA_USER_ID,nickName);
                chatIntent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
                startActivity(chatIntent);
                break;
            case R.id.colletion_btn:
                if (collectionBtn.isSelected() == false){
                    talentPersionPresenter.collectionAction(MyApplication.sToken,selectedAcountName);
                }else {
                    talentPersionPresenter.cancleCollection(MyApplication.sToken,selectedAcountName);
                }
                break;
            case R.id.iv_back_persion:
                finishActivityByAnimation(this);
                break;
        }
    }

    @Override
    protected void dismissNewok() {

    }

    public void collectionSucced() {
        printn("已收藏~");
        collectionBtn.setText("取消收藏");
        collectionBtn.setSelected(true);
    }

    /**
     * 取消收藏成功
     */
    public void cancelSuccess() {
        printn("已取消收藏");
        collectionBtn.setText("收藏");
        collectionBtn.setSelected(false);
    }

    /**
     * 已经收藏
     */
    public void hadCollected() {
        collectionBtn.setText("取消收藏");
        collectionBtn.setSelected(true);
    }
}
