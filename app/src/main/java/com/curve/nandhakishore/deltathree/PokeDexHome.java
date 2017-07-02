package com.curve.nandhakishore.deltathree;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class PokeDexHome extends AppCompatActivity {

    EditText searchText;
    ProgressBar pBar;
    Pokemon required;
    customTextView name, types, abilities, speed, xp, hp, attack, defense, height, weight;
    ImageView sprite;
    String pokeName;
    CardView card;
    RelativeLayout rl, error;
    Button searchButton;
    boolean found = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poke_dex_home);

        searchText = (EditText) findViewById(R.id.pokeName);
        searchButton = (Button) findViewById(R.id.search_button);
        initCard();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(searchText.getText()))
                    new findPokemon().execute();
                else
                    Toast.makeText(getApplicationContext(), "The text field is blank", Toast.LENGTH_SHORT).show();
            }
        });

        searchText.setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH){
                    if (!TextUtils.isEmpty(searchText.getText()))
                        new findPokemon().execute();
                    else
                        Toast.makeText(getApplicationContext(), "The text field is blank", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }

    private class findPokemon extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            error.setVisibility(View.GONE);
            card.setVisibility(View.VISIBLE);
            found = false;
            rl.setVisibility(View.GONE);
            pBar.setVisibility(View.VISIBLE);
            pokeName = searchText.getText().toString();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String url = "http://pokeapi.co/api/v2/pokemon/";
            found = PokeUtils.searchPoke(url, pokeName);
            if(found)
                required = PokeUtils.getPokeInfo(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pBar.setVisibility(View.GONE);
            if (found) {
                createEntry(required);
                rl.setVisibility(View.VISIBLE);
            }
            else
                error.setVisibility(View.VISIBLE);
        }
    }

    private void createEntry(Pokemon p) {

        name.setText(p.name);
        speed.setText("Speed: " + String.valueOf(p.speed));
        xp.setText("XP: " + String.valueOf(p.experience));
        hp.setText("HP: " + String.valueOf(p.hp));
        attack.setText("Attack: " + String.valueOf(p.attack));
        defense.setText("Defense: " + String.valueOf(p.defense));
        weight.setText("Weight: " + String.valueOf(p.weight));
        height.setText("Height: " + String.valueOf(p.height));
        if (p.sprite != null)
            Glide.with(getApplicationContext()).load(p.sprite).into(sprite);
        else
            Glide.with(getApplicationContext()).load(R.drawable.ic_action_name).fitCenter().into(sprite);

        String typ = "";
        String abil = "Abilities: ";

        for (int i = 0; i < p.types.size(); i++)
            if (i == p.types.size()-1)
                typ = typ + p.types.get(i);
            else
                typ = typ + p.types.get(i) + ", ";

        for (int i = 0; i < p.abilities.size(); i++)
            if (i == p.abilities.size()-1)
                abil = abil + p.abilities.get(i);
            else
                abil = abil + p.abilities.get(i) + ", ";

        abilities.setText(abil);
        types.setText(typ);
    }

    private void initCard(){
        name = (customTextView) findViewById(R.id.name);
        types = (customTextView) findViewById(R.id.types);
        abilities = (customTextView) findViewById(R.id.abilities);
        speed = (customTextView) findViewById(R.id.speed);
        xp = (customTextView) findViewById(R.id.experience);
        hp = (customTextView) findViewById(R.id.hp);
        height = (customTextView) findViewById(R.id.height);
        weight = (customTextView) findViewById(R.id.weight);
        attack = (customTextView) findViewById(R.id.attack);
        defense = (customTextView) findViewById(R.id.defense);
        sprite = (ImageView) findViewById(R.id.sprite);
        pBar = (ProgressBar) findViewById(R.id.p_bar);
        rl = (RelativeLayout) findViewById(R.id.card);
        card = (CardView) findViewById(R.id.cv);
        error = (RelativeLayout) findViewById(R.id.no_poke);
    }
}

