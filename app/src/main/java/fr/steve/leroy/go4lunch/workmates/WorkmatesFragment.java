package fr.steve.leroy.go4lunch.workmates;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.FragmentWorkmatesBinding;
import fr.steve.leroy.go4lunch.model.Workmate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkmatesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkmatesFragment extends Fragment {

    private FragmentWorkmatesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_workmates, container, false );
    }
}