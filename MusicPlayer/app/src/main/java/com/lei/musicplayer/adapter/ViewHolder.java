package com.lei.musicplayer.adapter;


import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lei on 2016/9/6.
 */

public class ViewHolder {


    private SparseArray<View> mViews;
    private View mConvertView;

    private ViewHolder(Context context, ViewGroup parent, int viewId){

        mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(viewId,parent,false);
        mConvertView.setTag(this);

    }

    /**
     * get viewHolder
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder getViewHolder(Context context,View convertView, ViewGroup parent,int layoutId,int position){

        if (convertView == null){
            return new ViewHolder(context,parent,layoutId);
        }

        return (ViewHolder) convertView.getTag();

    }



    public <T extends View>T getView(int viewId){

        View view = mViews.get(viewId);

        if (view == null){
            view = getConvertView().findViewById(viewId);
            mViews.put(viewId,view);
        }

        return (T) view;
    }


    public View getConvertView(){
        return mConvertView;
    }

    /**
     * setText of textView
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId,String text){

        TextView view = getView(viewId);
        view.setText(text);

        return this;
    }


    /**
     *
     * @param viewId
     * @param resId
     * @return
     */
    public ViewHolder setImageResource(int viewId,int resId){

        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);

        return this;
    }



}
