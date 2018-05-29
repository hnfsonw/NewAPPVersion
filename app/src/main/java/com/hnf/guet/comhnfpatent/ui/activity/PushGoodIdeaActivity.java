package com.hnf.guet.comhnfpatent.ui.activity;

import android.Manifest;
import android.content.ContentProvider;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.presenter.PushGoodIdeaPresenter;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hnf.guet.comhnfpatent.util.UserUtil;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.filter.LFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PushGoodIdeaActivity extends BaseActivity {

    private static final String TAG = "PushGoodIdeaActivity";
    private PushGoodIdeaPresenter presenter;
    private ArrayList<String> imagePaths = null;
    private ArrayList<String> filePaths = null;
    private int columnWidth;
    private GridAdapter gridAdapter;

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.idea_commit_btn)
    Button commitBtn;

    @BindView(R.id.idea_title_editText)
    EditText titleEdText;

    @BindView(R.id.idea_content_editText)
    EditText contentEdText;

    @BindView(R.id.idea_image_btn)
    Button imagePickerBtn;

    @BindView(R.id.idea_file_btn)
    Button filePickerBtn;

    @BindView(R.id.grid_view)
    GridView gridView;

    @BindView(R.id.idea_content_number_text)
    TextView textNumber;

    @BindView(R.id.file_name_textview)
    TextView fileNameText;

    @BindView(R.id.sp_line)
    ImageView line;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_push_good_idea;
    }

    private int totalNumber = 0;

    @Override
    protected void init() {
        presenter = new PushGoodIdeaPresenter(this,this);
        initView();
        initListener();
    }

    private void initListener() {
        contentEdText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                totalNumber = totalNumber+count;
                textNumber.setText(totalNumber+"/300字");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (titleEdText.length() > 0 && contentEdText.length() > 0){
                    commitBtn.setBackgroundResource(R.drawable.edg_shape_pink);
                    commitBtn.setEnabled(true);
                }

            }
        });
    }

    private void initView() {
        mTvTitle.setText("发布需求");
        commitBtn.setEnabled(false);
        commitBtn.setBackgroundResource(R.drawable.edg_shape_gray);

        //得到GridView中每个ImageView宽高
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 4 ? 4 : cols;
        gridView.setNumColumns(cols);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
        columnWidth = (screenWidth - columnSpace * (cols-1)) / cols;
        gridView.setVisibility(View.GONE);
        fileNameText.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_back,R.id.idea_commit_btn,R.id.idea_image_btn,R.id.idea_file_btn})
    public void onViewClicked(View view) {
        String title = titleEdText.getText().toString().trim();
        String content = contentEdText.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;
            case R.id.idea_commit_btn:
                //提交需求的文件，包括图片和文件
                if (imagePaths != null || filePaths != null){
                    presenter.pushImages(imagePaths,filePaths,MyApplication.sToken,MyApplication.sAcountId,title,content);
                }else {
                    presenter.pushIdea(MyApplication.sToken,MyApplication.sAcountId,title,content);
                }
                break;
            case R.id.idea_image_btn:
                PhotoPickerIntent intent = new PhotoPickerIntent(PushGoodIdeaActivity.this);
                intent.setSelectModel(SelectModel.MULTI);
                intent.setShowCarema(true);
                intent.setMaxTotal(4);
                intent.setSelectedPaths(imagePaths);
                startActivityForResult(intent,11);
                break;
            case R.id.idea_file_btn:
                new LFilePicker().withActivity(PushGoodIdeaActivity.this)
                        .withRequestCode(12)
                        .start();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 11:
                    gridView.setVisibility(View.VISIBLE);
                    loadAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    break;
                case 12:
                    List<String> list = data.getStringArrayListExtra("paths");
                    if(filePaths == null){
                        filePaths = new ArrayList<>();
                    }
                    filePaths.clear();
                    filePaths.addAll(list);
                    getFileName(list);
                    break;
            }
        }
    }

    /**
     * 获取文件名
     * @param list
     */
    private void getFileName(List<String> list) {
        int start = 0;
        int end = 0;
        StringBuffer sb = new StringBuffer("");
        for (int i = 0;i<list.size();i++){
            start = list.get(i).lastIndexOf("/");
            end = list.get(i).lastIndexOf(".");
            if (start != -1 && end != -1){
                if (i == (list.size() - 1)){
                    sb.append(list.get(i).substring(start+1,list.get(i).length()));
                }else {
                    sb.append(list.get(i).substring(start+1,list.get(i).length())+"，");
                }
            }
        }
        line.setVisibility(View.VISIBLE);
        fileNameText.setVisibility(View.VISIBLE);
        fileNameText.setText(sb.toString());
    }

    private void loadAdpater(ArrayList<String> paths) {
        if(imagePaths == null){
            imagePaths = new ArrayList<>();
        }
        imagePaths.clear();
        imagePaths.addAll(paths);
        if(gridAdapter == null){
            gridAdapter = new GridAdapter(imagePaths);
            gridView.setAdapter(gridAdapter);
        }else {
            gridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void dismissNewok() {

    }

    //插入成功
    public void succeed() {
        dismissLoading();
        printn("发布成功");
        finishActivityByAnimation(this);
    }

    //插入失败
    public void inserFaiure(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }

    private class GridAdapter extends BaseAdapter{
        private ArrayList<String> listUrls;

        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
        }

        @Override
        public int getCount() {
            return listUrls.size();
        }

        @Override
        public String getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.item_image, null);
                imageView = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(imageView);
                // 重置ImageView宽高
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columnWidth, columnWidth);
                imageView.setLayoutParams(params);
            }else {
                imageView = (ImageView) convertView.getTag();
            }
            //框架里自带glide
            Glide.with(PushGoodIdeaActivity.this)
                    .load(new File(getItem(position)))
                    .placeholder(R.mipmap.default_error)
                    .error(R.mipmap.default_error)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
            return convertView;
        }
    }
}
