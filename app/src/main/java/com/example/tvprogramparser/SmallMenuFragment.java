package com.example.tvprogramparser;

import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

public class SmallMenuFragment extends BottomSheetDialogFragment {

    /**
     * Remember providing this method with specific Bundle:
     * TLS.ARG_COUNT: int,
     * String-array res name: String
     */
    public static SmallMenuFragment createInstance(Bundle savedInfo) {
        SmallMenuFragment smallMenu = new SmallMenuFragment();
        smallMenu.setArguments(savedInfo);
        return smallMenu;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle onSavedInstanceState) {

        return inflater.inflate(R.layout.fragment_small_menu,
                container,
                false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle onSavedInstanceState) {

        RecyclerView recView = (RecyclerView) view;
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        recView.setAdapter(new ItemAdapter(getArguments()));
    }

    private static class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView textCell;

        CustomViewHolder(@NonNull LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.fragment_small_menu_item,
                    container, false));
            textCell = itemView.findViewById(R.id.text);
        }
    }

    private static class ItemAdapter extends RecyclerView.Adapter<CustomViewHolder>{
        private int argCount;
        private String itemNamesArray;

        ItemAdapter(Bundle args) {
            this.argCount = args.getInt(TLS.ARG_COUNT);
            this.itemNamesArray = args.getString(TLS.CURRENT_ARRAY_NAME);
        }

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
            return new CustomViewHolder(LayoutInflater.from(container.getContext()), container);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int pos) {
            holder.textCell.setText(R.string.helloThere);
        }

        @Override
        public int getItemCount() {
            return argCount;
        }
    }
}