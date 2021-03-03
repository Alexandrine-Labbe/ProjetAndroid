package fr.alexandrine.uneapplinulle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;


import java.util.List;

import fr.alexandrine.uneapplinulle.R;
import fr.alexandrine.uneapplinulle.model.Joke;

public class AdapterForJokes extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<Joke> jokes;

    public AdapterForJokes(Context context, List<Joke> jokes) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.jokes = jokes;
    }

    @Override
    public int getCount() {
        return this.jokes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.jokes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.jokes.get(position).getId();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = this.layoutInflater.inflate(R.layout.joke_list_item, parent, false);

        TextView jokeText = (TextView) itemView.findViewById(R.id.joke_text);

        jokeText.setText(this.jokes.get(position).getJoke());

        return itemView;
    }
}
