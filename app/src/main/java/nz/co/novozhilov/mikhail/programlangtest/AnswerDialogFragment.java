package nz.co.novozhilov.mikhail.programlangtest;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Dialog with answer result (correct / wrong)
 * and right answer
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public final class AnswerDialogFragment extends DialogFragment{

    //labels for extras
    public static final String EXTRA_ANSWER_TEXT = "ANSWER_TEXT";
    public static final String EXTRA_CORRECT = "CORRECT";
    public static final String EXTRA_LAST = "LAST";

    // string with the right answer
    private String mAnswerText;
    // flag - answer is correct or not
    private boolean mCorrect;
    // is the last question
    private boolean isLast;


    /**
     * Fragment constructor
     * adds arguments to the extras bundle
     *
     * @param correct - flag if test passed
     * @param answerText - right answer's text
     * @return new fragment object
     */
    public static AnswerDialogFragment newInstance(boolean correct, String answerText, boolean isLast) {
        Bundle args = new Bundle();
        args.putString(EXTRA_ANSWER_TEXT, answerText);
        args.putBoolean(EXTRA_CORRECT, correct);
        args.putBoolean(EXTRA_LAST, isLast);
        AnswerDialogFragment fragment = new AnswerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get attributes from bundle
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        mCorrect = extras.getBoolean(EXTRA_CORRECT);
        mAnswerText = extras.getString(EXTRA_ANSWER_TEXT);
        isLast = extras.getBoolean(EXTRA_LAST);

        // do not close dialog
        this.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){
        final View dialog = inflater.inflate(R.layout.fragment_answer, container, false);

        //set status
        ImageView statusIcon = (ImageView)dialog.findViewById(R.id.iv_answer_title);
        TextView statusText = (TextView)dialog.findViewById(R.id.answer_title);
        Button btnExit = (Button)dialog.findViewById(R.id.answer_exit_btn);
        if (mCorrect) {
            //set passed
            statusIcon.setImageResource(R.drawable.ic_passed_large);
            int color = getResources().getColor(R.color.text_green);
            statusIcon.setColorFilter(color);
            statusText.setText(getResources().getText(R.string.answer_correct));
        } else {
            //set failed
            statusIcon.setImageResource(R.drawable.ic_failed_large);
            int color = getResources().getColor(R.color.text_red);
            statusIcon.setColorFilter(color);
            statusText.setText(getResources().getText(R.string.answer_wrong));
        }
        // set right answer text
        TextView tvAnswerText = (TextView)dialog.findViewById(R.id.answer_text);
        if(isLast){
            btnExit.setText("DONE");
        }
        tvAnswerText.setText(mAnswerText);

        //set listeners
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getActivity() instanceof QuestionCallbacks) {
            QuestionCallbacks qa = (QuestionCallbacks) getActivity();
            qa.startNextQuestion(mCorrect);
        }
    }
}