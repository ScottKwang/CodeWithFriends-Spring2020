package com.example.coursehelper.View;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coursehelper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public static final String TAG_FRAG = "com.exmaple.coursehelper.View.LoginFragment";
    private View view;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                fragmentManager.popBackStackImmediate();// pop itself

                int top = fragmentManager.getBackStackEntryCount()-1;
                if(top >= 0){
                    FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(top);
                    Fragment currentFragment = fragmentManager.findFragmentByTag(backStackEntry.getName());
                    currentFragment.getView().setVisibility(View.VISIBLE);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        return view;
    }
}
