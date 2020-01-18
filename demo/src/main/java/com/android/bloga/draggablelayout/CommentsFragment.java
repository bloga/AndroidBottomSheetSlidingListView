package com.android.bloga.draggablelayout;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.android.bloga.draggableframelayout.DraggableLayout;

import java.util.ArrayList;
import java.util.List;

public class CommentsFragment extends Fragment  {

    DraggableLayout commentsFrameLayout;
    ListView commentsListView;
    List<String> commentsListItems = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView = layoutInflater.inflate(R.layout.comments_layout, null,false);

        commentsFrameLayout = inflatedView.findViewById(R.id.commentsFrameLayout);
        commentsListView = inflatedView.findViewById(R.id.commentsListView);

        commentsListItems.add("List Item 1");
        commentsListItems.add("List Item 2");
        commentsListItems.add("List Item 3");
        commentsListItems.add("List Item 4");
        commentsListItems.add("List Item 5");
        commentsListItems.add("List Item 6");
        commentsListItems.add("List Item 7");
        commentsListItems.add("List Item 8");
        commentsListItems.add("List Item 9");
        commentsListItems.add("List Item 10");
        commentsListItems.add("List Item 11");
        commentsListItems.add("List Item 12");
        commentsListItems.add("List Item 13");
        commentsListItems.add("List Item 14");
        commentsListItems.add("List Item 15");
        commentsListItems.add("List Item 16");
        commentsListItems.add("List Item 17");
        commentsListItems.add("List Item 18");
        commentsListItems.add("List Item 19");
        commentsListItems.add("List Item 20");
        commentsListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, commentsListItems));

        commentsFrameLayout.setCommentsListView(commentsListView);
        commentsFrameLayout.setDragLayoutListener(new DraggableLayout.DraggableLayoutListener() {
            @Override
            public void onDialogClose() {
                closeDialog();
            }
        });

        return inflatedView;
    }
    private void closeDialog() {
        getActivity().findViewById(R.id.root).setBackgroundColor(Color.parseColor("#ffffff"));
        getActivity().getSupportFragmentManager().beginTransaction().remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.commentFragment)).commit();
    }


}
