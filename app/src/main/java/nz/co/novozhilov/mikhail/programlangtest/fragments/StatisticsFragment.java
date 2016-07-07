package nz.co.novozhilov.mikhail.programlangtest.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import nz.co.novozhilov.mikhail.programlangtest.FormatHelper;
import nz.co.novozhilov.mikhail.programlangtest.R;
import nz.co.novozhilov.mikhail.programlangtest.classes.Question;

/**
 * Show statistics for finished tests
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public final class StatisticsFragment extends Fragment implements View.OnClickListener{

    private View mFragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("Statistics");

        // get statistics fragment layout
        View fragment_view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // get buttons from activity's layout
        Button btnClearJava = (Button) fragment_view.findViewById(R.id.stat_java_clear_btn);
        Button btnClearC = (Button) fragment_view.findViewById(R.id.stat_c_clear_btn);

        // set icon color
        int color = getResources().getColor(R.color.material_blue_600);
        ImageView ivJava = (ImageView)fragment_view.findViewById(R.id.statistics_java_icon);
        ImageView ivC = (ImageView)fragment_view.findViewById(R.id.statistics_c_icon);
        ivJava.setColorFilter(color);
        ivC.setColorFilter(color);


        // set implemented method onClick as the onClickListener
        btnClearJava.setOnClickListener(this);
        btnClearC.setOnClickListener(this);

        // set statistics text for all test types
        setStatisticsText(fragment_view, R.string.file_key_statistics_java, R.id.stat_java_total,
                R.id.stat_java_correct, R.id.stat_java_incorrect, R.id.stat_java_avg_scope, R.id.stat_java_avg_time);
        setStatisticsText(fragment_view, R.string.file_key_statistics_c, R.id.stat_c_total,
                R.id.stat_c_correct, R.id.stat_c_incorrect, R.id.stat_c_avg_scope, R.id.stat_c_avg_time);

        mFragmentView = fragment_view;

        return fragment_view;
    }

    @Override
    public void onClick(View v) {
        // action depends on the view's id
        switch (v.getId()) {
            case R.id.stat_java_clear_btn:
                // remove statistics for java test
                clearStatistics(Question.JAVA_TEST);
                break;
            case R.id.stat_c_clear_btn:
                // remove statistics for C test
                clearStatistics(Question.C_TEST);
                break;
        }
    }

    /**
     * Clear statistics for the test of selected type
     *
     * @param testType - type of test: java, C etc.
     */
    private void clearStatistics(int testType){
        Context context = getActivity();
        int fileId;
        // get file key (depends on test type)
        switch (testType) {
            case Question.JAVA_TEST:
                fileId = R.string.file_key_statistics_java;
                break;
            case Question.C_TEST:
                fileId = R.string.file_key_statistics_c;
                break;
            default:
                fileId = R.string.file_key_statistics_java;
        }
        // get shared preferences file by file key
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(fileId), Context.MODE_PRIVATE);

        // clear statistics
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().apply();

        // update widgets
        switch (testType) {
            case Question.JAVA_TEST:
                setStatisticsText(mFragmentView, R.string.file_key_statistics_java, R.id.stat_java_total,
                        R.id.stat_java_correct, R.id.stat_java_incorrect,
                        R.id.stat_java_avg_scope, R.id.stat_java_avg_time);
                break;
            case Question.C_TEST:
                setStatisticsText(mFragmentView, R.string.file_key_statistics_c, R.id.stat_c_total,
                        R.id.stat_c_correct, R.id.stat_c_incorrect,
                        R.id.stat_c_avg_scope, R.id.stat_c_avg_time);
                break;
        }
    }

    /**
     * set statistics text for one test type
     * from internal storage file (preferences)
     *
     * @param fragment_view - fragment's layout
     * @param fileKeyId - id of file's key
     * @param idTotal - widget for total
     * @param idCorrect - widget for correct answers
     * @param idIncorrect - widget for incorrect answers
     * @param idScope - widget for avg scope
     * @param idTime - widget for avg time
     */
    private void setStatisticsText(View fragment_view, int fileKeyId, int idTotal,
                                   int idCorrect, int idIncorrect, int idScope, int idTime){
        Context context = getActivity();
        // find widgets
        TextView tvTotal = (TextView)fragment_view.findViewById(idTotal);
        TextView tvCorrect = (TextView)fragment_view.findViewById(idCorrect);
        TextView tvIncorrect = (TextView)fragment_view.findViewById(idIncorrect);
        TextView tvScope = (TextView)fragment_view.findViewById(idScope);
        TextView tvTime = (TextView)fragment_view.findViewById(idTime);

        // get shared preferences file by file key
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(fileKeyId), Context.MODE_PRIVATE);
        // set text
        int total = sharedPref.getInt(getString(R.string.saved_questions_count), 0);
        int mistaken = sharedPref.getInt(getString(R.string.saved_mistakes_count), 0);
        int avgScope = sharedPref.getInt(getString(R.string.saved_average_score), 0);
        long avgTime = sharedPref.getLong(getString(R.string.saved_average_time), 0);
        tvTotal.setText("Questions answered: " + total);
        tvCorrect.setText("Correct: " + (total - mistaken));
        tvIncorrect.setText("Incorrect: " + mistaken);
        tvScope.setText("Average scope: " + avgScope + "%");
        tvTime.setText("Average time: " + FormatHelper.getTimeString(avgTime));
    }
}
