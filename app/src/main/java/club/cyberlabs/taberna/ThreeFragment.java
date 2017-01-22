package club.cyberlabs.taberna;

/**
 * Created by Himanshu on 21-01-2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Aman on 21-01-2017.
 */



public class ThreeFragment extends Fragment implements View.OnClickListener{

    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_three, container, false);
        TextView tv1=(TextView)root.findViewById(R.id.ent);
        tv1.setOnClickListener(this);
        TextView tv2=(TextView)root.findViewById(R.id.nov);
        tv2.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.ent)
        {
            Intent intent=new Intent(this.getContext(),SearchActivity.class);
            intent.putExtra("search-key","list/exam");
            startActivity(intent);
        }
        else if(view.getId()==R.id.nov)
        {
            Intent intent=new Intent(this.getContext(),SearchActivity.class);
            intent.putExtra("search-key","list/novel");
            startActivity(intent);
        }
    }

}
