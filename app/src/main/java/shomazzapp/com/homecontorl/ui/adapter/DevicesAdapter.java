package shomazzapp.com.homecontorl.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import shomazzapp.com.homecontorl.R;
import shomazzapp.com.homecontorl.mvp.model.Device;
import shomazzapp.com.homecontorl.mvp.presnter.DeviceListPresenter;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.MyViewHolder> {

    private ArrayList<Device> devices;
    private LayoutInflater inflater;
    private DeviceListPresenter presenter;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView imView;
        public Button btnToggle;

        public MyViewHolder(@NonNull View item) {
            super(item);
            btnToggle = (Button) item.findViewById(R.id.btn_device_toggle);
            name = (TextView) item.findViewById(R.id.tv_device_name);
            imView = (ImageView) item.findViewById(R.id.im_view_device);
        }

        public void bind(Device device, Context context) {
            Picasso.get().load(device.getPhotoAddress()).into(imView);
            name.setText(device.getName());
            if (device.isOn()) btnToggle.setBackground(ContextCompat.getDrawable(context, R.drawable.device_item_button_bckg_on));
            else btnToggle.setBackground(ContextCompat.getDrawable(context, R.drawable.device_item_button_bckg_off));
        }
    }

    public DevicesAdapter(Context context, ArrayList<Device> devices, DeviceListPresenter presenter) {
        this.devices = devices;
        this.presenter = presenter;
        this.inflater = LayoutInflater.from(context);
    }

    public DevicesAdapter(Context context, DeviceListPresenter presenter) {
        this.inflater = LayoutInflater.from(context);
        this.devices = new ArrayList<>();
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public DevicesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(inflater
                .inflate(R.layout.recycler_item_devices, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(devices.get(position), presenter.getContext());
        holder.itemView.setOnClickListener(v -> presenter.toggleDevice
                (devices.get(position).getId()));
    }

    public void replaceItems(@NonNull List<Device> devices) {
        this.devices.clear();
        this.devices.addAll(devices);
        notifyDataSetChanged();
    }

    public void changeDeviceToggle(int id){
        for (int i =0; i < devices.size(); i++){
            if (devices.get(i).getId() == id) {
                devices.get(i).changeToggle();
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }
}