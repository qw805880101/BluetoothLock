package com.tc.bluetoothlock.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tc.bluetoothlock.R;

public class CustomDialog {

    private Context context;
    private Dialog dialog;
    private TextView txtHint, txtMessage;
    private Display display;
    private Button btConfirm, btCancel;

    private OnConfirmClickListener mOnConfirmClickListener;

    public CustomDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    @SuppressWarnings("deprecation")
    public CustomDialog builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_view, null);

        view.setMinimumWidth(display.getWidth());

        txtHint = view.findViewById(R.id.txt_hint);
        txtMessage = view.findViewById(R.id.txt_message);

        btConfirm = view.findViewById(R.id.bt_confirm);
        btConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnConfirmClickListener != null)
                    mOnConfirmClickListener.onClickConfirm();
            }
        });
        btCancel = view.findViewById(R.id.bt_cancel);
        btCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnConfirmClickListener.onClickCancel();
                dialog.dismiss();
            }
        });

        dialog = new Dialog(context, R.style.customDialog);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = dialogWindow.getWindowManager();
        Display defaultDisplay = m.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (defaultDisplay.getWidth() * 0.70);
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        return this;
    }

    /**
     * 设置弹框标题
     *
     * @param hint
     */
    public CustomDialog setTitle(String hint) {
        txtHint.setText(hint);
        return this;
    }

    /**
     * 设置提示语
     *
     * @param message
     * @return
     */
    public CustomDialog setMessage(String message) {
        txtMessage.setText(message);
        return this;
    }

    public CustomDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public CustomDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public CustomDialog setConfirmListener(OnConfirmClickListener mOnConfirmClickListener) {
        this.mOnConfirmClickListener = mOnConfirmClickListener;
        return this;
    }

    public interface OnConfirmClickListener {
        void onClickConfirm();

        void onClickCancel();
    }

}
