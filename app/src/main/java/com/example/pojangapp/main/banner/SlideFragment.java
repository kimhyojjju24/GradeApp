package com.example.pojangapp.main.banner;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.pojangapp.R;
import com.example.pojangapp.main.Store;
import com.example.pojangapp.storedetail.StoreDetailActivity;

public class SlideFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final String[] PAGE_TITLES =
            new String[] { "1000001","1000002","1000003"};

    @StringRes
    private static final int[] PAGE_IMAGE =
            new int[] {
                    R.drawable.bunsik, R.drawable.boong, R.drawable.ttoek
            };
    private SliderViewModel sliderViewModel;

    public static SlideFragment newInstance(int index) {
        SlideFragment fragment = new SlideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sliderViewModel = ViewModelProviders.of(this).get(SliderViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        sliderViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_view_pager, container, false);
        final ImageView imageView = root.findViewById(R.id.imageBanner);
        sliderViewModel.getText().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer index) {
                imageView.setImageResource(PAGE_IMAGE[index]);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Banner.setAccess(true);
                        Intent intent = new Intent(getContext(), StoreDetailActivity.class);
                        Store.setStoreNum(PAGE_TITLES[index]);
                        startActivity(intent);
                    }
                });
            }
        });
        return root;
    }
}