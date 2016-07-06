package nz.co.novozhilov.mikhail.programlangtest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Main menu
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public final class MenuMainFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle(R.string.app_name);

        // get main menu fragment layout
        View fragment_view = inflater.inflate(R.layout.fragment_menu_main, container, false);
        // get buttons from fragment's layout
        Button btnMainJava = (Button) fragment_view.findViewById(R.id.btn_main_java);
        Button btnMainC = (Button) fragment_view.findViewById(R.id.btn_main_c);
        Button btnMainCPlus = (Button) fragment_view.findViewById(R.id.btn_main_c_plus);
        Button btnMainCSharp = (Button) fragment_view.findViewById(R.id.btn_main_c_sharp);
        Button btnStatistics = (Button) fragment_view.findViewById(R.id.btn_menu_statistics);

        // set implemented method onClick as the onClickListener
        btnMainJava.setOnClickListener(this);
        btnMainC.setOnClickListener(this);
        btnMainCPlus.setOnClickListener(this);
        btnMainCSharp.setOnClickListener(this);
        btnStatistics.setOnClickListener(this);

        return fragment_view;
    }


    @Override
    public void onClick(View v) {
        // action depends on the view's id
        switch (v.getId()) {
            case R.id.btn_main_java:
                // start java test menu fragment
                startFragment(new MenuJavaFragment());
                break;
            case R.id.btn_main_c:
                // open menu for a c test
                startFragment(new MenuCFragment());
                break;
            case R.id.btn_main_c_plus:
                // open menu for a c test
                startFragment(new MenuCPlusFragment());
                break;
            case R.id.btn_main_c_sharp:
                // open menu for a c test
                startFragment(new MenuCSharpFragment());
                break;
            case R.id.btn_menu_statistics:
                // show statistics by groups (java/c):
                startFragment(new StatisticsFragment());
                break;
        }
    }

    /**
     * Replace current fragment with the next one
     *
     * @param fragment - next fragment
     */
    private void startFragment(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null); //add to stack for proper back navigation
        transaction.commit();
    }
}
