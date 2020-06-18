package com.example.bdsqllite.UserInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.app.Dialog;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bdsqllite.Adapter.MyAdapter;
import com.example.bdsqllite.BasedatoSQL.BaseSQLDatos;
import com.example.bdsqllite.Config.Constants;
import com.example.bdsqllite.Database.AppDatabase;
import com.example.bdsqllite.Entities.Asignatura;
import com.example.bdsqllite.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    // definicion de los objetos
    RecyclerView mRecyclerView;
    MyAdapter myAdapter;

    BaseSQLDatos repo;

    Button btAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repo = new BaseSQLDatos(this.getApplicationContext());

        btAdd = findViewById(R.id.btAdd);
        mRecyclerView = findViewById(R.id.recycleview1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new MyAdapter(this, repo);

        mRecyclerView.setAdapter(myAdapter);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dlg = new Dialog(MainActivity.this);
                dlg.setContentView(R.layout.add_new);
                dlg.setTitle("Agregue asignaturas");
                dlg.setCancelable(false);
                Button btAdd = (Button) dlg.findViewById(R.id.btnew);
                Button Cancelar = (Button) dlg.findViewById(R.id.btcancel);


                btAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText_Name = (EditText) dlg.findViewById(R.id.editText_Name);
                        EditText editText_Des = (EditText) dlg.findViewById(R.id.editText_Desc);
                        ImageView imageView = (ImageView) dlg.findViewById(R.id.imageAsig);

                        if ((editText_Name.getText().toString().contentEquals("")) ||
                                (editText_Des.getText().toString().contentEquals(""))) {
                            Toast.makeText(MainActivity.this, "Nombre y descripcion es necesario",
                                    Toast.LENGTH_LONG).show();

                        } else {
                            String nAsignatura, nDes;


                            nAsignatura = editText_Name.getText().toString();
                            nDes = editText_Des.getText().toString();

                            Asignatura asignaturaObj = new Asignatura();

                            asignaturaObj.setTitle(nAsignatura);
                            asignaturaObj.setDescription(nDes);

                            //long resultadoInsert = db.asignaturaDao().insert(asignaturaObj);
                            long agregar = repo.agregar(asignaturaObj);
                            if (agregar > 0) {
                                myAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Agregado con exito!!!", Toast.LENGTH_LONG).show();
                                editText_Name.setText("");
                                editText_Des.setText("");
                            } else {
                                Toast.makeText(MainActivity.this, "Error al Agregar", Toast.LENGTH_LONG).show();
                            }
                            dlg.cancel();
                        }
                    }
                });

                Cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg.cancel();
                    }
                });

                dlg.show();

            }

        });
    }
}

