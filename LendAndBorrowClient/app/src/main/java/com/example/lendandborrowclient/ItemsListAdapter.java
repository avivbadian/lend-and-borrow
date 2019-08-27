package com.example.lendandborrowclient;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lendandborrowclient.Models.Item;
import com.javaproject.nimrod.cinema.Objects.MovieDisplay;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nimrod on 13/06/2017.
 */

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.ItemsAdapterViewHolder>
{
    List<Item> _itemsList;
    ItemClickedListener m_listener;
    private List<Item> _displayedItems;

    public ItemsListAdapter(ItemClickedListener listener)
    {
        m_listener = listener;
    }

    @Override
    public ItemsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        return new ItemsAdapterViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.movie_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ItemsAdapterViewHolder holder, int position)
    {
        if (position < _displayedItems.size())
            holder.bind(_displayedItems.get(position));
        else
            Log.d("RecyclerView",
                    "onBindViewHolder: Trying to bind position: " +
                            position +
                            "when data size is: " +
                            _itemsList.size());
    }

    @Override
    public int getItemCount()
    {
        return _displayedItems == null ? 0 : _displayedItems.size();
    }

    public void SetData(List<Item> items)
    {
        _itemsList = items;
        _displayedItems = items;
        notifyDataSetChanged();
    }

    public void FilterDataByMovieName(String query)
    {
        _displayedItems = _itemsList.
                stream().
                filter(new Predicate<Item>() {
                    @Override
                    public boolean test(Item item) {
                        return item.Title.toLowerCase().contains(query.toLowerCase());
                    }
                }).
                collect(Collectors.toList());

        notifyDataSetChanged();
    }

    public void SetImageAt(Bitmap image, int position)
    {
        if (position < _moviesList.size())
            _moviesList.get(position).MoviePicture = image;

        notifyItemChanged(position);
    }

    public void ClearFilteredData()
    {
        _displayedMovies = _moviesList;

        notifyDataSetChanged();
    }

    class ItemsAdapterViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_movie_desc_preview)
        TextView _movieTitle;
        @BindView(R.id.iv_movie_image_preview)
        ImageView _moviePicture;
        @BindView(R.id.card_view) CardView _cardView;

        MoviesAdapterViewHolder(View view)
        {
            super(view);

            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.card_view, R.id.iv_movie_image_preview, R.id.tv_movie_desc_preview})
        public void OnChooseMovieClicked()
        {
            m_listener.OnMovieItemClicked(_displayedMovies.get(getAdapterPosition()));
        }

        public void bind(final MovieDisplay movie)
        {
            _movieTitle.setText(movie.MovieDetails.Name);

            if (movie.MoviePicture != null)
                _moviePicture.setImageBitmap(movie.MoviePicture);
        }

    }
}
