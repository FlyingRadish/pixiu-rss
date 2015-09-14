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
public class MessageDialog extends BaseDialog {

    public MessageDialog(Context context) {
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
            return new MessageDialog(ctx);
        }

        @Override
        protected int getContentLayoutId() {
            return R.layout.dialog_message;
        }

        public Builder setMessage(CharSequence msg) {
            this.msg = msg;
            return this;
        }

        public MessageDialog create() {
            MessageDialog dialog = (MessageDialog) super.create();
            msg = msg == null ? "" : msg;
            dialog.setMessage(msg);
            return dialog;
        }
    }
}
