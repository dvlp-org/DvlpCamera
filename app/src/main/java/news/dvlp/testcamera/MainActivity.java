package news.dvlp.testcamera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Map;

import news.dvlp.testcamera.IOSDialog.ActionSheetDialog;
import news.dvlp.testcamera.utils.FileUtils;
import news.dvlp.testcamera.utils.ToastUtils;
import news.dvlp.testcamera.utils.Utils;

public class MainActivity extends Activity {

    private String tempPicPath = "";//图片拍照路径
    private String tempDir = "";//临时目录
    private String cardidFrontPhotoPath, cardidBackPhotoPath;
    private boolean hasFront, hasBack;// 拥有正反身份证照片

    private int code_photo_camera;// 标记是否拍照10=拍照11选择
    private int code_photo;// photo 处理Code标记（正反）
    private final int CODE_PHOTO_FRONT = 1, CODE_PHOTO_BACK = 2;// photo 身份证正反CODE
    private ImageView icCardidFront,icCardidBack;
    private int res_picture_front_default = R.mipmap.icon_offical_camera_on;// 默认身份证正
    private int res_picture_back_default = R.mipmap.icon_offical_camera_off;// 默认身份证反

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView=findViewById(R.id.btn);
        TextView textView2=findViewById(R.id.btn2);
        icCardidFront=findViewById(R.id.iv_front);
        icCardidBack=findViewById(R.id.iv_back);

        Utils.init(this);

        try {
            // 创建临时目录
            tempDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "temp";
            File file = new File(tempDir);
            if (!file.exists()) file.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tempPicPath = getNewFilePath();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPhoto(1);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPhoto(CODE_PHOTO_BACK);

            }
        });
    }

    /**
     * 身份证拍照
     */
    private void processPhoto(int code) {
        code_photo = code;


        ActionSheetDialog actionSheetDialog = new ActionSheetDialog(this).builder().setCancelable(true);
        boolean photoIn = false;//是否已经有图片
        if (code_photo == CODE_PHOTO_FRONT) {
            photoIn = isFileExist(cardidFrontPhotoPath);
        } else if (code_photo == CODE_PHOTO_BACK) {
            photoIn = isFileExist(cardidBackPhotoPath);
        }
        if (photoIn) {
            actionSheetDialog.addSheetItem("查看", ActionSheetDialog.SheetItemColor.Blue,
                    new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            String path = "";
                            if (code_photo == CODE_PHOTO_FRONT) {
                                path = cardidFrontPhotoPath;
                            } else if (code_photo == CODE_PHOTO_BACK) {
                                path = cardidBackPhotoPath;
                            }
                            Intent intent = new Intent(MainActivity.this, ImageBrowserActivity.class);
                            intent.putExtra("path", path);
                            startActivity(intent);
                        }
                    }).addSheetItem("删除", ActionSheetDialog.SheetItemColor.Red,
                    new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            if (code_photo == CODE_PHOTO_FRONT) {
                                icCardidFront.setImageResource(res_picture_front_default);
                                new File(cardidFrontPhotoPath).delete();
                                cardidFrontPhotoPath = null;
                                hasFront = false;
                            } else if (code_photo == CODE_PHOTO_BACK) {
                                icCardidBack.setImageResource(res_picture_back_default);
                                new File(cardidBackPhotoPath).delete();
                                cardidBackPhotoPath = null;
                                hasBack = false;
                            }
                        }
                    });
            actionSheetDialog.show();
        } else {
            code_photo_camera = 10;
            tempPicPath = getNewFilePath();
            processPhotoImpl(code_photo);
        }
