package alexa4.friendphoto.ui;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import alexa4.friendphoto.R;
import alexa4.friendphoto.models.Friend;
import alexa4.friendphoto.repositories.DataRepository;
import io.reactivex.disposables.Disposable;

public class FriendsFragment extends Fragment {
    private static final String TAG = "FriendsFragment";

    private DataRepository mRepository;
    private RecyclerView mRecyclerView;
    private Disposable disposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.friends_fragment, container, false);

        mRepository = ((RepositoryProvider) getActivity()).getRepository();
        mRecyclerView = root.findViewById(R.id.friendsList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false));

        disposable = mRepository.downloadFriends()
                .subscribe(friends ->
                                mRecyclerView.setAdapter(new FriendsAdapter(friends)),
                        this::showError);
        return root;
    }

    @Override
    public void onDestroy() {
        mRepository = null;
        disposable.dispose();
        super.onDestroy();
    }

    private void showError(Throwable e) {
        e.printStackTrace();

        Toast.makeText(getContext(),
                "Unable to load friends", Toast.LENGTH_SHORT).show();
    }


    private class FriendsAdapter extends
            RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {
        private ArrayList<Friend> mFriends;

        public class FriendsViewHolder extends RecyclerView.ViewHolder {
            public ImageView mImage;
            public TextView mInitials;
            public TextView mStatus;

            public FriendsViewHolder(@NonNull View view) {
                super(view);
                mImage = view.findViewById(R.id.friend_photo);
                mInitials = view.findViewById(R.id.friend_initials);
                mStatus = view.findViewById(R.id.friend_status);
            }
        }

        public FriendsAdapter(ArrayList<Friend> friends) {
            mFriends = friends;
        }

        @NonNull
        @Override
        public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.friends_item, parent, false);

            return new FriendsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
            Friend friend = mFriends.get(position);
            Picasso.get()
                    .load(friend.mPhotoUrl)
                    .into(holder.mImage);
            holder.mInitials.setText(friend.mName + " " + friend.mLastName);
            holder.mStatus.setText(friend.mStatus);

            // Set up listener to show photos fragment
            holder.mInitials.getRootView()
                    .setOnClickListener(v -> {
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.add(R.id.fragment_root,
                                new PhotosFragment(mFriends.get(position).mId));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    });
        }

        @Override
        public int getItemCount() {
            return mFriends.size();
        }
    }
}
