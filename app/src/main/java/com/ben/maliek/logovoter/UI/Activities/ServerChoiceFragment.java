package com.ben.maliek.logovoter.UI.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ben.maliek.logovoter.R;

/**
 * Created by Ben on 8/23/2014.
 */
public class ServerChoiceFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        ViewGroup dialog = (ViewGroup)inflater.inflate(R.layout.change_server_dialog_layout, null);
        final EditText et = (EditText)dialog.findViewById(R.id.server_location);
        et.setText(HomeActivity.SERVER_ROOT);

        builder.setMessage(R.string.change_server_location)
                .setView(dialog)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            HomeActivity.SERVER_ROOT = et.getText().toString();
                        } catch (Exception e) {
                            Log.d("FAILURE", "Crap, the URL didn't change!");
                        }

                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
