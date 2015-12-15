package com.teitsmch.hearthmaker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import java.util.UUID;

/**
 * @author josiah
 * @version 10/25/15
 */
public class NewDeckDialog extends DialogFragment {

  private DialogListener hostActivity;
  private int checked;

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    hostActivity = (DialogListener) getActivity();
    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    // Set the dialog title
    builder.setTitle("Choose a class...")
        .setView(R.layout.dialog_new_deck)
            // Specify the list array, the items to be selected by default (null for none),
            // and the listener through which to receive callbacks when items are selected
        .setSingleChoiceItems(R.array.card_class_dialog, checked,
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                checked = which;
              }
            })
        // Set the action buttons
        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int id) {
            final EditText editText = (EditText) getDialog().findViewById(R.id.nameEditText);
            String[] classes = getResources().getStringArray(R.array.card_class_dialog);
            hostActivity.onDialogComplete(checked,
                classes.hashCode() + UUID.randomUUID().hashCode(), editText.getText().toString());
            getDialog().dismiss();
          }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int id) {
            dismiss();
          }
        });

    return builder.create();
  }

  public interface DialogListener {
    void onDialogComplete(int type, int id, String name);
  }
}