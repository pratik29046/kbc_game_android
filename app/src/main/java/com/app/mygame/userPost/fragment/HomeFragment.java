package com.app.mygame.userPost.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import androidx.viewpager2.widget.ViewPager2;

import com.app.mygame.network.ApiService;
import com.app.mygame.network.RetrofitClient;
import com.app.mygame.userPost.adapter.ImageSliderAdapter;
import com.app.mygame.userPost.adapter.TournamentAdapter;
import com.app.mygame.R;
import com.app.mygame.databinding.FragmentHomeBinding;
import com.app.mygame.userPost.responseVo.AllTournamentsResponse;
import com.app.mygame.userPost.responseVo.BannerResponse;
import com.app.mygame.utils.DateUtility;

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
        callToTopThreeTournamentsUserRegister(apiService);
        return binding.getRoot();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @SuppressLint("QueryPermissionsNeeded")
    @Override
    public void onImageClick(BannerResponse.Data bannerData, int position) {
        String url = bannerData.reDirectUrl;
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Intent chooserIntent = Intent.createChooser(intent, "Open with");
                startActivity(chooserIntent);
            }
        } else {
            Toast.makeText(requireContext(), "Invalid URL", Toast.LENGTH_SHORT).show();
        }
    }

    private void callToBannerApp(ApiService apiService) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                // Execute the API call synchronously in the background
                Response<BannerResponse> response = apiService.banner().execute();
                // Post the result to the UI thread
                requireActivity().runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        var bannerResponse = response.body();
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

    private void setupImageSlider(ViewPager2 viewPager, List<BannerResponse.Data> banners) {
        // Pass `this` (HomeFragment) as the listener for image clicks
        ImageSliderAdapter adapter = new ImageSliderAdapter(requireContext(), banners, this);
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
        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                JSONObject jsonObject = new JSONObject(errorBody);
                String errorMessage = jsonObject.getString("message");
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Unknown error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error processing response", Toast.LENGTH_SHORT).show();
        }
    }
    private void callToTournaments(ApiService apiService) {
        Call<AllTournamentsResponse> loginCall = apiService.tournaments();
        loginCall.enqueue(new Callback<AllTournamentsResponse>() {
            @Override
            public void onResponse(Call<AllTournamentsResponse> call, Response<AllTournamentsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    var tournamentsResponse = response.body();
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

    private void callToTopThreeTournamentsUserRegister(ApiService apiService) {
        Call<AllTournamentsResponse> loginCall = apiService.topThreeUserTournamentsRegister();
        loginCall.enqueue(new Callback<AllTournamentsResponse>() {
            @Override
            public void onResponse(Call<AllTournamentsResponse> call, Response<AllTournamentsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    var tournamentsResponse = response.body();
                    if ("success".equalsIgnoreCase(tournamentsResponse.status)) {
                        if(tournamentsResponse.data.isEmpty()){
                            binding.featuredTournamentCard.setVisibility(View.GONE);
                        }else{
                            // Display the first tournament in the larger card
                            AllTournamentsResponse.Data firstTournament = tournamentsResponse.data.get(0);
                            binding.featuredTournamentCard.setVisibility(View.VISIBLE);
                            binding.gameTitle.setText(firstTournament.title);
                            String formattedDate = DateUtility.formatDate(firstTournament.tournamentStartDateTime);
                            binding.gameDate.setText(formattedDate); // format as needed
                            binding.gameTime.setText(formattedDate); // format as needed

                            // Display the second tournament in the first small card
                            if (tournamentsResponse.data.size() > 1) {
                                AllTournamentsResponse.Data secondTournament = tournamentsResponse.data.get(1);
                                binding.upcomingGame1.setVisibility(View.VISIBLE);
                                binding.nextGameTitle1.setText(secondTournament.title);
                                String formattedDate2 = DateUtility.formatDate(firstTournament.tournamentStartDateTime);
                                binding.nextGameDate1.setText(formattedDate2); // format as needed
                            }else{
                                binding.upcomingGame1.setVisibility(View.GONE);
                            }
                            // Display the third tournament in the second small card
                            if (tournamentsResponse.data.size() > 2) {
                                AllTournamentsResponse.Data thirdTournament = tournamentsResponse.data.get(2);
                                binding.upcomingGame2.setVisibility(View.VISIBLE);
                                binding.nextGameTitle2.setText(thirdTournament.title);
                                String formattedDate3 = DateUtility.formatDate(firstTournament.tournamentStartDateTime);
                                binding.nextGameDate2.setText(formattedDate3 ); // format as needed
                            }else {
                                binding.upcomingGame2.setVisibility(View.GONE);
                            }
                        }

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
}
