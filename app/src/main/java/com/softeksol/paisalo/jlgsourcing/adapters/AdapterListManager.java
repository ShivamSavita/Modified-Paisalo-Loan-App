package com.softeksol.paisalo.jlgsourcing.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.entities.Manager;
import com.softeksol.paisalo.jlgsourcing.entities.ViewHolders.ManagerViewHolder;

import java.util.List;

public class AdapterListManager extends ArrayAdapter<Manager> {
    Context context;
    int resourecId;
    List<Manager> managers = null;

    public AdapterListManager(Context context, @LayoutRes int resource, @NonNull List<Manager> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourecId = resource;
        this.managers = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ManagerViewHolder holder;
        if (v == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            v = inflater.inflate(resourecId, parent, false);
            holder = new ManagerViewHolder();
            holder.tvManagerName = (TextView) v.findViewById(R.id.designamtionName);
            holder.tvManagerCode = (TextView) v.findViewById(R.id.placeAndGroup);
            holder.tvManagerCreator = (TextView) v.findViewById(R.id.branccodeAndCreator);
            holder.linearLayout_list = (LinearLayout) v.findViewById(R.id.linearLayout_list);
            v.setTag(holder);
        } else {
            holder = (ManagerViewHolder) v.getTag();
        }

        Manager manager = managers.get(position);
        holder.tvManagerName.setText(manager.FOName);
        holder.tvManagerCode.setText(manager.AreaName+"/"+manager.AreaCd);
        holder.tvManagerCreator.setText(manager.FOCode+"/"+manager.Creator);
        if (position % 2 == 1) {
            holder.linearLayout_list.setBackgroundResource(R.drawable.managerlist_gradientgrey);
        } else {
            holder.linearLayout_list.setBackgroundResource(R.drawable.managerlist_gradient);
        }

        Animation animation = new ScaleAnimation(
                0f, 1f,
                0, 1f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f);
        animation.setFillAfter(true);
        animation.setDuration(220);
        v.setAnimation(animation);

        return v;
    }

}
