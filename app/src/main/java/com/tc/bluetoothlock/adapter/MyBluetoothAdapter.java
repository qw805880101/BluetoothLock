package com.tc.bluetoothlock.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.psylife.wrmvplibrary.utils.LogUtil;
import com.tc.bluetoothlock.R;
import com.tc.bluetoothlock.adapter.MyBluetoothAdapter.BluetoothHolder;

import java.util.List;

public class MyBluetoothAdapter extends RecyclerView.Adapter<BluetoothHolder> {

    private List<BluetoothDevice> mBluetoothDevices;
    private Context mContext;
    private LayoutInflater mInflater;

    public MyBluetoothAdapter(Context mContext, List<BluetoothDevice> mBluetoothDevices) {
        this.mContext = mContext;
        this.mBluetoothDevices = mBluetoothDevices;
        mInflater = LayoutInflater.from(mContext);
    }

    /**
     * 更新
     *
     * @param mBluetoothDevices
     */
    public void setNewBluetoothDevices(List<BluetoothDevice> mBluetoothDevices) {
        this.mBluetoothDevices = mBluetoothDevices;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BluetoothHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_bluetooth, null);
        BluetoothHolder mBluetoothHolder = new BluetoothHolder(view);
        return mBluetoothHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothHolder holder, int position) {
        BluetoothDevice mBluetoothDevice = mBluetoothDevices.get(position);

        LogUtil.d("name:" + mBluetoothDevice.getName());
        LogUtil.d("address:" + mBluetoothDevice.getAddress());

        if (null != mBluetoothDevice.getName() && !"".equals(mBluetoothDevice.getName()))
            holder.txtBluetoothName.setText(mBluetoothDevice.getName());
        holder.txtBluetoothAddress.setText(mBluetoothDevice.getAddress());
    }

    @Override
    public int getItemCount() {
        return mBluetoothDevices.size();
    }

    public class BluetoothHolder extends RecyclerView.ViewHolder {

        TextView txtBluetoothName;
        TextView txtBluetoothAddress;

        public BluetoothHolder(View itemView) {
            super(itemView);
            txtBluetoothName = itemView.findViewById(R.id.txt_bluetooth_name);
            txtBluetoothAddress = itemView.findViewById(R.id.txt_bluetooth_address);
        }
    }
}
