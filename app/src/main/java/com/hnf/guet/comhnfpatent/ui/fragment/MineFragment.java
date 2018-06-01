package com.hnf.guet.comhnfpatent.ui.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.base.BaseFragment;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.bean.ResultBean;
import com.hnf.guet.comhnfpatent.presenter.MinePresenter;
import com.hnf.guet.comhnfpatent.ui.activity.AboutMeActivity;
import com.hnf.guet.comhnfpatent.ui.activity.FeedbackActivity;
import com.hnf.guet.comhnfpatent.ui.activity.HomeActivity;
import com.hnf.guet.comhnfpatent.ui.activity.ModifyPasswordActivity;
import com.hnf.guet.comhnfpatent.ui.activity.MyCollectionsActivity;
import com.hnf.guet.comhnfpatent.ui.activity.MyPushActivity;
import com.hnf.guet.comhnfpatent.ui.view.RemoveDialog;
import com.hnf.guet.comhnfpatent.ui.view.VesionDialog;
import com.hnf.guet.comhnfpatent.util.ImageUtils;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hnf.guet.comhnfpatent.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG="MineFragment";
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private ImageView iv_personal_icon;
    //    @BindView(R.id.my_message_layout)
    LinearLayout messageLayout;

    //    @BindView(R.id.reset_pass_layout)
    LinearLayout resetPassLayout;

    //    @BindView(R.id.version_upgrade_layout)
    LinearLayout versionUpgradeLayout;

    //    @BindView(R.id.feed_back_layout)
    LinearLayout opinionLayout;

    //    @BindView(R.id.about_me_layout)
    LinearLayout aboutMeLayout;

    //    @BindView(R.id.head_img)
    CircleImageView headerImg;

    //    @BindView(R.id.my_push_layout)
    LinearLayout myPushLayout;  //我的发布

    //    @BindView(R.id.my_star_layout)
    LinearLayout starLayout;   //我的收藏

    //    @BindView(R.id.login_out_btn)
    Button loginOutBtn;

    TextView nickText;

    private ResultBean mResult;
    private VesionDialog mVersionDialog;
    private MinePresenter presenter;
    private HomeActivity hActivity;
    private RemoveDialog mRemoveDialog;
    private ArrayList<String> imagePaths = null;
    private String headUrl = "";
    private SharedPreferences myshare;
    private SharedPreferences mGlobalvariable;
    private Bitmap myBitmap;


    @Override
    protected View getLayoutRes(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment,null);
        messageLayout = view.findViewById(R.id.my_message_layout);
        resetPassLayout = view.findViewById(R.id.reset_pass_layout);
        versionUpgradeLayout = view.findViewById(R.id.version_upgrade_layout);
        opinionLayout = view.findViewById(R.id.feed_back_layout);
        aboutMeLayout = view.findViewById(R.id.about_me_layout);
        headerImg = view.findViewById(R.id.head_img);
        myPushLayout = view.findViewById(R.id.my_push_layout);
        starLayout = view.findViewById(R.id.my_star_layout);
        loginOutBtn = view.findViewById(R.id.login_out_btn);
        nickText = view.findViewById(R.id.nick_tv);
        return view;
    }


    @Override
    protected void init() {
        mGlobalvariable = getContext().getSharedPreferences("globalvariable",Context.MODE_PRIVATE);
        myshare = getContext().getSharedPreferences("hnf", Context.MODE_PRIVATE);
        hActivity = (HomeActivity) getActivity();
        presenter = new MinePresenter(getContext(),hActivity,this);
        initView();
        initListener();
    }

    @Override
    protected void dismissNewok() {

    }

    @Override
    public void setData(List<ResultBean> data) {

    }

    private void initListener() {
        messageLayout.setOnClickListener(this);
        resetPassLayout.setOnClickListener(this);
        versionUpgradeLayout.setOnClickListener(this);
        opinionLayout.setOnClickListener(this);
        aboutMeLayout.setOnClickListener(this);
        headerImg.setOnClickListener(this);
        myPushLayout.setOnClickListener(this);
        starLayout.setOnClickListener(this);
        loginOutBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_img:
                toCanmeraPermissions();
                pickPictureDialog();
                LogUtils.e(TAG, "头像呗点击了");
                break;
            case R.id.my_push_layout:
                Intent toMyIdea = new Intent(getActivity(), MyPushActivity.class);
                startActivity(toMyIdea);
                LogUtils.e(TAG, "发布");
                break;
            case R.id.my_star_layout:
                Intent toCollections = new Intent(getActivity(), MyCollectionsActivity.class);
                startActivity(toCollections);
                LogUtils.e(TAG, "收藏");
                break;
            case R.id.my_message_layout:
                HomeActivity homeActivity = (HomeActivity) getActivity();
                homeActivity.toMessageFragment();
                LogUtils.e(TAG, "消息");
                break;
            case R.id.reset_pass_layout:
                LogUtils.e(TAG, "密码");
                Intent intentTWO = new Intent(getActivity(),ModifyPasswordActivity.class);
                getActivity().startActivity(intentTWO);
                break;
            case R.id.version_upgrade_layout:
                presenter.checkNewVersion();
                break;
            case R.id.feed_back_layout:
                Intent feebIntent = new Intent(getActivity(),FeedbackActivity.class);
                getActivity().startActivity(feebIntent);
                break;
            case R.id.about_me_layout:
                Intent toMe = new Intent(getActivity(), AboutMeActivity.class);
                startActivity(toMe);
                break;
            case R.id.login_out_btn:
                showDialog();
                LogUtils.e(TAG, "btn");
                break;
        }
    }

    private void pickPictureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        takePicture();
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void takePicture() {
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment
                .getExternalStorageDirectory(), "image.jpg");
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(getContext(), "com.hnf.guet.comhnfpatent.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
        }
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }


    private void showDialog() {
        mRemoveDialog.show();
        mRemoveDialog.initTitle("确认退出吗？",false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        HomeActivity activity = new HomeActivity();
        if (resultCode == activity.RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Log.d(TAG,"哈哈哈:"+photo);
            headUrl = ImageUtils.savePhoto(photo);
            myshare.edit().putString("imageUrl",headUrl).apply();
            LogUtils.e(TAG,"图片本地化后的路径："+ImageUtils.savePhoto(photo));
            photo = ImageUtils.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            myBitmap = photo;
            uploadPic(headUrl);
        }
    }

    private void uploadPic(String headUrl) {
        presenter.uploadHeadImage(headUrl, MyApplication.sToken,MyApplication.sAcountId);
    }


    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "哈哈哈哈The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    private void initView() {
        nickText.setText(mGlobalvariable.getString("nickName","未设置昵称"));
        headUrl = myshare.getString("imageUrl","");
        LogUtils.e(TAG,"本地头像地址："+headUrl+"昵称："+mGlobalvariable.getString("nickName",""));
        if (!headUrl.equals("")){
            File fileTwo = new File(headUrl);
            if (fileTwo.exists()){
                Bitmap bm = BitmapFactory.decodeFile(headUrl);
                headerImg.setImageBitmap(bm);
            }
        }
        if (mRemoveDialog == null){
            mRemoveDialog = new RemoveDialog(getContext(),(HomeActivity)getActivity());
        }
    }

    public void setCompleted() {
        headerImg.setImageBitmap(myBitmap);
    }
}
