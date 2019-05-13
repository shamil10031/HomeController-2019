package shomazzapp.com.homecontorl.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        public TextView id;
        public TextView name;
        public TextView isOn;
        public TextView descriprion;
        public ImageView imView;

        public MyViewHolder(@NonNull View item) {
            super(item);
            id = (TextView) item.findViewById(R.id.tv_device_id);
            name = (TextView) item.findViewById(R.id.tv_device_name);
            isOn = (TextView) item.findViewById(R.id.tv_device_is_on);
            descriprion = (TextView) item.findViewById(R.id.tv_device_descr);
            imView = (ImageView) item.findViewById(R.id.im_view_device);
        }

        public void bind(Device device) {
            Picasso.get().load(device.getPhotoAddress()).into(imView);
            id.setText("id: " + device.getId());
            name.setText(device.getName());
            descriprion.setText(device.getDescription());
            isOn.setText(device.isOn() ? "On" : "Off");
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
        holder.bind(devices.get(position));
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