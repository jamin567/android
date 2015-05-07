package edu.umsl.proj.kyu.volleyproject;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PageAdapter extends RecyclerView.Adapter<PageAdapter.ViewHolder> {
	private RecyclerView mRecyclerView;
	private List<Page> mPages;
	private int mLastPosition = -1;

    public PageAdapter(RecyclerView recyclerView, List<Page> pages) {
		mRecyclerView = recyclerView;
		mPages = pages;
    }

    @Override
    public PageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mRecyclerView.getContext()).inflate(R.layout.pageadapter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Page page = mPages.get(position);

        holder.mTitleTextView.setText(page.getTitle());

		if (page.getThumbnail() != null) {
			holder.mImageView.setImageResource(R.drawable.placeholder);
			Picasso.with(mRecyclerView.getContext()).load(page.getThumbnail().getSource()).placeholder(R.drawable.placeholder).into(holder.mImageView);
		} else {
			holder.mImageView.setImageResource(R.drawable.noimage);
		}

		setAnimation(holder.itemView, position);
    }

	public void refresh() {
		mRecyclerView.scrollToPosition(0);
		super.notifyDataSetChanged();
		mLastPosition = -1; //reset animation position
	}

    @Override
    public int getItemCount() {
        return mPages.size();
    }

	private void setAnimation(View viewToAnimate, int position) {
		// If the view wasn't previously displayed on screen, it's animated
		if (position > mLastPosition) {
			AnimatorSet animatorSet = new AnimatorSet();
			animatorSet.playTogether(ObjectAnimator.ofFloat(viewToAnimate, "translationY", mRecyclerView.getHeight() / 2, 0), ObjectAnimator.ofFloat(viewToAnimate, "alpha", 0, 1));
			animatorSet.setDuration(250);
			animatorSet.start();
			mLastPosition = position;
		}
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		@InjectView(R.id.page_title)
        TextView mTitleTextView;
		@InjectView(R.id.page_image)
        ImageView mImageView;

		public ViewHolder(View view) {
			super(view);
			ButterKnife.inject(this, view);
		}
	}
}