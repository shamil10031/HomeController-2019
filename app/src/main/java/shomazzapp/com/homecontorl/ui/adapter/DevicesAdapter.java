package shomazzapp.com.homecontorl.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.MyViewHolder> {

    private ArrayList<Device> devices;
    private LayoutInflater inflater;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView name;
        public TextView descriprion;
        public ImageView imView;

        public MyViewHolder(@NonNull View item) {
            super(item);
            id = (TextView) item.findViewById(R.id.tv_device_id);
            name = (TextView) item.findViewById(R.id.tv_device_name);
            descriprion = (TextView) item.findViewById(R.id.tv_device_descr);
            imView = (ImageView) item.findViewById(R.id.im_view_device);
        }

        public void bind(Device device) {
            Picasso.get().load(device.getPhotoAddress()).into(imView);
            id.setText(device.getId()+"");
            name.setText(device.getName());
            descriprion.setText(device.getDescription());
        }
    }

    public DevicesAdapter(Context context, ArrayList<Device> devices) {
        this.devices = devices;
        this.inflater = LayoutInflater.from(context);
    }

    public DevicesAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.devices = new ArrayList<>();
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
    }

    public void replaceItems(@NonNull List<Device> devices) {
        this.devices.clear();
        this.devices.addAll(devices);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }
}