package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link DeckFragment.DeckFragmentListener} interface to handle interaction events. Use
 * the {@link DeckFragment#newInstance} factory method to create an instance of this fragment.
 */
public class DeckFragment extends Fragment {

  @InjectView(R.id.textview_class)
  TextView mClassTextView;

  private View mDeckFragmentView;

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_TYPE = "param1";

  private int mType;

  private DeckFragmentListener mListener;

  /**
   * Use this factory method to create a new instance of this fragment using the provided
   * parameters.
   *
   * @param type Parameter 1.
   * @return A new instance of fragment DeckFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static DeckFragment newInstance(int type) {
    DeckFragment fragment = new DeckFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_TYPE, type);
    fragment.setArguments(args);
    return fragment;
  }

  public DeckFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mType = getArguments().getInt(ARG_TYPE);
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    mDeckFragmentView = inflater.inflate(R.layout.fragment_deck, container, false);
    ButterKnife.inject(this, mDeckFragmentView);
    String[] classes = getResources().getStringArray(R.array.card_class_dialog);
    mClassTextView.setText("This deck can be filled with " + classes[mType]);

    return mDeckFragmentView;
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onAttach(Context activity) {
    super.onAttach(activity);
    mListener = (DeckFragmentListener) activity;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  /**
   * This interface must be implemented by activities that contain this fragment to allow an
   * interaction in this fragment to be communicated to the activity and potentially other fragments
   * contained in that activity. <p> See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html" >Communicating with
   * Other Fragments</a> for more information.
   */
  public interface DeckFragmentListener {
    // TODO: Update argument type and name
    public void onFragmentInteraction(Uri uri);
  }

}
