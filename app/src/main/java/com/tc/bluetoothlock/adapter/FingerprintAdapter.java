package com.tc.bluetoothlock.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.psylife.wrmvplibrary.utils.ToastUtils;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.bean.LockInfo;

import java.util.List;

public class FingerprintAdapter extends BaseQuickAdapter<LockInfo, BaseViewHolder> {

    public FingerprintAdapter(List<LockInfo> data) {
        super(R.layout.item_password, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LockInfo item) {

        LinearLayout passwordItem = helper.getView(R.id.content);
        passwordItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        LinearLayout del = helper.getView(R.id.right);
        del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, LockInfoActivity.class);
//                mContext.startActivity(intent);
                ToastUtils.showToast(mContext, "删除");
            }
        });

    }
}
