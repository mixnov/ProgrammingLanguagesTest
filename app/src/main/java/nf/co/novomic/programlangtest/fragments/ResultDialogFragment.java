package nf.co.novomic.programlangtest.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import nf.co.novomic.programlangtest.FormatHelper;
import nf.co.novomic.programlangtest.QuestionCallbacks;
import nf.co.novomic.programlangtest.R;

/**
 * Dialog with results of the test
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public final class ResultDialogFragment extends DialogFragment implements View.OnClickListener {

    //labels for extras
    public static final String EXTRA_MISTAKES = "MISTAKES";
    public static final String EXTRA_QUESTIONS = "QUESTIONS";
    public static final String EXTRA_TIME_SPENT = "TIME_SPENT";
    public static final String EXTRA_PASSED = "PASSED";


    // mistakes count
    private int mMistakes;
    // questions count
    private int mQuestions;
    // time spent for the test (in ms)
    private long mTimeSpent;
    // flag - passed or not
    private boolean mPassed;

    /**
     * Fragment constructor
     * adds arguments to the extras bundle
     *
     * @param passed - flag if test passed
     * @param mistakes - number of mistakes
     * @param questions - num of questions
     * @param timeSpent - time spent for the test
     * @return new fragment object
     */
    public static ResultDialogFragment newInstance(boolean passed, int mistakes,
                                                   int questions, long timeSpent) {

        Bundle args = new Bundle();
        args.putInt(EXTRA_MISTAKES, mistakes);
        args.putInt(EXTRA_QUESTIONS, questions);
        args.putLong(EXTRA_TIME_SPENT, timeSpent);
        args.putBoolean(EXTRA_PASSED, passed);
        ResultDialogFragment fragment = new ResultDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get attributes from bundle
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        mMistakes = extras.getInt(EXTRA_MISTAKES);
        mQuestions = extras.getInt(EXTRA_QUESTIONS);
        mTimeSpent = extras.getLong(EXTRA_TIME_SPENT);
        mPassed = extras.getBoolean(EXTRA_PASSED);

        // do not close dialog
        this.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){
        View dialog = inflater.inflate(R.layout.fragment_result, container, false);

        //set status
        ImageView statusIcon = (ImageView)dialog.findViewById(R.id.test_result_icon);
        TextView statusText = (TextView)dialog.findViewById(R.id.test_result_status);
        if (mTimeSpent == 0) {
            // practice mode (not for test simulation)
            statusIcon.setImageResource(R.drawable.ic_statistics);
            statusIcon.setColorFilter(R.color.material_blue_600);
            statusText.setText(getResources().getText(R.string.result_status_statistics));
            dialog.findViewById(R.id.test_result_time).setVisibility(View.GONE);
        } else {
            if (mPassed) {
                //set passed
                statusIcon.setImageResource(R.drawable.ic_passed_large);
                statusIcon.setColorFilter(R.color.text_green);
                statusText.setText(getResources().getText(R.string.result_status_passed));
            } else {
                //set failed
                statusIcon.setImageResource(R.drawable.ic_failed_large);
                statusIcon.setColorFilter(R.color.text_red);
                statusText.setText(getResources().getText(R.string.result_status_failed));
            }
            TextView tvTime = (TextView)dialog.findViewById(R.id.test_result_time);
            String time = String.format(getString(R.string.time), FormatHelper.getTimeString(mTimeSpent));
            tvTime.setText(time);
        }
        // set details
        TextView tvScore = (TextView)dialog.findViewById(R.id.test_result_score);
        tvScore.setText("Your score: " + ((100*(mQuestions-mMistakes))/mQuestions)+ "%");
        TextView tvCorrect = (TextView)dialog.findViewById(R.id.test_result_correct);
        tvCorrect.setText("Correct answers: " + (mQuestions - mMistakes));
        TextView tvWrong = (TextView)dialog.findViewById(R.id.test_result_wrong);
        tvWrong.setText("Wrong answers: " + mMistakes);

        //find buttons
        Button btnExit = (Button)dialog.findViewById(R.id.test_result_exit_btn);
        Button btnTryAgain = (Button)dialog.findViewById(R.id.test_result_again_btn);
        //set listeners
        btnExit.setOnClickListener(this);
        btnTryAgain.setOnClickListener(this);

        return dialog;
    }

    @Override
    public void onResume() {
        //change the size of the dialog
//        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
//        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
//        getDialog().getWindow().setLayout(width, height);
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_result_exit_btn:
                getActivity().finish();
                break;
            case R.id.test_result_again_btn:
                if (getActivity() instanceof QuestionCallbacks) {
                    QuestionCallbacks qa = (QuestionCallbacks) getActivity();
                    qa.RestartActivity();
                }
                dismiss();
                break;
        }
    }
}
