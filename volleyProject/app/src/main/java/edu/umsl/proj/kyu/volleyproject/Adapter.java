package edu.umsl.proj.kyu.volleyproject;

/**
 * Created by Kyu on 4/28/2015.
 */
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import edu.umsl.proj.kyu.volleyproject.SubClasses.Page;
//Third party animation sdk
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.squareup.picasso.Picasso;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
	private RecyclerView mRecyclerView;
	private List<Page> mPages;
	private int mLastPosition = -1;

    public Adapter(RecyclerView recyclerView, List<Page> pages) {
        Log.i("banana", "Adapter.PageAdapter");
        mRecyclerView = recyclerView;
        mPages = pages;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("banana", "Adapter.onCreateViewHolder");
        View view = LayoutInflater.from(mRecyclerView.getContext()).inflate(R.layout.pageadapter_item, parent, false);
        return new ViewHolder(view);
    }

    // insert thumbnail and title into every single imageview
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i("banana", "Adapter.onBindViewHolder");
        Page page = mPages.get(position);
        holder.mTitleTextView.setText(page.getTitle());

		if (page.getThumbnail() != null) {
			holder.mImageView.setImageResource(R.drawable.holder);
			Picasso.with(mRecyclerView.getContext()).load(page.getThumbnail().getSource()).placeholder(R.drawable.holder).into(holder.mImageView);
		} else {
			holder.mImageView.setImageResource(R.drawable.invalid_image);
		}

		setAnimation(holder.itemView, position);
    }

	public void refresh() {
        Log.i("banana", "Adapter.refresh");
		mRecyclerView.scrollToPosition(0);
        super.notifyDataSetChanged();
        mLastPosition = -1; //reset animation position
    }

    @Override
    public int getItemCount() {
        Log.i("banana", "Adapter.getItemCount");
        return mPages.size();
    }

	private void setAnimation(View viewToAnimate, int p) {
        Log.i("banana", "Adapter.setAnimation");
		// If the view wasn't previously displayed on screen, it's animated
		if (p > mLastPosition) {
			AnimatorSet animatorSet = new AnimatorSet();
			animatorSet.playTogether(ObjectAnimator.ofFloat(viewToAnimate, "translationY", mRecyclerView.getHeight() / 2, 0), ObjectAnimator.ofFloat(viewToAnimate, "alpha", 0, 1));
			animatorSet.setDuration(250);
			animatorSet.start();
			mLastPosition = p;
		}
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		@InjectView(R.id.page_title)
        TextView mTitleTextView;
        @InjectView(R.id.page_image)
        ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            Log.i("banana", "ViewHolder.ViewHolder");
            ButterKnife.inject(this, view);
		}
	}
}

