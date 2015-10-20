package edu.gvsu.cis.campbjos.hearthstonebuilder;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters.CardAdapter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.UI.DividerItemDecoration;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link CardViewFragment.OnFragmentInteractionListener} interface to handle interaction events.
 * Use the {@link CardViewFragment#newInstance} factory method to create an instance of this
 * fragment.
 */
public class CardViewFragment extends Fragment implements LoadCardJsonTask.JsonTaskListener, AdapterView.OnItemSelectedListener {
  private static final String URL = "https://omgvamp-hearthstone-v1.p.mashape.com/cards?";
  private static final String COLLECT_PARAM = "collectible=1";
  private ArrayList<Card> cards;
  private ArrayList<Card> visibleCards;
  private CardAdapter adapter;
  private AsyncTask jsonTask;
  private View cardFragmentView;
  private View loadingView;
  private View emptyText;
  private View spinnerLayout;

  private final int CLASS_SPINNER_ID = R.id.spinner_class;
  private final int COST_SPINNER_ID = R.id.spinner_cost;
  private final int TYPE_SPINNER_ID = R.id.spinner_type;
  private final int RARITY_SPINNER_ID = R.id.spinner_rarity;
  private final int SET_SPINNER_ID = R.id.spinner_set;

  private String classFilter;
  private String costFilter;
  private String typeFilter;
  private String rarityFilter;
  private String cardSetFilter;

  private static Spinner classSpinner;
  private static Spinner costSpinner;
  private static Spinner typeSpinner;
  private static Spinner rareSpinner;
  private static Spinner setSpinner;

  private static ArrayList<Spinner> spinners;
  private static int[] idArray;
  static {
    spinners = new ArrayList<>();

    idArray = new int[]{R.array.card_class,
        R.array.cost, R.array.card_type, R.array.rarity, R.array.card_set};
  }
  private OnFragmentInteractionListener mListener;

  // TODO: Rename and change types and number of parameters
  public static CardViewFragment newInstance() {
    return new CardViewFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    cards = new ArrayList<>();
    visibleCards = new ArrayList<>();

    classFilter = "CLEAR";
    costFilter = "CLEAR";
    typeFilter = "CLEAR";
    rarityFilter = "CLEAR";
    cardSetFilter = "CLEAR";
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    jsonTask = new LoadCardJsonTask(this);
    jsonTask.execute(URL + COLLECT_PARAM,
        getStringFromManifest("hearthstone_api_key"), cards);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    cardFragmentView = inflater.inflate(R.layout.fragment_card_view, container, false);
    RecyclerView categoryRecycler
        = (RecyclerView) cardFragmentView.findViewById(R.id.card_recyclerview);
    loadingView = cardFragmentView.findViewById(R.id.loading_spinner);
    emptyText = cardFragmentView.findViewById(R.id.empty_event_text);
    spinnerLayout = cardFragmentView.findViewById(R.id.spinner_layout);
    spinnerLayout.setVisibility(View.GONE);

    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),
        LinearLayoutManager.VERTICAL, false);
    categoryRecycler.setHasFixedSize(true);
    categoryRecycler.isVerticalScrollBarEnabled();
    categoryRecycler.setLayoutManager(mLayoutManager);
    adapter = new CardAdapter(visibleCards);
    categoryRecycler.setAdapter(adapter);
    categoryRecycler.addOnItemTouchListener(
            new RecyclerItemClickListener(getActivity(),
                    new RecyclerItemClickListener.OnItemClickListener() {
                      @Override
                      public void onItemClick(View view, int position) {

                      }
                    })
    );
    categoryRecycler.addItemDecoration
        (new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    setHasOptionsMenu(true);
    findSpinnerViews();
    addSpinners();

    for (int i = 0; i < 5; i++) {
      setSpinnerAdapter(spinners.get(i), idArray[i]);
      spinners.get(i).setOnItemSelectedListener(this);
    }

    return cardFragmentView;
  }

  private void addSpinners() {
    spinners.clear();
    spinners.add(classSpinner);
    spinners.add(costSpinner);
    spinners.add(typeSpinner);
    spinners.add(rareSpinner);
    spinners.add(setSpinner);
  }

  private void findSpinnerViews() {
    classSpinner = (Spinner) cardFragmentView.findViewById(R.id.spinner_class);
    costSpinner = (Spinner) cardFragmentView.findViewById(R.id.spinner_cost);
    typeSpinner = (Spinner) cardFragmentView.findViewById(R.id.spinner_type);
    rareSpinner = (Spinner) cardFragmentView.findViewById(R.id.spinner_rarity);
    setSpinner = (Spinner) cardFragmentView.findViewById(R.id.spinner_set);
  }

  private void setSpinnerAdapter(Spinner currentSpinner, int arrayId) {
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        getActivity(), arrayId, R.layout.spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    currentSpinner.setAdapter(adapter);
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.navigation_drawer, menu);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void onTaskComplete() {
    if (cards.isEmpty()) {
      emptyText.setVisibility(View.VISIBLE);
    }
    visibleCards.clear();
    visibleCards.addAll(CardFilter.filterCards(cards, "CLEAR", "CLEAR", "CLEAR", "CLEAR", "CLEAR"));
    adapter.notifyDataSetChanged();
    loadingView.setVisibility(View.GONE);
  }

  @Override
  public void onMessage(String s) {
    final CardViewFragment frag = this;
    Snackbar.make(cardFragmentView, s, Snackbar.LENGTH_INDEFINITE).setAction("Retry",
        new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        jsonTask = new LoadCardJsonTask(frag).execute(URL + COLLECT_PARAM,
            getStringFromManifest("hearthstone_api_key"), cards);
        emptyText.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
      }
    }).show();
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    String selection = (String) parent.getAdapter().getItem(position);

    switch (parent.getId()) {
      case CLASS_SPINNER_ID:
        if (selection.contains("CLEAR")) {
          classFilter = "CLEAR";
        } else {
          classFilter = selection;
        }
        break;
      case COST_SPINNER_ID:
        costFilter = selection;
        break;
      case TYPE_SPINNER_ID:
        typeFilter = selection;
        break;
      case RARITY_SPINNER_ID:
        rarityFilter = selection;
        break;
      case SET_SPINNER_ID:
        cardSetFilter = selection;
        break;
      default:
        break;
    }

    visibleCards.clear();
    visibleCards.addAll(CardFilter.filterCards(cards,
        classFilter, costFilter, typeFilter, rarityFilter, cardSetFilter));
    adapter.notifyDataSetChanged();

    if (visibleCards.isEmpty()) {
      emptyText.setVisibility(View.VISIBLE);
    } else {
      emptyText.setVisibility(View.INVISIBLE);
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    // Cancel
  }

  /**
   * This interface must be implemented by activities that contain this fragment to allow an
   * interaction in this fragment to be communicated to the activity and potentially other fragments
   * contained in that activity. <p> See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html" >Communicating with
   * Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }

  private String getStringFromManifest(String key) {
    String results = "";
    try {
      Context context = getActivity().getBaseContext();
      ApplicationInfo ai = context.getPackageManager()
          .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
      results = (String) ai.metaData.get(key);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return results;
  }

  public View getProgressView() {
    return loadingView;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case R.id.action_search:
        if (spinnerLayout.getVisibility() == View.VISIBLE) {
          spinnerLayout.setVisibility(View.GONE);
        } else {
          spinnerLayout.setVisibility(View.VISIBLE);
        }
        return true;
      default:
        spinnerLayout.setVisibility(View.VISIBLE);
        return true;
    }
  }
}
