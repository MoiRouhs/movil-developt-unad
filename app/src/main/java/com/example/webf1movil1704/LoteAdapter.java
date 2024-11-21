package com.example.webf1movil1704;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LoteAdapter extends RecyclerView.Adapter<LoteAdapter.LoteViewHolder> {

    private List<Lote> loteList;

    public LoteAdapter(List<Lote> loteList) {
        this.loteList = loteList;
    }

    @NonNull
    @Override
    public LoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lote, parent, false);
        return new LoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoteViewHolder holder, int position) {
        Lote lote = loteList.get(position);
        holder.textViewId.setText(String.valueOf(lote.getId()));
        holder.textViewUbicacion.setText(lote.getUbicacion());
        holder.textViewArea.setText(String.format("%.2f ha", lote.getAreaSembrada()));
    }

    @Override
    public int getItemCount() {
        return loteList.size();
    }

    static class LoteViewHolder extends RecyclerView.ViewHolder {

        TextView textViewId, textViewUbicacion, textViewArea;

        public LoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewUbicacion = itemView.findViewById(R.id.textViewUbicacion);
            textViewArea = itemView.findViewById(R.id.textViewArea);
        }
    }
}
