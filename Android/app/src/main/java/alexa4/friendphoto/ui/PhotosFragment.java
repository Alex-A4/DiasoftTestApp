package alexa4.friendphoto.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import alexa4.friendphoto.R;
import alexa4.friendphoto.models.Photo;
import alexa4.friendphoto.repositories.DataRepository;
import io.reactivex.disposables.Disposable;

public class PhotosFragment extends Fragment {
    private static final String TAG = "PhotosFragment";

    private DataRepository mRepository;
    private RecyclerView mRecyclerView;
    private Disposable mDisposable;
    private PhotosAdapter mAdapter;
    private int mWidth;

    private int mFriendId;

    public PhotosFragment(int friendId) {
        mFriendId = friendId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.photos_fragment, container, false);

        mRepository = ((RepositoryProvider) getActivity()).getRepository();

        mRecyclerView = root.findViewById(R.id.photos_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,
                RecyclerView.VERTICAL, false));
        mAdapter = new PhotosAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(30));

        mDisposable = mRepository.downloadPhotos(mFriendId)
                .subscribe(photos -> {
                            mWidth = mRecyclerView.getWidth();
                            mAdapter.updateData(photos);
                        },
                        this::showError
                );

        return root;
    }

    private void showError(Throwable e) {
        Log.e(TAG, e.getStackTrace().toString());

        Toast.makeText(getContext(),
                "Unable to load photos", Toast.LENGTH_SHORT).show();
        getFragmentManager().popBackStack();
    }

    @Override
    public void onDestroy() {
        mRepository = null;
        mDisposable.dispose();
        super.onDestroy();
    }

    private class PhotosAdapter extends
            RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder> {
        private ArrayList<Photo> mPhotos;

        public class PhotosViewHolder extends RecyclerView.ViewHolder {
            public ImageView mImage;
            public TextView mText;

            public PhotosViewHolder(@NonNull View view) {
                super(view);
                mImage = view.findViewById(R.id.photo_item);
                mImage.requestLayout();
                mImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImage.getLayoutParams().width = mWidth / 2;

                mText = view.findViewById(R.id.photo_text);
            }
        }

        public PhotosAdapter(ArrayList<Photo> photos) {
            mPhotos = photos;
        }

        public void updateData(ArrayList<Photo> photos) {
            mPhotos = photos;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public PhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                   int viewType) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.photo_item, parent, false);

            return new PhotosViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotosViewHolder holder, int position) {
            Photo photo = mPhotos.get(position);

            // Set up listener to observe photo in full screen
            holder.mImage.setOnClickListener(v -> {
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.add(R.id.fragment_root, new PhotosFragment());
//                transaction.addToBackStack(null);
//                transaction.commit();
            });
            holder.mText.setText(photo.mText);
            Picasso.get()
                    .load(photo.mSmallSize)
                    .into(holder.mImage);
        }

        @Override
        public int getItemCount() {
            return mPhotos.size();
        }
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = space;
            outRect.left = space;
            outRect.right = space;
        }
    }
}
