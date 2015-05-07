package edu.umsl.proj.kyu.volleyproject;

/**
 * Created by Kyu on 4/28/2015.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

//I used the following link as reference for "Recylerview"
//github.com/chiuki/android-recyclerview//
public class AutofitRecyclerView extends RecyclerView {
	private GridLayoutManager manager;
	private int columnWidth = -1;

	public AutofitRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
        Log.i("banana", "AutofitRecyclerView.AutofitRecyclerView");

        if (attrs != null) {
            int[] attrsArray = {android.R.attr.columnWidth};
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            columnWidth = array.getDimensionPixelSize(0, -1);
            array.recycle();
        }

        manager = new GridLayoutManager(getContext(), 1);
        setLayoutManager(manager);
	}

	@Override
	protected void onMeasure(int widthSpec, int heightSpec) {
		super.onMeasure(widthSpec, heightSpec);
        Log.i("banana", "AutofitRecyclerView.onMeasure");
		if (columnWidth > 0) {
			int spanCount = Math.max(1, getMeasuredWidth() / columnWidth);
			manager.setSpanCount(spanCount);
		}
	}
}