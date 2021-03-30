package com.example.tvprogramparser.Ui.Fragments;

import android.content.Context;
import android.os.Bundle;

import com.example.tvprogramparser.Components.FavouriteObject;
import com.example.tvprogramparser.Components.OnCompleteListener;
import com.example.tvprogramparser.Components.WorkDoneListener;
import com.example.tvprogramparser.R;
import com.example.tvprogramparser.TLS;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.lang.ArrayIndexOutOfBoundsException;

public class SmallMenuFragment extends BottomSheetDialogFragment {
    public static SmallMenuFragment local;

    /**
     * Remember providing this method with specific Bundle:
     * TLS.ARG_COUNT: int,
     * TLS.CURRENT_ARRAY_ID: int,
     * TLS.CHOSEN_OBJECT_NAME: String,
     * TLS.CHOSEN_LAYOUT: String
     */
    public static SmallMenuFragment createInstance(Bundle savedInfo) {
        SmallMenuFragment smallMenu = local;

        if (smallMenu == null) {
            synchronized (SmallMenuFragment.class) {
                smallMenu = local;
                if (smallMenu == null)
                    smallMenu = local = new SmallMenuFragment();
            }
        }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        local = null;
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
        private Context upperContext;

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
            upperContext = container.getContext();
            return new CustomViewHolder(LayoutInflater.from(container.getContext()), container);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, final int pos) {
            try {
                holder.textCell.setText(fragmentOptions[pos]);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chosenLayout.equals(TLS.ADD_TO_FAVOURITES))
                        addFavouriteProgramMethod();
                    else if (chosenLayout.equals(TLS.DELETE_FROM_FAVOURITE_PROGRAMS))
                        deleteFromFavouritePrograms();
                    else if (chosenLayout.equals(TLS.DELETE_FROM_FAVOURITE_CHANNELS))
                        deleteFromFavouriteChannels();

                    try {
                        local.onStop();
                        local.onDestroy();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                private void addFavouriteProgramMethod() {
                    // TODO: add a Toast to notify user about successful add to favourites
                    if (pos == 0) {
                        FavouriteObject.parseFavouriteProgram(chosenObjectName, upperContext);
                    }
                    try {
                        WorkDoneListener.complete(TLS.ADD_TO_FAVOURITES, OnCompleteListener.Result.SUCCESS);
                    } catch (NullPointerException e) {
                        // pass
                    }
                }

                private void deleteFromFavouritePrograms() {
                    if (pos == 0) {
                        FavouriteObject.deleteFromFavouritePrograms(chosenPos, upperContext);
                    }
                    try {
                        WorkDoneListener.complete(TLS.DELETE_FROM_FAVOURITE_PROGRAMS, OnCompleteListener.Result.SUCCESS);
                    } catch (NullPointerException e) {
                        // pass
                    }
                }

                private void deleteFromFavouriteChannels() {
                    if (pos == 0) {
                        FavouriteObject.deleteFromFavouriteChannels(chosenPos, upperContext);
                    }
                    try {
                        WorkDoneListener.complete(TLS.DELETE_FROM_FAVOURITE_CHANNELS, OnCompleteListener.Result.SUCCESS);
                    } catch (NullPointerException e) {
                        // pass
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