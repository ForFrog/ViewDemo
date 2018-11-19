package com.frog.viewdemo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.frog.viewdemo.w.CreditSesameView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ZhiMaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ZhiMaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZhiMaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int type;


    public ZhiMaFragment() {
        // Required empty public constructor
    }

    public static ZhiMaFragment newInstance(int type) {
        ZhiMaFragment fragment = new ZhiMaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_zhi_ma, container, false);

        CreditSesameView creditSesameView = (CreditSesameView) view.findViewById(R.id.sesame_view);
        creditSesameView.setSesameValues(900);

        return view;
    }



}
