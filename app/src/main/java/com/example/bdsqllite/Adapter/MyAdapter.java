package com.example.bdsqllite.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bdsqllite.BasedatoSQL.BaseSQLDatos;
import com.example.bdsqllite.Entities.Asignatura;
import com.example.bdsqllite.Holder.MyHolder;
import com.example.bdsqllite.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    Context c;
    BaseSQLDatos repo = null;


    public MyAdapter(Context c, BaseSQLDatos repo) {
        this.c=c;
        this.repo = repo;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,null);
        return new MyHolder(view);
    }

    public void notifyChanged() {
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        holder.mTitle.setText( BaseSQLDatos.asignaturas.get(position).getTitle());
        holder.mDescription.setText(BaseSQLDatos.asignaturas.get(position).getDescription());

        holder.setCreateContextMenu(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Opciones");

                //Eliminando elemento
                menu.add("Eliminar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        try {
                            Asignatura current = BaseSQLDatos.asignaturas.get(position);
                            repo.eliminar(current.getId());
                            Toast.makeText(c, "Eliminado con Exito!!!", Toast.LENGTH_SHORT).show();
                            notifyChanged();
                        }
                        catch(Exception ex) {
                            Toast.makeText(c, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        return true;
                    }
                });

                //Actualizando elemento
                menu.add("Actualizar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Aquí va el código actualizar
                        final Dialog dlg = new Dialog(c);

                        dlg.setContentView(R.layout.add_new);
                        dlg.setTitle("Actualizar asignaturas");
                        dlg.setCancelable(false);
                        Button btAddNew = (Button) dlg.findViewById(R.id.btnew);
                        Button btCancel = (Button) dlg.findViewById(R.id.btcancel);

                        final EditText editText_Name = (EditText) dlg.findViewById(R.id.editText_Name);
                        final EditText editText_Des = (EditText) dlg.findViewById(R.id.editText_Desc);

                        final Asignatura current = BaseSQLDatos.asignaturas.get(position);
                        editText_Name.setText(current.getTitle());
                        editText_Des.setText(current.getDescription());

                        btAddNew.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImageView imageView = (ImageView) dlg.findViewById(R.id.imageAsig);

                                if ((editText_Name.getText().toString().contentEquals("")) ||
                                        (editText_Des.getText().toString().contentEquals(""))) {
                                    Toast.makeText(c, "Debe llenar todo los campos",
                                            Toast.LENGTH_LONG).show();

                                } else {
                                    String nAsignatura, nDes;

                                    nAsignatura = editText_Name.getText().toString();
                                    nDes = editText_Des.getText().toString();

                                    current.setTitle(nAsignatura);
                                    current.setDescription(nDes);
                                    try {
                                        long resultadoInsert = repo.actualizar(current);
                                        notifyChanged();
                                        Toast.makeText(c, "Agregados con Exito!!!", Toast.LENGTH_LONG).show();
                                        editText_Name.setText("");
                                        editText_Des.setText("");
                                    }
                                    catch(Exception e) {
                                        Toast.makeText(c, "Error al Agregar!!!: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                    dlg.cancel();
                                }
                            }
                        });

                        btCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dlg.cancel();
                            }
                        });

                        dlg.show();

                        return true;
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return BaseSQLDatos.asignaturas.size();
    }

}
