package fr.steve.leroy.go4lunch.workmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

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
    private List<User> mUserList;
    private WorkmateListAdapter adapter;
    private WorkmateViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_workmate, container, false);
        configureBinding(view);
        initViewModel();
        viewModel.getAllUsers();
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
        viewModel.init();
        setUpUserList();
    }

    private void setUpUserList() {
        viewModel.getUsers().observe(getViewLifecycleOwner(), this::initUserList);
    }

    private void configureRecycleView() {
        mUserList = new ArrayList<>();
        adapter = new WorkmateListAdapter(mUserList);
        binding.fragmentWorkmatesListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.fragmentWorkmatesListRv.setAdapter(adapter);
    }

    private void initUserList(List<User> userList) {
        if (!userList.isEmpty()) {
            this.mUserList = userList;
        } else {
            List<User> noFriendsList= new ArrayList<>();
            User user = new User();
            // TODO ProfileUrl
            user.setProfileUrl("");
            user.setUserName(getString(R.string.no_friends));
            noFriendsList.add(user);
            this.mUserList = noFriendsList;
        }
        adapter.update(this.mUserList);
    }
}