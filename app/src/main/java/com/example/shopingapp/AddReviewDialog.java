package com.example.shopingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.shopingapp.groceryItemActivity.key;

public class AddReviewDialog extends DialogFragment {
    public interface AddReview{
        void AddaReview(ArrayList<Review> reviews);
    }
    private DataBase db;
    private TextView txtWarning,itemName;
    private Button btnSubmit;
    private EditText userName,userreviews;
    private AddReview addReview;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view=getActivity().getLayoutInflater().inflate(R.layout.dialog_add_review,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity()).setView(view);
        Bundle bundle=getArguments();
        initDialog(view);
        if(bundle !=null){
            final GroceryItem item=bundle.getParcelable(key);
            if(item!=null){
                itemName.setText(item.getName());
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username=userName.getText().toString();
                        String userReview=userreviews.getText().toString();
                        if(username.equals("") || userReview.equals("")){
                            txtWarning.setText("Fill all the detials");
                            txtWarning.setVisibility(View.VISIBLE);
                        }else{
                            txtWarning.setVisibility(View.GONE);
                            int id=item.getId();
                            String date= getCurrentDate();
                            ArrayList<Review> allReviews=db.groceryDao().getitemById(id).getReviews();
                            Review newReview=new Review(id,username,userReview,date);
                            allReviews.add(0,newReview);
                            db.groceryDao().updateReview(id,allReviews);
                            try{
                                addReview= (AddReview) getActivity();
                                addReview.AddaReview(allReviews);
                            }catch(ClassCastException e){
                                e.printStackTrace();
                            }
                            dismiss();
                        }
                    }
                });
            }
        }
        return builder.create();
    }

    private void initDialog(View view) {
        db= DataBase.getInstance(getActivity());
        txtWarning=view.findViewById(R.id.txtWarning);
        itemName=view.findViewById(R.id.itemNameDailog);
        btnSubmit=view.findViewById(R.id.btnSubmit);
        userName=view.findViewById(R.id.yourNameDailog);
        userreviews=view.findViewById(R.id.reviewDialog);

    }

    private String getCurrentDate() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd:MM:YYYY");
        return simpleDateFormat.format(calendar.getTime());
    }
}
