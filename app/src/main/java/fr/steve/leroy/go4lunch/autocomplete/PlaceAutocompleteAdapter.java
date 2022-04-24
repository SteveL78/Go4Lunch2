package fr.steve.leroy.go4lunch.autocomplete;

import android.content.Context;
import android.gesture.Prediction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import fr.steve.leroy.go4lunch.R;

/**
 * Created by Steve LEROY on 04/12/2021.
 */
public class PlaceAutocompleteAdapter extends ArrayAdapter<Prediction> {

    private Context context;
    private List<Prediction> predictionList;

    public PlaceAutocompleteAdapter(Context context, List<Prediction> predictionList){
        super(context, R.layout.place_row_layout);
        this.context = context;
        this.predictionList = predictionList;
    }
/*
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.place_row_layout,parent,false);
        if (predictionList != null && predictionList.size() > 0) {
            Prediction prediction = predictionList.get(position);
            TextView textViewName = listItem.findViewById(R.id.textViewName);
            //textViewName.setText(prediction.getDescription());
        }
        return listItem;
    }
*/
}
