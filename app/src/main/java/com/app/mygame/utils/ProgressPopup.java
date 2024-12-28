package com.app.mygame.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

import com.app.mygame.databinding.ProgressPopupBinding;

public class ProgressPopup {
    private final AlertDialog dialog;
    private final ProgressPopupBinding binding;

    public ProgressPopup(Context context) {
        // Inflate the custom layout using ViewBinding
        binding = ProgressPopupBinding.inflate(LayoutInflater.from(context));

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(binding.getRoot())
                .setCancelable(false);
        dialog = builder.create();

        // Set the dialog background to transparent
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    // Show the progress popup
    public void show(String message) {
        // Run on the main thread
        runOnUiThread(() -> {
            if (!dialog.isShowing()) {
                binding.progressMessage.setText(message); // Set the progress message
                dialog.show();
            }
        });
    }

    // Dismiss the progress popup
    public void dismiss() {
        // Run on the main thread
        runOnUiThread(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        });
    }

    // Helper method to ensure code runs on the main thread
    private void runOnUiThread(Runnable action) {
        if (android.os.Looper.getMainLooper().isCurrentThread()) {
            action.run();
        } else {
            new android.os.Handler(android.os.Looper.getMainLooper()).post(action);
        }
    }
}
