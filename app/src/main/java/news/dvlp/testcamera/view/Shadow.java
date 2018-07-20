package news.dvlp.testcamera.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import news.dvlp.testcamera.R;

/**
 * 自定义身份证拍照
 *
 * @Author Yangkui
 * @Date 2017/11/20.
 */

@SuppressLint("AppCompatCustomView")
public class Shadow extends ImageView {

    private int screenHeitht;
    private int screenWidth;
    private Context ctx;
    private Bitmap girlBitmap;
    private int girlBitWidth;
    private int girlBitHeight;
    private Rect girlSrcRect;
    private Rect girlDesRect;

    public Shadow(Context context) {
        this(context, null);
        this.ctx = context;
        setBitmapRs(R.mipmap.icon_open_usercard1);
    }

    public Shadow(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        this.ctx = context;
        setBitmapRs(R.mipmap.icon_open_usercard1);

    }

    public Shadow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ctx = context;
        initView();
        setBitmapRs(R.mipmap.icon_open_usercard1);

    }

    public  void  setBitmapRs(int rs){
        initBitmap(rs);
        invalidate();
    }
    private void initView() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(girlBitmap,girlSrcRect,getShadowRegionRect(),new Paint());
        //canvas.clipRect(0, 0, screenWidth, screenHeitht);
        canvas.clipRect(getShadowRegionRect(), Region.Op.DIFFERENCE);
        canvas.drawColor(0x60000000);
        canvas.save();
        canvas.restore();
        girlSrcRect = new Rect(0, 0, girlBitWidth, girlBitHeight);
        girlDesRect = new Rect(0, 0, girlBitWidth, girlBitHeight);



    }
    private void initBitmap(int resources) {
        //图片的宽和高
        girlBitmap = ((BitmapDrawable)ctx.getResources().getDrawable(resources)).getBitmap();
        girlBitWidth = girlBitmap.getWidth();
        girlBitHeight = girlBitmap.getHeight();


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenHeitht = getMeasuredHeight();
        screenWidth = getMeasuredWidth();
    }

    /**
     * 获取身份证取景框的矩形
     *
     * @return
     */
    private Rect getShadowRegionRect() {
        int height = (int) (screenWidth * 0.78);//拍照的阴影框的高度为屏幕宽度的80%  0.8
        int width = (int) (height * 1.58);//身份证宽高比例为1.6
//        int height= (int) (screenWidth/1.6);
        int x_center = screenWidth / 2;
        int y_center = screenHeitht / 2;

//       return new Rect(0, y_center - (height / 2), screenWidth, height/2 + y_center);
        return new Rect(x_center - (height / 2), y_center - (width / 2), x_center + (height / 2), (width / 2) + y_center);

    }

    /***
     * 获取框的宽度
     * 是显示横着的宽
     * @return
     */
    public int getBoxWidth(){
        int height = (int) (screenWidth * 0.78);//拍照的阴影框的高度为屏幕宽度的80%  0.8
        int width = (int) (height * 1.58);//身份证宽高比例为1.6
        int i = (screenHeitht - width) / 2;

        return i;
    }
}
