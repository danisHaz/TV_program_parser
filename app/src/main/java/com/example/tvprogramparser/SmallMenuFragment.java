package com.example.tvprogramparser;

import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.lang.ArrayIndexOutOfBoundsException;

public class SmallMenuFragment extends BottomSheetDialogFragment {

    /**
     * Remember providing this method with specific Bundle:
     * TLS.ARG_COUNT: int,
     * TLS.CURRENT_ARRAY_ID: int,
     * TLS.CHOSEN_OBJECT_NAME: String,
     * TLS.CHOSEN_LAYOUT: String
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

        // TODO: handle with getArguments 'null' problem
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
        private int resourcesOptionsId;
        private String[] fragmentOptions;
        private String chosenObjectName;
        private String chosenLayout;
        private int chosenPos;

        ItemAdapter(Bundle args) {
            this.argCount = args.getInt(TLS.ARG_COUNT);
            this.resourcesOptionsId = args.getInt(TLS.CURRENT_ARRAY_ID);
            this.chosenObjectName = args.getString(TLS.CHOSEN_OBJECT_NAME);
            this.chosenLayout = args.getString(TLS.CHOSEN_LAYOUT);
            this.chosenPos = args.getInt(TLS.CHOSEN_POSITION);
        }

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
            this.fragmentOptions =  container.getContext().getResources().getStringArray(resourcesOptionsId);
            return new CustomViewHolder(LayoutInflater.from(container.getContext()), container);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, final int pos) {
            try {
                holder.textCell.setText(fragmentOptions[pos]);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            final int position = pos;

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chosenLayout.equals(TLS.ADD_TO_FAVOURITES))
                        addFavouriteProgramMethod();
                    else if (chosenLayout.equals(TLS.DELETE_FROM_FAVOURITE_PROGRAMS))
                        deleteFromFavouritePrograms();
                    else if (chosenLayout.equals(TLS.DELETE_FROM_FAVOURITE_CHANNELS))
                        deleteFromFavouriteChannels();
                }

                private void addFavouriteProgramMethod() {
                    // TODO: add a Toast to notify user about successful add to favourites
                    if (position == 0) {
                        FavouriteObject.parseFavouriteProgram(chosenObjectName);
                    }
                }

                private void deleteFromFavouritePrograms() {
                    if (position == 0) {
                        FavouriteObject.deleteFromFavouritePrograms(chosenPos);
                    }
                }

                private void deleteFromFavouriteChannels() {
                    if (position == 0) {
                        FavouriteObject.deleteFromFavouriteChannels(chosenPos);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return argCount;
        }
    }
}