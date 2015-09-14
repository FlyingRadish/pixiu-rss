package org.houxg.pixiurss.utils.dialog;

import android.content.Context;
import android.widget.TextView;

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

    public void setMessage(CharSequence msg) {
        TextView textMsg = (TextView) findViewById(R.id.dialog_text_message);
        if (textMsg != null) {
            textMsg.setText(msg);
        }
    }

    public static class Builder extends BaseDialog.Builder {

        CharSequence msg = null;

        public Builder(Context context) {
            super(context);
        }

        @Override
        protected BaseDialog getDialogInstance(Context ctx) {
            return new EditDialog(ctx);
        }

        @Override
        protected int getContentLayoutId() {
            return R.layout.dialog_message;
        }

        public Builder setMessage(CharSequence msg) {
            this.msg = msg;
            return this;
        }

        public EditDialog create() {
            EditDialog dialog = (EditDialog) super.create();
            msg = msg == null ? "" : msg;
            dialog.setMessage(msg);
            return dialog;
        }
    }
}
