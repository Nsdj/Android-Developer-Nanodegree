package com.example.android.popularmovies.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.MovieSortType;

/**
 * Created by Neil on 3/26/2017.
 */

public class SortTypeChooserDialog extends DialogFragment
{
    private static final String ARG_EXISTING_TYPE = "existingType";

    private MovieSortType mSelectedSortType;
    private OnItemSelectedListener mOnItemSelectedListener;

    public interface OnItemSelectedListener
    {
        void sortTypSelected(MovieSortType newSortType);
    }

    public static SortTypeChooserDialog newInstance(MovieSortType existingChoice)
    {
        SortTypeChooserDialog dialog = new SortTypeChooserDialog();

        Bundle args = new Bundle();
        args.putInt(ARG_EXISTING_TYPE, existingChoice.ordinal());

        dialog.setArguments(args);

        return dialog;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener itemSelectListener)
    {
        mOnItemSelectedListener = itemSelectListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        mSelectedSortType = MovieSortType.values()[getArguments().getInt(ARG_EXISTING_TYPE)];

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.sort_type))
                .setSingleChoiceItems(R.array.sort_options, mSelectedSortType.ordinal(), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        mSelectedSortType = MovieSortType.values()[which];
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (mOnItemSelectedListener != null)
                        {
                            mOnItemSelectedListener.sortTypSelected(mSelectedSortType);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null);

        return builder.create();
    }
}
