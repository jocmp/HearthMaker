package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

/**
 * @author josiah
 * @version 10/25/15
 */
public class RenameDeckDialog extends DialogFragment {

  private DialogListener hostActivity;
  private int checked;

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    hostActivity = (DialogListener) getActivity();
    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    // Set the dialog title
    builder.setTitle("Rename deck...")
        .setView(R.layout.dialog_rename_deck)
        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int id) {
            final EditText editText = (EditText) getDialog().findViewById(R.id.renameEditText);
            hostActivity.onRenameDeckResult(editText.getText().toString());
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
    void onRenameDeckResult(String name);
  }
}