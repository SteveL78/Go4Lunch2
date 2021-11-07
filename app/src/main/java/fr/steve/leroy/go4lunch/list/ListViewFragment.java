package fr.steve.leroy.go4lunch.list;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.maps.model.PlacesSearchResult;

import java.util.ArrayList;
import java.util.List;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.FragmentListViewBinding;
import fr.steve.leroy.go4lunch.databinding.FragmentWorkmateBinding;
import fr.steve.leroy.go4lunch.model.Workmate;
import fr.steve.leroy.go4lunch.workmates.WorkmateListAdapter;
import fr.steve.leroy.go4lunch.workmates.WorkmateViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListViewFragment extends Fragment {

    private FragmentListViewBinding binding;
    private List<PlacesSearchResult> mPlacesSearchResults;
    private RestaurantListAdapter adapter;
    private RestaurantListViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_workmate, container, false);
        configureBinding(view);
        initViewModel();
        viewModel.getAllRestaurants();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureRecycleView();
    }

    private void configureBinding(View view) {
        binding = FragmentWorkmateBinding.bind(view);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(WorkmateViewModel.class);

        setUpRestaurantList();
    }

    private void setUpRestaurantList() {
        viewModel.getRestaurants().observe(getViewLifecycleOwner(), this::initUserList);
    }

    private void configureRecycleView() {
        mUserList = new ArrayList<>();
        adapter = new WorkmateListAdapter(mUserList);
        binding.fragmentWorkmatesRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.fragmentWorkmatesRecyclerview.setAdapter(adapter);
    }

    private void initUserList(List<Workmate> workmateList) {
        if (!workmateList.isEmpty()) {
            this.mUserList = workmateList;
        } else {
            List<Workmate> noFriendsList= new ArrayList<>();
            Workmate user = new Workmate();
            // TODO ProfileUrl
            user.setProfileUrl("");
            user.setFirstName(getString(R.string.no_friends));
            noFriendsList.add(user);
            this.mUserList = noFriendsList;
        }
        adapter.update(this.mUserList);
    }
}