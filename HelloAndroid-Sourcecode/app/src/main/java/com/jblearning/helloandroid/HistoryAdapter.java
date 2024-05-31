package com.jblearning.helloandroid;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final int HEADER = 0;
    private static final int ITEMS = 1;
    private List<WeightEntry> weightList;
    private DatabaseHelper dbHelper;
    private Context dbContext;
    private ImageView expandedImage;
    public HistoryAdapter(Context dbContext, List<WeightEntry> weightList, ImageView Expandedview) {
        this.dbContext = dbContext;
        this.weightList = weightList;
        this.expandedImage = Expandedview;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER : ITEMS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        dbHelper = new DatabaseHelper(dbContext);
        if (viewType == HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_weight_entry, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weight_entry, parent, false);
            return new EntryViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEMS) {
            WeightEntry weightEntry = weightList.get(position-1);// Adjust for header
            EntryViewHolder entryView = (EntryViewHolder) holder;
            entryView.tvWeight.setText(String.valueOf(weightEntry.getWeight()));
            entryView.tvDate.setText(weightEntry.getDate());
            if (weightEntry.getImagePath() != null &&
                    !weightEntry.getImagePath().isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(weightEntry.getImagePath());
                Glide.with(dbContext).load(new File(weightEntry.getImagePath())).into(((EntryViewHolder) holder).ivExpandImage);

                ((EntryViewHolder) holder).ivExpandImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(dbContext);
                        dialog.setContentView(R.layout.expanded_image);
                        expandedImage = dialog.findViewById(R.id.expandedImageView);
                        Glide.with(dbContext).load(new File(weightEntry.getImagePath())).into(expandedImage);
                        expandedImage.setVisibility(View.VISIBLE);

                        expandedImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                expandedImage.setVisibility(View.GONE);
                            }
                        });
                        dialog.show();
                    }
                });
            }

            entryView.ivDeleteImage.setOnClickListener(v -> {
                // Delete the user from the database
                DeleteEntry(weightEntry.getId());
                weightList.remove(position - 1);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, weightList.size());
            });
        }
    }

    @Override
    public int getItemCount() {
        return weightList.size() + 1;
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public static class EntryViewHolder extends RecyclerView.ViewHolder {
        TextView tvWeight, tvDate, tvId;
        ImageView ivDeleteImage;
        ImageView ivExpandImage;
        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWeight = itemView.findViewById(R.id.tv_weight);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivExpandImage = itemView.findViewById(R.id.imageButton);
            ivDeleteImage = itemView.findViewById(R.id.deleteIcon);
        }
    }
    protected void DeleteEntry(int Id) {
        dbHelper.deleteWeightEntry(Id);
    }
}