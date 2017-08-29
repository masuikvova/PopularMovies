package ru.gdgkazan.popularmovies.screen.details;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.gdgkazan.popularmovies.R;
import ru.gdgkazan.popularmovies.model.content.Review;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {

    private List<Review> data = new ArrayList<>();

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review review = data.get(position);
        holder.tContent.setText(review.getContent().trim());
        holder.tvAuthor.setText(review.getAuthor());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Review> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class ReviewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvAuthor)
        TextView tvAuthor;
        @BindView(R.id.tvContent)
        TextView tContent;

        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
