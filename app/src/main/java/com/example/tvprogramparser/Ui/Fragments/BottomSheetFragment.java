package com.example.tvprogramparser.Ui.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import com.example.tvprogramparser.Components.Channel;
import com.example.tvprogramparser.Components.MainChannelsList;
import com.example.tvprogramparser.R;
import com.example.tvprogramparser.Ui.Activities.ManageFavouritesActivity;

import org.jsoup.nodes.Document;

public class BottomSheetFragment extends Fragment {
    private static Document doc = null;
    private static BottomSheetFragment local;

    // TODO: rewrite createInstance
    public static BottomSheetFragment createInstance() {
        BottomSheetFragment newFragment = local;
        if (newFragment == null)  {
            synchronized (BottomSheetFragment.class) {
                newFragment = local;
                if (newFragment == null)
                    newFragment = local = new BottomSheetFragment();
            }
        }
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle onSavedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet,
                container,
                false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        RecyclerView recView = (RecyclerView) view.findViewById(R.id.main_recycler);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        recView.setAdapter(new CustomAdapter(view.getContext()));
    }

    private static String[] getNamesArray(Channel[] arr) {
        String[] str = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            str[i] = arr[i].getName();
        }
        return str;
    }

    private static class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView text;
        final ImageView image;
        final ConstraintLayout itemChannelsLayout;

        public CustomViewHolder(@NonNull View view) {
            super(view);
            text = (TextView) view.findViewById(R.id.channel_name);
            image = (ImageView) view.findViewById(R.id.image);
            itemChannelsLayout = (ConstraintLayout) view.findViewById(R.id.item_channels_layout);
        }
    }

    private static class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
        final int count;
        final Channel[] channels;
        final Bitmap[] bitmaps;

        CustomAdapter(@NonNull final Context context) {
            channels = MainChannelsList.getChannelsList();
            bitmaps = new Bitmap[channels.length];
            for (int i = 0; i < channels.length; i++) {
                bitmaps[i] = channels[i].getIcon(context);
            }

            count = channels.length;
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, final int pos) {
            try {
                holder.text.setText(channels[pos].getName());
                holder.image.setImageBitmap(bitmaps[pos]);
                if (pos % 2 == 1)
                    holder.itemChannelsLayout.setBackgroundResource(R.drawable.item_channels_list_shape);
                else
                    holder.itemChannelsLayout.setBackgroundResource(R.drawable.item_channels_list_shape_no_borders);

                holder.itemChannelsLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(),
                                ManageFavouritesActivity.class);

                        intent.putExtra("name", channels[pos]);
                        intent.putExtra("pos", pos);
                        view.getContext().startActivity(intent);
                    }
                });
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e("BottomSheetFragment", "position is out of bounds when binding recView");
            }
        }

        @Override
        @NonNull
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup group, final int type) {
            return new CustomViewHolder(LayoutInflater.from(group.getContext())
                    .inflate(R.layout.item_channels_list, group, false));
        }

        @Override
        public int getItemCount() {
            return count;
        }
    }
}
