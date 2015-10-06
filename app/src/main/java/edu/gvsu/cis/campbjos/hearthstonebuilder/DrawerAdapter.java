/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Adapter for the planet data used in our drawer menu.
 * @author HeartBuilder Team
 * @version Fall 2015
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {
  private String[] dataset;
  private OnItemClickListener listener;

  public DrawerAdapter(String[] myDataset, OnItemClickListener myListener) {
    dataset = myDataset;
    listener = myListener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater vi = LayoutInflater.from(parent.getContext());

    View view = vi.inflate(R.layout.drawer_list_item, parent, false);
    TextView tv = (TextView) view.findViewById(android.R.id.text1);
    return new ViewHolder(tv);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    holder.textView.setText(dataset[position]);
    holder.textView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        listener.onClick(view, position);
      }
    });
  }

  @Override
  public int getItemCount() {
    return dataset.length;
  }

  /**
   * Interface for receiving click events from cells.
   */
  interface OnItemClickListener {
    void onClick(View view, int position);
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public final TextView textView;

    public ViewHolder(TextView myTextView) {
      super(myTextView);
      textView = myTextView;
    }
  }
}
