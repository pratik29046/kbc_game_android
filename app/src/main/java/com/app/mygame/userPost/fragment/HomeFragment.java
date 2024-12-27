package com.app.mygame.userPost.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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

import com.app.mygame.network.ApiService;
import com.app.mygame.network.RetrofitClient;
import com.app.mygame.userPost.adapter.ImageSliderAdapter;
import com.app.mygame.userPost.adapter.TournamentAdapter;
import com.app.mygame.userPost.model.TournamentCard;
import com.app.mygame.R;
import com.app.mygame.databinding.FragmentHomeBinding;
import com.app.mygame.userPost.responseVo.AllTournamentsResponse;
import com.app.mygame.userPost.responseVo.BannerResponse;
import com.app.mygame.utils.TokenManager;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements ImageSliderAdapter.OnImageClickListener {
    private FragmentHomeBinding binding;
    private Handler sliderHandler;
    private static final long SLIDER_DELAY = 4000;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        callToBannerApp(apiService);
        callToTournaments(apiService);
        return binding.getRoot();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onImageClick(String url, int position) {
        // Handle image click
        Toast.makeText(requireContext(), "Image " + (position + 1) + " clicked", Toast.LENGTH_SHORT).show();
    }
    private void callToBannerApp(ApiService apiService) {
        // Create a single-threaded executor for asynchronous API calls
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Run the API call in a background thread
        executor.execute(() -> {
            try {
                // Execute the API call synchronously in the background
                Response<BannerResponse> response = apiService.banner().execute();

                // Post the result to the UI thread
                requireActivity().runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        BannerResponse bannerResponse = response.body();
                        if ("success".equalsIgnoreCase(bannerResponse.status)) {
                            List<BannerResponse.Data> smallFilteredBanners = new ArrayList<>();
                            List<BannerResponse.Data> largeFilteredBanners = new ArrayList<>();

                            for (BannerResponse.Data banner : bannerResponse.data) {
                                if ("SMALL".equals(banner.bannerType)) {
                                    smallFilteredBanners.add(banner);
                                } else {
                                    largeFilteredBanners.add(banner);
                                }
                            }

                            sliderHandler = new Handler(Looper.getMainLooper());
                            // Set up ImageSlider for small banners
                            setupImageSlider(binding.viewPager, smallFilteredBanners);
                            startAutoSlider(smallFilteredBanners.size());
                            // Set up ImageSlider for large banners
                            setupImageSlider(binding.viewPagerBottom, largeFilteredBanners);
                            startAutoSliderBottom(largeFilteredBanners.size());
                        } else {
                            Toast.makeText(requireContext(), bannerResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        handleErrorResponse(response);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show()
                );
            }
        });
        executor.shutdown();
    }

//    private void callToBannerApp(ApiService apiService) {
//        Call<BannerResponse> loginCall = apiService.banner();
//        loginCall.enqueue(new Callback<BannerResponse>() {
//            @Override
//            public void onResponse(Call<BannerResponse> call, Response<BannerResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    BannerResponse bannerResponse = response.body();
//                    if ("success".equalsIgnoreCase(bannerResponse.status)) {
//                        // Filter banners by bannerType (e.g., "SMALL" and "LARGE")
//                        List<BannerResponse.Data> smallFilteredBanners = new ArrayList<>();
//                        List<BannerResponse.Data> largeFilteredBanners = new ArrayList<>();
//                        for (BannerResponse.Data banner : bannerResponse.data) {
//                            if ("SMALL".equals(banner.bannerType)) {
//                                smallFilteredBanners.add(banner);
//                            } else {
//                                largeFilteredBanners.add(banner);
//                            }
//                        }
//                        sliderHandler = new Handler(Looper.getMainLooper());
//                        // Set up ImageSlider for small banners
//                        setupImageSlider(binding.viewPager, smallFilteredBanners);
//                        startAutoSlider(smallFilteredBanners.size());
//                        // Set up ImageSlider for large banners
//                        setupImageSlider(binding.viewPagerBottom, largeFilteredBanners);
//                        startAutoSliderBottom(largeFilteredBanners.size());
//                    } else {
//                        Toast.makeText(requireContext(), bannerResponse.message, Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    handleErrorResponse(response);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BannerResponse> call, Throwable t) {
//                Toast.makeText(requireContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    private void callToTournaments(ApiService apiService) {
        Call<AllTournamentsResponse> loginCall = apiService.tournaments();
        loginCall.enqueue(new Callback<AllTournamentsResponse>() {
            @Override
            public void onResponse(Call<AllTournamentsResponse> call, Response<AllTournamentsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AllTournamentsResponse tournamentsResponse = response.body();
                    if ("success".equalsIgnoreCase(tournamentsResponse.status)) {

                        TournamentAdapter tournamentAdapter = new TournamentAdapter(requireContext(), tournamentsResponse.data,
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

                    } else {
                        Toast.makeText(requireContext(), tournamentsResponse.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Unknown error";
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject errorJson = new JSONObject(errorBody);
                            Log.e("Error", "Error body: " + errorBody);
                            errorMessage = errorJson.optString("message", "Unknown error");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AllTournamentsResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupImageSlider(ViewPager2 viewPager, List<BannerResponse.Data> banners) {
        ImageSliderAdapter adapter = new ImageSliderAdapter(requireContext(), banners, (url, position) -> {
            // Handle image click if needed
        });
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
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

    private void handleErrorResponse(Response<BannerResponse> response) {
        String errorMessage = "Unknown error";
        if (response.errorBody() != null) {
            try {
                String errorBody = response.errorBody().string();
                JSONObject errorJson = new JSONObject(errorBody);
                errorMessage = errorJson.optString("message", "Unknown error");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}
