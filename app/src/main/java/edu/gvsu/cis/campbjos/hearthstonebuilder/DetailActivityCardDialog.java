package edu.gvsu.cis.campbjos.hearthstonebuilder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
public class DetailActivityCardDialog extends DialogFragment {

  private DialogListener hostActivity;
  private int checked;

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    hostActivity = (DialogListener) getActivity();
    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    String[] decks =
    // Set the dialog title
    builder.setTitle("Choose a deck to add...")
            // Specify the list array, the items to be selected by default (null for none),
            // and the listener through which to receive callbacks when items are selected
        .setSingleChoiceItems(getActivity().fileList(), checked,
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

  public String[] loadDecks(String[] fileList) {
    Double fileId = 0D;
    JsonObject currentDeckObject;
    JsonParser jsonParser = new JsonParser();
    int fileSize = fileList.length;
    for (String file : fileList) {
      try {
        fileId = Double.parseDouble(file);
      } catch (NumberFormatException numExcept) {
        // Hm. That was weird. . .
        continue;
      }
      currentDeckObject = readFileStreamToJson(file, jsonParser);
      if (currentDeckObject != null) {
        mView.setNavigationMenuItem(
            currentDeckObject.get("id").getAsInt(),
            currentDeckObject.get("name").getAsString());
      }
    }
  }

  public interface DialogListener {
    void onDialogComplete(int type, int id, String name);
  }
}