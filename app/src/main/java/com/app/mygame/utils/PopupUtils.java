package com.app.mygame.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.mygame.R;
import com.app.mygame.databinding.CustomPopupBinding; // Import ViewBinding for custom_popup layout
import com.app.mygame.databinding.SuccessPopupBinding;

public class PopupUtils {

    // Normal Popup: Simple confirmation with custom design
    public static void showNormalPopup(@NonNull Context context, String title, String message, DialogInterface.OnClickListener onConfirmListener) {
        // Inflate the custom layout using ViewBinding
        CustomPopupBinding binding = CustomPopupBinding.inflate(LayoutInflater.from(context));

        // Set title and message dynamically using ViewBinding
        binding.popupTitle.setText(title);
        binding.popupMessage.setText(message);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(binding.getRoot())
                .setCancelable(false);

        final AlertDialog dialog = builder.create(); // Create the dialog instance
        dialog.show(); // Show the dialog

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        binding.positiveButton.setOnClickListener(v -> {
            onConfirmListener.onClick(null, DialogInterface.BUTTON_POSITIVE);
            dialog.dismiss(); // Dismiss the dialog after positive action
        });

        binding.negativeButton.setOnClickListener(v -> {
            dialog.dismiss(); // Dismiss the dialog on negative action
        });
    }

    // Actionable Popup: For more customized actions with custom design
    public static void showActionablePopup(@NonNull Context context, String title, String message, String positiveText, String negativeText,
                                           DialogInterface.OnClickListener onPositiveAction, DialogInterface.OnClickListener onNegativeAction) {
        // Inflate the custom layout using ViewBinding
        CustomPopupBinding binding = CustomPopupBinding.inflate(LayoutInflater.from(context));

        // Set title and message dynamically using ViewBinding
        binding.popupTitle.setText(title);
        binding.popupMessage.setText(message);

        // Set text for buttons
        binding.positiveButton.setText(positiveText);
        binding.negativeButton.setText(negativeText);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(binding.getRoot())
                .setCancelable(false);

        final AlertDialog dialog = builder.create(); // Create the dialog instance
        dialog.show(); // Show the dialog

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // Set onClickListeners for buttons
        binding.positiveButton.setOnClickListener(v -> {
            onPositiveAction.onClick(null, DialogInterface.BUTTON_POSITIVE);
            dialog.dismiss(); // Dismiss the dialog after positive action
        });

        binding.negativeButton.setOnClickListener(v -> {
            onNegativeAction.onClick(null, DialogInterface.BUTTON_NEGATIVE);
            dialog.dismiss(); // Dismiss the dialog on negative action
        });
    }

    public static void showSuccessPopup(@NonNull Context context, String title, String message,String buttonText,
                                        DialogInterface.OnClickListener onSuccessListener) {
        // Inflate the custom layout using ViewBinding
        SuccessPopupBinding binding = SuccessPopupBinding.inflate(LayoutInflater.from(context));

        // Set title and message dynamically using ViewBinding
        binding.popupTitle.setText(title);
        binding.popupMessage.setText(message);
        binding.successButton.setText(buttonText);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(binding.getRoot())
                .setCancelable(false);  // Makes sure the dialog is not canceled by tapping outside

        final AlertDialog dialog = builder.create(); // Create the dialog instance
        dialog.show(); // Show the dialog

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // Set the action for the Success button
        binding.successButton.setOnClickListener(v -> {
            if (dialog.isShowing()) {
                dialog.dismiss(); // Dismiss the dialog after success button is clicked
            }
        });
    }
}
