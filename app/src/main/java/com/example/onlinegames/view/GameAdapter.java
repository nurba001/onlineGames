package com.example.onlinegames.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinegames.R;
import com.example.onlinegames.data.GameEntity;
import com.example.onlinegames.databinding.ItemGameCardBinding;

public class GameAdapter extends ListAdapter<GameEntity, GameAdapter.GameViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(GameEntity game);
    }

    private final OnItemClickListener listener;
    private final Context context;

    public GameAdapter(Context context, OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGameCardBinding binding = ItemGameCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new GameViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        GameEntity currentGame = getItem(position);
        holder.bind(currentGame, context, listener);
    }

    // --- ИСПРАВЛЕНИЕ ЗДЕСЬ ---
    // Было: private static class ...
    // Стало: public static class ... (Сделали класс ПУБЛИЧНЫМ)
    public static class GameViewHolder extends RecyclerView.ViewHolder {

        private final ItemGameCardBinding binding;

        public GameViewHolder(ItemGameCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(GameEntity game, Context context, OnItemClickListener listener) {
            binding.textViewTitle.setText(game.getTitle());
            binding.textViewPlatform.setText(game.getPlatform());

            String genreYear = game.getGenre() + ", " + game.getYear();
            binding.textViewGenreYear.setText(genreYear);

            // Загрузка картинки
            if (game.getImageUrl() != null && !game.getImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(game.getImageUrl())
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(binding.imageViewCover);
            } else {
                // Если ссылки нет, ставим заглушку
                binding.imageViewCover.setImageResource(R.drawable.ic_launcher_background);
            }

            itemView.setOnClickListener(v -> listener.onItemClick(game));
        }
    }

    private static final DiffUtil.ItemCallback<GameEntity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<GameEntity>() {
                @Override
                public boolean areItemsTheSame(@NonNull GameEntity oldItem, @NonNull GameEntity newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull GameEntity oldItem, @NonNull GameEntity newItem) {
                    return oldItem.getTitle().equals(newItem.getTitle()) &&
                            oldItem.getGenre().equals(newItem.getGenre()) &&
                            oldItem.getYear() == newItem.getYear();
                }
            };
}