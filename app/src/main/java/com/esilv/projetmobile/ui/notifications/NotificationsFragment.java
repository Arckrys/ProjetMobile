package com.esilv.projetmobile.ui.notifications;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.esilv.projetmobile.KitsuService;
import com.esilv.projetmobile.MainActivity;
import com.esilv.projetmobile.R;
import com.esilv.projetmobile.UserPage;
import com.esilv.projetmobile.ui.home.HomeFragment;
import com.esilv.projetmobile.ui.home.MangaAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Console;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    EditText username;
    EditText password;
    EditText access;
    Button login;

    public static final String PREFS = "PREFS";
    public static final String PREFS_USER= "PREFS_USER";
    public static final String PREFS_ACCESS= "PREFS_ACCESS";
    public static final String PREFS_INDEX= "PREFS_INDEX";
    SharedPreferences sharedPreferences;

    public class KitsuLogin {
        @SerializedName("access_token")
        String access_token;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        /*NotificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        login = root.findViewById(R.id.login);
        username = root.findViewById(R.id.username);
        password = root.findViewById(R.id.password);
        sharedPreferences = getContext().getSharedPreferences(PREFS, MODE_PRIVATE);

        if (sharedPreferences.contains(PREFS_USER)) {
            username.setText(sharedPreferences.getString(PREFS_USER, ""));

        }

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String user = username.getText().toString();
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString(PREFS_USER, user);
                edit.commit();
                String pass = password.getText().toString();
                LoginUser(user, pass, v);


            }
        });


        return root;
    }
    public void LoginUser(String user, String pass, final View v){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kitsu.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        KitsuService service = retrofit.create(KitsuService.class);
        Call<KitsuLogin> call = service.getKitsuLogin("password", user, pass);
        call.enqueue(new Callback<KitsuLogin>() {
            @Override
            public void onResponse(Call<KitsuLogin> call, Response<KitsuLogin> response) {
                KitsuLogin result = response.body();
                String access = result.access_token;
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString(PREFS_ACCESS, access);
                edit.commit();
                Navigation.findNavController(v).navigate(R.id.action_navigation_notifications_to_navigation_user_page);


            }

            @Override
            public void onFailure(Call<KitsuLogin> call, Throwable t) {

            }
        });

    }

    /*public void Save (View root){
        String user = username.getText().toString();
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(PREFS_USER, user);
        edit.commit();
        String pass = password.getText().toString();
        LoginUser(user, pass);
    }*/
}