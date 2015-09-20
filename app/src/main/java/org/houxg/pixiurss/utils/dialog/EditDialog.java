package org.houxg.pixiurss.utils.dialog;

import android.content.Context;
import android.widget.EditText;

import org.houxg.pixiurss.R;

/**
 * desc:纯文字Dialog
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/13
 */
public class EditDialog extends BaseDialog {

    public EditDialog(Context context) {
        super(context);
    }

    public void setText(CharSequence text) {
        EditText textMsg = (EditText) findViewById(R.id.dialog_edit);
        if (textMsg != null) {
            textMsg.setText(text);
        }
    }

    public void setHint(CharSequence text) {
        EditText textMsg = (EditText) findViewById(R.id.dialog_edit);
        if (textMsg != null) {
            textMsg.setHint(text);
        }
    }

    public void setTextColor(int textColor) {
        EditText textMsg = (EditText) findViewById(R.id.dialog_edit);
        if (textMsg != null) {
            textMsg.setTextColor(textColor);
        }
    }

    public void setHintColor(int hintColor) {
        EditText textMsg = (EditText) findViewById(R.id.dialog_edit);
        if (textMsg != null) {
            textMsg.setHintTextColor(hintColor);
        }
    }

    public static class Builder extends BaseDialog.Builder {

        CharSequence text = null;
        CharSequence hint = null;
        int textColor;
        boolean setTextColor = false;
        int hintColor;
        boolean setHintColor = false;

        public Builder(Context context) {
            super(context);

        }

        @Override
        protected BaseDialog getDialogInstance(Context ctx) {
            return new EditDialog(ctx);
        }

        @Override
        protected int getContentLayoutId() {
            return R.layout.dialog_edit;
        }

        public Builder setText(CharSequence text) {
            this.text = text;
            return this;
        }

        public Builder setHint(CharSequence text) {
            this.hint = text;
            return this;
        }

        public Builder setTextColor(int textColor) {
            this.textColor = textColor;
            this.setTextColor = true;
            return this;
        }

        public Builder setHintColor(int hintColor) {
            this.hintColor = hintColor;
            this.setHintColor = true;
            return this;
        }

        public EditDialog create() {
            EditDialog dialog = (EditDialog) super.create();
            text = text == null ? "" : text;
            hint = hint == null ? "" : hint;
            dialog.setText(text);
            dialog.setHint(hint);
            if (setHintColor) {
                dialog.setHintColor(hintColor);
            }
            if (setTextColor) {
                dialog.setTextColor(textColor);
            }
            return dialog;
        }
    }
}
