package org.houxg.pixiurss.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.houxg.pixiurss.R;

/**
 * desc:
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/13
 */
public class BaseDialog extends Dialog {

    public interface OnButtonClickListener {
        void onClick(BaseDialog dialog, int buttonType);
    }

    Bundle data;
    OnButtonClickListener positiveButtonListener;
    OnButtonClickListener negativeButtonListener;

    public BaseDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void setData(Bundle data) {
        this.data = data;
    }

    public String getDataString(String key, String defVal) {
        return data == null ? defVal : data.getString(key, defVal);
    }

    public long getDataLong(String key, long defVal) {
        return data == null ? defVal : data.getLong(key, defVal);
    }

    public long getDataInt(String key, int defVal) {
        return data == null ? defVal : data.getInt(key, defVal);
    }

    public float getDataInt(String key, float defVal) {
        return data == null ? defVal : data.getFloat(key, defVal);
    }

    public double getDataDouble(String key, double defVal) {
        return data == null ? defVal : data.getDouble(key, defVal);
    }


    public static OnButtonClickListener getDismissListener() {
        return new OnButtonClickListener() {
            @Override
            public void onClick(BaseDialog dialog, int buttonType) {
                dialog.dismiss();
            }
        };
    }

    public void setButtonListener(int which, OnButtonClickListener listener) {
        View button;
        switch (which) {
            case BUTTON_POSITIVE:
                this.positiveButtonListener = listener;
                button = findViewById(R.id.dialog_btn_positive);
                if (button != null) {
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (positiveButtonListener != null) {
                                positiveButtonListener.onClick(BaseDialog.this, Dialog.BUTTON_POSITIVE);
                            }
                        }
                    });
                }
                break;

            case BUTTON_NEGATIVE:
                this.negativeButtonListener = listener;
                button = findViewById(R.id.dialog_btn_negative);
                if (button != null) {
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (negativeButtonListener != null) {
                                negativeButtonListener.onClick(BaseDialog.this, Dialog.BUTTON_NEGATIVE);
                            }
                        }
                    });
                }
                break;
        }
    }

    public void setButtonColor(int which, ColorStateList textColor, int bgResId) {
        TextView button = getButton(which);
        if (button == null) {
            return;
        }

        button.setTextColor(textColor);
        button.setBackgroundResource(bgResId);
    }

    public void setButtonText(int which, CharSequence text) {
        TextView button = getButton(which);
        if (button == null) {
            return;
        }

        button.setText(text);
    }

    private TextView getButton(int which) {
        TextView button = null;
        switch (which) {
            case BUTTON_POSITIVE:
                button = (TextView) findViewById(R.id.dialog_btn_positive);
                break;
            case BUTTON_NEGATIVE:
                button = (TextView) findViewById(R.id.dialog_btn_negative);
                break;
        }
        return button;
    }

    public void setButtonColor(int which, int textColor, int bgResId) {
        TextView button = getButton(which);
        if (button == null) {
            return;
        }

        button.setTextColor(textColor);
        button.setBackgroundResource(bgResId);
    }

    public void setButtonVisibility(int which, int visibility) {
        TextView button = getButton(which);
        if (button == null) {
            return;
        }
        button.setVisibility(visibility);
    }


    public static class Builder {
        //TODO:放到style?
        protected final int positiveBtnBgId = R.drawable.dialog_btn_positive_bg;
        protected final int negativeBtnBgId = R.drawable.dialog_btn_negative_bg;
        protected final int positiveBtnTextColorId = R.color.dialog_btn_positive_text;
        protected final int negativeBtnTextColorId = R.color.dialog_btn_negative_text;
        // Button
        float widthScale = 888f / 1080f;
        public final static int BUTTON_HAS_NO_ONE = 0;
        public final static int BUTTON_HAS_POSITIVE = 1;
        public final static int BUTTON_HAS_NEGATIVE = 2;

        protected Context ctx;
        protected CharSequence title = "";
        protected View contentView = null;
        protected CharSequence positiveBtnText = null;
        protected CharSequence negativeBtnText = null;

        protected boolean cancelable = true;
        protected boolean outsideTouchable = true;

        protected OnButtonClickListener positiveBtnClickListener = null;
        protected OnButtonClickListener negativeBtnClickListener = null;

        protected int buttonState = BUTTON_HAS_NO_ONE;

        public Builder(Context context) {
            this.ctx = context;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setNoTitle() {
            this.title = null;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(int titleId) {
            this.title = ctx.getText(titleId);
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, OnButtonClickListener listener) {
            return setNegativeButton(ctx.getText(negativeButtonText), listener);
        }

        public Builder setNegativeButton(CharSequence negativeButtonText, OnButtonClickListener listener) {
            this.negativeBtnText = negativeButtonText;
            this.negativeBtnClickListener = listener;
            this.buttonState |= BUTTON_HAS_NEGATIVE;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText, OnButtonClickListener listener) {
            return setPositiveButton((String) ctx.getText(positiveButtonText), listener);
        }

        public Builder setPositiveButton(String positiveButtonText, OnButtonClickListener listener) {
            this.positiveBtnText = positiveButtonText;
            this.positiveBtnClickListener = listener;
            this.buttonState |= BUTTON_HAS_POSITIVE;
            return this;
        }

        protected BaseDialog getDialogInstance(Context ctx) {
            BaseDialog dialog = new BaseDialog(ctx);
            return dialog;
        }

        protected int getContentLayoutId() {
            return -1;
        }

        public BaseDialog create() {
            BaseDialog dialog = getDialogInstance(ctx);
            //初始化基础DialogView
            View dialogView = LayoutInflater.from(ctx).inflate(R.layout.dialog_base, null);
            dialog.setContentView(dialogView);

            //设置宽度
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
            lp.width = (int) (dm.widthPixels * widthScale);
            dialog.getWindow().setAttributes(lp);

            //设置Dialog属性
            dialog.setCancelable(cancelable);
            dialog.setCanceledOnTouchOutside(outsideTouchable);

            //设置标题
            TextView textTitle = (TextView) dialog.findViewById(R.id.dialog_text_title);
            if (this.title != null) {
                textTitle.setText(title);
            } else {
                textTitle.setVisibility(View.GONE);
            }

            //设置内容
            ViewGroup contentContainer = (ViewGroup) dialog.findViewById(R.id.dialog_panel_content);
            if (contentView != null) {
                contentContainer.removeAllViews();
                contentContainer.addView(contentView);
            } else if (getContentLayoutId() > 0) {
                contentContainer.removeAllViews();
                View contentView = LayoutInflater.from(ctx).inflate(getContentLayoutId(), contentContainer, true);
//                contentContainer.addView(contentView);
            }

            //设置按钮
            if (buttonState == BUTTON_HAS_NO_ONE) {
                dialog.findViewById(R.id.dialog_panel_btn).setVisibility(View.GONE);
            }
            if ((buttonState & BUTTON_HAS_POSITIVE) > 0) {
                setButton(dialog,
                        Dialog.BUTTON_POSITIVE,
                        positiveBtnText,
                        positiveBtnTextColorId,
                        positiveBtnBgId,
                        positiveBtnClickListener);
            }
            if ((buttonState & BUTTON_HAS_NEGATIVE) > 0) {
                setButton(dialog,
                        Dialog.BUTTON_NEGATIVE,
                        negativeBtnText,
                        negativeBtnTextColorId,
                        negativeBtnBgId,
                        negativeBtnClickListener);
            }

            if ((buttonState & (BUTTON_HAS_NEGATIVE | BUTTON_HAS_POSITIVE)) <= 0) {
                dialog.findViewById(R.id.dialog_divider_btn).setVisibility(View.GONE);
            }
            return dialog;
        }

        private void initButton(BaseDialog dialog, TextView button, int which, int colorId, CharSequence text,
                                OnButtonClickListener listener) {
            if (text != null) {
                button.setText(text);
                button.setTextColor(ctx.getResources().getColorStateList(colorId));
                if (listener != null) {
                    dialog.setButtonListener(which, listener);
                }
            } else {
                button.setVisibility(View.GONE);
            }
        }

        private void setButton(BaseDialog dialog, int which, CharSequence text, int textColor, int bgResId, OnButtonClickListener listener) {
            if (text != null) {
                dialog.setButtonColor(which, ctx.getResources().getColorStateList(textColor), bgResId);
                dialog.setButtonText(which, text);
                dialog.setButtonListener(which, listener);
            } else {
                // if no button just set the visibility to GONE
                dialog.setButtonVisibility(which, View.GONE);
            }
        }
    }
}
