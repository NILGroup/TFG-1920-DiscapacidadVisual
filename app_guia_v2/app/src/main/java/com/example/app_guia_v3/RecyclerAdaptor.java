package com.example.app_guia_v3;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdaptor extends RecyclerView.Adapter<RecyclerAdaptor.ViewHolder> {


    private  ArrayList<String> listaDestinos;
    private  OnItemClickListener listener;

    //Con interface podemos utilizar metodos y variables de manera polimorfica
    public interface OnItemClickListener {
        //metodo onclick donde retornaremos la posicion de cada elemento de la lista
        void onItemClick(int posicion);
    }
    //Constructor que recibe parametros y los agrega a los de esta clase
    public RecyclerAdaptor(ArrayList<String> lista, OnItemClickListener listener){
        this.listaDestinos = lista;
        this.listener = listener;
    }

    //ViewHolder obtenemos la vista de nuestro adaptador
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adaptador_destinos,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);
        //Retornamos la vista
        return viewHolder;
    }

    //En este metodo recibimos la vista y la posicion del elemento de nuestro recycler

    public void  onBindViewHolder(ViewHolder holder, int position) {
        //retornamos la posicion actual
        holder.bind(listaDestinos.get(position),position,listener);
    }

    @Override
    public int getItemCount() {
        return listaDestinos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtnota;
        public ViewHolder(View itemView) {
            super(itemView);
            //llamamos el texview del xml
            txtnota=(TextView)itemView.findViewById(R.id.txtDest);

        }

        public void bind(final String dest, final int posicion, final OnItemClickListener listener) {
            //Agregamos la nota a nuestro texview del adaptador
            txtnota.setText(dest);

            //Metodo onclic cuando nosotros precionemos un elemento se retornara la posicion
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(posicion);
                }
            });

        }
    }
    public void setFilter(ArrayList<String> listaD){
            this.listaDestinos = new ArrayList<>();
            this.listaDestinos.addAll(listaD);
            notifyDataSetChanged();
    }

}
