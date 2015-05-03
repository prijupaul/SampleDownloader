package au.com.pactera.sampledownloader.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Iterator;
import java.util.List;

import au.com.pactera.sampledownloader.R;
import au.com.pactera.sampledownloader.dto.Rows;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by priju.jacobpaul on 3/05/15.
 */
public class DownloadItemAdapter extends BaseAdapter {

    Context mContext;
    List<Rows> mRowItems;

    public DownloadItemAdapter(Context context, List<Rows> rowItems){
        mContext = context;
        mRowItems = rowItems;
    }

    public void setRows(List<Rows> rowItems){
        mRowItems = rowItems;
    }

    /**
     * Remove the unwanted items
     */
    public void parseAndRemoveItems(){

        Iterator<Rows> rowsIterator = mRowItems.listIterator();
        while (rowsIterator.hasNext()){
            Rows rows =rowsIterator.next();
            if(rows.getImageHref() == null && rows.getDescription() == null){
                rowsIterator.remove();
            }
        }
    }

    @Override
    public int getCount() {
        return mRowItems.size();
    }

    @Override
    public long getItemId(int position) {
        return mRowItems.indexOf(getItem(position));
    }

    @Override
    public Object getItem(int position) {
        return mRowItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View vi = null;

        if(vi == null){
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder(convertView );
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        vi = convertView;

        String title = mRowItems.get(position).getTitle();
        String imageUrl = mRowItems.get(position).getImageHref();
        String description = mRowItems.get(position).getDescription();
        viewHolder.mTitle.setText(title);
        viewHolder.mDescription.setText(description);
        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.abc_item_background_holo_dark)
                .transform(new TransformImage(viewHolder.mImageView))
                .into(viewHolder.mImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        // Failed to download image
                    }
                });

        return vi;
    }

    public static class ViewHolder{
        @InjectView(R.id.title)
        TextView mTitle;
        @InjectView(R.id.icon)
        ImageView mImageView;
        @InjectView(R.id.description)
        TextView mDescription;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public class TransformImage implements Transformation {

        private ImageView mImageView;
        public TransformImage(ImageView imageView){
            mImageView = imageView;
        }

        /**
         * Transform the image to the required size
         * @param source source bitmap
         * @return scaled bitmap
         */
        @Override public Bitmap transform(Bitmap source) {

            int width = source.getWidth();
            int height = source.getHeight();

            float currentWidth = mImageView.getWidth() == 0 ? mContext.getResources().getInteger(R.integer.default_image_width) : mImageView.getWidth();
            float currentHeight = mImageView.getHeight() == 0 ? mContext.getResources().getInteger(R.integer.default_image_height) :mImageView.getHeight();

            float scaleWidth = (currentWidth) / width;
            float scaleHeight = (currentHeight) / height;

            Matrix matrix = new Matrix();
            // resize the new bitmap.
            matrix.postScale(scaleWidth, scaleHeight);

            Bitmap resizedBitmap = Bitmap.createBitmap(source, 0, 0, width, height, matrix, false);
            if (resizedBitmap != source) {
                source.recycle();
            }
            return resizedBitmap;
        }

        @Override public String key() { return mContext.getString(R.string.app_name); }
    }
}
