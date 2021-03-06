package com.mikkelthygesen.android.tid_bma_java;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikkelthygesen.android.tid_bma_java.models.BlockedItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BlackListAdapter extends RecyclerView.Adapter<BlackListAdapter.BlackListHolder> {

    private PackageManager packageManager;
    private List<BlockedItem> items;
    private boolean blocked;

    public BlackListAdapter(PackageManager packageManager, List<BlockedItem> items) {
        this.packageManager = packageManager;
        this.items = items;
        blocked = false;
    }
    public BlackListAdapter(PackageManager packageManager, List<BlockedItem> items, boolean blocked){
        this.packageManager = packageManager;
        this.items = items;
        this.blocked = blocked;
    }

    @NonNull
    @Override
    public BlackListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int app = R.layout.fragment_blacklist_one_app;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(app, parent, false);
        return new BlackListHolder((ViewGroup) view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlackListHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Set<String> getCheckPackageNames() {
        Set<String> packageNames = new HashSet<>();
        for (BlockedItem item : items) {
            if (item.isChecked()) {
                packageNames.add(item.getPackageInfo().packageName);
            }
        }
        return packageNames;
    }

    public class BlackListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mAppNameTextView;
        private ImageView mIconImage;
        private CheckBox mSelectionState;

        public BlackListHolder(ViewGroup parent) {
            super(parent);

            //Creating the views.
            mAppNameTextView = parent.findViewById(R.id.appName);
            mIconImage = parent.findViewById(R.id.app_icon);
            mSelectionState = parent.findViewById(R.id.checkApp);

            parent.setOnClickListener(this);

            mSelectionState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    items.get(getAdapterPosition()).setChecked(isChecked);
                }
            });
        }

        public void bind(BlockedItem blockedItem) {
            PackageInfo packageInfo = blockedItem.getPackageInfo();
            //Bind the icon to the imageView.
            mIconImage.setImageDrawable(packageManager
                    .getApplicationIcon(packageInfo.applicationInfo));
            //Bind the app's name to the textView.
            mAppNameTextView.setText(packageManager.getApplicationLabel(
                    packageInfo.applicationInfo).toString());
            if(!blocked)mSelectionState.setChecked(blockedItem.isChecked());
            else mSelectionState.setVisibility(View.INVISIBLE);

        }

        @Override
        public void onClick(View v) {
            BlockedItem blockedItem = items.get(getAdapterPosition());
            blockedItem.setChecked(!blockedItem.isChecked());
            mSelectionState.setChecked(blockedItem.isChecked());
        }
    }
}
