package com.kennedy.peter.bloquery.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import com.kennedy.peter.bloquery.R;

public class AskQuestionDialog extends DialogFragment {
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialogFragment, DialogInterface dialog);
        public void onDialogNegativeClick(DialogFragment dialogFragment, DialogInterface dialog);
    }

    NoticeDialogListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (NoticeDialogListener) activity;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_user_input_text, null))
                .setPositiveButton("Ask!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogPositiveClick(AskQuestionDialog.this, dialog);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogNegativeClick(AskQuestionDialog.this, dialog);
                    }
                });
        return builder.create();
    }
}
