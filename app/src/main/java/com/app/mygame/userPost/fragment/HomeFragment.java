package com.app.mygame.userPost.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.app.mygame.userPost.adapter.ImageSliderAdapter;
import com.app.mygame.userPost.adapter.TournamentAdapter;
import com.app.mygame.userPost.model.TournamentCard;
import com.app.mygame.R;
import com.app.mygame.databinding.FragmentHomeBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ImageSliderAdapter.OnImageClickListener {
    private FragmentHomeBinding binding;
    private Handler sliderHandler;
    private static final long SLIDER_DELAY = 4000;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Set up ViewPager
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://www.ratri.app/media/posters/2024-05-24530bc06d6b114d74b9e0d51c79ffd9b0.jpg");
        imageUrls.add("https://www.ratri.app/media/posters/2024-04-17d35989c8ec034eec9c75862bd6adbdb4.jpeg");
        imageUrls.add("https://www.ratri.app/media/posters/2024-06-0373e7f8e199c54633b928703d84853b21.jpg");

        ImageSliderAdapter adapter = new ImageSliderAdapter(requireContext(), imageUrls, this);
        binding.viewPager.setAdapter(adapter);

        // Set page transformer for smooth transitions
        binding.viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            private static final float MIN_SCALE = 0.85f;
            private static final float MIN_ALPHA = 0.5f;

            @Override
            public void transformPage(@NonNull View page, float position) {
                int pageWidth = page.getWidth();
                int pageHeight = page.getHeight();

                if (position < -1) {
                    page.setAlpha(0f);
                } else if (position <= 1) {
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;

                    if (position < 0) {
                        page.setTranslationX(horzMargin - vertMargin / 2);
                    } else {
                        page.setTranslationX(-horzMargin + vertMargin / 2);
                    }

                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
                } else {
                    page.setAlpha(0f);
                }
            }
        });

        // Set up auto-sliding
        sliderHandler = new Handler(Looper.getMainLooper());
        startAutoSlider(imageUrls.size());

        List<String> bottomImageUrls = new ArrayList<>();
        bottomImageUrls.add("https://www.ratri.app/media/posters/2023-09-019649b651a16140bf9f5af51f927d9992.jpg");
        bottomImageUrls.add("https://www.ratri.app/media/posters/2023-09-01d696d519d46945cea80507d62cd80ce1.jpg");


        ImageSliderAdapter bottomAdapter = new ImageSliderAdapter(requireContext(), bottomImageUrls, this);
        binding.viewPagerBottom.setAdapter(bottomAdapter);
        binding.viewPagerBottom.setPageTransformer(new ViewPager2.PageTransformer() {
            private static final float MIN_SCALE = 0.85f;
            private static final float MIN_ALPHA = 0.5f;

            @Override
            public void transformPage(@NonNull View page, float position) {
                int pageWidth = page.getWidth();
                int pageHeight = page.getHeight();

                if (position < -1) {
                    page.setAlpha(0f);
                } else if (position <= 1) {
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;

                    if (position < 0) {
                        page.setTranslationX(horzMargin - vertMargin / 2);
                    } else {
                        page.setTranslationX(-horzMargin + vertMargin / 2);
                    }

                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
                } else {
                    page.setAlpha(0f);
                }
            }
        });

        // Set up auto-sliding for the bottom ViewPager
        startAutoSliderBottom(bottomImageUrls.size());

        // Setup Tournament RecyclerView
        List<TournamentCard> tournaments = new ArrayList<>();
        tournaments.add(new TournamentCard(
                "PUBG Mobile Tournament",
                "Join our weekly tournament and win exciting prizes!",
                "https://i.pinimg.com/564x/dc/08/ef/dc08efac84fea420504da91097df375f.jpg",
                "24 Dec 2024",
                "Prize: ₹10,000"
        ));
        tournaments.add(new TournamentCard(
                "Free Fire Championship",
                "Squad battle for ultimate glory",
                "https://i.pinimg.com/564x/dc/08/ef/dc08efac84fea420504da91097df375f.jpg",
                "26 Dec 2024",
                "Prize: ₹15,000"
        ));
        tournaments.add(new TournamentCard(
                "PUBG Mobile Tournament",
                "Join our weekly tournament and win exciting prizes!",
                "https://i.pinimg.com/564x/dc/08/ef/dc08efac84fea420504da91097df375f.jpg",
                "24 Dec 2024",
                "Prize: ₹10,000"
        ));
        tournaments.add(new TournamentCard(
                "Free Fire Championship",
                "Squad battle for ultimate glory",
                "https://i.pinimg.com/564x/dc/08/ef/dc08efac84fea420504da91097df375f.jpg",
                "26 Dec 2024",
                "Prize: ₹15,000"
        ));
        tournaments.add(new TournamentCard(
                "PUBG Mobile Tournament",
                "Join our weekly tournament and win exciting prizes!",
                "https://i.pinimg.com/564x/dc/08/ef/dc08efac84fea420504da91097df375f.jpg",
                "24 Dec 2024",
                "Prize: ₹10,000"
        ));
        tournaments.add(new TournamentCard(
                "Free Fire Championship",
                "Squad battle for ultimate glory",
                "https://i.pinimg.com/564x/dc/08/ef/dc08efac84fea420504da91097df375f.jpg",
                "26 Dec 2024",
                "Prize: ₹15,000"
        ));


        TournamentAdapter tournamentAdapter = new TournamentAdapter(requireContext(), tournaments,
                (tournament, position) -> {
                    // Navigate to TournamentDetailsFragment on click without using NavHostFragment
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("tournament", (Serializable) tournament);
                    TournamentDetailsFragment tournamentDetailsFragment = new TournamentDetailsFragment();
                    tournamentDetailsFragment.setArguments(bundle);

                    // Perform the fragment transaction to navigate to TournamentDetailsFragment
                    FragmentTransaction transaction = requireFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainContent, tournamentDetailsFragment); // Replace the container with the new fragment
                    transaction.addToBackStack(null); // Optional: Add to back stack so the user can navigate back
                    transaction.commit();
                });

        binding.tournamentRecyclerView.setAdapter(tournamentAdapter);
        binding.tournamentRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return binding.getRoot();
    }

    private void startAutoSlider(final int count) {
        final Runnable sliderRunnable = new Runnable() {
            @Override
            public void run() {
                if (binding.viewPager.getCurrentItem() < count - 1) {
                    binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
                } else {
                    binding.viewPager.setCurrentItem(0);
                }
                sliderHandler.postDelayed(this, SLIDER_DELAY);
            }
        };
        sliderHandler.postDelayed(sliderRunnable, SLIDER_DELAY);
    }

    private void startAutoSliderBottom(final int count) {
        final Runnable sliderRunnable = new Runnable() {
            @Override
            public void run() {
                if (binding.viewPagerBottom.getCurrentItem() < count - 1) {
                    binding.viewPagerBottom.setCurrentItem(binding.viewPagerBottom.getCurrentItem() + 1);
                } else {
                    binding.viewPagerBottom.setCurrentItem(0);
                }
                sliderHandler.postDelayed(this, SLIDER_DELAY);
            }
        };
        sliderHandler.postDelayed(sliderRunnable, SLIDER_DELAY);
    }


    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onImageClick(String url, int position) {
        // Handle image click
        Toast.makeText(requireContext(), "Image " + (position + 1) + " clicked", Toast.LENGTH_SHORT).show();
    }
}
