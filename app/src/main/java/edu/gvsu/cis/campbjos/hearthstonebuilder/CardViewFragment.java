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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.gvsu.cis.campbjos.hearthstonebuilder.CustomAdapters.CardAdapter;
import edu.gvsu.cis.campbjos.hearthstonebuilder.Entity.Card;
import edu.gvsu.cis.campbjos.hearthstonebuilder.UI.DividerItemDecoration;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CardViewFragment extends Fragment implements LoadCardJsonTask.JsonTaskListener, AdapterView.OnItemSelectedListener {

  @InjectView(R.id.card_recyclerview)
  RecyclerView mCategoryRecycler;

  @InjectView(R.id.loading_spinner)
  View mLoadingView;

  @InjectView(R.id.empty_event_text)
  TextView mEmptyTextView;

  private static final String URL = "https://omgvamp-hearthstone-v1.p.mashape.com/cards?";
  private static final String COLLECT_PARAM = "collectible=1";
  private ArrayList<Card> cards;
  private ArrayList<Card> visibleCards;
  private CardAdapter adapter;
  private AsyncTask jsonTask;
  private View spinnerLayout;
  private View mCardFragmentView;
  
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
  private String textFilter;

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
    textFilter = "";
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
    mCardFragmentView = inflater.inflate(R.layout.fragment_card_view, container, false);

    ButterKnife.inject(this, mCardFragmentView);
    
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),
        LinearLayoutManager.VERTICAL, false);
    mCategoryRecycler.setHasFixedSize(true);
    mCategoryRecycler.isVerticalScrollBarEnabled();
    mCategoryRecycler.setLayoutManager(mLayoutManager);
    adapter = new CardAdapter(visibleCards);
    mCategoryRecycler.setAdapter(adapter);
    mCategoryRecycler.addOnItemTouchListener(
        new RecyclerItemClickListener(getActivity(),
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {

              }
            })
    );
    mCategoryRecycler.addItemDecoration
        (new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    return mCardFragmentView;
  }

  private void addSpinners() {
    spinners.clear();
    spinners.add(classSpinner);
    spinners.add(costSpinner);
    spinners.add(typeSpinner);
    spinners.add(rareSpinner);
    spinners.add(setSpinner);
  }

//  private void setSpinnerAdapter(Spinner currentSpinner, int arrayId) {
//    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//        getActivity(), arrayId, R.layout.spinner_item);
//    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//    currentSpinner.setAdapter(adapter);
//  }


  @Override
  public void onTaskComplete() {
    if (cards.isEmpty()) {
      mEmptyTextView.setVisibility(View.VISIBLE);
    }
    visibleCards.clear();
    visibleCards.addAll(CardFilter.filterCards(cards, classFilter, costFilter, typeFilter,
            rarityFilter, cardSetFilter, textFilter));
    adapter.notifyDataSetChanged();
    mLoadingView.setVisibility(View.GONE);
  }

  @Override
  public void onMessage(String s) {
    final CardViewFragment frag = this;
    Snackbar.make(mCardFragmentView, s, Snackbar.LENGTH_INDEFINITE).setAction("Retry",
        new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        jsonTask = new LoadCardJsonTask(frag).execute(URL + COLLECT_PARAM,
            getStringFromManifest("hearthstone_api_key"), cards);
        mEmptyTextView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
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
    if (visibleCards.isEmpty()) {
      mEmptyTextView.setVisibility(View.VISIBLE);
    } else {
      mEmptyTextView.setVisibility(View.INVISIBLE);
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    // Cancel
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
    return mLoadingView;
  }

  public void setCardList(List<Card> list) {
    visibleCards.clear();
    visibleCards.addAll(list);
    adapter.notifyDataSetChanged();
  }
}
