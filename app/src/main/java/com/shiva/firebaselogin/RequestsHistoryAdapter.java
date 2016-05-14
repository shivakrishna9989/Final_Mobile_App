package com.shiva.firebaselogin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

public class RequestsHistoryAdapter extends RecyclerView.Adapter<RequestsHistoryAdapter.RequestViewHolder> {

    private List<Request> mRequestsList;
    private LinkedHashMap<String, Integer> mMap = new LinkedHashMap<>();
    private boolean mIsProvider;
    RequestListener listener;

    public interface RequestListener {
        void onClaimPressed(int position, String type);
    }

    public RequestsHistoryAdapter(List<Request> mRequestsList,RequestListener listener, boolean isProvider) {
        this.mRequestsList = mRequestsList;
        this.mIsProvider = isProvider;
        this.listener = listener;
    }

    public void refreshList(List<Request> refreshData) {
        mRequestsList.clear();
        mRequestsList.addAll(refreshData);
        createSections();
        notifyDataSetChanged();
    }

    public void createSections() {
        mMap.clear();
        for (int i=0; i<mRequestsList.size(); i++) {
            String ch = mRequestsList.get(i).getCompleted().substring(0,1);
            if (! mMap.containsKey(ch)) {
                mMap.put(ch,i);
            }
        }
    }

    public static class RequestViewHolder extends  RecyclerView.ViewHolder{
        public final TextView title;
        public final TextView section;
        public final Button claim;

        public RequestViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.request);
            section = (TextView) view.findViewById(R.id.section);
            claim = (Button) view.findViewById(R.id.claim);
        }
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.requests_list_item, parent, false);
        return new RequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RequestViewHolder holder, final int position) {
        holder.title.setText(mRequestsList.get(position).getName());
        holder.section.setText(mRequestsList.get(position).getCompleted());
        String section = getSection(position);
        holder.section.setVisibility(mMap.get(section) == position ? View.VISIBLE : View.GONE);
        holder.claim.setVisibility(mIsProvider ? View.VISIBLE : View.GONE);
        if (mRequestsList.get(position).getCompleted().equalsIgnoreCase("pending")) {
            holder.claim.setText("Claim");
        } else if (mRequestsList.get(position).getCompleted().equalsIgnoreCase("inprogress")) {
            holder.claim.setText("Complete");
        } else {
            holder.claim.setVisibility(View.GONE);
        }
        holder.claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.claim.getText().toString().equalsIgnoreCase("claim")) {
                    listener.onClaimPressed(position, "inprogress");
                } else {
                    listener.onClaimPressed(position, "completed");
                }
            }
        });
    }

    public String getSection(int position) {
        return mRequestsList.get(position).getCompleted().substring(0,1);
    }

    @Override
    public int getItemCount() {
        return mRequestsList.size();
    }
}
