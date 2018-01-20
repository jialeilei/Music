package com.lei.musicplayer.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;


public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected LayoutInflater mInflater;
	protected List<T> mData ;
	protected  int mItemLayout;

	public CommonAdapter(Context context, List<T> data, int mItemLayout) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mItemLayout = mItemLayout;
		this.mData = data;

	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public List<T> getData() {
		return mData;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = ViewHolder.getViewHolder(mContext,convertView,parent,mItemLayout,position);

		convert(viewHolder,mData.get(position),position);

		return viewHolder.getConvertView();
	}

	public abstract void convert(ViewHolder viewHolder,T item,int position);



}
