package com.example.shopingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class all_Categories_Dialog extends DialogFragment {
    public interface GetCategory{
        void onGetCategory(String category);
    }
    private GetCategory getCategory;
    private DataBase db;
    private ArrayList<String> allCategory ;
    public static final String activity="activity";
    public static final String category="categories";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        db=DataBase.getInstance(getActivity());
        allCategory= (ArrayList<String>) db.groceryDao().getAllCategories();
        View view=getActivity().getLayoutInflater().inflate(R.layout.all_category_dialog,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity()).setView(view);
        Bundle bundle=getArguments();

        if(bundle!=null){
            final String callingActivity=bundle.getString(activity);
            if(callingActivity!=null){
                ListView listView=view.findViewById(R.id.listView);
                
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,allCategory);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (callingActivity) {
                            case "main_activity":
                                Intent intent = new Intent(getActivity(), SearchActivity.class);
                                intent.putExtra(category, allCategory.get(position));
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(intent);
                                dismiss();
                                break;
                            case "search_activity":
                                try {
                                    getCategory = (GetCategory) getActivity();
                                    getCategory.onGetCategory(allCategory.get(position));
                                    dismiss();
                                } catch (ClassCastException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                break;
                        }

                    }
                });

            }
        }
        return builder.create();
    }
}
