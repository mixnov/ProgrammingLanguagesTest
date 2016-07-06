package nz.co.novozhilov.mikhail.programlangtest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Menu for c test
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public final class MenuCPlusFragment extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("C++ test");

        // get c menu fragment layout
        View fragment_view = inflater.inflate(R.layout.fragment_menu_c_plus, container, false);

        // get buttons from activity's layout
        Button btnMenuQuestions = (Button) fragment_view.findViewById(R.id.btn_menu_c_plus_questions);
        Button btnMenuTest = (Button) fragment_view.findViewById(R.id.btn_menu_c_plus_test);
        Button btnMenuReview = (Button) fragment_view.findViewById(R.id.btn_menu_c_plus_review);

        // set implemented method onClick as the onClickListener
        btnMenuQuestions.setOnClickListener(this);
        btnMenuTest.setOnClickListener(this);
        btnMenuReview.setOnClickListener(this);

        return fragment_view;
    }

    @Override
    public void onClick(View v) {
        // action depends on the view's id
        switch (v.getId()) {
            case R.id.btn_menu_c_plus_questions:
                // open question categories
                startFragment(CategoryListFragment.newInstance(Question.C_PLUS));
                break;
            case R.id.btn_menu_c_plus_test:
                //start exam simulator
                Intent testActivity = new Intent(v.getContext(), TestActivity.class);
                testActivity.putExtra(TestActivity.EXTRA_MAX_ERRORS, 3);
                testActivity.putExtra(TestActivity.EXTRA_TEST_TYPE, Question.C_PLUS);
                v.getContext().startActivity(testActivity);
                break;
            case R.id.btn_menu_c_plus_review:
                // start question activity with array list of questions
                // from error table
                Intent reviewActivity = new Intent(v.getContext(), ReviewActivity.class);
                reviewActivity.putExtra(TestActivity.EXTRA_TEST_TYPE, Question.C_PLUS);
                v.getContext().startActivity(reviewActivity);
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