//        code_photo_camera = 10;
//        tempPicPath = getNewFilePath();
//        processPhotoImpl(code_photo);
    }
    /***************拍照\选择相关处理****************/
    private void processPhotoImpl(int code_photo) {
        // 权限适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (code_photo_camera == 10) {
                String[] ps = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (checkPermissionGranted(ps))
                    processIntentCamera(code_photo);
                else
                    requestPermissions(ps, code_photo_camera);
            } else {
                // 功能已分发拍照页面
            }
        } else {
            if (code_photo_camera == 10) {
                processIntentCamera(code_photo);
            } else {
                // 功能已分发拍照页面
            }
        }
    }

    /**
     * 跳转拍照
     * 必须获取到权限信息
     */
    private void processIntentCamera(int code) {
        Intent intent = new Intent(this, CardCameraActivity.class);
        intent.putExtra("path", tempPicPath);
        intent.putExtra("zf", code);
        intent.putExtra("isShow", "0");
        startActivityForResult(intent, code_photo_camera);
    }

    /***************图片压缩**********************/

    /**
     * 设置图片成功
     */
    private void setDealPictureSuccess(String photoPath) {
        // 设置图片2View
        if (code_photo == CODE_PHOTO_FRONT) {
            cardidFrontPhotoPath = photoPath;
            Glide.with(this)
                    .load(new File(cardidFrontPhotoPath))
                    .into(icCardidFront);
            // 上传图片识别
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String frontCode = FileUtils.fileToBase64(cardidFrontPhotoPath);
                        if (mHandler != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("frontCode", frontCode);
                            Message message = mHandler.obtainMessage();
                            message.setData(bundle);
                            message.what = 2;
                            mHandler.sendMessage(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else if (code_photo == CODE_PHOTO_BACK) {
            cardidBackPhotoPath = photoPath;
            Glide.with(this)
                    .load(new File(cardidBackPhotoPath))
                    .into(icCardidBack);
            // 上传照片识别
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String backCode = FileUtils.fileToBase64(cardidBackPhotoPath);
                        if (mHandler != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("backCode", backCode);
                            Message message = mHandler.obtainMessage();
                            message.setData(bundle);
                            message.what = 3;
                            mHandler.sendMessage(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    private MyHandler mHandler = new MyHandler(this);// hanlder

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity act = mActivity.get();
            if (act != null) {
                Bundle bundle = msg.getData();
                if (bundle != null) {
                    if (msg.what == 1) {
                        String photoPath = bundle.getString("photoPath");
                        ToastUtils.showDownToast(photoPath+"图片地址--------");
                        act.setDealPictureSuccess(photoPath);
                    } else if (msg.what == 2) {
                        String frontCode = bundle.getString("frontCode");
                        act.reqFrontCard(frontCode);
                    } else if (msg.what == 3) {
                        String backCodde = bundle.getString("backCode");
                        act.reqBackCard(backCodde);
                    }
                }
            }
        }
    }

    /**
     * 获取一个新的，可用的文件路径
     *
     * @return
     */
    private String getNewFilePath() {
        return tempDir + File.separator + System.currentTimeMillis() + ".jpg";
    }

    /**
     * 检查权限是否允许
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissionGranted(String[] permissions) {
        boolean flag = true;
        for (String p : permissions) {
            if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                flag = false;
                break;
            }
        }
        return flag;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissioned = false;
        if (requestCode == 10) {
            for (int i = 0; i < permissions.length; i++) {
                String s = permissions[i];
                if (s.equals(Manifest.permission.CAMERA) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    permissioned = true;
                    processIntentCamera(code_photo);
                    break;
                }
            }
        } else {
            for (int i = 0; i < permissions.length; i++) {
                String s = permissions[i];
                if (s.equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    permissioned = true;

                    break;
                }
            }
        }
        if (!permissioned) {
            ToastUtils.showShortToast("您拒绝了相关权限申请");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode > 9) {
            try {
                // 得到结果处理
                String photoPath = "";
                if (resultCode == 100) {
                    try {
                        photoPath = data.getStringExtra("photoPath");
                        // 处理图片

                        String newPath = getNewFilePath();
                        copyFile(photoPath, newPath);
                        photoPath = newPath;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    photoPath = tempPicPath;

                }
                // 处理图片相关信息
                if (isFileExist(photoPath)) {
                    // 图片压缩处理
                    processComparess(photoPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showShortToast("拍照出错，请重试");
            }
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) throws Exception {
        int bytesum = 0;
        int byteread = 0;
        File oldfile = new File(oldPath);
        if (oldfile.exists()) { //文件存在时
            InputStream inStream = new FileInputStream(oldPath); //读入原文件
            FileOutputStream fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[1024];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; //字节数 文件大小
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
        }
    }

    /**
     * 判断路径文件是否存在
     *
     * @param path
     * @return
     */
    private boolean isFileExist(String path) {
        return TextUtils.isEmpty(path) ? false : new File(path).exists();
    }
    /**
     * 处理压缩程序
     *
     * @param photoPath
     */
    private void processComparess(final String photoPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    compressImageByPixel(photoPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mHandler != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("photoPath", photoPath);
                    Message message = mHandler.obtainMessage();
                    message.what = 1;
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }
            }
        }).start();
    }
    /**
     * 压缩
     *
     * @param imgPath
     * @return
     * @author JPH
     * @date 2014-12-5下午11:30:59
     */
    private void compressImageByPixel(String imgPath) throws Exception {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        BitmapFactory.decodeFile(imgPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;
        float maxWSize = 640;//max size
        float maxHSize = 415;//max size
        int be = 1;
        if (width >= height && width > maxWSize) {//缩放比,用高或者宽其中较大的一个数据进行计算
            be = (int) (newOpts.outWidth / maxWSize);
            be++;
        } else if (width < height && height > maxHSize) {
            be = (int) (newOpts.outHeight / maxHSize);
            be++;
        }
        newOpts.inSampleSize = be;//设置采样率
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, new FileOutputStream(new File(imgPath)));
    }

    // 上传前身份证
    private void reqFrontCard(String frontCard) {
        ToastUtils.showDownToast("上传请求"+frontCard);
    }

    private void reqBackCard(String frontCard) {
        ToastUtils.showDownToast("上传请求2"+frontCard);
    }

}