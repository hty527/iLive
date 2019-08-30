package com.android.gift.room.doalog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.gift.R;
import com.android.gift.util.AppUtils;
import com.android.gift.view.ShapeTextView;


/**
 * TinyHung@Outlook.com
 * 2017/10/19
 * 对输入框进行包装，避免了键盘弹起布局顶起的问题
 */

public class InputKeyBoardDialog extends AppCompatDialog{

    private int content_charMaxNum=99;//留言字数上限
    private String mHintTtext="写评论...";
    private String indexOutErrortex="评论内容超过字数限制";
    private final InputMethodManager mInputMethodManager;
    private int mMode;
    private EditText mInputText;
    private ShapeTextView mBtnSubmit;

    public static InputKeyBoardDialog getInstance(@NonNull Activity context){
        return new InputKeyBoardDialog(context);
    }

    public InputKeyBoardDialog(@NonNull Activity context) {
        super(context,R.style.ButtomDialogTransparentAnimationStyle);
        setContentView(R.layout.dialog_input_keyboard_layout);
        mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        initLayoutParams(Gravity.BOTTOM);
        initView();
    }

    /**
     * 设置Dialog依附在屏幕中的位置
     */
    protected void initLayoutParams(int gravity) {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();//得到布局管理者
        WindowManager systemService = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);//得到窗口管理者
        DisplayMetrics displayMetrics=new DisplayMetrics();//创建设备屏幕的管理者
        systemService.getDefaultDisplay().getMetrics(displayMetrics);//得到屏幕的宽高
        int hight= LinearLayout.LayoutParams.WRAP_CONTENT;//取出布局的高度
        attributes.height= hight;
        attributes.width= systemService.getDefaultDisplay().getWidth();
        attributes.gravity= gravity;
    }

    private void initView() {
        mInputText = (EditText) findViewById(R.id.input_edit_text);
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_submit:
                        if(mInputText.getText().toString().trim().isEmpty()){
                            Toast.makeText(getContext(),"请输入聊天内容",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(null!= mOnActionFunctionListener){
                            if(mOnActionFunctionListener.isAvailable()){
                                if(mMode==0){
                                    InputKeyBoardDialog.this.dismiss();
                                }
                                mOnActionFunctionListener.onSubmit(mInputText.getText().toString(),false);
                                mInputText.setText("");
                            }
                        }
                        break;
                }
            }
        };
        mBtnSubmit = (ShapeTextView) findViewById(R.id.btn_submit);
        mBtnSubmit.setOnClickListener(onClickListener);
        //监听输入框文字
        mInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence)&&charSequence.length()>0){
                    mBtnSubmit.setBackGroundColor(Color.parseColor("#FF7575"));
                    mBtnSubmit.setBackGroundSelectedColor(Color.parseColor("#EC5D83"));
                    if(charSequence.length()>content_charMaxNum){
                        Toast.makeText(getContext(),indexOutErrortex,Toast.LENGTH_SHORT).show();
                        mInputText.setText(AppUtils.getInstance().subString(charSequence.toString(),content_charMaxNum));
                        mInputText.setSelection(mInputText.getText().toString().length());
                    }
                }else{
                    mBtnSubmit.setBackGroundColor(Color.parseColor("#87868B"));
                    mBtnSubmit.setBackGroundSelectedColor(Color.parseColor("#A4A4A6"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(null!=mInputMethodManager&&null!=mInputText) {
            mInputMethodManager.hideSoftInputFromWindow(mInputText.getWindowToken(), 0);
        }
    }

    @Override
    public void show() {
        super.show();
        if(null!=mInputMethodManager&&null!=mInputText) {
            if(null!=mInputMethodManager) mInputMethodManager.showSoftInput(mInputText, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     *
     * @param mode 0：点击确认按钮后关闭输入框 1：不关闭
     */
    public InputKeyBoardDialog setMode(int mode){
        this.mMode=mode;
        return this;
    }

    /**
     * 设置确认按钮文字
     * @param submitText
     */
    public InputKeyBoardDialog setSubmitText(String submitText) {
        if(null!= mBtnSubmit){
            mBtnSubmit.setText(submitText);
        }
        return this;
    }


    /**
     * 回显输入框文字
     * @param inputText
     */
    public InputKeyBoardDialog setInputText(String inputText) {
        if(null!=mInputText&&!TextUtils.isEmpty(inputText)){
            mInputText.setText(inputText);
            mInputText.setSelection(inputText.length());
        }
        return this;
    }

    /**
     * 设置输入字数限制
     * @param charMaxNum
     */
    public InputKeyBoardDialog setMaxTextCount(int charMaxNum) {
        this.content_charMaxNum=charMaxNum;
        return this;
    }


    /**
     * 设置HintText
     * @param hintTtext
     */
    public InputKeyBoardDialog setHintText(String hintTtext) {
        this.mHintTtext=hintTtext;
        if(null!= mInputText)  mInputText.setHint(mHintTtext);
        return this;
    }

    /**
     * 设置背景透明度 完全透明 0.0 - 1.0 完全不透明
     * @param windownAmount
     */
    public InputKeyBoardDialog setBackgroundWindown(float windownAmount) {
        getWindow().setDimAmount(windownAmount);
        return this;
    }


    /**
     * 设置文字超出提示语
     * @param errorText
     */
    public InputKeyBoardDialog setIndexOutErrorText(String errorText){
        this.indexOutErrortex=errorText;
        return this;
    }

    public void destroy() {
        mOnActionFunctionListener=null;
    }

    public  interface OnActionFunctionListener {
        void onSubmit(String content, boolean tanmuOpen);
        boolean isAvailable();
    }
    public InputKeyBoardDialog setOnActionFunctionListener(OnActionFunctionListener onActionFunctionListener) {
        mOnActionFunctionListener = onActionFunctionListener;
        return this;
    }
    private OnActionFunctionListener mOnActionFunctionListener;
}