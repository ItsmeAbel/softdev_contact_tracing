package com.example.guireglogin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

public class AddFriend extends AppCompatDialogFragment {
    private EditText editTextFriend;
    private EditText editTextNumber;
    private AddFriendListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_addfriend, null);

        builder.setView(view)
                .setTitle(getString(R.string.new_friend))
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = editTextFriend.getText().toString();
                        String number = editTextNumber.getText().toString();
                        listener.applyTexts(name, number);
                    }
                });
        editTextFriend = view.findViewById(R.id.friend_name);
        editTextNumber = view.findViewById(R.id.friend_number);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddFriendListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must Implemen AddFriendListener");
        }
    }

    public interface AddFriendListener{
        void applyTexts(String name, String number);
    }
}
