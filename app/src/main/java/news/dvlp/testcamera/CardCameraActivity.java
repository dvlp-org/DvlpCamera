package news.dvlp.testcamera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import news.dvlp.testcamera.view.ClipCamera;
import news.dvlp.testcamera.view.Shadow;

/**
 * Created by liubaigang on 2018/7/18.
 */

public class CardCameraActivity extends Activity implements View.OnClickListener, ClipCamera.IAutoFocus {
    TextView xiangce;
    ImageView light_open;
    private ClipCamera camera;
    private Button btn_shoot;
    private ImageView btn_cancle;
    private String path; // 需要拍照完保存的位置
    private int PHOTO_CODE = 100;
    private Shadow shadow;
    private Camera m_Camera;
    private boolean isOpen = true;
    private CameraManager manager;
    String s = "";
    private LinearLayout ll_b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        path = getIntent().getStringExtra("path");
    }



    /**
     * 初始化View
     */
    protected void initView() {
        setContentView(R.layout.activity_open_card_camera);
        camera = (ClipCamera) findViewById(R.id.open_surface_view);
        btn_shoot = (Button) findViewById(R.id.btn_open_shoot);
        xiangce = (TextView) findViewById(R.id.xiangce);
        light_open = (ImageView) findViewById(R.id.light_open);
        shadow = (Shadow) findViewById(R.id.shadow);
        btn_cancle = (ImageView) findViewById(R.id.btn_open_cancle);
        ll_b = (LinearLayout) findViewById(R.id.ll_b);
        btn_shoot.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
        xiangce.setOnClickListener(this);
        camera.setIAutoFocus(this);
        //初始化
        manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        int zf = getIntent().getIntExtra("zf", -1);
        if (zf == 1) {
            shadow.setBitmapRs(R.mipmap.icon_open_usercard1);
        } else if (zf == 2) {
            shadow.setBitmapRs(R.mipmap.icon_open_usercard2);
        }
        light_open.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if ("torch".equals(s)) {
                    s = camera.changeFlashLight(false);
                    light_open.setImageResource(R.mipmap.light_open);
                } else {
                    s = camera.changeFlashLight(true);
                    light_open.setImageResource(R.mipmap.icon_light_close);
                }

            }
        });
    }

    protected void initData() {
        String isShow = getIntent().getStringExtra("isShow");
        if (TextUtils.isEmpty(isShow)){
            xiangce.setVisibility(View.VISIBLE);
        }else{
            xiangce.setVisibility(View.GONE);
        }

    }

    public void isShowPhoto(boolean isShow) {
        if (isShow) {
            xiangce.setVisibility(View.VISIBLE);
        } else {
            xiangce.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        btn_shoot.post(new Runnable() {
            @Override
            public void run() {
                int height = btn_shoot.getHeight();
                int bHeight = shadow.getBoxWidth();
                if (bHeight > height) {
                    ViewGroup.LayoutParams layoutParams = ll_b.getLayoutParams();
                    layoutParams.height = bHeight;
                    ll_b.setLayoutParams(layoutParams);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_shoot:
                btn_shoot.setEnabled(false);
                btn_shoot.setClickable(false);
                takePhoto();
                break;
            case R.id.btn_open_cancle:
                finish();
                break;
            case R.id.xiangce:
                toPhoto(v);
                break;
        }
    }


    private void toPhoto(View view) {
        processIntentSelect();
    }

    /**
     * 开启拍照
     */
    public void takePhoto() {
        if (path == null || "".equals(path)) {
            path = Environment.getExternalStorageDirectory().getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        }
        camera.takePicture(path);
    }

    /**
     * 跳转选择照片
     */
    private void processIntentSelect() {
        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTO_CODE);
    }

    /**
     * 聚焦使用
     */
    @Override
    public void autoFocus() {
        camera.setAutoFocus();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_CODE) {
            // 获取图片路径
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String photoPath = c.getString(columnIndex);
                c.close();
                Intent intent = new Intent();
                intent.putExtra("photoPath", photoPath);
                setResult(100, intent);
                finish();
            } catch (Exception e) {

            }

        }
    }


}
