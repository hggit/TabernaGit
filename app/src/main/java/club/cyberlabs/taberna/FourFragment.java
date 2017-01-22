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



public class FourFragment extends Fragment implements View.OnClickListener{

    public FourFragment() {
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
        View root= inflater.inflate(R.layout.fragment_four, container, false);
        TextView tv1=(TextView)root.findViewById(R.id.kid);
        tv1.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.kid)
        {
            Intent intent=new Intent(this.getContext(),SearchActivity.class);
            intent.putExtra("search-key","list/toys");
            startActivity(intent);
        }
    }

}
