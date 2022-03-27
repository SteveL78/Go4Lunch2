package fr.steve.leroy.go4lunch.ui.workmates_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.FragmentWorkmateBinding;
import fr.steve.leroy.go4lunch.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkmateFragment} factory method to
 * create an instance of this fragment.
 */
public class WorkmateFragment extends Fragment {

    private FragmentWorkmateBinding binding;
    private List<User> workmateList;
    private WorkmateListAdapter adapter;
    private WorkmateViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_workmate, container, false );
        configureBinding( view );
        initViewModel();
        viewModel.getAllUsers();
        configureSwipeRefreshLayout();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        configureRecycleView();
    }

    private void configureBinding(View view) {
        binding = FragmentWorkmateBinding.bind( view );
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider( this ).get( WorkmateViewModel.class );
        viewModel.init();
        setUpWorkmateList();
    }

    private void setUpWorkmateList() {
        viewModel.getWorkmateList().observe( getViewLifecycleOwner(), this::initWorkmateList );
    }

    private void configureRecycleView() {
        workmateList = new ArrayList<>();
        adapter = new WorkmateListAdapter( workmateList );
        binding.fragmentWorkmatesRecyclerview.setLayoutManager( new LinearLayoutManager( getActivity() ) );
        binding.fragmentWorkmatesRecyclerview.setAdapter( adapter );
    }

    private void initWorkmateList(List<User> workmateList) {
        if (!workmateList.isEmpty()) {
            this.workmateList = workmateList;
        } else {
            List<User> noFriendsList = new ArrayList<>();
            User user = new User();
            user.setUsername( getString( R.string.no_friends ) );
            noFriendsList.add( user );
            this.workmateList = noFriendsList;
        }
        adapter.update( this.workmateList );
        binding.swipeRefreshLayout.setRefreshing( false );
    }

    private void configureSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setUpWorkmateList();
            }
        } );
    }
}