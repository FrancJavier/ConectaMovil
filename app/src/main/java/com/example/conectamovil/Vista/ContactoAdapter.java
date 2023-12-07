// ListaDeContactosActivity.java
// (El código de ListaDeContactosActivity no cambia)

package com.example.conectamovil.Vista;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.conectamovil.R;
import com.example.conectamovil.Controlador.InfoUsuario;
import java.util.ArrayList;

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder> {

    private ArrayList<InfoUsuario> listaDeContactos;

    public ContactoAdapter(ArrayList<InfoUsuario> listaDeContactos) {
        this.listaDeContactos = listaDeContactos;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        InfoUsuario usuario = listaDeContactos.get(position);
        holder.textViewNombreContacto.setText(usuario.getNombre());
        holder.textViewCorreoContacto.setText(usuario.getCorreo());
        holder.textViewIDContacto.setText(usuario.getId());
        // Puedes establecer otros datos según tus necesidades
    }

    @Override
    public int getItemCount() {
        return listaDeContactos.size();
    }

    public static class ContactoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombreContacto;
        TextView textViewCorreoContacto;
        TextView textViewIDContacto;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreContacto = itemView.findViewById(R.id.textViewNombreContacto);
            textViewCorreoContacto = itemView.findViewById(R.id.textViewCorreoContacto);
            textViewIDContacto = itemView.findViewById(R.id.textViewIDContacto);
            // Otros elementos del diseño se pueden inicializar aquí
        }
    }
}
