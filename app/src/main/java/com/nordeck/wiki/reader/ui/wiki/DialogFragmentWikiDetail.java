package com.nordeck.wiki.reader.ui.wiki;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.model.WikiDetail;
import com.squareup.picasso.Picasso;

/**
 * Created by parker on 9/7/15.
 */
public class DialogFragmentWikiDetail extends DialogFragment implements DialogInterface.OnClickListener {
    public static final String TAG = "DialogFragmentWikiDetail";

    private static final String EXTRA_WIKI_DETAIL = "extra_wiki_detail";

    public interface OnWikiSelectedListener {
        void onWikiSelected(WikiDetail wiki);
    }

    private OnWikiSelectedListener mListener;

    private WikiDetail mDetail;

    public static DialogFragmentWikiDetail newInstance(@NonNull WikiDetail detail, @NonNull OnWikiSelectedListener
            listener) {
        DialogFragmentWikiDetail fragment = new DialogFragmentWikiDetail();
        fragment.setListener(listener);
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_WIKI_DETAIL, detail);
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(OnWikiSelectedListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mDetail = args.getParcelable(EXTRA_WIKI_DETAIL);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.wiki_detail_btn_pos, this)
                .setNegativeButton(R.string.wiki_detail_btn_neg, this)
                .setView(getView(mDetail)).create();
    }

    public View getView(WikiDetail detail) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_wiki_detail, null, false);
        ImageView iv = (ImageView) v.findViewById(R.id.wiki_iv_logo);
        TextView tvTitle = (TextView) v.findViewById(R.id.wiki_tv_title);
        TextView tvUrl = (TextView) v.findViewById(R.id.wiki_tv_url);
        TextView tvDesc = (TextView) v.findViewById(R.id.wiki_tv_desc);

        Picasso.with(getActivity().getApplicationContext()).load(detail.getImage()).into(iv);
        tvTitle.setText(detail.getTitle());
        tvUrl.setText(detail.getUrl());
        if (!TextUtils.isEmpty(detail.getDesc())) {
            tvDesc.setText(detail.getDesc());
        } else if (!TextUtils.isEmpty(detail.getHeadline())) {
            tvDesc.setText(detail.getHeadline());
        }
        return v;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss();
        } else if (which == DialogInterface.BUTTON_POSITIVE) {
            mListener.onWikiSelected(mDetail);
        }
    }
}
