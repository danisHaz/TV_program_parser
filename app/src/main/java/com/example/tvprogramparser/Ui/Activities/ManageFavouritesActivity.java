package com.example.tvprogramparser.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvprogramparser.Components.OnCompleteListener;
import com.example.tvprogramparser.Components.WorkDoneListener;
import com.example.tvprogramparser.R;
import com.example.tvprogramparser.TLS;
import com.example.tvprogramparser.Ui.Fragments.SmallMenuFragment;
import com.example.tvprogramparser.ProgramListViewModel;
import com.example.tvprogramparser.databinding.ActivityManageFavouritesBinding;

public class ManageFavouritesActivity extends AppCompatActivity {
    private ProgramListViewModel viewModel;
    private ActivityManageFavouritesBinding binding;
/**
*   Do not forget to write onitemclicklistener for ListView
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_favourites);
        viewModel = new ProgramListViewModel(this, intent);
        binding.setList(viewModel);
        setListeners();
    }

    private void setListeners() {
        ListView lv = (ListView) findViewById(R.id.manageFavouritesList);
        lv.setOnItemClickListener(
                (AdapterView<?> adapter, View view, int pos, long id) -> {
                    Bundle fragmentInfo = new Bundle();
                    fragmentInfo.putInt(TLS.ARG_COUNT, getResources()
                            .getStringArray(R.array.favouriteProgramsMenu).length);
                    fragmentInfo.putInt(TLS.CURRENT_ARRAY_ID, R.array.favouriteProgramsMenu);
                    fragmentInfo.putString(
                            TLS.CHOSEN_OBJECT_NAME,
                            ((TextView)view).getText().toString()
                    );
                    fragmentInfo.putString(TLS.CHOSEN_LAYOUT, TLS.ADD_TO_FAVOURITES);
                    fragmentInfo.putInt(TLS.CHOSEN_POSITION, pos);

                    SmallMenuFragment smallMenu = SmallMenuFragment.createInstance(fragmentInfo);
                    WorkDoneListener.setNewListener(new OnCompleteListener() {
                        @Override
                        public void doWork(Bundle bundle) {
                            Toast.makeText(getApplicationContext(), "Program is set to favorites",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }.setTag(TLS.ADD_TO_FAVOURITES));
                    smallMenu.show(getSupportFragmentManager(), "smallMenu");
                }
        );
    }

    public void setListViewAdapter(ArrayAdapter<String> adapter) {
        ((ListView) findViewById(R.id.manageFavouritesList)).setAdapter(adapter);
    }

}