package com.ecebuc.gesmediaplayer.Fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecebuc.gesmediaplayer.AudioUtils.AlbumAdapter;
import com.ecebuc.gesmediaplayer.Audios.Album;
import com.ecebuc.gesmediaplayer.R;

import java.util.ArrayList;

public class AlbumsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private RecyclerView albumRecyclerView;
    private RecyclerView.Adapter albumRecyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private Context context;

    public AlbumsFragment() {
        // Required empty public constructor
    }
    public static AlbumsFragment newInstance(String param1, String param2) {
        AlbumsFragment fragment = new AlbumsFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_albums, container, false);

        //populating the list with the albums on device
        ArrayList<Album> albumList = loadAlbum();

        //Recycler view setup for songs display
        albumRecyclerView = (RecyclerView) rootView.findViewById(R.id.albums_recycler_view);
        albumRecyclerView.setHasFixedSize(true);

        recyclerLayoutManager = new GridLayoutManager(getActivity(), 2);
        albumRecyclerView.setLayoutManager(recyclerLayoutManager);

        albumRecyclerAdapter = new AlbumAdapter(albumList);
        albumRecyclerView.setAdapter(albumRecyclerAdapter);
        albumRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private ArrayList<Album> loadAlbum() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        String albumId, albumTitle, albumArtist, albumArt;
        ArrayList<Album> albumList = new ArrayList<>();
        final String[] ALBUM_SUMMARY_PROJECTION = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM_ART };

        Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Audio.Albums.ALBUM + " ASC";
        Cursor cursor = contentResolver.query(
                albumUri,
                ALBUM_SUMMARY_PROJECTION,
                null, null,
                sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                albumTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                albumArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

                //in case there is no album art
                /*if(albumArt == null){
                    albumArt = BitmapFactory.(getResources(), R.drawable.ic_album_black_24dp);
                }*/
                // Save to albumList
                albumList.add(new Album(albumId, albumTitle, albumArtist, albumArt));
            }
        }
        cursor.close();

        if(albumList.isEmpty()) {
            return null;
        } else {
          return albumList;
        }
    }

}