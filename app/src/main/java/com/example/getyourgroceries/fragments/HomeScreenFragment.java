/* HomeScreenFragment class. */
package com.example.getyourgroceries;

// Import statements.
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Create an object to represent the home screen.
 */
public class HomeScreenFragment extends Fragment {

    /**
     * Empty constructor.
     */
    public HomeScreenFragment() {}

    /**
     * Create the view.
     * @param inflater Inflater to set an XML file.
     * @param container Containing view.
     * @param savedInstanceState The saved state.
     * @return The created view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Get Your Groceries");
        // Inflate the layout for this fragment.
        //return inflater.inflate(R.layout.fragment_home_screen, container, false);
        View v = inflater.inflate(R.layout.fragment_home_screen, container, false);


        TextView description = v.findViewById(R.id.home_description);
        ImageView homelogo = v.findViewById(R.id.home_logoimage);
        Button quickaddbutton = v.findViewById(R.id.quickaddbutton);

        description.setText("Plan your meals with ease and efficiency!");
        return v;



    }

}