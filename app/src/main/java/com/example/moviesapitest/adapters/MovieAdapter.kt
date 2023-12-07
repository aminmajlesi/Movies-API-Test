package com.example.moviesapitest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapitest.databinding.ItemMoviesListBinding
import com.example.moviesapitest.models.Search
import com.example.moviesapitest.ui.MovieViewModel

class MovieAdapter() : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: ItemMoviesListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindItem(movie:Search) {
            binding.tvMovieTitle.text = movie.Title
            binding.tvMoviesYear.text = movie.Year
            binding.tvMoviesType.text = movie.Type
            binding.tvMoviesImdb.text = movie.imdbID

            itemView.apply {
                Glide.with(this).load(movie.Poster).into(binding.ivMoviePicture)
            }

        }

    }
    lateinit var movieViewModel: MovieViewModel


    private val differCallBack = object : DiffUtil.ItemCallback<Search>() {
        override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this , differCallBack)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(ItemMoviesListBinding.inflate(
            LayoutInflater
            .from(parent.context),parent,false))

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = differ.currentList[position]
        holder.bindItem(movie)

        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let { it(movie) }
            }
        }

    }

    private var onItemClickListener: ((Search) -> Unit)? = null

    fun setOnItemClickListener(listener: (Search) -> Unit) {
        onItemClickListener = listener
    }

}