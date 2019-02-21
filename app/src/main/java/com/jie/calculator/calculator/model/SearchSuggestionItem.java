package com.jie.calculator.calculator.model;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jie.calculator.calculator.R;

/**
 * Created on 2019/2/21.
 *
 * @author Jie.Wu
 */
public class SearchSuggestionItem implements IModel {
    public static final int TYPE = 0x1235;

    private String result;
    private String search;


    public SearchSuggestionItem(String result, String search) {
        this.result = result;
        this.search = search;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        holder.addOnClickListener(R.id.rl_container);
        TextView tvSuggestion = holder.getView(R.id.tv_search_suggestion);
        if (TextUtils.isEmpty(result) || TextUtils.isEmpty(search)) {
            tvSuggestion.setText(null);
        } else {
            SpannableString ss = new SpannableString(result);
            if (result.contains(search)) {
                int start = result.indexOf(search);
                int ent = start + search.length();
                ss.setSpan(new ForegroundColorSpan(Color.BLACK), start, ent, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tvSuggestion.setText(ss);
        }
    }

    public String getResult() {
        return result;
    }

    @Override
    public int getItemType() {
        return TYPE;
    }
}
